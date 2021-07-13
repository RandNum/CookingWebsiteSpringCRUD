package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
  List<Recipe> findByPublished(boolean published);
  List<Recipe> findByTitleContaining(String title);
}