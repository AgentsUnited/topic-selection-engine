package eu.councilofcoaches.coachingengine.topicselection.model;

import eu.councilofcoaches.coachingengine.DataController;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class defining a basic topic structure using {@link TopicNodes}.
 *
 * @author Tessa Beinema
 */
public abstract class TopicStructure {
    protected List<TopicNode> topicNodes;
    protected TopicNode startNode;
    protected SelectionParameterMapping selectionParameterMapping;
    protected DataController dataController;

    public TopicStructure(DataController dataController) {
        this.dataController = dataController;
        this.topicNodes = new ArrayList<TopicNode>();
        this.startNode = new TopicNode(TopicName.START, 1.0, 1.0);
        this.topicNodes.add(this.startNode);
        this.createStructure();
        this.addSelectionParameters();

        this.selectionParameterMapping = new SelectionParameterMapping(this.dataController);
    }

    public TopicNode getStartNode() {
        return this.startNode;
    }

    public SelectionParameterMapping getSelectionParameterMapping () {
        return this.selectionParameterMapping;
    }

    public abstract void createStructure();

    public abstract void addSelectionParameters();

    public void addSelectionParameterToNode(Enum nodeName, SelectionParameter parameter) {
        for (TopicNode node : this.topicNodes) {
            if (node.getTitle().equals(nodeName)) {
                node.addSelectionParameter(parameter);
            }
        }
    }
}
