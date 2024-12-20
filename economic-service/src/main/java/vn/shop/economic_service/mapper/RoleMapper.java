package vn.shop.economic_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.shop.economic_service.dto.request.RoleRequest;
import vn.shop.economic_service.dto.response.RoleResponse;
import vn.shop.economic_service.entity.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
