package vn.shop.economic_service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.*;
import vn.shop.economic_service.dto.request.OrderDetailRequest;
import vn.shop.economic_service.dto.request.OrderRequest;
import vn.shop.economic_service.dto.response.OrderResponse;
import vn.shop.economic_service.entity.OrderDetail;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.OrderDetailMapper;
import vn.shop.economic_service.mapper.OrderMapper;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    OrderMapper orderMapper;
    OrderDetailMapper orderDetailMapper;
    DeliveryMethodRepository deliveryMethodRepository;
    PaymentMethodRepository paymentMethodRepository;
    UserRepository userRepository;
    ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        var order = orderMapper.toOrder(request);
        order.setOrder_date(LocalDate.now());
        order.setStatus_delivery("On the way");
        order.setStatus_payment("Unpaid");

        var delivery = deliveryMethodRepository.findById(request.getDelivery_id())
                .orElseThrow(() -> new AppException(ErrorCode.DELIVERY_METHOD_NOT_EXIST));

        var payment = paymentMethodRepository.findById(request.getPayment_id())
                .orElseThrow(() -> new AppException(ErrorCode.PAYMENT_METHOD_NOT_EXIST));

        var user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        order.setDeliveryMethod(delivery);
        order.setPaymentMethod(payment);
        order.setUser(user);

        //List Order Product
        Set<OrderDetail> orderDetails = new HashSet<>();
        var orderDetailList = request.getOrderDetails();
        for(OrderDetailRequest orderDetailRequest: orderDetailList){
            var orderDetail = orderDetailMapper.toOrderDetail(orderDetailRequest);
            var product = productRepository.findById(orderDetailRequest.getProduct_id())
                    .orElseThrow(()->new AppException(ErrorCode.PRODUCT_NOT_EXIST));
            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);
        }

        order.setOrderDetails(orderDetails);
        order = orderRepository.save(order);
        var orderResponse = orderMapper.toOrderResponse(order);
        orderResponse.setFullname(user.getLastname() + user.getLastname());
        return orderResponse;
    }

    public List<OrderResponse> getAll(){
        var orders = orderRepository.findAll();
        var orderResponses = orders.stream().map(orderMapper::toOrderResponse).toList();

        for(int i = 0; i < orderResponses.size(); i++){
            orderResponses.get(i).setFullname(orders.get(i).getUser().getLastname()
                    + orders.get(i).getUser().getFirstname());
        }

        return orderResponses;
    }

}
