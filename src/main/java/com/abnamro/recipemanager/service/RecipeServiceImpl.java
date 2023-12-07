package com.abnamro.recipemanager.service;
/* Created by IndunilPrasanna */

import com.abnamro.recipemanager.model.Ingredient;
import com.abnamro.recipemanager.model.Recipe;
import com.abnamro.recipemanager.repository.IngredientRepository;
import com.abnamro.recipemanager.repository.RecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;


    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Page<Recipe> getAllRecipes(PageRequest pageRequest) {
        return recipeRepository.findAll(pageRequest);
    }

    @Override
    public Recipe addRecipe(Recipe recipe) {

        Set<Ingredient> set = new HashSet<>();

        recipe.getIngredients().forEach(ingredient -> {
            Ingredient existing = ingredientRepository.findByName(ingredient.getName());
            if (existing == null) {
                existing = ingredientRepository.save(ingredient);
            }

            set.add(existing);
        });

        recipe.setIngredients(set.stream().toList());

        return recipeRepository.save(recipe);
    }

    @Override
    public Recipe updateRecipe(Recipe recipe) {
        return recipeRepository.saveAndFlush(recipe);
    }

    @Override
    public Boolean deleteRecipe(Long id) {
        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public Optional<Recipe> getRecipeById(Long recipeId) {
        return recipeRepository.findById(recipeId);
    }

    @Override
    public Page<Recipe> filterRecipes(String includedIngredient, String excludeIngredient, String instruction, Integer servings, Boolean isVegetarian, Pageable pageable) {

        return recipeRepository.filterRecipes(includedIngredient, excludeIngredient, instruction, servings, isVegetarian, pageable);
    }
}
