package eu.agentsunited.topicselectionengine.topicselection.model;

import eu.agentsunited.topicselectionengine.DataController;

/**
 * Class defining a topic structure for the physical activity domain using {@link TopicNodes}.
 *
 * @author Tessa Beinema
 */
public class TopicStructurePhysicalActivity extends TopicStructure {

    public TopicStructurePhysicalActivity(DataController dataController) {
        super(dataController);
    }

    @Override
    public void createStructure() {
        TopicNode nodeIntroduction = new TopicNode(TopicName.INTRODUCTION, 0.0, 0.0);
        nodeIntroduction.setTopic(new Topic(Domain.PHYSICALACTIVITY, TopicName.INTRODUCTION));
        this.topicNodes.add(nodeIntroduction);
        this.startNode.addChild(nodeIntroduction);

        TopicNode nodeGoalSetting = new TopicNode(TopicName.GOALSETTING, 0.0, 0.0);
        nodeGoalSetting.setTopic(new Topic(Domain.PHYSICALACTIVITY, TopicName.GOALSETTING));
        this.topicNodes.add(nodeGoalSetting);
        this.startNode.addChild(nodeGoalSetting);

        TopicNode nodeFeedback = new TopicNode(TopicName.FEEDBACK, 0.0, 0.0);
        nodeFeedback.setTopic(new Topic(Domain.PHYSICALACTIVITY, TopicName.FEEDBACK));
        this.topicNodes.add(nodeFeedback);
        this.startNode.addChild(nodeFeedback);

        TopicNode nodeGatherInformation = new TopicNode(TopicName.GATHERINFORMATION, 0.0, 0.0);
        nodeGatherInformation.setTopic(new Topic(Domain.PHYSICALACTIVITY, TopicName.GATHERINFORMATION));
        this.topicNodes.add(nodeGatherInformation);
        this.startNode.addChild(nodeGatherInformation);
    }

    @Override
    public void addSelectionParameters() {
        CategoricalSelectionParameter introductionCompletedEnabling = new CategoricalSelectionParameter("paUserCompletedIntroductionEnabling", 1.0);
        introductionCompletedEnabling.addToValueMap("true", 0.0);
        introductionCompletedEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.INTRODUCTION, introductionCompletedEnabling);

        CategoricalSelectionParameter introductionCompletedLimiting = new CategoricalSelectionParameter("paUserCompletedIntroductionLimiting", 1.0);
        introductionCompletedLimiting.addToValueMap("true", 1.0);
        introductionCompletedLimiting.addToValueMap("false", 0.0);
        this.addSelectionParameterToNode(TopicName.GOALSETTING, introductionCompletedLimiting);
        this.addSelectionParameterToNode(TopicName.FEEDBACK, introductionCompletedLimiting);
        this.addSelectionParameterToNode(TopicName.GATHERINFORMATION, introductionCompletedLimiting);

        CategoricalSelectionParameter goalTypeSet = new CategoricalSelectionParameter("paUserHasGoalType", 0.3);
        goalTypeSet.addToValueMap("true", 0.0);
        goalTypeSet.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.GOALSETTING, goalTypeSet);

        CategoricalSelectionParameter longTermGoalSet = new CategoricalSelectionParameter("paUserHasLongTermGoal", 0.6);
        longTermGoalSet.addToValueMap("true", 0.0);
        longTermGoalSet.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.GOALSETTING, longTermGoalSet);

        CategoricalSelectionParameter shortTermGoalSet = new CategoricalSelectionParameter("paUserHasShortTermGoalEnabling", 1.0);
        shortTermGoalSet.addToValueMap("true", 0.0);
        shortTermGoalSet.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.GOALSETTING, shortTermGoalSet);

        CategoricalSelectionParameter shortTermGoalSetLimiting = new CategoricalSelectionParameter("paUserHasShortTermGoalLimiting", 1.0);
        shortTermGoalSetLimiting.addToValueMap("true", 1.0);
        shortTermGoalSetLimiting.addToValueMap("false", 0.0);
        this.addSelectionParameterToNode(TopicName.FEEDBACK, shortTermGoalSetLimiting);
        this.addSelectionParameterToNode(TopicName.GATHERINFORMATION, shortTermGoalSetLimiting);

        CategoricalSelectionParameter weightMeasurementEnabling = new CategoricalSelectionParameter("paUserHasWeightMeasurementEnabling", 1.0);
        weightMeasurementEnabling.addToValueMap("true", 0.0);
        weightMeasurementEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.GATHERINFORMATION, weightMeasurementEnabling);

        CategoricalSelectionParameter weightMeasurementLimiting = new CategoricalSelectionParameter("paUserHasWeightMeasurementLimiting", 1.0);
        weightMeasurementLimiting.addToValueMap("true", 1.0);
        weightMeasurementLimiting.addToValueMap("false", 0.0);
        this.addSelectionParameterToNode(TopicName.GATHERINFORMATION, weightMeasurementLimiting);

        CategoricalSelectionParameter feedbackEnabling = new CategoricalSelectionParameter("paUserCompletedFeedbackEnabling", 1.0);
        feedbackEnabling.addToValueMap("true", 0.0);
        feedbackEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.FEEDBACK, feedbackEnabling);

        CategoricalSelectionParameter dataLimiting = new CategoricalSelectionParameter("paDataLimiting", 1.0);
        feedbackEnabling.addToValueMap("true", 1.0);
        feedbackEnabling.addToValueMap("false", 0.0);
        this.addSelectionParameterToNode(TopicName.FEEDBACK, feedbackEnabling);


    }
}
