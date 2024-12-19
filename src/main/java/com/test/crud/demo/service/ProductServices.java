package com.test.crud.demo.service;

import com.test.crud.demo.dto.ProductRequest;
import com.test.crud.demo.model.Product;
import com.test.crud.demo.repo.ProductRepositroy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServices {
    private ProductRepositroy productRepositroy;

    @Transactional
    @SneakyThrows
    public Product create(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .price(productRequest.getPrice())
                .build();
        productRepositroy.save(product);
        return  product;
    }

    public List<Product> findAll(){
        return productRepositroy.findAll();
    }

    public Optional<Product> findById(int id){
        return productRepositroy.findById(id);
    }

    public void deleteById(int id){
        Optional<Product> product = productRepositroy.findById(id);
        if (product.isPresent()){
            throw  new RuntimeException("ID Not Found");
        }
        productRepositroy.deleteById(id);
    }

    public Product update (int id,ProductRequest productRequest){
        Optional<Product> product = productRepositroy.findById(id);
        if (product.isEmpty()){
            throw new RuntimeException("Id not found");
        }
        product.get().setName(productRequest.getName());
        product.get().setPrice(productRequest.getPrice());
        Product product1 = productRepositroy.save(product.get());
        return product1;
    }
}
