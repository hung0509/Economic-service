package vn.shop.economic_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.CartProductRepository;
import vn.shop.economic_service.entity.CartProduct;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CartProductService {
    CartProductRepository cartProductRepository;

    public List<CartProduct> getAllCartProduct() {
        return cartProductRepository.findAll();
    }
}
