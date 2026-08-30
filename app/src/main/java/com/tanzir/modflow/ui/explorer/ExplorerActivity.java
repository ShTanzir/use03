package com.tanzir.modflow.ui.explorer;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tanzir.modflow.R;
import com.tanzir.modflow.models.Project;
import com.tanzir.modflow.storage.ProjectStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity for exploring APK/project file structure.
 */
public class ExplorerActivity extends AppCompatActivity {
    
    public static final String EXTRA_PROJECT_ID = "project_id";
    
    private ProjectStorage projectStorage;
    private Project currentProject;
    private FileExplorerAdapter fileExplorerAdapter;
    private RecyclerView recyclerViewFiles;
    
    private File currentDirectory;
    private final List<FileItem> currentFiles = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
        
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
        loadDirectory(new File(currentProject.getApkPath()).getParentFile());
    }
    
    private void setupViews() {
        recyclerViewFiles = findViewById(R.id.recyclerViewFiles);
        recyclerViewFiles.setLayoutManager(new LinearLayoutManager(this));
        fileExplorerAdapter = new FileExplorerAdapter(currentFiles, this::onFileClick);
        recyclerViewFiles.setAdapter(fileExplorerAdapter);
    }
    
    private void loadDirectory(File directory) {
        currentDirectory = directory;
        currentFiles.clear();
        
        if (directory == null || !directory.exists()) {
            fileExplorerAdapter.notifyDataSetChanged();
            return;
        }
        
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                currentFiles.add(new FileItem(file.getName(), file.isDirectory(), file.length()));
            }
        }
        
        fileExplorerAdapter.notifyDataSetChanged();
    }
    
    private void onFileClick(FileItem fileItem) {
        if (fileItem.isDirectory) {
            File newDir = new File(currentDirectory, fileItem.name);
            loadDirectory(newDir);
        } else {
            Toast.makeText(this, "File: " + fileItem.name, Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onBackPressed() {
        if (currentDirectory != null && currentDirectory.getParentFile() != null) {
            loadDirectory(currentDirectory.getParentFile());
        } else {
            super.onBackPressed();
        }
    }
    
    public static class FileItem {
        public final String name;
        public final boolean isDirectory;
        public final long size;
        
        public FileItem(String name, boolean isDirectory, long size) {
            this.name = name;
            this.isDirectory = isDirectory;
            this.size = size;
        }
    }
}
