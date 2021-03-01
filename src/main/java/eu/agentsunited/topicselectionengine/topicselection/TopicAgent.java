package eu.agentsunited.topicselectionengine.topicselection;

import eu.agentsunited.topicselectionengine.DataController;
import eu.agentsunited.topicselectionengine.exception.DatabaseException;
import eu.agentsunited.topicselectionengine.topicselection.model.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;

/**
 * Class modelling a topic agent. One agent has and reasons over one {@link TopicStructure}, or one {@link TopicScript} or a mix of both.
 *
 * @author Tessa Beinema
 */
public class TopicAgent {

    public static final Logger logger = ServiceManager.getLogger(TopicAgent.class);
    private Domain domain;
    private DataController dataController;
    private int scriptedTopicSelectionProbability;
    private TopicStructure topicStructure;
    private TopicScript topicScript;


    public TopicAgent(Domain domain, DataController dataController, int scriptedTopicSelectionProbability, TopicStructure topicStructure, TopicScript topicScript) {
        this.dataController = dataController;
        this.scriptedTopicSelectionProbability = scriptedTopicSelectionProbability;
        this.topicStructure = topicStructure;
        this.topicScript = topicScript;
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
     * Returns the probability that the topic selection will use a topic script to select new topics. (100 = 100% scripted, 0 = 100% automatic.)
     * @return the scriptedTopicSelectionProbability
     */

    public int getScriptProbability() { return this.scriptedTopicSelectionProbability; }

    /**
     * Determines which topic to address now by selecting a topic node using the topic selection algorithm.
     * @return A topic node.
     */
    public TopicNode selectNewTopic() throws DatabaseException, IOException {
        Random random = new Random ();
        int randomForScriptedSelection = random.nextInt(100);

        if (randomForScriptedSelection <= this.getScriptProbability()){

            TopicNode selectedNode;
            selectedNode = this.getNextTopicFromScript();

            logger.debug("Selected Topic: " + selectedNode.getTopic().getTopicName());
            return selectedNode;
        }
        else {
            TopicNode startNode = this.topicStructure.getStartNode();
            TopicNode selectedNode;
            List<Map<String, Double>> nodeProbabilityLog = new ArrayList<Map<String, Double>>();
            selectedNode = this.navigateTopicStructure(startNode, nodeProbabilityLog);
            logger.debug("Selected Topic: " + selectedNode.getTopic().getTopicName());
            return selectedNode;
        }
    }

    /**
     * Method that returns the next topic from the script.
     * @return The next TopicNode.
     */
    public TopicNode getNextTopicFromScript() throws DatabaseException, IOException {
        List<TopicNode> nodes = this.topicScript.getTopicNodes();

        for (TopicNode node : nodes) {
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
            logger.debug("Checking if topic has been discussed for: " + node.getTitle());
            Map<String, String> categoricalSelectionParameterValues = this.topicScript.getSelectionParameterMapping().getCategoricalSelectionParameterValues(categoricalParameterNames);
            Map<String, Double> continuousSelectionParameterValues = this.topicScript.getSelectionParameterMapping().getContinuousSelectionParameterValues(continuousParameterNames);
            double nodeRelevance = node.getRelevance(categoricalSelectionParameterValues, continuousSelectionParameterValues);
            if (nodeRelevance == 1.0) {
                return node;
            }
        }
        return nodes.get(nodes.size()-1);
    }

    /**
     * Algorithm that navigates the topic structure using the values calculated per node. Recursively does this until it reaches an end-node (leaf).
     * Is influenced by the explorationProbability (if explorationProbability is high then a balanced random selection is made. If it is low, the node
     * with the highest score is chosen.
     * @param node A node to start from.
     * @return The selected TopicNode.
     */
    public TopicNode navigateTopicStructure(TopicNode node, List<Map<String, Double>> nodeRelevances) throws DatabaseException, IOException {
        Map<TopicNode, Double> childNodesWithRelevances = new HashMap<TopicNode, Double>();

        TopicNode chosenNode = node;
        if(node.hasChildren()) {
            childNodesWithRelevances = this.calculateRelevanceForNodes(node.getChildren());
            Map<String, Double> nodesAndRelevances = new HashMap<String, Double>();
            for(TopicNode key : childNodesWithRelevances.keySet()) {
                nodesAndRelevances.put(key.getTitle().toString(), childNodesWithRelevances.get(key));
            }
            nodeRelevances.add(nodesAndRelevances);
            Random random = new Random();

            int randomForExploration = random.nextInt(100);
            if (randomForExploration > this.getExplorationProbability()) {
                logger.debug("Chosing from these nodes through exploitation.");
                double highestRelevanceSoFar = -1.0;
                for(TopicNode mapNode : childNodesWithRelevances.keySet()) {
                    if(childNodesWithRelevances.get(mapNode) > highestRelevanceSoFar) {
                        chosenNode = mapNode;
                        highestRelevanceSoFar = childNodesWithRelevances.get(mapNode);
                        mapNode.setLastSelectionValue(highestRelevanceSoFar);
                    }
                }
                logger.debug("Chosen Node: " + chosenNode.getTitle() + " with probability: " + childNodesWithRelevances.get(chosenNode) + "\n");
                return navigateTopicStructure(chosenNode, nodeRelevances);
            }
            else {
                logger.debug("Chosing from these nodes through exploration.");
                int maxForRandom = 0;
                for(double probability : childNodesWithRelevances.values()) {
                    maxForRandom += probability*100;
                }

                int nodeToChooseInt = random.nextInt(maxForRandom);
                logger.debug("Max value for random: " + maxForRandom + ", Node to choose int: " + nodeToChooseInt);

                int sumOfValuesSoFar = 0;
                for(TopicNode mapNode : childNodesWithRelevances.keySet()) {
                    logger.debug("NodeName: " + mapNode.getTitle());

                    if ((nodeToChooseInt >= sumOfValuesSoFar) && (nodeToChooseInt <= (sumOfValuesSoFar+childNodesWithRelevances.get(mapNode)*100))) {
                        chosenNode = mapNode;
                        logger.debug("Chosen Node: " + chosenNode.getTitle() + "\n");
                        return navigateTopicStructure(chosenNode, nodeRelevances);
                    }
                    else {
                        sumOfValuesSoFar += childNodesWithRelevances.get(mapNode)*100;
                    }
                }
            }
        }

        logger.info(nodeRelevances.toString());
        return node;
    }

    public Map<TopicNode, Double> calculateRelevanceForNodes(List<TopicNode> nodes) throws DatabaseException, IOException {
        Map<TopicNode, Double> nodesWithRelevances = new HashMap<TopicNode, Double>();

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
            logger.debug("Calculating probability for Node: " + node.getTitle());
            logger.debug("\tSelection parameters involved: " + categoricalParameterNames.toString() + continuousParameterNames.toString());
            Map<String, String> categoricalSelectionParameterValues = this.topicStructure.getSelectionParameterMapping().getCategoricalSelectionParameterValues(categoricalParameterNames);
            Map<String, Double> continuousSelectionParameterValues = this.topicStructure.getSelectionParameterMapping().getContinuousSelectionParameterValues(continuousParameterNames);
            double nodeProbability = node.getRelevance(categoricalSelectionParameterValues, continuousSelectionParameterValues);

            logger.debug("\tCalculated importance: " + nodeProbability);
            nodesWithRelevances.put(node,nodeProbability);
        }
        return nodesWithRelevances;
    }
}
