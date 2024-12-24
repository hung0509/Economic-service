package vn.shop.economic_service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.SizeRepository;
import vn.shop.economic_service.dto.request.SizeRequest;
import vn.shop.economic_service.dto.response.SizeResponse;
import vn.shop.economic_service.mapper.SizeMapper;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SizeService {
    SizeRepository sizeRepository;
    SizeMapper sizeMapper;

    @Transactional
    public SizeResponse create(SizeRequest request){
        var size = sizeMapper.toSize(request);
        size = sizeRepository.save(size);
        return sizeMapper.toSizeResponse(size);
    }

    public List<SizeResponse> getAll(){
        var size = sizeRepository.findAll();
        return size.stream().map(sizeMapper::toSizeResponse).toList();
    }
}
