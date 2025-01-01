package vn.shop.economic_service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.PermissionRepository;
import vn.shop.economic_service.Repository.RoleRepository;
import vn.shop.economic_service.dto.request.RoleRequest;
import vn.shop.economic_service.dto.response.RoleResponse;
import vn.shop.economic_service.entity.Permission;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.RoleMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class  RoleService {
    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;

    @Transactional
    public RoleResponse create(RoleRequest request){
        var role = roleMapper.toRole(request);
        List<Permission> permissions = permissionRepository
                .findAllById(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    @Transactional
    public void delete(String id){
        var role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXIST));
        if(Objects.nonNull(role))
            roleRepository.deleteById(id);
    }

    public List<RoleResponse> getAll(){
        return roleRepository.findAll().stream()
                .map(roleMapper::toRoleResponse).toList();
    }
}
