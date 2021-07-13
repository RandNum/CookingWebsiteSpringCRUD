package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Recipe;
import com.example.demo.repository.RecipeRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {
	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	@Autowired
	RecipeRepository recipeRepository;
	
	
	
	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('CHEF') or hasRole('ADMIN')")
//	public String userAccess() {
//		return "User Content.";
//	}
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

	@GetMapping("/mod")
	@PreAuthorize("hasRole('CHEF')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return "Admin Board.";
	}
}