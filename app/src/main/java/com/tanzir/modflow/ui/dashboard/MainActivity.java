package com.tanzir.modflow.ui.dashboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.tanzir.modflow.R;
import com.tanzir.modflow.core.analyzer.ApkAnalyzer;
import com.tanzir.modflow.models.Project;
import com.tanzir.modflow.storage.ProjectStorage;
import com.tanzir.modflow.ui.projects.ProjectActivity;
import com.tanzir.modflow.ui.recipes.RecipeActivity;
import com.tanzir.modflow.ui.settings.SettingsActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Main dashboard activity for MODFLOW.
 */
public class MainActivity extends AppCompatActivity {
    
    private ProjectStorage projectStorage;
    private ApkAnalyzer apkAnalyzer;
    private RecyclerView recentProjectsRecyclerView;
    private RecentProjectsAdapter recentProjectsAdapter;
    
    private final ActivityResultLauncher<String> apkImportLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            this::handleApkImport
    );
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        projectStorage = new ProjectStorage(this);
        apkAnalyzer = new ApkAnalyzer();
        
        setupViews();
        loadRecentProjects();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadRecentProjects();
    }
    
    private void setupViews() {
        // Setup toolbar
        MaterialButton newProjectButton = findViewById(R.id.btnNewProject);
        newProjectButton.setOnClickListener(v -> importApk());
        
        MaterialButton importApkButton = findViewById(R.id.btnImportApk);
        importApkButton.setOnClickListener(v -> importApk());
        
        MaterialButton openProjectButton = findViewById(R.id.btnOpenProject);
        openProjectButton.setOnClickListener(v -> openProjectsList());
        
        MaterialButton recipesButton = findViewById(R.id.btnRecipes);
        recipesButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecipeActivity.class);
            startActivity(intent);
        });
        
        MaterialButton settingsButton = findViewById(R.id.btnSettings);
        settingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
        
        // Setup recent projects RecyclerView
        recentProjectsRecyclerView = findViewById(R.id.recyclerViewRecentProjects);
        recentProjectsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentProjectsAdapter = new RecentProjectsAdapter(projectStorage.getRecentProjects(5));
        recentProjectsRecyclerView.setAdapter(recentProjectsAdapter);
    }
    
    private void loadRecentProjects() {
        List<Project> recentProjects = projectStorage.getRecentProjects(5);
        recentProjectsAdapter.setProjects(recentProjects);
        recentProjectsAdapter.notifyDataSetChanged();
        
        View emptyView = findViewById(R.id.viewEmptyProjects);
        if (recentProjects.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            recentProjectsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            recentProjectsRecyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    private void importApk() {
        apkImportLauncher.launch("application/vnd.android.package-archive");
    }
    
    private void handleApkImport(Uri uri) {
        if (uri == null) {
            return;
        }
        
        try {
            // Create a new project
            Project project = new Project();
            
            // Copy APK to project directory
            File projectDir = projectStorage.getProjectDirectory(project.getId());
            File sourceApk = new File(projectDir, "source.apk");
            
            try (InputStream inputStream = getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(sourceApk)) {
                
                if (inputStream == null) {
                    Toast.makeText(this, R.string.error_apk_invalid, Toast.LENGTH_SHORT).show();
                    return;
                }
                
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
            
            // Set basic project info
            String apkName = getFileNameFromUri(uri);
            project.setName(apkName.replace(".apk", ""));
            project.setOriginalApkName(apkName);
            project.setApkPath(sourceApk.getAbsolutePath());
            
            // Analyze the APK
            ApkAnalyzer.AnalysisResult analysis = apkAnalyzer.analyze(sourceApk);
            
            if (!analysis.isValid()) {
                Toast.makeText(this, 
                    getString(R.string.error_apk_invalid) + ": " + analysis.errors.get(0), 
                    Toast.LENGTH_LONG).show();
                sourceApk.delete();
                return;
            }
            
            // Save project
            projectStorage.saveProject(project);
            
            Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
            
            // Open project
            Intent intent = new Intent(this, ProjectActivity.class);
            intent.putExtra(ProjectActivity.EXTRA_PROJECT_ID, project.getId());
            startActivity(intent);
            
        } catch (Exception e) {
            Toast.makeText(this, 
                getString(R.string.error) + ": " + e.getMessage(), 
                Toast.LENGTH_LONG).show();
        }
    }
    
    private String getFileNameFromUri(Uri uri) {
        String result = "unknown.apk";
        if (uri.getLastPathSegment() != null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }
    
    private void openProjectsList() {
        Intent intent = new Intent(this, ProjectActivity.class);
        startActivity(intent);
    }
}
