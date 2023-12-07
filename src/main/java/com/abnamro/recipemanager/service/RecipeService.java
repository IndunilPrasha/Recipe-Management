package com.abnamro.recipemanager.service;
/* Created by IndunilPrasanna */

import com.abnamro.recipemanager.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface RecipeService {

    Page<Recipe> getAllRecipes(PageRequest pageRequest);

    Recipe addRecipe(Recipe recipe);

    Recipe updateRecipe(Recipe recipe);

    Boolean deleteRecipe(Long recipeId);

    Optional<Recipe> getRecipeById(Long recipeId);

    Page<Recipe> filterRecipes(String includedIngredient, String excludeIngredient, String instruction, Integer servings, Boolean isVegetarian, Pageable pageable);
}
