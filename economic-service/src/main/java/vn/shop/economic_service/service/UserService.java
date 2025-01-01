package vn.shop.economic_service.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.AndressRepository;
import vn.shop.economic_service.Repository.CartRepository;
import vn.shop.economic_service.Repository.RoleRepository;
import vn.shop.economic_service.Repository.UserRepository;
import vn.shop.economic_service.dto.request.MailRequest;
import vn.shop.economic_service.dto.request.UserCreateRequest;
import vn.shop.economic_service.dto.request.UserUpdateRequest;
import vn.shop.economic_service.dto.response.UserResponse;
import vn.shop.economic_service.entity.Andress;
import vn.shop.economic_service.entity.Cart;
import vn.shop.economic_service.entity.Role;
import vn.shop.economic_service.entity.User;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.UserMapper;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.*;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    RoleRepository roleRepository;
    AndressRepository andressRepository;
    PasswordEncoder passwordEncoder;
    CartRepository cartRepository;
    AmazonSimpleEmailService amazonSimpleEmailService;

    String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String LOWER = "abcdefghijklmnopqrstuvwxyz";
    String DIGITS = "0123456789";
    String SPECIAL = "!@#$%^&*()-_=+[]{}|;:'\",.<>/?";
    String ALL_CHARACRTER = UPPER + LOWER + DIGITS + SPECIAL;

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.EXISTED_USERNAME);
        }

        if(userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.EXISTED_EMAIL);
        }

        var user = userMapper.toCreateUser(request);

        String token = UUID.randomUUID().toString();

        user.setVerify(false);
        user.setToken_verify(token);//Set cho 1 verify token đẻ thực hiện gui mail

        Role role = roleRepository.findById("USER")
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));

        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        //Tạo 1 tài khoản mới thì mặc đinh địa chỉ chưa có phải update sau
        Andress andress = Andress.builder()
                .apartment_no("")
                .sweet_name("")
                .city("")
                .created_at(LocalDate.now())
                .build();
        user.setAndress(andress);

        Cart cart = Cart.builder()
                .products(null)
                .created_at(LocalDate.now())
                .updated_at(LocalDate.now())
                .user(user)
                .build();

        cartRepository.save(cart); //Khi tạo 1 người dụng tạo cho họ 1 giỏ hàng trống

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreated_at(LocalDate.now());
        user.setUpdated_at(LocalDate.now());

        user = userRepository.save(user);

        //Tiến hành gửi mail bangf simple mail
        String url = "http://localhost:8080/economic-service/users/verify/" + token;
        String message = "<p>To active your account, please click on the link below</p>" +
                         "<p><a href=\"" + url + "\">Verify Account</a></p>";

        MailRequest mail = MailRequest.builder()
                .fromAddress("hungtaithe12@gmail.com")
                .toAddress(request.getEmail())
                .subject("Account verification required")
                .content(message)
                .build();

        sendMail(mail);
        return userMapper.toUserResponse(user);
    }

    @Transactional
    public UserResponse update(String id, UserUpdateRequest request){

        var user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        String usename = user.getUsername();
        Andress andress = andressRepository.findById(user.getAndress().getId())
                .orElseThrow(() -> new AppException(ErrorCode.ANDRESS_NOT_EXIST));

        user = userMapper.toUpdateUser(request);
        user.setId(id);
        user.setUsername(usename);
        //Cập nhật password
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        //Cập nhật địa chỉ
        if(andress != null) {
            andress.setApartment_no(request.getAndress().getApartment_no());
            andress.setSweet_name(request.getAndress().getSweet_name());
            andress.setCity(request.getAndress().getCity());
            andress.setUpdated_at(LocalDate.now());
        }

        user.setAndress(andress);
        user = userRepository.saveAndFlush(user);

        return userMapper.toUserResponse(user);
    }

    public void SendMailResetPassword(String email){
        var user = userRepository.findByEmail(email);

        if(Objects.isNull(user))
            throw new AppException(ErrorCode.USER_NOT_EXIST);

        //Neu ton tai generate 1 password gom 8 ky tu
        String reset_password = generatePassword();
        user.setPassword(passwordEncoder.encode(reset_password));
        userRepository.saveAndFlush(user); //Luu mat khau xuong

        //Tien hanh gui mail
        String message = "<p>Below is the password we reset: " + reset_password + "</p>";

        MailRequest mailRequest = MailRequest
                .builder()
                .fromAddress("hungtaithe12@gmail.com")
                .toAddress(email)
                .subject("Reset password required")
                .content(message)
                .build();
        sendMail(mailRequest);
    }

    @Transactional
    public void delete(String id){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        userRepository.delete(user);
    }

    public UserResponse get(String id){
        var user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getAll(){
        var user = userRepository.findAll();
        return user.stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getByUsername(String username){
        var user = userRepository.findByUsername(username);
        if(Objects.isNull(user)){
            throw new AppException(ErrorCode.USER_NOT_EXIST);
        }
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getCustomer(String roles, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        var users = userRepository.findAllByRoleName(roles, pageable);

        return users.stream().map(userMapper::toUserResponse).toList();
    }

    public void verifyAccount(String token){
        User user = userRepository.findByToken_verify(token);

        if(Objects.isNull(user))
            throw new AppException(ErrorCode.USER_NOT_EXIST);

        LocalDate expire = user.getUpdated_at().plusDays(1); // Thời gian hết hạn là 1 ngày
        if(expire.isBefore(LocalDate.now())){
            throw new AppException(ErrorCode.LINK_EXPIRE);
        }

        user.setVerify(true);
        user.setToken_verify(null);
        user.setCreated_at(LocalDate.now());
        user = userRepository.save(user);
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

}
