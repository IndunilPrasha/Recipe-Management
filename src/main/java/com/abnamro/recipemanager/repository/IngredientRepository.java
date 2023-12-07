package com.abnamro.recipemanager.repository;
/* Created by IndunilPrasanna */

import com.abnamro.recipemanager.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Ingredient findByName(String name);
}
