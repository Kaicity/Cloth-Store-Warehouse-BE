package com.example.ctapi.dtos.Response;

import com.example.ctapi.dtos.request.BaseSearchDto;
import lombok.Data;
import java.util.List;

@Data
public class ProductSearchDto extends BaseSearchDto<List<ProductDto>> {
    String idCompany;
    String status;
}
