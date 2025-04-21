package com.simplesdental.product.infrastructure.mapper;

import com.simplesdental.product.domain.model.Category;
import com.simplesdental.product.infrastructure.dto.*;
import com.simplesdental.product.infrastructure.dto.request.CategoryRequest;
import com.simplesdental.product.infrastructure.dto.response.CategoryResponse;
import com.simplesdental.product.infrastructure.dto.response.CategoryWithoutProductsResponse;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResponse toResponse(Category category);

    CategoryResponse toResponse(CategoryOutput output);

    CategoryWithoutProductsResponse toSaveResponse(CategoryOutput output);

    CategoryOutput toResponseOutput(Category category);

    List<CategoryResponse> toResponseList(List<Category> categories);

    List<CategoryOutput> toResponseListOutput(List<Category> categories);

    Category toEntity(CategoryRequest request);

    @Mapping(target = "products", ignore = true)
    void updateEntityFromDto(CategoryRequest request, @MappingTarget Category entity);

}
