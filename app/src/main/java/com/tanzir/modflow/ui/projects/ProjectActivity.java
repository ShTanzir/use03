package com.tanzir.modflow.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.tanzir.modflow.R;
import com.tanzir.modflow.models.Project;
import com.tanzir.modflow.storage.ProjectStorage;
import com.tanzir.modflow.ui.analyzer.AnalyzerActivity;
import com.tanzir.modflow.ui.explorer.ExplorerActivity;
import com.tanzir.modflow.ui.workflow.WorkflowBuilderActivity;

/**
 * Activity for viewing project details and accessing project tools.
 */
public class ProjectActivity extends AppCompatActivity {
    
    public static final String EXTRA_PROJECT_ID = "project_id";
    
    private ProjectStorage projectStorage;
    private Project currentProject;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        
        projectStorage = new ProjectStorage(this);
        
        String projectId = getIntent().getStringExtra(EXTRA_PROJECT_ID);
        if (projectId != null) {
            currentProject = projectStorage.loadProject(projectId);
        }
        
        if (currentProject == null) {
            Toast.makeText(this, "Project not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        setupViews();
        displayProjectInfo();
    }
    
    private void setupViews() {
        MaterialButton btnAnalyzer = findViewById(R.id.btnAnalyzer);
        btnAnalyzer.setOnClickListener(v -> {
            Intent intent = new Intent(this, AnalyzerActivity.class);
            intent.putExtra(AnalyzerActivity.EXTRA_PROJECT_ID, currentProject.getId());
            startActivity(intent);
        });
        
        MaterialButton btnExplorer = findViewById(R.id.btnExplorer);
        btnExplorer.setOnClickListener(v -> {
            Intent intent = new Intent(this, ExplorerActivity.class);
            intent.putExtra(ExplorerActivity.EXTRA_PROJECT_ID, currentProject.getId());
            startActivity(intent);
        });
        
        MaterialButton btnWorkflow = findViewById(R.id.btnWorkflow);
        btnWorkflow.setOnClickListener(v -> {
            Intent intent = new Intent(this, WorkflowBuilderActivity.class);
            intent.putExtra(WorkflowBuilderActivity.EXTRA_PROJECT_ID, currentProject.getId());
            startActivity(intent);
        });
        
        MaterialButton btnBuild = findViewById(R.id.btnBuild);
        btnBuild.setOnClickListener(v -> {
            Toast.makeText(this, "Build functionality - coming soon", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void displayProjectInfo() {
        TextView textProjectName = findViewById(R.id.textProjectName);
        TextView textPackageName = findViewById(R.id.textPackageName);
        TextView textVersion = findViewById(R.id.textVersion);
        TextView textApkPath = findViewById(R.id.textApkPath);
        TextView textBuildCount = findViewById(R.id.textBuildCount);
        TextView textPatchCount = findViewById(R.id.textPatchCount);
        
        textProjectName.setText(currentProject.getName());
        
        if (currentProject.getPackageName() != null) {
            textPackageName.setText(currentProject.getPackageName());
            textPackageName.setVisibility(View.VISIBLE);
        } else {
            textPackageName.setVisibility(View.GONE);
        }
        
        if (currentProject.getVersionName() != null) {
            textVersion.setText("v" + currentProject.getVersionName());
            textVersion.setVisibility(View.VISIBLE);
        } else {
            textVersion.setVisibility(View.GONE);
        }
        
        textApkPath.setText(currentProject.getApkPath());
        textBuildCount.setText(String.valueOf(currentProject.getBuildCount()));
        textPatchCount.setText(String.valueOf(currentProject.getPatchCount()));
    }
}
