package com.abnamro.recipemanager.controller;
/* Created by IndunilPrasanna */

import com.abnamro.recipemanager.TestUtil;
import com.abnamro.recipemanager.model.Recipe;
import com.abnamro.recipemanager.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void given_valid_recipe_then_create_recipe_with_CREATED() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(TestUtil.mockRecipeData())));
        result.andExpect(status().isCreated());
    }

    @Test
    public void given_valid_id_then_return_recipe_with_OK() throws Exception {
        when(recipeService.getRecipeById(anyLong())).thenReturn(Optional.of(TestUtil.mockRecipeData()));
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/recipes/1"));
        result.andExpect(status().isOk());
    }

    @Test
    public void given_invalid_id_then_throws_not_found_exception() throws Exception {
        when(recipeService.getRecipeById(anyLong())).thenReturn(Optional.ofNullable(null));
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/recipes/4"));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void given_valid_id_then_delete_recipe_with_NO_CONTENT() throws Exception {
        when(recipeService.deleteRecipe(anyLong())).thenReturn(true);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/recipes/2"));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void given_invalid_id_when_delete_then_throws_not_found_exception() throws Exception {
        when(recipeService.deleteRecipe(anyLong())).thenReturn(false);
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/recipes/5"));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void given_paginations_then_return_page_recipes_with_OK() throws Exception {
        when(recipeService.getAllRecipes(any())).thenReturn(TestUtil.createMockRecipePage());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/recipes")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void given_valid_recipe_then_update_recipe_with_OK() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(TestUtil.mockRecipeData())));
        result.andExpect(status().isOk());
    }

    @Test
    public void given_valid_recipe_parameter_then_return_recipe_with_OK() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Recipe> mockPage = TestUtil.createMockRecipePage();
        when(recipeService.filterRecipes(anyString(), anyString(), anyString(), anyInt(), anyBoolean(), any())).thenReturn(mockPage);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/recipes/filter").param("servings", "4")
                        .param("includedIngredients", "potato")
                        .param("excludedIngredients", "ingredient3")
                        .param("instruction","potato")
                        .param("isVegetarian","true")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString().contains("potato");
    }

}
