package vn.shop.economic_service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.DeliveryMethodRepository;
import vn.shop.economic_service.dto.request.DeliveryRequest;
import vn.shop.economic_service.dto.response.DeliveryResponse;
import vn.shop.economic_service.mapper.DeliveryMapper;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class DeliveryService {
    DeliveryMethodRepository deliveryMethodRepository;
    DeliveryMapper deliveryMapper;

    @Transactional
    public DeliveryResponse create(DeliveryRequest request){
        var delivery = deliveryMapper.toDeliveryMethod(request);

        return deliveryMapper.toDeliveryResponse(deliveryMethodRepository.save(delivery));
    }

    public List<DeliveryResponse> getAll(){
        return deliveryMethodRepository.findAll()
                .stream().map(deliveryMapper::toDeliveryResponse).toList();
    }
}
