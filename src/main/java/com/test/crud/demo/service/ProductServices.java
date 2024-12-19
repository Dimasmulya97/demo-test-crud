package com.test.crud.demo.service;

import com.test.crud.demo.constant.Constants;
import com.test.crud.demo.dto.ProductRequest;
import com.test.crud.demo.dto.Response;
import com.test.crud.demo.exception.BadRequestCustomException;
import com.test.crud.demo.exception.NotFoundException;
import com.test.crud.demo.exception.ValidatorException;
import com.test.crud.demo.model.Product;
import com.test.crud.demo.repo.ProductRepositroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServices {
    private final ProductRepositroy productRepositroy;

    @Transactional
    @SneakyThrows
    public Response<Object> create(ProductRequest productRequest){
        if (productRequest.getName().isEmpty()) {
            throw badRequestCustomException("Nama product tidak boleh kosong");
        }
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();
        productRepositroy.save(product);
        return Response.builder()
                .responseCode(Constants.Response.SUCCESS_CODE)
                .responseMessage(Constants.Response.SUCCESS_MESSAGE)
                .data(product)
                .build();
    }

    public Response<Object> findAll(){
        List<Product> productList= productRepositroy.findAll();
        return Response.builder()
                .responseCode(Constants.Response.SUCCESS_CODE)
                .responseMessage(Constants.Response.SUCCESS_MESSAGE)
                .data(productList)
                .build();
    }

    public Response<Object> findById(int id){
        Optional<Product> product =  productRepositroy.findById(id);
        if (product.isEmpty()){
            throw badRequestCustomException(Constants.Response.NOT_FOUND_MESSAGE);
        }
        return Response.builder()
                .responseCode(Constants.Response.SUCCESS_CODE)
                .responseMessage(Constants.Response.SUCCESS_MESSAGE)
                .data(product)
                .build();
    }

    public void deleteById(int id){
        Optional<Product> product = productRepositroy.findById(id);
        if (!product.isPresent()){
            throw  runtimeException(Constants.Response.NOT_FOUND_MESSAGE);
        }
        productRepositroy.deleteById(id);
    }

    public Response<Object> update (int id,ProductRequest productRequest){
        Optional<Product> product = productRepositroy.findById(id);
        if (product.isEmpty()){
            throw  runtimeException(Constants.Response.NOT_FOUND_MESSAGE);
        }
        product.get().setName(productRequest.getName());
        product.get().setPrice(productRequest.getPrice());
        Product product1 = productRepositroy.save(product.get());
        return Response.builder()
                .responseCode(Constants.Response.SUCCESS_CODE)
                .responseMessage(Constants.Response.SUCCESS_MESSAGE)
                .data(product1)
                .build();
    }


    public ValidatorException validatorException(String message, String value) {
        return new ValidatorException(message, value);
    }

    private BadRequestCustomException badRequestCustomException(String message) {
        return new BadRequestCustomException(message);
    }

    private NotFoundException notFoundException(String message) {
        return new NotFoundException(message);
    }

    private RuntimeException runtimeException(String message) {
        return new RuntimeException(message);
    }
}
