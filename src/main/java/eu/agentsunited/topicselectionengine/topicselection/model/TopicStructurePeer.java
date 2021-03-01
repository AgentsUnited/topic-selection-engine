package eu.agentsunited.topicselectionengine.topicselection.model;

import eu.agentsunited.topicselectionengine.DataController;

/**
 * Class defining a topic structure for the peer domain using {@link TopicNode}.
 *
 * @author Tessa Beinema
 */
public class TopicStructurePeer extends TopicStructure {

    public TopicStructurePeer(DataController dataController) {
        super(dataController);
    }

    @Override
    public void createStructure() {
        TopicNode nodeIntroduction = new TopicNode(TopicName.INTRODUCTION, 0.0, 0.0);
        nodeIntroduction.setTopic(new Topic(Domain.PEER, TopicName.INTRODUCTION));
        this.topicNodes.add(nodeIntroduction);
        this.startNode.addChild(nodeIntroduction);

        TopicNode nodeGatherInformation = new TopicNode(TopicName.GATHERINFORMATION, 0.0, 0.0);
        nodeGatherInformation.setTopic(new Topic(Domain.PEER, TopicName.GATHERINFORMATION));
        this.topicNodes.add(nodeGatherInformation);
        this.startNode.addChild(nodeGatherInformation);
    }

    @Override
    public void addSelectionParameters() {
        CategoricalSelectionParameter introductionCompletedEnabling = new CategoricalSelectionParameter("peerUserCompletedIntroductionEnabling", 1.0);
        introductionCompletedEnabling.addToValueMap("true", 0.0);
        introductionCompletedEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.INTRODUCTION, introductionCompletedEnabling);

        CategoricalSelectionParameter introductionCompletedLimiting = new CategoricalSelectionParameter("peerUserCompletedIntroductionLimiting", 1.0);
        introductionCompletedLimiting.addToValueMap("true", 1.0);
        introductionCompletedLimiting.addToValueMap("false", 0.0);
        this.addSelectionParameterToNode(TopicName.GATHERINFORMATION, introductionCompletedLimiting);

        CategoricalSelectionParameter gatherInformationCompletedEnabling = new CategoricalSelectionParameter("peerUserCompletedCaaSEnabling", 1.0);
        gatherInformationCompletedEnabling.addToValueMap("true", 0.0);
        gatherInformationCompletedEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.GATHERINFORMATION, gatherInformationCompletedEnabling);
    }
}
