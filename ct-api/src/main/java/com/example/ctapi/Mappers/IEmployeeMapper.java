package com.example.ctapi.Mappers;

import com.example.ctapi.dtos.Response.EmployeeDto;
import com.example.ctcommondal.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IEmployeeMapper {

    IEmployeeMapper INSTANCE = Mappers.getMapper(IEmployeeMapper.class);

    @Mapping(target = "agency.id", source = "agencyId")
    EmployeeDto toEmployeeDtoFromEntity(EmployeeEntity employee);
}
