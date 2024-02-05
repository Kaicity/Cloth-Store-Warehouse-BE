package com.example.ctapi.serviceImpl;

import com.example.ctapi.dtos.Response.CustomerDto;
import com.example.ctapi.dtos.Response.CustomerInfoDto;
import com.example.ctapi.Mappers.ICustomerMapper;
import com.example.ctapi.services.ICustomerService;
import com.example.ctcommondal.repository.ICustomerInfoRespository;
import com.example.ctcommondal.repository.ICustomerRepository;
import com.example.ctcommondal.entity.CustomerEntity;
import com.example.ctcommondal.entity.CustomerInfoEntity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ICustomerServiceImpl implements ICustomerService {
    private static final Logger logger = LoggerFactory.getLogger(IProductServiceImpl.class);
    private final ICustomerRepository customerRepository;
    private final ICustomerInfoRespository customerInfoRespository;

    @Override
    public void addCustomer(CustomerDto customer) {
        try {
            CustomerEntity customerEntity = ICustomerMapper.INSTANCE.toFromCustomerDto(customer);
            customerRepository.save(customerEntity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    //Check username account of customer
    @Override
    public Optional<CustomerEntity> loginAccount(String phone, String password) {
        try {
            return customerRepository.findOneByUsernameAndPassword(phone, password);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }

    //Create Customer Information when Order Shopping
    @Override
    public void addCustomerInfo(CustomerInfoDto customerInfo) {
        try {
            CustomerInfoEntity customerInfoEntity = ICustomerMapper.INSTANCE.toFromCustomerInfoDto(customerInfo);
            customerInfoRespository.save(customerInfoEntity);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw e;
        }
    }
}
