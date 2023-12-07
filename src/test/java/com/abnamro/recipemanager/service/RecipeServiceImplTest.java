package com.abnamro.recipemanager.service;
/* Created by IndunilPrasanna */


import com.abnamro.recipemanager.TestUtil;
import com.abnamro.recipemanager.model.Recipe;
import com.abnamro.recipemanager.repository.IngredientRepository;
import com.abnamro.recipemanager.repository.RecipeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    private RecipeService recipeService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, ingredientRepository);
    }

    @Test
    public void given_valid_recipe_should_save_successful(){
        Recipe recipe = TestUtil.mockRecipeData();
        when(recipeRepository.save(any())).thenReturn(recipe);
        Recipe result = recipeService.addRecipe(recipe);
        Assertions.assertEquals(1, result.getIngredients().size());
    }

    @Test
    public void given_valid_recipe_should_update_successful(){
        Recipe recipe = TestUtil.mockRecipeData();
        when(recipeRepository.saveAndFlush(any())).thenReturn(recipe);
        Recipe result = recipeService.updateRecipe(recipe);
        Assertions.assertEquals("rice", result.getName());
    }

    @Test
    public void given_valid_recipe_id_should_delete_and_return_true(){
        when(recipeRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(anyLong());
        Boolean result = recipeService.deleteRecipe(1L);
        Assertions.assertTrue(result);
    }

    @Test
    public void given_Invalid_recipe_id_should_return_false(){
        when(recipeRepository.existsById(anyLong())).thenReturn(false);
        Boolean result = recipeService.deleteRecipe(5L);
        Assertions.assertFalse(result);
    }

    @Test
    public void given_valid_recipe_id_should_return_recipt_object(){
        Recipe recipe = TestUtil.mockRecipeData();
        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.of(recipe));
        Optional<Recipe> result = recipeService.getRecipeById(1L);
        Assertions.assertEquals(1L, result.get().getId());
    }

    @Test
    public void given_Invalid_recipe_id_should_return_null(){
        when(recipeRepository.findById(anyLong())).thenReturn(java.util.Optional.ofNullable(null));
        Optional<Recipe> result = recipeService.getRecipeById(1L);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void given_page_request_should_return_page_recipe_object(){
        Page<Recipe> recipes = TestUtil.createMockRecipePage();
        when(recipeRepository.findAll(any(PageRequest.class))).thenReturn(recipes);
        Page<Recipe> result = recipeService.getAllRecipes(PageRequest.of(0, 10));
        Assertions.assertEquals(2, result.getTotalElements());
    }

    @Test
    public void given_page_request_should_return_selected_page_recipe_object(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recipe> mockPage = new PageImpl<>(Collections.emptyList());

        when(recipeRepository.filterRecipes(anyString(), anyString(), anyString(), anyInt(), anyBoolean(), any()))
                .thenReturn(mockPage);

        recipeService.filterRecipes("potato", "chicken", "curry", 4, true, pageable);

        verify(recipeRepository, times(1))
                .filterRecipes("potato", "chicken", "curry", 4, true, pageable);
    }


}
