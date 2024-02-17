package com.example.ctapi.services;

import com.example.ctapi.dtos.Response.CustomerDto;
import com.example.ctapi.dtos.Response.CustomerSearchDto;
import com.example.ctapi.dtos.Response.SupplierDto;
import com.example.ctapi.dtos.Response.SupplierSearchDto;

public interface ISupplierService {
    void CreateSupplier(SupplierDto supplier);
    void deleteSupplierById(String id);

    void updateSupplier(SupplierDto supplier);

    SupplierDto getSupplierById(String id);

    SupplierSearchDto getAllSupplierBaseSearch();
}
