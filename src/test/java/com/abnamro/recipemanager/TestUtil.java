package com.abnamro.recipemanager;
/* Created by IndunilPrasanna */

import com.abnamro.recipemanager.model.Ingredient;
import com.abnamro.recipemanager.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static Recipe mockRecipeData(){
        Recipe obj = new Recipe();
        obj.setId(1L);
        obj.setIngredients(Arrays.asList(new Ingredient(1L, "cineman"), new Ingredient(3L, "rice")));
        obj.setName("rice");
        obj.setServings(4);
        obj.setVegetarian(true);
        return obj;

    }

    public static Page<Recipe> createMockRecipePage() {
        List<Recipe> recipes = Arrays.asList(
                new Recipe(1L,"rice", true, 6, "potato", Arrays.asList(new Ingredient(5L, "potato"), new Ingredient(6L, "rice"))),
                new Recipe(2L,"chicken curry", false, 8, "onion, chicken", Arrays.asList(new Ingredient(2L, "onion"), new Ingredient(8L, "chicken")))
        );

        return new PageImpl<>(recipes);
    }

    public static List<Recipe> createRecipeList(){
        List<Recipe> recipes = Arrays.asList(
                new Recipe(null,"rice", true, 6, "potato", Arrays.asList(new Ingredient( null,"salt"), new Ingredient(null, "rice"))),
                new Recipe(null,"chicken curry", false, 8, "onion, chicken", Arrays.asList(new Ingredient(null, "onion"), new Ingredient(null, "chicken")))
        );
        return recipes;
    }
}
