package com.test.crud.demo.service;

import com.test.crud.demo.constant.Constants;
import com.test.crud.demo.dto.ProductRequest;
import com.test.crud.demo.dto.Response;
import com.test.crud.demo.dto.UserRequest;
import com.test.crud.demo.exception.BadRequestCustomException;
import com.test.crud.demo.model.Product;
import com.test.crud.demo.model.User;
import com.test.crud.demo.repo.ProductRepositroy;
import com.test.crud.demo.repo.UserRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductServices productServices;

    @Mock
    private ProductRepositroy productRepositroy;

    private final ProductRequest productRequest = ProductRequest.builder()
            .name("Handphone")
            .price(100000)
            .build();

    @Test
    @DisplayName("should return success response")
    public void testCreate_product_success() {
        Product product = new Product();
        product.setId(1);
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());

        when(productRepositroy.save(any(Product.class))).thenReturn(product);

        Response<Object> response = productServices.create(productRequest);
        assertNotNull(response);
        assertEquals(Constants.Response.SUCCESS_CODE, response.getResponseCode());
        assertEquals(Constants.Response.SUCCESS_MESSAGE, response.getResponseMessage());
        assertNotNull(response.getData());

        verify(productRepositroy, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("should return failure response when product creation fails")
    public void testCreate_product_failure() {
        when(productRepositroy.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productServices.create(productRequest);
        });

        assertNotNull(exception);
        assertEquals("Database error", exception.getMessage());

        verify(productRepositroy, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("should return failure response when product name is invalid")
    public void testCreate_product_validation_failure() {
        ProductRequest invalidRequest = new ProductRequest();
        invalidRequest.setName("");
        invalidRequest.setPrice(productRequest.getPrice());

        BadRequestCustomException exception = assertThrows(
                BadRequestCustomException.class,
                () -> productServices.create(invalidRequest)
        );

        assertEquals("Nama product tidak boleh kosong", exception.getMessage());

        verifyNoInteractions(productRepositroy);
    }



    @Test
    @DisplayName("should return success response")
    public void testFindAll_product_success() {
        List<Product> mockProducts = Arrays.asList(new Product(), new Product());
        when(productRepositroy.findAll()).thenReturn(mockProducts);

        Response<Object> response = productServices.findAll();

        assertNotNull(response);
        assertEquals(Constants.Response.SUCCESS_CODE, response.getResponseCode());
        assertEquals(Constants.Response.SUCCESS_MESSAGE, response.getResponseMessage());
        assertNotNull(response.getData());
        assertEquals(2, ((List<?>) response.getData()).size());

        verify(productRepositroy, times(1)).findAll();
        verifyNoMoreInteractions(productRepositroy);
    }

    @Test
    @DisplayName("should return success response when product is found")
    public void testFindById_success() {
        int productId = 1;
        Product product = new Product();
        product.setId(productId);
        product.setName(productRequest.getName());
        product.setPrice(productRequest.getPrice());

        when(productRepositroy.findById(productId)).thenReturn(Optional.of(product));

        Response<Object> response = productServices.findById(productId);

        assertNotNull(response);
        assertEquals(Constants.Response.SUCCESS_CODE, response.getResponseCode());
        assertEquals(Constants.Response.SUCCESS_MESSAGE, response.getResponseMessage());
        assertNotNull(response.getData());

        verify(productRepositroy, times(1)).findById(productId);
    }

    @Test
    @DisplayName("should throw exception when product is not found")
    public void testFindById_error() {
        int productId = 1;

        when(productRepositroy.findById(productId)).thenReturn(Optional.empty());

        BadRequestCustomException exception = assertThrows(
                BadRequestCustomException.class,
                () -> productServices.findById(productId)
        );

        assertEquals(Constants.Response.NOT_FOUND_MESSAGE, exception.getMessage());

        verify(productRepositroy, times(1)).findById(productId);
    }

    @Test
    @DisplayName("should throw RuntimeException when product ID is not found")
    public void testDeleteById_error_productNotFound() {
        int nonExistentProductId = 999; // ID yang tidak ada

        when(productRepositroy.findById(nonExistentProductId)).thenReturn(Optional.empty());

        // Call the service
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productServices.deleteById(nonExistentProductId);
        });

        // Verify the exception message
        assertEquals(Constants.Response.NOT_FOUND_MESSAGE, exception.getMessage());

        // Verify repository interaction (should not call deleteById)
        verify(productRepositroy, never()).deleteById(nonExistentProductId);
    }


    @Test
    @DisplayName("should delete product by ID successfully")
    public void testDeleteById_success() {
        int existingProductId = 1; // ID yang ada
        Product product = new Product(existingProductId, productRequest.getName(), productRequest.getPrice());

        when(productRepositroy.findById(existingProductId)).thenReturn(Optional.of(product));
        doNothing().when(productRepositroy).deleteById(existingProductId);

        // Call the service
        productServices.deleteById(existingProductId);

        // Verify repository interaction
        verify(productRepositroy, times(1)).deleteById(existingProductId);
    }


    @Test
    @DisplayName("should update product successfully")
    public void testUpdate_product_success() {
        int existingProductId = 1;
        ProductRequest updatedProductRequest = new ProductRequest();
        updatedProductRequest.setName(productRequest.getName());
        updatedProductRequest.setPrice(productRequest.getPrice());

        Product existingProduct = new Product();
        existingProduct.setId(existingProductId);
        existingProduct.setName(productRequest.getName());
        existingProduct.setPrice(productRequest.getPrice());

        when(productRepositroy.findById(existingProductId)).thenReturn(Optional.of(existingProduct));
        when(productRepositroy.save(any(Product.class))).thenReturn(existingProduct);

        Response<Object> response = productServices.update(existingProductId, updatedProductRequest);

        assertNotNull(response);
        assertEquals(Constants.Response.SUCCESS_CODE, response.getResponseCode());
        assertEquals(Constants.Response.SUCCESS_MESSAGE, response.getResponseMessage());
        Product updatedProduct = (Product) response.getData();
        assertEquals(updatedProductRequest.getName(), updatedProduct.getName());
        assertEquals(updatedProductRequest.getPrice(), updatedProduct.getPrice());

        verify(productRepositroy, times(1)).findById(existingProductId);
        verify(productRepositroy, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("should throw RuntimeException when product ID is not found")
    public void testUpdate_product_notFound() {
        int nonExistentProductId = 999;
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName(productRequest.getName());
        productRequest.setPrice(productRequest.getPrice());

        when(productRepositroy.findById(nonExistentProductId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productServices.update(nonExistentProductId, productRequest);
        });

        assertEquals(Constants.Response.NOT_FOUND_MESSAGE, exception.getMessage());

        verify(productRepositroy, times(1)).findById(nonExistentProductId);
        verify(productRepositroy, never()).save(any(Product.class));
    }

}
