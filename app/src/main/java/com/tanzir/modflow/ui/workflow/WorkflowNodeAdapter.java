package com.tanzir.modflow.ui.workflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tanzir.modflow.R;

import java.util.List;
import java.util.function.Consumer;

/**
 * Adapter for displaying workflow nodes.
 */
public class WorkflowNodeAdapter extends RecyclerView.Adapter<WorkflowNodeAdapter.NodeViewHolder> {
    
    private List<WorkflowBuilderActivity.WorkflowNodeItem> nodes;
    private final Consumer<WorkflowBuilderActivity.WorkflowNodeItem> onItemClick;
    
    public WorkflowNodeAdapter(List<WorkflowBuilderActivity.WorkflowNodeItem> nodes, 
                               Consumer<WorkflowBuilderActivity.WorkflowNodeItem> onItemClick) {
        this.nodes = nodes;
        this.onItemClick = onItemClick;
    }
    
    @NonNull
    @Override
    public NodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_workflow_node, parent, false);
        return new NodeViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NodeViewHolder holder, int position) {
        WorkflowBuilderActivity.WorkflowNodeItem nodeItem = nodes.get(position);
        holder.textNodeTitle.setText(nodeItem.node.getName());
        holder.textNodeType.setText(nodeItem.node.getType().name());
        
        if (nodeItem.node.getErrorMessage() != null) {
            holder.textNodeDescription.setText(nodeItem.node.getErrorMessage());
            holder.textNodeDescription.setVisibility(View.VISIBLE);
        } else {
            holder.textNodeDescription.setVisibility(View.GONE);
        }
        
        holder.itemView.setOnClickListener(v -> onItemClick.accept(nodeItem));
    }
    
    @Override
    public int getItemCount() {
        return nodes.size();
    }
    
    static class NodeViewHolder extends RecyclerView.ViewHolder {
        TextView textNodeTitle;
        TextView textNodeType;
        TextView textNodeDescription;
        
        NodeViewHolder(View itemView) {
            super(itemView);
            textNodeTitle = itemView.findViewById(R.id.textNodeTitle);
            textNodeType = itemView.findViewById(R.id.textNodeType);
            textNodeDescription = itemView.findViewById(R.id.textNodeDescription);
        }
    }
}
