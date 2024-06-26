package com.example.ctapi.services;

import com.example.ctapi.dtos.Response.ProductDto;
import com.example.ctapi.dtos.Response.ProductSearchDto;

import java.util.List;

public interface IProductService {

    ProductSearchDto getAllProductUseBaseSearch();

    List<ProductDto> getAllProductByIds(List<String> productIds);

    void addProduct(ProductDto product);

    void deleteProduct(String id);

    void updateProduct(ProductDto product);

    ProductDto seachProductForCode(String code);

    ProductSearchDto seachProductForName(String name);

}
