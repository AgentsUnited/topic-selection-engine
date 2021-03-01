package eu.agentsunited.topicselectionengine.topicselection.model;

import eu.agentsunited.topicselectionengine.DataController;

/**
 * Class defining a topic script for the physical activity domain using {@link TopicNode}.
 *
 * @author Tessa Beinema
 */
public class TopicScriptGeneral extends TopicScript {

    public TopicScriptGeneral(DataController dataController) {
        super(dataController);
    }

    @Override
    public void createScript() {
        TopicNode nodeIntroduction = new TopicNode(TopicName.INTRODUCTION, 0.0, 0.0);
        nodeIntroduction.setTopic(new Topic(Domain.GENERAL, TopicName.INTRODUCTION));
        this.topicNodes.add(nodeIntroduction);

        TopicNode nodeGoalSetting = new TopicNode(TopicName.GOALSETTING, 0.0, 0.0);
        nodeGoalSetting.setTopic(new Topic(Domain.GENERAL, TopicName.GOALSETTING));
        this.topicNodes.add(nodeGoalSetting);

        TopicNode nodeFeedback = new TopicNode(TopicName.FEEDBACK, 0.0, 0.0);
        nodeFeedback.setTopic(new Topic(Domain.GENERAL, TopicName.FEEDBACK));
        this.topicNodes.add(nodeFeedback);
    }

    @Override
    public void addSelectionParameters() {
        CategoricalSelectionParameter introductionCompleted = new CategoricalSelectionParameter("paUserCompletedIntroduction", 1.0);
        introductionCompleted.addToValueMap("true", 0.0);
        introductionCompleted.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.INTRODUCTION, introductionCompleted);

        CategoricalSelectionParameter goalSettingCompleted = new CategoricalSelectionParameter("paUserCompletedGoalSetting", 1.0);
        goalSettingCompleted.addToValueMap("true", 0.0);
        goalSettingCompleted.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.GOALSETTING, goalSettingCompleted);

        CategoricalSelectionParameter feedbackCompleted = new CategoricalSelectionParameter("paUserCompletedFeedback", 1.0);
        feedbackCompleted.addToValueMap("true", 0.0);
        feedbackCompleted.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.FEEDBACK, feedbackCompleted);
    }
}
