package vn.shop.economic_service.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import feign.FeignException;
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
import vn.shop.economic_service.Repository.RoleRepository;
import vn.shop.economic_service.Repository.UserRepository;
import vn.shop.economic_service.Repository.httpclient.OutboundIdentityClient;
import vn.shop.economic_service.Repository.httpclient.OutboundUserClient;
import vn.shop.economic_service.dto.request.*;
import vn.shop.economic_service.dto.response.AuthenticateResponse;
import vn.shop.economic_service.dto.response.IntrospectResponse;
import vn.shop.economic_service.entity.*;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    OutboundIdentityClient outboundIdentityClient;
    OutboundUserClient outboundUserClient;
    RoleRepository roleRepository;
    AmazonSimpleEmailService amazonSimpleEmailService;

    String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String LOWER = "abcdefghijklmnopqrstuvwxyz";
    String DIGITS = "0123456789";
    String SPECIAL = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";
    String ALL_CHARACRTER = UPPER + LOWER + DIGITS + SPECIAL;

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

    @NonFinal
    @Value("${oauth2.client-id}")
    private String CLIENT_ID;

    @NonFinal
    @Value("${oauth2.client-secret}")
    private String CLIENT_SECRET;

    @NonFinal
    @Value("${oauth2.redirect_uri}")
    private String REDIRECT_URI;

    @NonFinal
    protected final String GRANT_TYPE = "authorization_code";

    public AuthenticateResponse outboundAuthentication(String code){
        String decodedCode = "";
        try {
            decodedCode = java.net.URLDecoder.decode(code, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new AppException(ErrorCode.DECODE_NOT_AVAILABLE);
        }
        var response = outboundIdentityClient.exchangeToken(ExchangeTokenRequest.builder()
                    .code(decodedCode)
                    .clientId(CLIENT_ID)
                    .clientSecret(CLIENT_SECRET)
                    .redirectUri(REDIRECT_URI)
                    .grantType(GRANT_TYPE)
                    .build());

        log.info("TOKEN RESPONSE {}", response);

        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        var user = userRepository.findByUsername(userInfo.getEmail());

        if(Objects.isNull(user)) { //Neeus chua ton tai thi them user nay vao he thong
            Role role = roleRepository.findById("USER")
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));

            Set<Role> roles = new HashSet<>();
            roles.add(role);

            Andress andress = Andress.builder()
                    .apartment_no("")
                    .sweet_name("")
                    .city("")
                    .created_at(LocalDate.now())
                    .build();

            String password = generatePassword();

            user = User.builder()
                    .email(userInfo.getEmail())
                    .username(userInfo.getEmail())
                    .password(password)
                    .firstname(userInfo.getFamilyName())
                    .lastname(userInfo.getGivenName())
                    .created_at(LocalDate.now())
                    .roles(roles)
                    .andress(andress)
                    .verify(true)
                    .token_verify(null)
                    .updated_at(LocalDate.now())
                    .build();

            Cart cart = Cart.builder()
                    .products(null)
                    .created_at(LocalDate.now())
                    .updated_at(LocalDate.now())
                    .user(user)
                    .build();

            user.setCart(cart);

            user = userRepository.save(user);

            //Tien hanh gui mail
            String message = "<p>Below is the password we reset: " + password + "</p>";

            MailRequest mailRequest = MailRequest
                    .builder()
                    .fromAddress("hungtaithe12@gmail.com")
                    .toAddress(user.getEmail())
                    .subject("Reset password required")
                    .content(message)
                    .build();
            sendMail(mailRequest);
        }

        var token = generateToken(user);

        return AuthenticateResponse.builder()
                .success(true)
                .token(token)
                .build();

    }

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

    private String generatePassword(){
        SecureRandom RANDOM = new SecureRandom();

        char[] password = new char[8];

        password[0] = UPPER.charAt(RANDOM.nextInt(UPPER.length()));
        password[1] = LOWER.charAt(RANDOM.nextInt(LOWER.length()));
        password[2] = DIGITS.charAt(RANDOM.nextInt(DIGITS.length()));
        password[3] = SPECIAL.charAt(RANDOM.nextInt(SPECIAL.length()));

        for(int i = 4; i < 8; i++){
            password[i] = ALL_CHARACRTER.charAt(RANDOM.nextInt(ALL_CHARACRTER.length()));
        }

        for (int i = 7; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = password[i];
            password[i] = password[j];
            password[j] = temp;
        }

        return new String(password);
    }

    public void sendMail(MailRequest request){
        SendEmailRequest sendEmailRequest = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses(request.getToAddress()))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content().withCharset("UTF-8").withData(request.getContent())))
                        .withSubject(new Content().withCharset("UTF-8").withData(request.getSubject()))
                )
                .withSource(request.getFromAddress());
        amazonSimpleEmailService.sendEmail(sendEmailRequest);
    }

}
