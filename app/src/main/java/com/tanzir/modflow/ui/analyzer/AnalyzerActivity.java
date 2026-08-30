package com.tanzir.modflow.ui.analyzer;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tanzir.modflow.R;
import com.tanzir.modflow.core.analyzer.ApkAnalyzer;
import com.tanzir.modflow.models.Project;
import com.tanzir.modflow.storage.ProjectStorage;

import java.io.File;
import java.util.List;

/**
 * Activity for displaying APK analysis results.
 */
public class AnalyzerActivity extends AppCompatActivity {
    
    public static final String EXTRA_PROJECT_ID = "project_id";
    
    private ProjectStorage projectStorage;
    private ApkAnalyzer apkAnalyzer;
    private Project currentProject;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analyzer);
        
        projectStorage = new ProjectStorage(this);
        apkAnalyzer = new ApkAnalyzer();
        
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
        analyzeApk();
    }
    
    private void setupViews() {
        TextView textTitle = findViewById(R.id.textAnalyzerTitle);
        textTitle.setText(currentProject.getName());
    }
    
    private void analyzeApk() {
        File apkFile = new File(currentProject.getApkPath());
        
        if (!apkFile.exists()) {
            Toast.makeText(this, "APK file not found", Toast.LENGTH_SHORT).show();
            return;
        }
        
        ApkAnalyzer.AnalysisResult result = apkAnalyzer.analyze(apkFile);
        displayResults(result);
    }
    
    private void displayResults(ApkAnalyzer.AnalysisResult result) {
        TextView textPackageName = findViewById(R.id.textPackageName);
        TextView textVersionName = findViewById(R.id.textVersionName);
        TextView textVersionCode = findViewById(R.id.textVersionCode);
        TextView textMinSdk = findViewById(R.id.textMinSdk);
        TextView textTargetSdk = findViewById(R.id.textTargetSdk);
        TextView textDexCount = findViewById(R.id.textDexCount);
        TextView textArchitectures = findViewById(R.id.textArchitectures);
        RecyclerView recyclerDexFiles = findViewById(R.id.recyclerDexFiles);
        
        textPackageName.setText(result.packageName != null ? result.packageName : "Unknown");
        textVersionName.setText(result.versionName != null ? result.versionName : "Unknown");
        textVersionCode.setText(String.valueOf(result.versionCode));
        textMinSdk.setText(String.valueOf(result.minSdk));
        textTargetSdk.setText(String.valueOf(result.targetSdk));
        textDexCount.setText(String.valueOf(result.dexFiles.size()));
        
        if (result.architectures != null && !result.architectures.isEmpty()) {
            textArchitectures.setText(String.join(", ", result.architectures));
        } else {
            textArchitectures.setText("None detected");
        }
        
        // Setup DEX files list
        DexFileAdapter dexFileAdapter = new DexFileAdapter(result.dexFiles);
        recyclerDexFiles.setLayoutManager(new LinearLayoutManager(this));
        recyclerDexFiles.setAdapter(dexFileAdapter);
        
        // Show errors/warnings if any
        View errorView = findViewById(R.id.viewErrors);
        if (!result.errors.isEmpty()) {
            errorView.setVisibility(View.VISIBLE);
            TextView textErrors = findViewById(R.id.textErrors);
            textErrors.setText(String.join("\n", result.errors));
        } else {
            errorView.setVisibility(View.GONE);
        }
    }
}
