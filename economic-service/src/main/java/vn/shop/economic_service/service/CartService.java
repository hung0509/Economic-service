package vn.shop.economic_service.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.*;
import vn.shop.economic_service.dto.request.CartProductResquest;
import vn.shop.economic_service.dto.response.CartProductResponse;
import vn.shop.economic_service.dto.response.CartResponse;
import vn.shop.economic_service.entity.*;
//import vn.shop.economic_service.entity.key.CartProductKey;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.CartMapper;
import vn.shop.economic_service.mapper.CartProductMapper;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CartService {
    CartRepository cartRepository;
    CartMapper cartMapper;
    CartProductMapper cartProductMapper;
    ProductRepository productRepository;
    CartProductRepository cartProductRepository;
    ProductSizeRepository productSizeRepository;

    public List<CartResponse> getAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        var carts = cartRepository.findAll(pageable);
        return carts.stream().map(cartMapper::toCartResponse).toList();
    }

    public CartProductResponse addProductinCart(CartProductResquest request){
        //Lấy ra giỏ hàng cần thêm sản phẩm
        int amount = request.getAmount();
        log.info("request: " + request.toString());

        Cart cart = cartRepository.findById(request.getCart_id())
                .orElseThrow(()-> new AppException(ErrorCode.CART_NOT_EXIST));

        Product product = productRepository.findByIdAndSize(request.getProduct_id(), request.getSize());

        log.info("product: " +product.toString());

        if(product != null) {
            //Lấy ra số lượng trong kho
            for(ProductSize productSize: product.getSizes()){
                log.info("productSize: " +productSize.toString());

                Size size = productSize.getSize();
                log.info("Size: "+ size.toString());
                if (size.getName().equals(request.getSize())) {
                    if (productSize.getStockQuantity() >= amount) {
                        //Tiền hành thêm product vào cart
                        //CartProductKey cartProductKey = new CartProductKey(product.getId(), cart.getId());
                        CartProduct cartProduct = cartProductRepository.findByProductByCart(request.getProduct_id(),
                                request.getCart_id(), request.getSize());
                        if(cartProduct != null){
                            int amount_initial = cartProduct.getAmount();
                            cartProduct.setAmount(amount + amount_initial);
                        }else{
                            cartProduct = CartProduct.builder()
                                    .product_pr(product)
                                    .cart_pr(cart)
                                    .amount(amount)
                                    .size(request.getSize())
                                    .build();
                        }
                        log.info(cartProduct.toString());
                        cartProduct = cartProductRepository.save(cartProduct); //Lưu sản phẩm vào giỏ hàng
                        log.info("cartProduct: " + cartProduct.toString());

                        //Cập nhật số lượng trong product
                        productSize.setStockQuantity(productSize.getStockQuantity() - amount);
                        productSizeRepository.save(productSize);

                        //Trả về CartProductResponse
                        var response = cartProductMapper.toCartProductResponse(cartProduct);
                        response.getProductCart().setSize(request.getSize());

                        return response;
                    } else
                        throw new AppException(ErrorCode.PRODUCT_NOT_ENOUGH);
                }
            }
        }else
            throw new AppException(ErrorCode.PRODUCT_NOT_EXIST);
        return null;
    }

    public CartResponse getCartById(String id){
        var cart = cartRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_EXIST));
        return cartMapper.toCartResponse(cart);
    }
}
