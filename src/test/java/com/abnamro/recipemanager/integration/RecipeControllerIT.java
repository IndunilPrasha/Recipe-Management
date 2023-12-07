package com.abnamro.recipemanager.integration;
/* Created by IndunilPrasanna */

import com.abnamro.recipemanager.TestUtil;
import com.abnamro.recipemanager.model.Recipe;
import com.abnamro.recipemanager.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.Collections;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RecipeService recipeService;

    @Test
    public void given_valid_recipe_then_create_recipe_with_CREATED() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(TestUtil.mockRecipeData())));
        result.andExpect(status().isCreated());
    }

    @Test
    public void given_valid_id_then_return_recipe_with_OK() throws Exception {
        recipeService.addRecipe(TestUtil.mockRecipeData());
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/recipes/1"));
        result.andExpect(status().isOk());
    }

    @Test
    public void given_invalid_id_then_throws_not_found_exception() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/v1/recipes/4"));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void given_valid_id_then_delete_recipe_with_NO_CONTENT() throws Exception {
        recipeService.addRecipe(TestUtil.mockRecipeData());
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/recipes/1"));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void given_invalid_id_when_delete_then_throws_not_found_exception() throws Exception {
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/recipes/5"));
        result.andExpect(status().isNotFound());
    }

    @Test
    public void given_paginations_then_return_page_recipes_with_OK() throws Exception {
        recipeService.addRecipe(TestUtil.mockRecipeData());
        recipeService.addRecipe(TestUtil.mockRecipeData());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/recipes")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void given_valid_recipe_then_update_recipe_with_OK() throws Exception {
        Recipe obj = recipeService.addRecipe(TestUtil.mockRecipeData());
        obj.setName("updated name");
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/v1/recipes").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsBytes(obj)));
        result.andExpect(status().isOk()).andReturn().getResponse().getContentAsString().contains("updated name");
    }

    @Test
    public void given_valid_recipe_parameter_then_return_recipe_with_OK() throws Exception {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Recipe> mockPage = new PageImpl<>(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/recipes/filter")
                        .param("servings", "4")
                        .param("includedIngredients", "ingredient1", "ingredient2")
                        .param("excludedIngredients", "ingredient3", "ingredient4")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk());
    }
}
