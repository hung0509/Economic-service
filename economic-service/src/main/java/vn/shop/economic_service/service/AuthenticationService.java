package vn.shop.economic_service.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import vn.shop.economic_service.Repository.InvalidatedTokenRepository;
import vn.shop.economic_service.Repository.UserRepository;
import vn.shop.economic_service.dto.request.AuthenticateRequest;
import vn.shop.economic_service.dto.request.IntrospectRequest;
import vn.shop.economic_service.dto.request.LogoutRequest;
import vn.shop.economic_service.dto.request.RefreshTokenRequest;
import vn.shop.economic_service.dto.response.AuthenticateResponse;
import vn.shop.economic_service.dto.response.IntrospectResponse;
import vn.shop.economic_service.entity.InvalidatedToken;
import vn.shop.economic_service.entity.User;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;

import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    public long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    public long REFRESHABLE_DURATION;

    public AuthenticateResponse authenticate(AuthenticateRequest request) {
        var user = userRepository.findByUsername(request.getUsername());

        log.info(user.getPassword());
        log.info(request.getPassword());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);


        String token = generateToken(user);

        return AuthenticateResponse.builder()
                .token(token)
                .success(true)
                .build();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isCheck = true;

        try {
            verify(token, false);// xác thực không phải là refresh
        }catch(AppException appException){
            isCheck = false;
        }

        return IntrospectResponse.builder()
                .verify(isCheck)
                .build();
    }

    @Transactional
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verify(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            //convert date to localdate
            LocalDate localDate = expiryTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder()
                            .id(jit)
                            .expireDate(localDate)
                            .build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    public AuthenticateResponse refresh(RefreshTokenRequest request) throws ParseException, JOSEException {
        var signerToken = verify(request.getToken(), true);

        String jwt_id = signerToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signerToken.getJWTClaimsSet().getExpirationTime();
        LocalDate localDate = expiryTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String username = signerToken.getJWTClaimsSet().getSubject();

        var user = userRepository.findByUsername(username);
            //    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .id(jwt_id)
                .expireDate(localDate)
                .build();

        String token = generateToken(user);
        return AuthenticateResponse.builder()
                .token(token)
                .success(true)
                .build();
    }


    public SignedJWT verify(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        boolean isValid = signedJWT.verify(verifier);

        Date expireDate = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet()
                .getExpirationTime();


        String id_token = signedJWT.getJWTClaimsSet().getJWTID();

        if(!isValid || (expireDate.before(new Date())) )
            throw new AppException(ErrorCode.UNAUTHENTICATED);


        //Kiểm tra xem có trong black list không
        if(invalidatedTokenRepository.existsById(id_token)) {
            log.info("invalidated token");
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    private String generateToken(User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("xuan-hung")
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("uid", user.getId())
                .claim("cid", user.getCart().getId())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user){
        log.info(user.getRoles().toString());
        StringJoiner joiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(role -> {
                joiner.add("ROLE_"+role.getName());
                if(!CollectionUtils.isEmpty(role.getPermissions())){
                    role.getPermissions().forEach(permission -> joiner.add(permission.getName()));
                }
            });
        }
        return joiner.toString();
    }

}
