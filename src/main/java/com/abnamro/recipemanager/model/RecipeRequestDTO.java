package com.abnamro.recipemanager.model;
/* Created by IndunilPrasanna */

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDTO {

    private String name;
    private boolean vegetarian;
    private int servings;
    private String instruction;
    private List<IngredientRequestDTO> ingredients;
}
