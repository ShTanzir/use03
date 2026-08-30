package com.tanzir.modflow.ui.workflow;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tanzir.modflow.R;
import com.tanzir.modflow.models.Project;
import com.tanzir.modflow.models.Workflow;
import com.tanzir.modflow.storage.ProjectStorage;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for building and editing visual workflows.
 */
public class WorkflowBuilderActivity extends AppCompatActivity {
    
    public static final String EXTRA_PROJECT_ID = "project_id";
    
    private ProjectStorage projectStorage;
    private Project currentProject;
    private Workflow currentWorkflow;
    private WorkflowNodeAdapter nodeAdapter;
    private RecyclerView recyclerViewNodes;
    
    private final List<WorkflowNodeItem> nodeItems = new ArrayList<>();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workflow_builder);
        
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
        
        // Load or create workflow
        currentWorkflow = currentProject.getCurrentWorkflow();
        if (currentWorkflow == null) {
            currentWorkflow = new Workflow();
            currentWorkflow.setName("Default Workflow");
        }
        
        setupViews();
        loadNodes();
    }
    
    private void setupViews() {
        recyclerViewNodes = findViewById(R.id.recyclerViewNodes);
        recyclerViewNodes.setLayoutManager(new LinearLayoutManager(this));
        nodeAdapter = new WorkflowNodeAdapter(nodeItems, this::onNodeClick);
        recyclerViewNodes.setAdapter(nodeAdapter);
        
        FloatingActionButton fabAddNode = findViewById(R.id.fabAddNode);
        fabAddNode.setOnClickListener(v -> {
            addNewNode();
        });
        
        FloatingActionButton fabRunWorkflow = findViewById(R.id.fabRunWorkflow);
        fabRunWorkflow.setOnClickListener(v -> {
            runWorkflow();
        });
    }
    
    private void loadNodes() {
        nodeItems.clear();
        if (currentWorkflow.getNodes() != null) {
            for (WorkflowNode node : currentWorkflow.getNodes()) {
                nodeItems.add(new WorkflowNodeItem(node));
            }
        }
        nodeAdapter.notifyDataSetChanged();
    }
    
    private void addNewNode() {
        // For now, just add a placeholder node
        WorkflowNode newNode = new WorkflowNode();
        newNode.setType(WorkflowNode.NodeType.SEARCH);
        newNode.setName("New Node");
        newNode.setErrorMessage("Configure this node");
        
        currentWorkflow.addNode(newNode);
        nodeItems.add(new WorkflowNodeItem(newNode));
        nodeAdapter.notifyItemInserted(nodeItems.size() - 1);
        
        Toast.makeText(this, "Node added", Toast.LENGTH_SHORT).show();
    }
    
    private void onNodeClick(WorkflowNodeItem nodeItem) {
        Toast.makeText(this, "Node: " + nodeItem.node.getName(), Toast.LENGTH_SHORT).show();
    }
    
    private void runWorkflow() {
        Toast.makeText(this, "Workflow execution - coming soon", Toast.LENGTH_SHORT).show();
    }
    
    public static class WorkflowNodeItem {
        public final WorkflowNode node;
        
        public WorkflowNodeItem(WorkflowNode node) {
            this.node = node;
        }
    }
}
