package vn.shop.economic_service.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.shop.economic_service.Repository.UserRepository;
import vn.shop.economic_service.entity.User;

@Configuration
@Slf4j
public class ApplicationConfig {

    @Bean
    public ApplicationRunner applicationRunner(UserRepository userRepository,PasswordEncoder passwordEncoder){
        return runner -> {
            if(!userRepository.existsByUsername("admin")){
                User u = User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(u);
                log.info("Successful");
            }
        };
    }
}
