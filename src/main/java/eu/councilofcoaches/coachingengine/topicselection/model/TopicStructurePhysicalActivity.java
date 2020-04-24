package eu.councilofcoaches.coachingengine.topicselection.model;

import eu.councilofcoaches.coachingengine.DataController;

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
        TopicNode nodeGoalSetting = new TopicNode(TopicName.GOALSETTING, 0.0, 0.0);
        nodeGoalSetting.setTopic(new Topic(Domain.PHYSICAL_ACTIVITY, TopicName.GOALSETTING));
        this.topicNodes.add(nodeGoalSetting);
        this.startNode.addChild(nodeGoalSetting);

        TopicNode nodeIntroduction = new TopicNode(TopicName.INTRODUCTION, 0.0, 0.0);
        nodeIntroduction.setTopic(new Topic(Domain.PHYSICAL_ACTIVITY, TopicName.INTRODUCTION));
        this.topicNodes.add(nodeIntroduction);
        this.startNode.addChild(nodeIntroduction);
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

        CategoricalSelectionParameter longTermGoalSet = new CategoricalSelectionParameter("paUserHasLongTermGoal", 1.0);
        longTermGoalSet.addToValueMap("true", 0.0);
        longTermGoalSet.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.GOALSETTING, longTermGoalSet);

    }
}
