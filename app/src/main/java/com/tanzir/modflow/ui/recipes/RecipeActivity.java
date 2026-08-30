package com.tanzir.modflow.ui.recipes;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tanzir.modflow.R;
import com.tanzir.modflow.models.Workflow;
import com.tanzir.modflow.storage.RecipeStorage;

import java.util.List;

/**
 * Activity for viewing and managing Mod Recipes (saved workflows).
 */
public class RecipeActivity extends AppCompatActivity {
    
    private RecipeStorage recipeStorage;
    private RecyclerView recyclerViewRecipes;
    private RecipeAdapter recipeAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);
        
        recipeStorage = new RecipeStorage(this);
        
        setupViews();
        loadRecipes();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes();
    }
    
    private void setupViews() {
        FloatingActionButton fabNewRecipe = findViewById(R.id.fabNewRecipe);
        fabNewRecipe.setOnClickListener(v -> {
            Toast.makeText(this, R.string.feature_coming_soon, Toast.LENGTH_SHORT).show();
        });
        
        recyclerViewRecipes = findViewById(R.id.recyclerViewRecipes);
        recyclerViewRecipes.setLayoutManager(new LinearLayoutManager(this));
    }
    
    private void loadRecipes() {
        List<Workflow> recipes = recipeStorage.getAllRecipes();
        
        if (recipes.isEmpty()) {
            findViewById(R.id.viewEmptyRecipes).setVisibility(android.view.View.VISIBLE);
            recyclerViewRecipes.setVisibility(android.view.View.GONE);
        } else {
            findViewById(R.id.viewEmptyRecipes).setVisibility(android.view.View.GONE);
            recyclerViewRecipes.setVisibility(android.view.View.VISIBLE);
            
            recipeAdapter = new RecipeAdapter(recipes);
            recyclerViewRecipes.setAdapter(recipeAdapter);
        }
    }
}
