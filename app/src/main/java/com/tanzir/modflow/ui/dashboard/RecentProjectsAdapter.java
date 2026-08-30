package com.tanzir.modflow.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanzir.modflow.R;
import com.tanzir.modflow.models.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying recent projects in the dashboard.
 */
public class RecentProjectsAdapter extends RecyclerView.Adapter<RecentProjectsAdapter.ProjectViewHolder> {
    
    private List<Project> projects;
    private OnProjectClickListener listener;
    private final SimpleDateFormat dateFormat;
    
    public interface OnProjectClickListener {
        void onProjectClick(Project project);
    }
    
    public RecentProjectsAdapter(List<Project> projects) {
        this.projects = new ArrayList<>(projects);
        this.dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());
    }
    
    public void setProjects(List<Project> projects) {
        this.projects = new ArrayList<>(projects);
    }
    
    public void setOnProjectClickListener(OnProjectClickListener listener) {
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public ProjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recent_project, parent, false);
        return new ProjectViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ProjectViewHolder holder, int position) {
        Project project = projects.get(position);
        holder.bind(project);
    }
    
    @Override
    public int getItemCount() {
        return projects.size();
    }
    
    class ProjectViewHolder extends RecyclerView.ViewHolder {
        
        private final TextView textProjectName;
        private final TextView textPackageName;
        private final TextView textLastModified;
        private final TextView textBuildStatus;
        
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView);
            textProjectName = itemView.findViewById(R.id.textProjectName);
            textPackageName = itemView.findViewById(R.id.textPackageName);
            textLastModified = itemView.findViewById(R.id.textLastModified);
            textBuildStatus = itemView.findViewById(R.id.textBuildStatus);
            
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onProjectClick(projects.get(position));
                }
            });
        }
        
        public void bind(Project project) {
            textProjectName.setText(project.getName());
            textPackageName.setText(project.getPackageName() != null ? 
                    project.getPackageName() : "Not analyzed");
            
            textLastModified.setText(dateFormat.format(new Date(project.getLastModifiedTime())));
            
            // Set build status
            int statusResId;
            String statusText;
            switch (project.getLastBuildStatus()) {
                case SUCCESS:
                    statusResId = R.color.modflow_success;
                    statusText = "Success";
                    break;
                case FAILED:
                    statusResId = R.color.modflow_error;
                    statusText = "Failed";
                    break;
                case BUILDING:
                    statusResId = R.color.modflow_warning;
                    statusText = "Building...";
                    break;
                default:
                    statusResId = R.color.modflow_text_secondary;
                    statusText = "Not built";
            }
            
            textBuildStatus.setText(statusText);
            textBuildStatus.setTextColor(
                    itemView.getContext().getResources().getColor(statusResId));
        }
    }
}
