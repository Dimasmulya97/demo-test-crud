package com.test.crud.demo.controller;

import com.test.crud.demo.dto.ProductRequest;
import com.test.crud.demo.model.Product;
import com.test.crud.demo.service.ProductServices;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
//@RequiredArgsConstructor
public class ProductController {

    @Autowired
    private  ProductServices productServices;

    @GetMapping("/findAll")
    public List<Product> ambilSemua(){
        return productServices.findAll();
    }

    @PostMapping("/create")
    public Product buatData(@RequestBody ProductRequest productRequest){
        return productServices.create(productRequest);
    }

    @PutMapping("/update")
    public Product update(@RequestParam("id") int id,@RequestBody ProductRequest productRequest){
        return  productServices.update(id,productRequest);
    }

    @DeleteMapping("/delete")
    public String delete(@RequestParam("id") int id){
        productServices.deleteById(id);
        return id +" sudah dihapus";
    }
}
