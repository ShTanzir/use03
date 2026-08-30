package com.tanzir.modflow.ui.analyzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanzir.modflow.R;

import java.util.List;

/**
 * Adapter for displaying DEX files list.
 */
public class DexFileAdapter extends RecyclerView.Adapter<DexFileAdapter.DexFileViewHolder> {
    
    private List<String> dexFiles;
    
    public DexFileAdapter(List<String> dexFiles) {
        this.dexFiles = dexFiles;
    }
    
    @NonNull
    @Override
    public DexFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dex_file, parent, false);
        return new DexFileViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull DexFileViewHolder holder, int position) {
        String dexFile = dexFiles.get(position);
        holder.textDexName.setText(dexFile);
    }
    
    @Override
    public int getItemCount() {
        return dexFiles.size();
    }
    
    static class DexFileViewHolder extends RecyclerView.ViewHolder {
        TextView textDexName;
        
        DexFileViewHolder(View itemView) {
            super(itemView);
            textDexName = itemView.findViewById(R.id.textDexName);
        }
    }
}
