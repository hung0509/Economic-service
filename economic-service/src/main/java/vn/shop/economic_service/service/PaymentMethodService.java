package vn.shop.economic_service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.PaymentMethodRepository;
import vn.shop.economic_service.dto.request.PaymentMethodRequest;
import vn.shop.economic_service.dto.response.PaymentMethodResonse;
import vn.shop.economic_service.entity.PaymentMethod;
import vn.shop.economic_service.mapper.PaymentMapper;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PaymentMethodService {
    PaymentMethodRepository paymentMethodRepository;
    PaymentMapper paymentMapper;

    @Transactional
    public PaymentMethodResonse create(PaymentMethodRequest request){
        PaymentMethod paymentMethod = paymentMapper.toPaymentMethod(request);

        paymentMethod = paymentMethodRepository.save(paymentMethod);

        return paymentMapper.toPaymentResonse(paymentMethod);
    }

    public List<PaymentMethodResonse> getAll(){
        return paymentMethodRepository.findAll()
                .stream().map(paymentMapper::toPaymentResonse).toList();
    }
}
