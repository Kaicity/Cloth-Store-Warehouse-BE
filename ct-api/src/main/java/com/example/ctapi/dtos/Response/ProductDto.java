package com.example.ctapi.dtos.Response;

import com.example.ctapi.dtos.bussinessLogic.CreateRandomID;
import com.example.ctcommon.enums.SpecificationProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@AllArgsConstructor
@Data
public class ProductDto {
    private String id;
    private String code;
    private String name;
    private Double price;
    private String status;
    private String description;
    private String image;
    @Enumerated(EnumType.STRING)
    private SpecificationProduct specification;
    private CateloryDto catelory;
    private CompanyDto company;
    List<ColorsDto> colors;
    List<SizesDto> sizes;

    public ProductDto(){
        this.id = CreateRandomID.generatingUID();
        this.code = CreateRandomID.generateRandomId();
    }
    public ProductDto(String id){
        this.id = id;
    }
}

