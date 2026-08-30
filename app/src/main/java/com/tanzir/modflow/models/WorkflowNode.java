package com.tanzir.modflow.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a workflow node in the visual workflow builder.
 */
public class WorkflowNode implements Serializable {
    
    public enum NodeType {
        // Input nodes
        APK_INPUT,
        FILE_INPUT,
        FOLDER_INPUT,
        
        // Search nodes
        FIND_CLASS,
        FIND_METHOD,
        FIND_STRING,
        FIND_RESOURCE,
        FIND_FILE,
        FIND_PATTERN,
        
        // Modification nodes
        INSERT_SMALI,
        REPLACE_SMALI,
        DELETE_CODE,
        ADD_FILE,
        REPLACE_RESOURCE,
        MODIFY_MANIFEST,
        MODIFY_XML,
        
        // Build nodes
        DECODE,
        BUILD_RESOURCES,
        BUILD_DEX,
        PACKAGE_APK,
        ALIGN,
        SIGN,
        VALIDATE,
        
        // Test nodes
        INSTALL,
        LAUNCH,
        COLLECT_LOGS,
        CHECK_RESULT
    }
    
    public enum NodeStatus {
        PENDING,
        RUNNING,
        SUCCESS,
        FAILED,
        SKIPPED
    }
    
    private String id;
    private String name;
    private NodeType type;
    private NodeStatus status;
    private int xPosition;
    private int yPosition;
    private WorkflowConfiguration configuration;
    private List<String> inputConnections;
    private List<String> outputConnections;
    private String errorMessage;
    
    public WorkflowNode() {
        this.id = java.util.UUID.randomUUID().toString();
        this.status = NodeStatus.PENDING;
        this.inputConnections = new ArrayList<>();
        this.outputConnections = new ArrayList<>();
        this.configuration = new WorkflowConfiguration();
    }
    
    public WorkflowNode(String name, NodeType type) {
        this();
        this.name = name;
        this.type = type;
    }
    
    public WorkflowNode(String name, NodeType type, int x, int y) {
        this(name, type);
        this.xPosition = x;
        this.yPosition = y;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public NodeType getType() { return type; }
    public void setType(NodeType type) { this.type = type; }
    
    public NodeStatus getStatus() { return status; }
    public void setStatus(NodeStatus status) { this.status = status; }
    
    public int getXPosition() { return xPosition; }
    public void setXPosition(int xPosition) { this.xPosition = xPosition; }
    
    public int getYPosition() { return yPosition; }
    public void setYPosition(int yPosition) { this.yPosition = yPosition; }
    
    public WorkflowConfiguration getConfiguration() { return configuration; }
    public void setConfiguration(WorkflowConfiguration configuration) { this.configuration = configuration; }
    
    public List<String> getInputConnections() { return inputConnections; }
    public void setInputConnections(List<String> inputConnections) { this.inputConnections = inputConnections; }
    
    public List<String> getOutputConnections() { return outputConnections; }
    public void setOutputConnections(List<String> outputConnections) { this.outputConnections = outputConnections; }
    
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    
    public void addInputConnection(String connectionId) {
        if (!this.inputConnections.contains(connectionId)) {
            this.inputConnections.add(connectionId);
        }
    }
    
    public void addOutputConnection(String connectionId) {
        if (!this.outputConnections.contains(connectionId)) {
            this.outputConnections.add(connectionId);
        }
    }
    
    public boolean isExecutable() {
        return type != null && status == NodeStatus.PENDING;
    }
    
    public void reset() {
        this.status = NodeStatus.PENDING;
        this.errorMessage = null;
    }
}
