package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Recipe;
import com.example.demo.repository.RecipeRepository;

@CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowedHeaders = "*", allowCredentials = "true")
@RestController
@RequestMapping("/api")
public class RecipeController {

  @Autowired
  RecipeRepository recipeRepository;
  
  @CrossOrigin(origins = "http://localhost:3000", methods = {RequestMethod.OPTIONS, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE}, allowedHeaders = "*", allowCredentials = "true")
  @GetMapping("/recipes")
  public ResponseEntity<List<Recipe>> getAllRecipes(@RequestParam(required = false) String title) {
    try {
      List<Recipe> recipes = new ArrayList<Recipe>();

      if (title == null)
        recipeRepository.findAll().forEach(recipes::add);
      else
        recipeRepository.findByTitleContaining(title).forEach(recipes::add);

      if (recipes.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }

      return new ResponseEntity<>(recipes, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @GetMapping("/recipes/{id}")
  public ResponseEntity<Recipe> getRecipeById(@PathVariable("id") long id) {
    Optional<Recipe> recipeData = recipeRepository.findById(id);

    if (recipeData.isPresent()) {
      return new ResponseEntity<>(recipeData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }


  @PostMapping("/recipes")
  public ResponseEntity<Recipe> createRecipe(@RequestBody Recipe recipe) {
    try {
      Recipe _recipe = recipeRepository
          .save(new Recipe(recipe.getTitle(), recipe.getDescription(), false));
      return new ResponseEntity<>(_recipe, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

 
  @PutMapping("/recipes/{id}")
  public ResponseEntity<Recipe> updateRecipe(@PathVariable("id") long id, @RequestBody Recipe recipe) {
    Optional<Recipe> recipeData = recipeRepository.findById(id);

    if (recipeData.isPresent()) {
      Recipe _recipe = recipeData.get();
      _recipe.setTitle(recipe.getTitle());
      _recipe.setDescription(recipe.getDescription());
      _recipe.setPublished(recipe.isPublished());
      return new ResponseEntity<>(recipeRepository.save(_recipe), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

 
  @DeleteMapping("/recipes/{id}")
  public ResponseEntity<HttpStatus> deleteRecipe(@PathVariable("id") long id) {
    try {
      recipeRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


  @DeleteMapping("/recipes")
  public ResponseEntity<HttpStatus> deleteAllRecipes() {
    try {
      recipeRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

  }

 
  @GetMapping("/recipes/published")
  public ResponseEntity<List<Recipe>> findByPublished() {
    try {
      List<Recipe> recipes = recipeRepository.findByPublished(true);

      if (recipes.isEmpty()) {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
      }
      return new ResponseEntity<>(recipes, HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
