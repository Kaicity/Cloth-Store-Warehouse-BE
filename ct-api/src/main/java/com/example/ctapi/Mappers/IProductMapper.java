package com.example.ctapi.Mappers;


import com.example.ctapi.dtos.Response.ProductDto;
import com.example.ctcommondal.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;


@Mapper
public interface IProductMapper {

    IProductMapper INSTANCE = Mappers.getMapper(IProductMapper.class);

    ProductDto toProductDtoFromEntity(ProductEntity product);

    @Mapping(target = "category_id", source = "catelory.id")
    @Mapping(target = "company_id", source = "company.id")
    List<ProductDto> toProductDtoListFromEntityList(List<ProductEntity> productEntityList);

    @Mapping(target = "categoryID", source = "catelory.id")
    @Mapping(target = "companyID", source = "company.id")
    ProductEntity toProductEntityFromDto(ProductDto product);

    List<ProductEntity> toProductEntityListFromDtoList(List<ProductDto> productEntityList);
}
