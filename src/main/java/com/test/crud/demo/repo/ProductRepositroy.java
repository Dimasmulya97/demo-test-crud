package com.test.crud.demo.repo;

import com.test.crud.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepositroy extends JpaRepository<Product,Integer> {
}
