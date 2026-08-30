package com.tanzir.modflow.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for a workflow node.
 */
public class WorkflowConfiguration implements Serializable {
    
    private Map<String, String> properties;
    
    public WorkflowConfiguration() {
        this.properties = new HashMap<>();
    }
    
    // Common configuration keys
    public static final String KEY_CLASS_NAME = "className";
    public static final String KEY_METHOD_NAME = "methodName";
    public static final String KEY_SEARCH_STRING = "searchString";
    public static final String KEY_FILE_PATH = "filePath";
    public static final String KEY_PATTERN = "pattern";
    public static final String KEY_INSERT_CONTENT = "insertContent";
    public static final String KEY_REPLACE_CONTENT = "replaceContent";
    public static final String KEY_TARGET_PATH = "targetPath";
    public static final String KEY_SOURCE_PATH = "sourcePath";
    public static final String KEY_VARIABLE_NAME = "variableName";
    public static final String KEY_SIGNING_PROFILE = "signingProfile";
    
    public void setProperty(String key, String value) {
        if (value != null) {
            this.properties.put(key, value);
        }
    }
    
    public String getProperty(String key) {
        return this.properties.get(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        String value = this.properties.get(key);
        return value != null ? value : defaultValue;
    }
    
    public boolean hasProperty(String key) {
        return this.properties.containsKey(key);
    }
    
    public void removeProperty(String key) {
        this.properties.remove(key);
    }
    
    public Map<String, String> getAllProperties() {
        return new HashMap<>(this.properties);
    }
    
    public void clear() {
        this.properties.clear();
    }
    
    // Convenience methods for common properties
    public void setClassName(String className) {
        setProperty(KEY_CLASS_NAME, className);
    }
    
    public String getClassName() {
        return getProperty(KEY_CLASS_NAME);
    }
    
    public void setMethodName(String methodName) {
        setProperty(KEY_METHOD_NAME, methodName);
    }
    
    public String getMethodName() {
        return getProperty(KEY_METHOD_NAME);
    }
    
    public void setSearchString(String searchString) {
        setProperty(KEY_SEARCH_STRING, searchString);
    }
    
    public String getSearchString() {
        return getProperty(KEY_SEARCH_STRING);
    }
    
    public void setFilePath(String filePath) {
        setProperty(KEY_FILE_PATH, filePath);
    }
    
    public String getFilePath() {
        return getProperty(KEY_FILE_PATH);
    }
    
    public void setPattern(String pattern) {
        setProperty(KEY_PATTERN, pattern);
    }
    
    public String getPattern() {
        return getProperty(KEY_PATTERN);
    }
    
    public void setInsertContent(String content) {
        setProperty(KEY_INSERT_CONTENT, content);
    }
    
    public String getInsertContent() {
        return getProperty(KEY_INSERT_CONTENT);
    }
    
    public void setReplaceContent(String content) {
        setProperty(KEY_REPLACE_CONTENT, content);
    }
    
    public String getReplaceContent() {
        return getProperty(KEY_REPLACE_CONTENT);
    }
    
    @Override
    public String toString() {
        return "WorkflowConfiguration{" +
                "properties=" + properties +
                '}';
    }
}
