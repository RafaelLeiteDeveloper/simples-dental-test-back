package com.simplesdental.product.infrastructure.mapper;

import com.simplesdental.product.domain.model.Category;
import com.simplesdental.product.domain.model.Product;
import com.simplesdental.product.infrastructure.dto.*;
import com.simplesdental.product.infrastructure.dto.request.ProductRequest;
import com.simplesdental.product.infrastructure.dto.response.ProductResponse;
import com.simplesdental.product.infrastructure.dto.response.ProductResponseV2;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponse toResponse(Product product);

    @Mapping(source = "categoryId", target = "category", qualifiedByName = "mapCategory")
    Product toEntity(ProductRequest dto);

    @Mapping(source = "categoryId", target = "category", qualifiedByName = "mapCategory")
    void updateEntityFromDto(ProductRequest dto, @MappingTarget Product entity);

    ProductResponse toResponse(ProductOutput output);

    ProductOutput toOutput(Product product);

    @Mapping(source = "id", target = "id")
    ProductResponseV2 toResponseV2(ProductOutput output);

    List<ProductResponseV2> toResponseV2List(List<ProductOutput> outputs);

    @Named("mapCategory")
    default Category mapCategory(Long categoryId) {
        if (categoryId == null) {
            return null;
        }
        Category category = new Category();
        category.setId(categoryId);
        return category;
    }

}