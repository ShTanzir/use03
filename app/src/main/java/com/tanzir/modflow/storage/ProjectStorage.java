package com.tanzir.modflow.storage;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tanzir.modflow.models.Project;
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
 * Manages project storage and retrieval.
 */
public class ProjectStorage {
    
    private static final String TAG = "ProjectStorage";
    private static final String PROJECTS_DIR = "projects";
    private static final String PROJECT_FILE = "project.json";
    
    private final Context context;
    private final Gson gson;
    
    public ProjectStorage(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    
    /**
     * Get the projects directory.
     */
    public File getProjectsDirectory() {
        File dir = new File(context.getFilesDir(), PROJECTS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
    
    /**
     * Get a specific project directory.
     */
    public File getProjectDirectory(String projectId) {
        File projectDir = new File(getProjectsDirectory(), projectId);
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }
        return projectDir;
    }
    
    /**
     * Save a project.
     */
    public boolean saveProject(Project project) {
        try {
            File projectDir = getProjectDirectory(project.getId());
            project.setProjectDirectory(projectDir.getAbsolutePath());
            
            File projectFile = new File(projectDir, PROJECT_FILE);
            String json = gson.toJson(project);
            
            try (OutputStreamWriter writer = new OutputStreamWriter(
                    new FileOutputStream(projectFile), StandardCharsets.UTF_8)) {
                writer.write(json);
            }
            
            Log.d(TAG, "Project saved: " + project.getName());
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Failed to save project", e);
            return false;
        }
    }
    
    /**
     * Load a project by ID.
     */
    public Project loadProject(String projectId) {
        try {
            File projectFile = new File(getProjectDirectory(projectId), PROJECT_FILE);
            if (!projectFile.exists()) {
                return null;
            }
            
            StringBuilder json = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(projectFile), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    json.append(line);
                }
            }
            
            Project project = gson.fromJson(json.toString(), Project.class);
            if (project != null) {
                project.setProjectDirectory(getProjectDirectory(projectId).getAbsolutePath());
            }
            return project;
        } catch (IOException e) {
            Log.e(TAG, "Failed to load project", e);
            return null;
        }
    }
    
    /**
     * Get all saved projects.
     */
    public List<Project> getAllProjects() {
        List<Project> projects = new ArrayList<>();
        File projectsDir = getProjectsDirectory();
        
        if (projectsDir.exists() && projectsDir.isDirectory()) {
            File[] projectDirs = projectsDir.listFiles(File::isDirectory);
            if (projectDirs != null) {
                for (File projectDir : projectDirs) {
                    File projectFile = new File(projectDir, PROJECT_FILE);
                    if (projectFile.exists()) {
                        Project project = loadProject(projectDir.getName());
                        if (project != null) {
                            projects.add(project);
                        }
                    }
                }
            }
        }
        
        // Sort by last modified time (most recent first)
        projects.sort((a, b) -> Long.compare(b.getLastModifiedTime(), a.getLastModifiedTime()));
        
        return projects;
    }
    
    /**
     * Delete a project.
     */
    public boolean deleteProject(String projectId) {
        try {
            File projectDir = getProjectDirectory(projectId);
            return deleteDirectory(projectDir);
        } catch (Exception e) {
            Log.e(TAG, "Failed to delete project", e);
            return false;
        }
    }
    
    /**
     * Check if a project exists.
     */
    public boolean projectExists(String projectId) {
        return new File(getProjectDirectory(projectId), PROJECT_FILE).exists();
    }
    
    /**
     * Get recent projects (limited count).
     */
    public List<Project> getRecentProjects(int limit) {
        List<Project> all = getAllProjects();
        if (all.size() <= limit) {
            return all;
        }
        return all.subList(0, limit);
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
