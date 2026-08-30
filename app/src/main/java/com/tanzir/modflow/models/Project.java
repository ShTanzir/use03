package com.tanzir.modflow.models;

import java.io.Serializable;

/**
 * Represents a MODFLOW project for APK modification.
 */
public class Project implements Serializable {
    
    private String id;
    private String name;
    private String originalApkName;
    private String packageName;
    private String versionName;
    private int versionCode;
    private int minSdk;
    private int targetSdk;
    private String[] architectures;
    private String apkPath;
    private long creationTime;
    private long lastModifiedTime;
    private int buildCount;
    private String currentWorkflowId;
    private int patchCount;
    private int warningCount;
    private int errorCount;
    private String projectDirectory;
    private BuildStatus lastBuildStatus;
    
    public enum BuildStatus {
        NOT_BUILT,
        SUCCESS,
        FAILED,
        BUILDING
    }
    
    public Project() {
        this.id = java.util.UUID.randomUUID().toString();
        this.creationTime = System.currentTimeMillis();
        this.lastModifiedTime = System.currentTimeMillis();
        this.buildCount = 0;
        this.patchCount = 0;
        this.warningCount = 0;
        this.errorCount = 0;
        this.lastBuildStatus = BuildStatus.NOT_BUILT;
        this.architectures = new String[0];
    }
    
    public Project(String name, String apkPath) {
        this();
        this.name = name;
        this.apkPath = apkPath;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getOriginalApkName() { return originalApkName; }
    public void setOriginalApkName(String originalApkName) { this.originalApkName = originalApkName; }
    
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    
    public String getVersionName() { return versionName; }
    public void setVersionName(String versionName) { this.versionName = versionName; }
    
    public int getVersionCode() { return versionCode; }
    public void setVersionCode(int versionCode) { this.versionCode = versionCode; }
    
    public int getMinSdk() { return minSdk; }
    public void setMinSdk(int minSdk) { this.minSdk = minSdk; }
    
    public int getTargetSdk() { return targetSdk; }
    public void setTargetSdk(int targetSdk) { this.targetSdk = targetSdk; }
    
    public String[] getArchitectures() { return architectures; }
    public void setArchitectures(String[] architectures) { this.architectures = architectures; }
    
    public String getApkPath() { return apkPath; }
    public void setApkPath(String apkPath) { this.apkPath = apkPath; }
    
    public long getCreationTime() { return creationTime; }
    public void setCreationTime(long creationTime) { this.creationTime = creationTime; }
    
    public long getLastModifiedTime() { return lastModifiedTime; }
    public void setLastModifiedTime(long lastModifiedTime) { this.lastModifiedTime = lastModifiedTime; }
    
    public int getBuildCount() { return buildCount; }
    public void setBuildCount(int buildCount) { this.buildCount = buildCount; }
    
    public String getCurrentWorkflowId() { return currentWorkflowId; }
    public void setCurrentWorkflowId(String currentWorkflowId) { this.currentWorkflowId = currentWorkflowId; }
    
    public int getPatchCount() { return patchCount; }
    public void setPatchCount(int patchCount) { this.patchCount = patchCount; }
    
    public int getWarningCount() { return warningCount; }
    public void setWarningCount(int warningCount) { this.warningCount = warningCount; }
    
    public int getErrorCount() { return errorCount; }
    public void setErrorCount(int errorCount) { this.errorCount = errorCount; }
    
    public String getProjectDirectory() { return projectDirectory; }
    public void setProjectDirectory(String projectDirectory) { this.projectDirectory = projectDirectory; }
    
    public BuildStatus getLastBuildStatus() { return lastBuildStatus; }
    public void setLastBuildStatus(BuildStatus lastBuildStatus) { this.lastBuildStatus = lastBuildStatus; }
    
    public void incrementBuildCount() {
        this.buildCount++;
        this.lastModifiedTime = System.currentTimeMillis();
    }
    
    public void incrementPatchCount() {
        this.patchCount++;
        this.lastModifiedTime = System.currentTimeMillis();
    }
    
    public Workflow getCurrentWorkflow() {
        // For now, return a default workflow if none exists
        // In a full implementation, this would load from storage
        if (currentWorkflowId == null) {
            return new Workflow("Default Workflow");
        }
        // Would need to load from storage - simplified for now
        return new Workflow();
    }
}
