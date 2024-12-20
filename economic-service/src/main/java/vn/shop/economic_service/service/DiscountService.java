package vn.shop.economic_service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.DiscountRepository;
import vn.shop.economic_service.dto.request.DiscountRequest;
import vn.shop.economic_service.dto.response.DiscountResponse;
import vn.shop.economic_service.entity.Discount;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.DiscountMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class DiscountService {
    DiscountRepository discountRepository;
    DiscountMapper discountMapper;

    @NonFinal
    @Value("${jwt.valid-discount-duration}")
    private long VALID_DISCOUNT_DURATION;

    @Transactional
    public DiscountResponse create(DiscountRequest request) {
        Discount discount = discountMapper.toDiscount(request);
        discount.setCode(generateRandomCode());
        LocalDate start_at =request.getStart_at();
        discount.setStart_at(start_at);
        discount.setEnd_at(start_at.plusDays(VALID_DISCOUNT_DURATION));
        discount = discountRepository.save(discount);
        return discountMapper.toDiscountResponse(discount);
    }

    @Transactional
    public void delete(String id) {
        var discount = discountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_EXIST));
        discount.setActive(false);
    }

    public DiscountResponse get(String id) {
        var discount = discountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_EXIST));

        return discountMapper.toDiscountResponse(discount);
    }

    public List<DiscountResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        var discounts = discountRepository.findAll(pageable);

        return discounts.stream().map(discountMapper::toDiscountResponse).toList();
    }

    private String generateRandomCode(){
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    public long count(){
        return discountRepository.count();
    }
}
