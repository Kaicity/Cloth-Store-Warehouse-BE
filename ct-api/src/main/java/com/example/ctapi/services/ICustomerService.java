package com.example.ctapi.services;

import com.example.ctapi.dtos.Response.CustomerDto;
import com.example.ctapi.dtos.Response.CustomerInfoDto;
import com.example.ctcommondal.entity.CustomerEntity;

import java.util.Optional;

public interface ICustomerService {
    void addCustomer(CustomerDto customer);

    //Check username account of customer
    Optional<CustomerEntity> loginAccount(String phone, String password);

    //Create Customer Information when Order Shopping
    void addCustomerInfo(CustomerInfoDto customerInfo);
}
