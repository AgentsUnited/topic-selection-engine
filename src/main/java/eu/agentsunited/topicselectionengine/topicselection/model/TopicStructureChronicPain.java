package eu.agentsunited.topicselectionengine.topicselection.model;

import eu.agentsunited.topicselectionengine.DataController;

/**
 * Class defining a topic structure for the chronic pain domain using {@link TopicNode}.
 *
 * @author Tessa Beinema
 */
public class TopicStructureChronicPain extends TopicStructure {

    public TopicStructureChronicPain(DataController dataController) {
        super(dataController);
    }

    @Override
    public void createStructure() {
        TopicNode nodeHealthEducation = new TopicNode(TopicName.HEALTHEDUCATION, 0.0, 0.0);
        nodeHealthEducation.setTopic(new Topic(Domain.CHRONICPAIN, TopicName.HEALTHEDUCATION));
        this.topicNodes.add(nodeHealthEducation);
        this.startNode.addChild(nodeHealthEducation);
    }

    @Override
    public void addSelectionParameters() {
        CategoricalSelectionParameter healthEducationCompletedEnabling = new CategoricalSelectionParameter("cpUserCompletedWeek3", 1.0);
        healthEducationCompletedEnabling.addToValueMap("true", 0.0);
        healthEducationCompletedEnabling.addToValueMap("false", 1.0);
        this.addSelectionParameterToNode(TopicName.HEALTHEDUCATION, healthEducationCompletedEnabling);
    }
}
