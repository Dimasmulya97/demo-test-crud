package com.test.crud.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "users")
public class User {
    @Id
    private int id;
    private String name;
    private String username;
    private String password;
    private String role;
}
