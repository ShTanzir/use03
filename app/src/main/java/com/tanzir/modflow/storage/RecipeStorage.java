package com.tanzir.modflow.storage;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tanzir.modflow.models.Workflow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages workflow recipe storage and retrieval.
 */
public class RecipeStorage {
    
    private static final String TAG = "RecipeStorage";
    private static final String RECIPES_DIR = "recipes";
    private static final String RECIPE_FILE = "recipe.json";
    
    private final Context context;
    private final Gson gson;
    
    public RecipeStorage(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    /**
     * Get the recipes directory.
     */
    public File getRecipesDirectory() {
        File dir = new File(context.getFilesDir(), RECIPES_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
    
    /**
     * Get a specific recipe directory.
     */
    public File getRecipeDirectory(String recipeId) {
        File recipeDir = new File(getRecipesDirectory(), recipeId);
        if (!recipeDir.exists()) {
            recipeDir.mkdirs();
        }
        return recipeDir;
    }
    
    /**
     * Save a recipe (workflow).
     */
    public boolean saveRecipe(Workflow workflow) {
        try {
            File recipeDir = getRecipeDirectory(workflow.getId());
            
            File recipeFile = new File(recipeDir, RECIPE_FILE);
            String json = gson.toJson(workflow);
            
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(recipeFile), StandardCharsets.UTF_8)) {
                writer.write(json);
            }
            
            Log.d(TAG, "Recipe saved: " + workflow.getName());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to save recipe", e);
            return false;
        }
    }
    
    /**
     * Load a recipe by ID.
     */
    public Workflow loadRecipe(String recipeId) {
        try {
            File recipeFile = new File(getRecipeDirectory(recipeId), RECIPE_FILE);
            if (!recipeFile.exists()) {
                return null;
            }
            
            StringBuilder json = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(recipeFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
            }
            
            return gson.fromJson(json.toString(), Workflow.class);
        } catch (IOException e) {
            Log.e(TAG, "Failed to load recipe", e);
            return null;
        }
    }
    
    /**
     * Get all saved recipes.
     */
    public List<Workflow> getAllRecipes() {
        List<Workflow> recipes = new ArrayList<>();
        File recipesDir = getRecipesDirectory();
        
        if (recipesDir.exists() && recipesDir.isDirectory()) {
            File[] recipeDirs = recipesDir.listFiles(File::isDirectory);
            if (recipeDirs != null) {
                for (File recipeDir : recipeDirs) {
                    File recipeFile = new File(recipeDir, RECIPE_FILE);
                    if (recipeFile.exists()) {
                        Workflow recipe = loadRecipe(recipeDir.getName());
                        if (recipe != null) {
                            recipes.add(recipe);
                        }
                    }
                }
            }
        }
        
        // Sort by modification date (most recent first)
        recipes.sort((a, b) -> Long.compare(b.getModificationDate(), a.getModificationDate()));
        
        return recipes;
    }
    
    /**
     * Delete a recipe.
     */
    public boolean deleteRecipe(String recipeId) {
        try {
            File recipeDir = getRecipeDirectory(recipeId);
            return deleteDirectory(recipeDir);
        } catch (Exception e) {
            Log.e(TAG, "Failed to delete recipe", e);
            return false;
        }
    }
    
    /**
     * Check if a recipe exists.
     */
    public boolean recipeExists(String recipeId) {
        return new File(getRecipeDirectory(recipeId), RECIPE_FILE).exists();
    }
    
    /**
     * Get recent recipes (limited count).
     */
    public List<Workflow> getRecentRecipes(int limit) {
        List<Workflow> all = getAllRecipes();
        if (all.size() <= limit) {
            return all;
        }
        return all.subList(0, limit);
    }
    
    /**
     * Export recipe to a file.
     */
    public boolean exportRecipe(Workflow workflow, File outputFile) {
        try {
            String json = gson.toJson(workflow);
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(outputFile), StandardCharsets.UTF_8)) {
                writer.write(json);
            }
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to export recipe", e);
            return false;
        }
    }
    
    /**
     * Import recipe from a file.
     */
    public Workflow importRecipe(File inputFile) {
        try {
            StringBuilder json = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
            }
            
            Workflow workflow = gson.fromJson(json.toString(), Workflow.class);
            if (workflow != null) {
                // Assign new ID to avoid conflicts
                workflow.setId(java.util.UUID.randomUUID().toString());
                saveRecipe(workflow);
            }
            return workflow;
        } catch (IOException e) {
            Log.e(TAG, "Failed to import recipe", e);
            return null;
        }
    }
    
    private boolean deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return directory.delete();
    }
}
