package com.abnamro.recipemanager.integration;
/* Created by IndunilPrasanna */

import com.abnamro.recipemanager.TestUtil;
import com.abnamro.recipemanager.model.Recipe;
import com.abnamro.recipemanager.repository.IngredientRepository;
import com.abnamro.recipemanager.repository.RecipeRepository;
import com.abnamro.recipemanager.service.RecipeService;
import com.abnamro.recipemanager.service.RecipeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeServiceImplIT {

    @Autowired
    RecipeService recipeService;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
        recipeService = new RecipeServiceImpl(recipeRepository, ingredientRepository);
    }

    @Test
    public void given_valid_recipe_should_save_successful(){
        Recipe recipe = TestUtil.mockRecipeData();
        Recipe result = recipeService.addRecipe(recipe);
        Assertions.assertEquals("rice",result.getName());
    }

    @Test
    public void given_valid_recipe_should_update_successful(){
        Recipe obj = recipeService.addRecipe(TestUtil.mockRecipeData());
        obj.setName("chicken");
        Recipe recipe = TestUtil.mockRecipeData();
        Recipe result = recipeService.updateRecipe(obj);
        Assertions.assertEquals("chicken", result.getName());
    }

    @Test
    public void given_valid_recipe_id_should_delete_and_return_true(){
        recipeService.addRecipe(TestUtil.mockRecipeData());
        Boolean result = recipeService.deleteRecipe(1L);
        Assertions.assertTrue(result);
    }

    @Test
    public void given_Invalid_recipe_id_should_return_false(){
        Boolean result = recipeService.deleteRecipe(5L);
        Assertions.assertFalse(result);
    }

    @Test
    public void given_valid_recipe_id_should_return_recipt_object(){
        recipeService.addRecipe(TestUtil.mockRecipeData());
        Optional<Recipe> result = recipeService.getRecipeById(1L);
        Assertions.assertEquals(1L, result.get().getId());
    }

    @Test
    public void given_Invalid_recipe_id_should_return_null(){
        Optional<Recipe> result = recipeService.getRecipeById(1L);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    public void given_page_request_should_return_page_recipe_object(){
        recipeService.addRecipe(TestUtil.mockRecipeData());
        recipeService.addRecipe(TestUtil.mockRecipeData());
        Page<Recipe> recipes = TestUtil.createMockRecipePage();
        Page<Recipe> result = recipeService.getAllRecipes(PageRequest.of(0, 10));
        Assertions.assertEquals(3, result.getTotalElements());
    }

    @Test
    public void given_page_request_should_return_selected_page_recipe_object_all_null(){
        Pageable pageable = PageRequest.of(0, 10);

        TestUtil.createRecipeList().forEach(recipe -> {recipeService.addRecipe(recipe);});

        Page<Recipe> recipeList = recipeService.filterRecipes(null, null, null,null, null, pageable);
        Assertions.assertEquals(6,recipeList.getTotalElements());
    }

    @Test
    public void given_page_request_should_return_selected_page_recipe_object_with_servings(){
        Pageable pageable = PageRequest.of(0, 10);

        TestUtil.createRecipeList().forEach(recipe -> {recipeService.addRecipe(recipe);});

        Page<Recipe> recipeList = recipeService.filterRecipes(null, null, null,7, null, pageable);
        Assertions.assertEquals(2,recipeList.getTotalElements());
        Assertions.assertEquals(8,recipeList.getContent().get(0).getServings());
    }

    @Test
    public void given_page_request_should_return_selected_page_recipe_object_with_isVegetarian(){
        Pageable pageable = PageRequest.of(0, 10);

        TestUtil.createRecipeList().forEach(recipe -> {recipeService.addRecipe(recipe);});

        Page<Recipe> recipeList = recipeService.filterRecipes(null, null, null,null, false, pageable);
        Assertions.assertEquals(4,recipeList.getTotalElements());
        Assertions.assertEquals(false,recipeList.getContent().get(0).isVegetarian());
    }

    @Test
    public void given_page_request_should_return_selected_page_recipe_object_with_instruction(){
        Pageable pageable = PageRequest.of(0, 10);

        TestUtil.createRecipeList().forEach(recipe -> {recipeService.addRecipe(recipe);});

        Page<Recipe> recipeList = recipeService.filterRecipes(null, null, "onion, chicken",null, null, pageable);
        Assertions.assertEquals(1,recipeList.getTotalElements());
        Assertions.assertEquals("onion, chicken",recipeList.getContent().get(0).getInstruction());
    }

}
