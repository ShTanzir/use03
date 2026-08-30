package com.tanzir.modflow.ui.explorer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanzir.modflow.R;

import java.util.List;
import java.util.function.Consumer;

/**
 * Adapter for displaying file explorer items.
 */
public class FileExplorerAdapter extends RecyclerView.Adapter<FileExplorerAdapter.FileViewHolder> {
    
    private List<ExplorerActivity.FileItem> files;
    private final Consumer<ExplorerActivity.FileItem> onItemClick;
    
    public FileExplorerAdapter(List<ExplorerActivity.FileItem> files, Consumer<ExplorerActivity.FileItem> onItemClick) {
        this.files = files;
        this.onItemClick = onItemClick;
    }
    
    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        ExplorerActivity.FileItem fileItem = files.get(position);
        holder.textFileName.setText(fileItem.name);
        
        if (fileItem.isDirectory) {
            holder.iconFile.setImageResource(R.drawable.ic_folder);
            holder.textFileSize.setVisibility(View.GONE);
        } else {
            holder.iconFile.setImageResource(R.drawable.ic_file);
            holder.textFileSize.setVisibility(View.VISIBLE);
            holder.textFileSize.setText(formatFileSize(fileItem.size));
        }
        
        holder.itemView.setOnClickListener(v -> onItemClick.accept(fileItem));
    }
    
    @Override
    public int getItemCount() {
        return files.size();
    }
    
    private String formatFileSize(long size) {
        if (size < 1024) {
            return size + " B";
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
        }
    }
    
    static class FileViewHolder extends RecyclerView.ViewHolder {
        TextView textFileName;
        TextView textFileSize;
        ImageView iconFile;
        
        FileViewHolder(View itemView) {
            super(itemView);
            textFileName = itemView.findViewById(R.id.textFileName);
            textFileSize = itemView.findViewById(R.id.textFileSize);
            iconFile = itemView.findViewById(R.id.iconFile);
        }
    }
}
