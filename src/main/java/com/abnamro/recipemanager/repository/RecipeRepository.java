package com.abnamro.recipemanager.repository;
/* Created by IndunilPrasanna */

import com.abnamro.recipemanager.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    @Query(value = """
            SELECT distinct r.* FROM recipe r 
                        JOIN recipe_ingredient ri ON r.id = ri.recipe_id 
                        JOIN ingredient i ON ri.ingredient_id = i.id 
                        WHERE 
            (CASE WHEN :servings IS NOT NULL THEN r.SERVINGS >=:servings ELSE true END) 
            AND 
            (CASE WHEN :isVegetarian IS NOT NULL THEN r.vegetarian = :isVegetarian ELSE true END) 
            AND 
                     
            (CASE WHEN :instruction IS NOT NULL THEN r.INSTRUCTION like  CONCAT('%', :instruction, '%') ELSE true END) 
                     
                     
            AND  (CASE WHEN :ingredientNameInclude IS NOT NULL THEN i.name = :ingredientNameInclude ELSE true END) 
                        AND r.id NOT IN ( 
                           SELECT DISTINCT r.id 
                           FROM recipe r 
                           JOIN recipe_ingredient ri ON r.id = ri.recipe_id 
                           JOIN ingredient i ON ri.ingredient_id = i.id 
                           WHERE (CASE WHEN :ingredientNameExclude IS NOT NULL THEN i.name =:ingredientNameExclude ELSE false END) 
                        ) 
            """,


            nativeQuery = true)
    Page<Recipe> filterRecipes(@Param("ingredientNameInclude") String ingredientNameInclude,
                               @Param("ingredientNameExclude") String ingredientNameExclude,
                               @Param("instruction") String instruction,
                               @Param("servings") Integer servings,
                               @Param("isVegetarian") Boolean isVegetarian,
                               Pageable pageable);

}
