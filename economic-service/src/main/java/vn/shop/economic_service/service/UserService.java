package vn.shop.economic_service.service;

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
import vn.shop.economic_service.dto.request.UserCreateRequest;
import vn.shop.economic_service.dto.request.UserUpdateRequest;
import vn.shop.economic_service.dto.response.UserResponse;
import vn.shop.economic_service.entity.Andress;
import vn.shop.economic_service.entity.Cart;
import vn.shop.economic_service.entity.Role;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.UserMapper;

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

    @Transactional
    public UserResponse createUser(UserCreateRequest request) {
        var user = userMapper.toCreateUser(request);

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
                .total_money(0)
                .created_at(LocalDate.now())
                .updated_at(LocalDate.now())
                .user(user)
                .build();
        cartRepository.save(cart); //Khi tạo 1 người dụng tạo cho họ 1 giỏ hàng trống

        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user = userRepository.save(user);
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

}
