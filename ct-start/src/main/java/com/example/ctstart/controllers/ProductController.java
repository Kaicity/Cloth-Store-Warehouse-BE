package com.example.ctstart.controllers;

import com.example.ctapi.dtos.Response.ProductDto;
import com.example.ctapi.dtos.Response.ResponseDto;
import com.example.ctapi.services.IProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/Food")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(IProductService.class);

    @Autowired
    private final IProductService productService;

    @PostMapping("/findAll")
    private ResponseEntity<?> seachAllProduct(HttpServletRequest request) {
        try {

            var result = productService.getAllProductUseBaseSearch();
            return ResponseEntity.ok(new ResponseDto(List.of("Successful for find!"), HttpStatus.OK.value(), result));

        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseDto(List.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @PostMapping("getAllProductByIds")
    private ResponseEntity<?> getAllProductByIds(@RequestBody List<String>productIds){
        try{
            var rs = productService.getAllProductByIds(productIds);

            return ResponseEntity.ok(new ResponseDto(
                    List.of("ok"),
                    HttpStatus.OK.value(),
                    rs
            ));
        }catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseDto(
                    List.of(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    null
            ));
        }
    }

    @PostMapping("/addProduct")
    private ResponseEntity<?> newProduct(@RequestBody ProductDto product) {
        int i = 0;
        try {
            productService.addProduct(product);
            return ResponseEntity.ok(new ResponseDto(List.of("Add product to success"), HttpStatus.CREATED.value(), product));
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseDto(List.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @DeleteMapping("/deleteProduct/{productId}")
    private ResponseEntity<?> deleteProduct(@PathVariable String productId) {
        int i = 0;
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(new ResponseDto(List.of("delete product to success"), HttpStatus.ACCEPTED.value(), productId + "-> is deleted"));
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseDto(List.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.value(), productId + "can not delete"));
        }
    }

    @PostMapping("/seachCode")
    private ResponseEntity<?> seachProductCode(@RequestBody Map<String, String> requestBody) {
        int i = 0;
        String productCode = requestBody.get("code");
        try {
            var result = productService.seachProductForCode(productCode);
            return ResponseEntity.ok(new ResponseDto(List.of("Seach success"), HttpStatus.CREATED.value(), result));
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseDto(List.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @PostMapping("/seachName")
    private ResponseEntity<?> seachProductName(@RequestBody Map<String, String> requestBody) {
        int i = 0;
        String productName = requestBody.get("name");
        try {
            var result = productService.seachProductForName(productName);
            return ResponseEntity.ok(new ResponseDto(List.of("Seach success"), HttpStatus.CREATED.value(), result));
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.ok(new ResponseDto(List.of(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }
}
