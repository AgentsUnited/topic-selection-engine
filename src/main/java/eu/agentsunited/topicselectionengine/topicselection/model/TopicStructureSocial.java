package eu.agentsunited.topicselectionengine.topicselection.model;

import eu.agentsunited.topicselectionengine.DataController;

/**
 * Class defining a topic structure for the social domain using {@link TopicNode}.
 *
 * @author Tessa Beinema
 */
public class TopicStructureSocial extends TopicStructure {

    public TopicStructureSocial(DataController dataController) {
        super(dataController);
    }

    @Override
    public void createStructure() {
        TopicNode nodeIntroduction = new TopicNode(TopicName.INTRODUCTION, 0.0, 0.0);
        nodeIntroduction.setTopic(new Topic(Domain.SOCIAL, TopicName.INTRODUCTION));
        this.topicNodes.add(nodeIntroduction);
        this.startNode.addChild(nodeIntroduction);

        TopicNode nodeGatherInformation = new TopicNode(TopicName.GATHERINFORMATION, 0.0, 0.0);
        nodeGatherInformation.setTopic(new Topic(Domain.SOCIAL, TopicName.GATHERINFORMATION));
        this.topicNodes.add(nodeGatherInformation);
        this.startNode.addChild(nodeGatherInformation);

        TopicNode nodeFeedback = new TopicNode(TopicName.FEEDBACK, 0.0, 0.0);
        nodeFeedback.setTopic(new Topic(Domain.SOCIAL, TopicName.FEEDBACK));
        this.topicNodes.add(nodeFeedback);
        this.startNode.addChild(nodeFeedback);
    }

    @Override
    public void addSelectionParameters() {
        CategoricalSelectionParameter introductionCompletedEnabling = new CategoricalSelectionParameter("socUserCompletedIntroductionEnabling", 1.0);
        introductionCompletedEnabling.addToValueMap("true", 0.0);
        introductionCompletedEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.INTRODUCTION, introductionCompletedEnabling);

        CategoricalSelectionParameter introductionCompletedLimiting = new CategoricalSelectionParameter("socUserCompletedIntroductionLimiting", 1.0);
        introductionCompletedLimiting.addToValueMap("true", 1.0);
        introductionCompletedLimiting.addToValueMap("false", 0.0);
        this.addSelectionParameterToNode(TopicName.GATHERINFORMATION, introductionCompletedLimiting);
        this.addSelectionParameterToNode(TopicName.FEEDBACK, introductionCompletedLimiting);

        CategoricalSelectionParameter gatherInformationCompletedEnabling = new CategoricalSelectionParameter("socUserCompletedCaaSEnabling", 1.0);
        gatherInformationCompletedEnabling.addToValueMap("true", 0.0);
        gatherInformationCompletedEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.GATHERINFORMATION, gatherInformationCompletedEnabling);

        CategoricalSelectionParameter gatherInformationCompletedLimiting = new CategoricalSelectionParameter("socUserCompletedCaaSLimiting", 1.0);
        gatherInformationCompletedLimiting.addToValueMap("true", 1.0);
        gatherInformationCompletedLimiting.addToValueMap("false", 0.0);
        this.addSelectionParameterToNode(TopicName.FEEDBACK, gatherInformationCompletedLimiting);

        CategoricalSelectionParameter feedbackCaaSCompletedEnabling = new CategoricalSelectionParameter("socUserCompletedFeedbackCaaSEnabling", 1.0);
        feedbackCaaSCompletedEnabling.addToValueMap("true", 0.0);
        feedbackCaaSCompletedEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.FEEDBACK, feedbackCaaSCompletedEnabling);
    }
}
