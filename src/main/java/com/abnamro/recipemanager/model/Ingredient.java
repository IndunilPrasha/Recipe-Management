package com.abnamro.recipemanager.model;
/* Created by IndunilPrasanna */

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "ingredients")
    private List<Recipe> recipes;

    public Ingredient(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
