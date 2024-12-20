package vn.shop.economic_service.service;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vn.shop.economic_service.Repository.CategoryRepository;
import vn.shop.economic_service.dto.request.CategoryRequest;
import vn.shop.economic_service.dto.response.CategoryResponse;
import vn.shop.economic_service.mapper.CategoryMapper;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    @Transactional
    public CategoryResponse create(CategoryRequest request) {
        var category = categoryMapper.toCategory(request);

        category = categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    public List<CategoryResponse> getAll() {
        var categories = categoryRepository.findAll();

        List<CategoryResponse> list = categories.stream()
                .map(categoryMapper::toCategoryResponse).toList();
        for(int i = 0; i < categories.size(); i++){
            list.get(i).setQuantity(categories.get(i).getProducts().size());
        }

        return list;
    }
}
