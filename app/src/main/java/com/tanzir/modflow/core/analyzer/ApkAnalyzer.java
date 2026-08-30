package com.tanzir.modflow.core.analyzer;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Analyzes APK files and extracts metadata.
 */
public class ApkAnalyzer {
    
    private static final String TAG = "ApkAnalyzer";
    
    /**
     * Result of APK analysis.
     */
    public static class AnalysisResult {
        public String packageName;
        public String versionName;
        public int versionCode;
        public int minSdk;
        public int targetSdk;
        public List<String> dexFiles;
        public List<String> architectures;
        public boolean hasResources;
        public boolean hasAssets;
        public boolean hasSignature;
        public long apkSize;
        public int fileCount;
        public List<String> warnings;
        public List<String> errors;
        
        public AnalysisResult() {
            this.dexFiles = new ArrayList<>();
            this.architectures = new ArrayList<>();
            this.warnings = new ArrayList<>();
            this.errors = new ArrayList<>();
        }
        
        public boolean isValid() {
            return errors.isEmpty();
        }
    }
    
    /**
     * Analyze an APK file.
     */
    public AnalysisResult analyze(File apkFile) {
        AnalysisResult result = new AnalysisResult();
        
        if (apkFile == null || !apkFile.exists()) {
            result.errors.add("APK file does not exist");
            return result;
        }
        
        if (!apkFile.getName().toLowerCase().endsWith(".apk")) {
            result.errors.add("File is not an APK");
            return result;
        }
        
        result.apkSize = apkFile.length();
        
        try (ZipFile zipFile = new ZipFile(apkFile)) {
            // Count files and analyze contents
            java.util.Enumeration<? extends ZipEntry> entries = zipFile.entries();
            
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String name = entry.getName();
                
                // Count DEX files
                if (name.endsWith(".dex")) {
                    result.dexFiles.add(name);
                }
                
                // Detect architectures from native libraries
                if (name.startsWith("lib/")) {
                    String[] parts = name.split("/");
                    if (parts.length >= 2) {
                        String arch = parts[1];
                        if (!result.architectures.contains(arch)) {
                            result.architectures.add(arch);
                        }
                    }
                }
                
                // Check for resources
                if ("resources.arsc".equals(name)) {
                    result.hasResources = true;
                }
                
                // Check for assets
                if (name.startsWith("assets/") && !name.equals("assets/")) {
                    result.hasAssets = true;
                }
                
                // Check for signature
                if (name.startsWith("META-INF/") && 
                    (name.endsWith(".RSA") || name.endsWith(".DSA") || name.endsWith(".SF"))) {
                    result.hasSignature = true;
                }
            }
            
            result.fileCount = zipFile.size();
            
            // Validate basic structure
            if (result.dexFiles.isEmpty()) {
                result.errors.add("No DEX files found in APK");
            }
            
            if (!result.hasSignature) {
                result.warnings.add("APK appears to be unsigned");
            }
            
        } catch (Exception e) {
            Log.e(TAG, "Error analyzing APK", e);
            result.errors.add("Failed to read APK: " + e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Get a human-readable architecture name.
     */
    public static String getArchitectureName(String arch) {
        switch (arch) {
            case "arm64-v8a":
                return "ARM64";
            case "armeabi-v7a":
                return "ARM";
            case "x86_64":
                return "x64";
            case "x86":
                return "x86";
            default:
                return arch;
        }
    }
    
    /**
     * Get a human-readable file size.
     */
    public static String formatFileSize(long bytes) {
        if (bytes < 1024) {
            return bytes + " B";
        } else if (bytes < 1024 * 1024) {
            return String.format("%.1f KB", bytes / 1024.0);
        } else if (bytes < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        } else {
            return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
        }
    }
}
