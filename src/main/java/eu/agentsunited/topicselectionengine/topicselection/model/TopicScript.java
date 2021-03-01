package eu.agentsunited.topicselectionengine.topicselection.model;

import eu.agentsunited.topicselectionengine.DataController;

import java.util.ArrayList;

/**
 * Abstract base class for a topic script.
 *
 * @author Tessa Beinema
 */
public abstract class TopicScript {

    protected ArrayList<TopicNode> topicNodes;
    protected DataController dataController;
    protected SelectionParameterMapping selectionParameterMapping;

    public TopicScript (DataController dataController) {
        this.dataController = dataController;
        this.topicNodes = new ArrayList<TopicNode>();
        this.createScript();
        this.addSelectionParameters();

        this.selectionParameterMapping = new SelectionParameterMapping(this.dataController);
    }

    public abstract void createScript ();

    public abstract void addSelectionParameters();

    public ArrayList<TopicNode> getTopicNodes() {
        return this.topicNodes;
    }

    public SelectionParameterMapping getSelectionParameterMapping() {
        return this.selectionParameterMapping;
    }

    public void addSelectionParameterToNode(Enum nodeName, SelectionParameter parameter) {
        for (TopicNode node : this.topicNodes) {
            if (node.getTitle().equals(nodeName)) {
                node.addSelectionParameter(parameter);
            }
        }
    }
}
