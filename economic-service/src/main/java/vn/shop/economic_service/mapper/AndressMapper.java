package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.shop.economic_service.dto.request.AndressRequest;
import vn.shop.economic_service.dto.response.AndressResonse;
import vn.shop.economic_service.entity.Andress;

@Mapper(componentModel = "spring")
public interface AndressMapper {
    Andress toUpdateAndress(AndressRequest request);

    Andress toCreateAndress(AndressRequest request);

    AndressResonse toAndressResonse(Andress andress);
}
