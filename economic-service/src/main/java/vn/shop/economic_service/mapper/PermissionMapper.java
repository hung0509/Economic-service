package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import vn.shop.economic_service.entity.Permission;
import vn.shop.economic_service.dto.request.PermissionRequest;
import vn.shop.economic_service.dto.response.PermissionResponse;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
