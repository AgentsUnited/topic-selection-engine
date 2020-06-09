package eu.councilofcoaches.coachingengine.topicselection;

import eu.councilofcoaches.coachingengine.DataController;
import eu.councilofcoaches.coachingengine.exception.DatabaseException;
import eu.councilofcoaches.coachingengine.topicselection.model.*;

import java.io.IOException;
import java.util.*;

/**
 * Class modelling a topic agent. One agent has and reasons over one {@link TopicStructure}.
 */
public class TopicAgent {

    private Domain domain;
    private DataController dataController;
    private TopicStructure topicStructure;


    public TopicAgent(Domain domain, DataController dataController, TopicStructure topicStructure) {
        this.dataController = dataController;
        this.topicStructure = topicStructure;
    }
    public Domain getDomain() {
        return this.domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    /**
     * Returns the probability that the topic selection makes a (balanced) random topic choice instead of chosing the topic with the highest value.
     * The probability should be a percentage.
     *
     * @return the exploration probability
     */
    public int getExplorationProbability() {
        return 0;
    }

    /**
     * Determines which topic to address now by selecting a topic node using the topic selection algorithm.
     * @return A topic.
     */
    public Topic selectNewTopic() throws DatabaseException, IOException {
        TopicNode startNode = this.topicStructure.getStartNode();
        TopicNode selectedNode;
        List<Map<String, Double>> nodeProbabilityLog = new ArrayList<Map<String, Double>>();
        selectedNode = this.navigateTopicStructure(startNode, nodeProbabilityLog);
        System.out.println("Selected Topic: " + selectedNode.getTopic().getTopicName());
        return selectedNode.getTopic();
    }

    /**
     * Algorithm that navigates the topic structure using the values calculated per node. Recursively does this until it reaches an end-node (leaf).
     * Is influenced by the explorationProbability (if explorationProbability is high then a balanced random selection is made. If it is low, the node
     * with the highest score is chosen.
     * @param node A node to start from.
     * @return The selected TopicNode.
     */
    public TopicNode navigateTopicStructure(TopicNode node, List<Map<String, Double>> nodeProbabilities) throws DatabaseException, IOException {
        Map<TopicNode, Double> childNodesWithProbabilities = new HashMap<TopicNode, Double>();

        TopicNode chosenNode = node;
        if(node.hasChildren()) {
            childNodesWithProbabilities = this.calculateProbabilitiesForChildren(node.getChildren());
            Map<String, Double> nodesAndProbabilities = new HashMap<String, Double>();
            for(TopicNode key : childNodesWithProbabilities.keySet()) {
                nodesAndProbabilities.put(key.getTitle().toString(), childNodesWithProbabilities.get(key));
            }
            nodeProbabilities.add(nodesAndProbabilities);
            Random random = new Random();

            int randomForExploration = random.nextInt(100);
            if (randomForExploration > this.getExplorationProbability()) {
                System.out.println("Chosing from these nodes through exploitation.");
                double highestPriorityValue = 0.0;
                for(TopicNode mapNode : childNodesWithProbabilities.keySet()) {
                    if(childNodesWithProbabilities.get(mapNode) > highestPriorityValue) {
                        chosenNode = mapNode;
                        highestPriorityValue = childNodesWithProbabilities.get(mapNode);
                    }
                }
                System.out.println("Chosen Node: " + chosenNode.getTitle() + " with probability: " + childNodesWithProbabilities.get(chosenNode) + "\n");
                return navigateTopicStructure(chosenNode, nodeProbabilities);
            }
            else {
                System.out.println("Chosing from these nodes through exploration.");
                int maxForRandom = 0;
                for(double probability : childNodesWithProbabilities.values()) {
                    maxForRandom += probability*100;
                }

                int nodeToChooseInt = random.nextInt(maxForRandom);
                System.out.println("Max value for random: " + maxForRandom + ", Node to choose int: " + nodeToChooseInt);

                int sumOfValuesSoFar = 0;
                for(TopicNode mapNode : childNodesWithProbabilities.keySet()) {
                    System.out.println("NodeName: " + mapNode.getTitle());

                    if ((nodeToChooseInt >= sumOfValuesSoFar) && (nodeToChooseInt <= (sumOfValuesSoFar+childNodesWithProbabilities.get(mapNode)*100))) {
                        chosenNode = mapNode;
                        System.out.println("Chosen Node: " + chosenNode.getTitle() + "\n");
                        return navigateTopicStructure(chosenNode, nodeProbabilities);
                    }
                    else {
                        sumOfValuesSoFar += childNodesWithProbabilities.get(mapNode)*100;
                    }
                }
            }
        }

        System.out.println(nodeProbabilities.toString());
        return node;
    }

    public Map<TopicNode, Double> calculateProbabilitiesForChildren(List<TopicNode> nodes) throws DatabaseException, IOException {
        Map<TopicNode, Double> nodesWithProbabilities = new HashMap<TopicNode, Double>();

        for(TopicNode node : nodes) {
            List<SelectionParameter> selectionParameters = node.getSelectionParameters();
            List<String> categoricalParameterNames = new ArrayList<String>();
            List<String> continuousParameterNames = new ArrayList<String>();
            for(SelectionParameter selectionParameter : selectionParameters) {
                if (selectionParameter instanceof CategoricalSelectionParameter) {
                    categoricalParameterNames.add(selectionParameter.getKey());
                }
                else if (selectionParameter instanceof ContinuousSelectionParameter) {
                    continuousParameterNames.add(selectionParameter.getKey());
                }
            }
            System.out.println("Calculating probability for Node: " + node.getTitle());
            System.out.println("\tSelection parameters involved: " + categoricalParameterNames.toString() + continuousParameterNames.toString());
            Map<String, String> categoricalSelectionParameterValues = this.topicStructure.getSelectionParameterMapping().getCategoricalSelectionParameterValues(categoricalParameterNames);
            Map<String, Double> continuousSelectionParameterValues = this.topicStructure.getSelectionParameterMapping().getContinuousSelectionParameterValues(continuousParameterNames);
            double nodeProbability = node.getProbability(categoricalSelectionParameterValues, continuousSelectionParameterValues);

            System.out.println("\tCalculated importance: " + nodeProbability);
            nodesWithProbabilities.put(node,nodeProbability);
        }
        return nodesWithProbabilities;
    }
}