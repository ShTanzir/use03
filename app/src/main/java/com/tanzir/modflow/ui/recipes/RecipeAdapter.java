package com.tanzir.modflow.ui.recipes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanzir.modflow.R;
import com.tanzir.modflow.models.Workflow;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying recipe list.
 */
public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    
    private List<Workflow> recipes;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    
    public RecipeAdapter(List<Workflow> recipes) {
        this.recipes = recipes;
    }
    
    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Workflow recipe = recipes.get(position);
        holder.textName.setText(recipe.getName());
        holder.textDescription.setText(recipe.getDescription() != null ? recipe.getDescription() : "No description");
        holder.textNodeCount.setText(String.valueOf(recipe.getNodes().size()) + " nodes");
        
        if (recipe.getLastModified() > 0) {
            holder.textDate.setText(dateFormat.format(recipe.getLastModified()));
        } else {
            holder.textDate.setText("");
        }
    }
    
    @Override
    public int getItemCount() {
        return recipes.size();
    }
    
    static class RecipeViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;
        TextView textNodeCount;
        TextView textDate;
        
        RecipeViewHolder(View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textRecipeName);
            textDescription = itemView.findViewById(R.id.textRecipeDescription);
            textNodeCount = itemView.findViewById(R.id.textNodeCount);
            textDate = itemView.findViewById(R.id.textRecipeDate);
        }
    }
}
