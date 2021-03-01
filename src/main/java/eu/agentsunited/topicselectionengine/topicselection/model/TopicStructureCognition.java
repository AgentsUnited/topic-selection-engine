package eu.agentsunited.topicselectionengine.topicselection.model;

import eu.agentsunited.topicselectionengine.DataController;

/**
 * Class defining a topic structure for the cognition domain using {@link TopicNode}.
 *
 * @author Tessa Beinema
 */
public class TopicStructureCognition extends TopicStructure {

    public TopicStructureCognition(DataController dataController) {
        super(dataController);
    }

    @Override
    public void createStructure() {
        TopicNode nodeGatherInformation = new TopicNode(TopicName.GATHERINFORMATION, 0.0, 0.0);
        nodeGatherInformation.setTopic(new Topic(Domain.COGNITION, TopicName.GATHERINFORMATION));
        this.topicNodes.add(nodeGatherInformation);
        this.startNode.addChild(nodeGatherInformation);
    }

    @Override
    public void addSelectionParameters() {
        CategoricalSelectionParameter gatherInformationCompletedEnabling = new CategoricalSelectionParameter("cogUserCompletedCaaSEnabling", 1.0);
        gatherInformationCompletedEnabling.addToValueMap("true", 0.0);
        gatherInformationCompletedEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.GATHERINFORMATION, gatherInformationCompletedEnabling);
    }
}
