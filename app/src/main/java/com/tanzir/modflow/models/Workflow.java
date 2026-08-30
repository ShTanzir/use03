package com.tanzir.modflow.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a complete workflow that can be saved as a recipe.
 */
public class Workflow implements Serializable {
    
    private String id;
    private String name;
    private String description;
    private String version;
    private long creationDate;
    private long modificationDate;
    private List<WorkflowNode> nodes;
    private List<WorkflowConnection> connections;
    private Map<String, String> variables;
    private List<String> compatibleCapabilities;
    
    public Workflow() {
        this.id = java.util.UUID.randomUUID().toString();
        this.creationDate = System.currentTimeMillis();
        this.modificationDate = System.currentTimeMillis();
        this.nodes = new ArrayList<>();
        this.connections = new ArrayList<>();
        this.variables = new HashMap<>();
        this.compatibleCapabilities = new ArrayList<>();
        this.version = "1.0";
    }
    
    public Workflow(String name) {
        this();
        this.name = name;
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    
    public long getCreationDate() { return creationDate; }
    public void setCreationDate(long creationDate) { this.creationDate = creationDate; }
    
    public long getModificationDate() { return modificationDate; }
    public void setModificationDate(long modificationDate) { this.modificationDate = modificationDate; }
    
    public List<WorkflowNode> getNodes() { return nodes; }
    public void setNodes(List<WorkflowNode> nodes) { this.nodes = nodes; }
    
    public List<WorkflowConnection> getConnections() { return connections; }
    public void setConnections(List<WorkflowConnection> connections) { this.connections = connections; }
    
    public Map<String, String> getVariables() { return variables; }
    public void setVariables(Map<String, String> variables) { this.variables = variables; }
    
    public List<String> getCompatibleCapabilities() { return compatibleCapabilities; }
    public void setCompatibleCapabilities(List<String> compatibleCapabilities) { 
        this.compatibleCapabilities = compatibleCapabilities; 
    }
    
    // Node management
    public void addNode(WorkflowNode node) {
        if (node != null && !nodes.contains(node)) {
            nodes.add(node);
            this.modificationDate = System.currentTimeMillis();
        }
    }
    
    public void removeNode(WorkflowNode node) {
        if (nodes.remove(node)) {
            // Also remove associated connections
            connections.removeIf(c -> 
                c.getSourceNodeId().equals(node.getId()) || 
                c.getTargetNodeId().equals(node.getId())
            );
            this.modificationDate = System.currentTimeMillis();
        }
    }
    
    public WorkflowNode getNodeById(String nodeId) {
        for (WorkflowNode node : nodes) {
            if (node.getId().equals(nodeId)) {
                return node;
            }
        }
        return null;
    }
    
    // Connection management
    public void addConnection(WorkflowConnection connection) {
        if (connection != null && !connections.contains(connection)) {
            connections.add(connection);
            this.modificationDate = System.currentTimeMillis();
        }
    }
    
    public void removeConnection(WorkflowConnection connection) {
        if (connections.remove(connection)) {
            this.modificationDate = System.currentTimeMillis();
        }
    }
    
    // Variable management
    public void setVariable(String key, String value) {
        variables.put(key, value);
        this.modificationDate = System.currentTimeMillis();
    }
    
    public String getVariable(String key) {
        return variables.get(key);
    }
    
    public String resolveVariable(String expression) {
        if (expression == null) return null;
        
        String result = expression;
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            if (result.contains(placeholder)) {
                result = result.replace(placeholder, entry.getValue());
            }
        }
        return result;
    }
    
    public int getNodeCount() {
        return nodes.size();
    }
    
    public void resetAllNodes() {
        for (WorkflowNode node : nodes) {
            node.reset();
        }
    }
    
    public Workflow copy() {
        Workflow copy = new Workflow(this.name + " (Copy)");
        copy.setDescription(this.description);
        copy.setVersion(this.version);
        // Deep copy nodes
        for (WorkflowNode node : this.nodes) {
            WorkflowNode newNode = new WorkflowNode(node.getName(), node.getType(), node.getXPosition(), node.getYPosition());
            newNode.setId(java.util.UUID.randomUUID().toString());
            newNode.setConfiguration(new WorkflowConfiguration());
            if (node.getConfiguration() != null) {
                newNode.getConfiguration().getAllProperties().putAll(node.getConfiguration().getAllProperties());
            }
            copy.addNode(newNode);
        }
        return copy;
    }
}
