package vn.shop.economic_service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.AndressRepository;
import vn.shop.economic_service.dto.request.AndressRequest;
import vn.shop.economic_service.dto.response.AndressResonse;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.AndressMapper;

import java.util.Objects;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AndressService {
    AndressRepository andressRepository;
    AndressMapper andressMapper;

    @Transactional
    public AndressResonse update(String id, AndressRequest request){
        var andress = andressRepository.findById(id)
                .orElseThrow(() ->  new AppException(ErrorCode.ANDRESS_NOT_EXIST));

        andress = andressMapper.toUpdateAndress(request);
        andress.setId(id);

        return andressMapper.toAndressResonse(andress);
    }

    public AndressResonse get(String id){
        var andress = andressRepository.findById(id)
                .orElseThrow(() ->  new AppException(ErrorCode.ANDRESS_NOT_EXIST));
        return andressMapper.toAndressResonse(andress);
    }
}
