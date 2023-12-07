package com.abnamro.recipemanager.controller;
/* Created by IndunilPrasanna */

import com.abnamro.recipemanager.model.Ingredient;
import com.abnamro.recipemanager.model.IngredientRequestDTO;
import com.abnamro.recipemanager.model.Recipe;
import com.abnamro.recipemanager.model.RecipeRequestDTO;
import com.abnamro.recipemanager.service.RecipeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Page<Recipe> recipes = recipeService.getAllRecipes(PageRequest.of(page, size));

        return ResponseEntity.ok(recipes.toList());
    }

//    @PostMapping
//    public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
//        Recipe createdRecipe = recipeService.addRecipe(recipe);
//        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
//    }

    @PostMapping
    public ResponseEntity<Recipe> createRecipe(@RequestBody RecipeRequestDTO recipeDTO) {
        Recipe createdRecipe = recipeService.addRecipe(convertDTOToEntity(recipeDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);
    }

    private Recipe convertDTOToEntity(RecipeRequestDTO recipeDTO) {
        Recipe recipe = new Recipe();
        recipe.setName(recipeDTO.getName());
        recipe.setVegetarian(recipeDTO.isVegetarian());
        recipe.setServings(recipeDTO.getServings());
        recipe.setInstruction(recipeDTO.getInstruction());

        List<Ingredient> ingredients = recipeDTO.getIngredients().stream()
                .map(this::convertIngredientDTOToEntity)
                .collect(Collectors.toList());
        recipe.setIngredients(ingredients);

        return recipe;
    }

    private Ingredient convertIngredientDTOToEntity(IngredientRequestDTO ingredientDTO) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(ingredientDTO.getName());
        return ingredient;
    }

    @PutMapping
    public Recipe updateRecipe(@RequestBody Recipe recipe){
        return recipeService.updateRecipe(recipe);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        if (recipeService.deleteRecipe(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        return recipe.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/filter")
    public Page<Recipe> searchRecipes(
            @RequestParam(required = false) String includedIngredients,
            @RequestParam(required = false) String excludeIngredient,
            @RequestParam(required = false) String instruction,
            @RequestParam(required = false) Integer servings,
            @RequestParam(required = false) Boolean isVegetarian,
            Pageable pageable) {
        return recipeService.filterRecipes(includedIngredients,excludeIngredient,instruction, servings, isVegetarian, pageable);
    }
}
