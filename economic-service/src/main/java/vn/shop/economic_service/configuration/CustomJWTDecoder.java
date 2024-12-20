package vn.shop.economic_service.configuration;

import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import vn.shop.economic_service.dto.request.IntrospectRequest;
import vn.shop.economic_service.service.AuthenticationService;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;


@Configuration
@Slf4j
public class CustomJWTDecoder implements JwtDecoder {
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Autowired
    AuthenticationService authenticationService;

    @Override
    public Jwt decode(String token) throws JwtException {
        //Loc cac truong lop token da bi huy
        try{
            var response = authenticationService.introspect(
                    IntrospectRequest.builder()
                            .token(token)
                            .build());


            if (!response.isVerify())
                throw new JwtException("Token invalid");

        }catch(JOSEException | ParseException e){
            throw new JwtException(e.getMessage());
        }

        //Tao authorize
        if(Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }
        return nimbusJwtDecoder.decode(token);
    }
}
