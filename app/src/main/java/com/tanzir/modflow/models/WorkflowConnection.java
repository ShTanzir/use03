package com.tanzir.modflow.models;

import java.io.Serializable;

/**
 * Represents a connection between two workflow nodes.
 */
public class WorkflowConnection implements Serializable {
    
    private String id;
    private String sourceNodeId;
    private String targetNodeId;
    private String sourcePort;
    private String targetPort;
    
    public WorkflowConnection() {
        this.id = java.util.UUID.randomUUID().toString();
    }
    
    public WorkflowConnection(String sourceNodeId, String targetNodeId) {
        this();
        this.sourceNodeId = sourceNodeId;
        this.targetNodeId = targetNodeId;
    }
    
    public WorkflowConnection(String sourceNodeId, String targetNodeId, String sourcePort, String targetPort) {
        this(sourceNodeId, targetNodeId);
        this.sourcePort = sourcePort;
        this.targetPort = targetPort;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getSourceNodeId() { return sourceNodeId; }
    public void setSourceNodeId(String sourceNodeId) { this.sourceNodeId = sourceNodeId; }
    
    public String getTargetNodeId() { return targetNodeId; }
    public void setTargetNodeId(String targetNodeId) { this.targetNodeId = targetNodeId; }
    
    public String getSourcePort() { return sourcePort; }
    public void setSourcePort(String sourcePort) { this.sourcePort = sourcePort; }
    
    public String getTargetPort() { return targetPort; }
    public void setTargetPort(String targetPort) { this.targetPort = targetPort; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        WorkflowConnection that = (WorkflowConnection) obj;
        return id != null && id.equals(that.id);
    }
    
    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
