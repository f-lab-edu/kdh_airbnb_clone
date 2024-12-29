package com.airbnb_clone.backend.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

// Data : Automatically generates boilerplate code for the class(getters, setters, toString, equals and so on)
@Data
// Entity : Indicates that this class is a JPA entity and will be mapped to a database table named User.
// Spring Data JPA will automatically recognize and manage this entity.
@Entity
public class UserEntity {
    @Id // Id marks the id field as the primary key for the database table.

    // Specifies how the primary key is generated.
    // GenerationType.IDENTITY : Relies on the database to generate a unique primary key value(e.g., an auto-increment column.)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String birthDate;
    private String phoneNumber;
    private String password;
}
