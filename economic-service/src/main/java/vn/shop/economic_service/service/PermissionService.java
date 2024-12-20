package vn.shop.economic_service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.PermissionRepository;
import vn.shop.economic_service.entity.Permission;
import vn.shop.economic_service.dto.request.PermissionRequest;
import vn.shop.economic_service.dto.response.PermissionResponse;
import vn.shop.economic_service.exception.AppException;
import vn.shop.economic_service.exception.ErrorCode;
import vn.shop.economic_service.mapper.PermissionMapper;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;

    @Transactional
    public PermissionResponse create(PermissionRequest request) {
        var permission = permissionMapper.toPermission(request);

        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    @Transactional
    public void delete(String id){
        var permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));

        if(!Objects.isNull(permission))
            permissionRepository.deleteById(id);
    }

    public PermissionResponse get(String id){
        var permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXIST));
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll(){
        var permission = permissionRepository.findAll();

        return permission.stream()
                .map(permissionMapper::toPermissionResponse)
                .collect(Collectors.toList());
    }
}
