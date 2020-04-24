package eu.councilofcoaches.coachingengine.topicselection.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Class representing a topic node, from which the topic structure is build.
 *
 * @author Tessa Beinema
 */
public class TopicNode {
    private List<TopicNode> children;
    private TopicName title;
    private Topic topic;

    private double aprioriValue;
    private double aprioriWeight;
    private List<SelectionParameter> selectionParameters;

    public TopicNode(TopicName title, double aprioriValue, double aprioriWeight) {
        this.children = new ArrayList<TopicNode>();
        this.title = title;

        this.aprioriValue = aprioriValue;
        this.aprioriWeight = aprioriWeight;
        this.selectionParameters = new ArrayList<SelectionParameter>();
    }

    public void addChild(TopicNode topicNode) {
        this.children.add(topicNode);
    }

    public TopicName getTitle() {
        return this.title;
    }

    public Topic getTopic() {
        return this.topic;
    }

    public TopicNode getChildNumber(int number) {
        return this.children.get(number);
    }

    public List<TopicNode> getChildren() {
        return this.children;
    }

    public int getNumberOfChildren() {
        return this.children.size();
    }

    public double getAprioriValue() {
        return this.aprioriValue;
    }

    public double getWeight() {
        return this.aprioriWeight;
    }

    public List<SelectionParameter> getSelectionParameters() {
        return this.selectionParameters;
    }

    /**
     * Method for calculating the relevance of this topic node based on the {@link SelectionParameter}s.
     *
     * @param categoricalSelectionParameterValues
     * @param continuousSelectionParameterValues
     * @return A Double representing the node's relevance.
     */
    public double getProbability(Map<String, String> categoricalSelectionParameterValues, Map<String, Double> continuousSelectionParameterValues) {
        double numerator = this.aprioriValue*this.aprioriWeight;
        double denominator = this.aprioriWeight;
        System.out.println("\tApriori numerator: " + numerator + " and denominator: " + denominator);
        for (SelectionParameter selectionParameter : this.selectionParameters) {
            double value;
            double weight;
            if (selectionParameter instanceof CategoricalSelectionParameter) {
                String category = categoricalSelectionParameterValues.get(selectionParameter.getKey());
                //System.out.println("\tParameter: " + selectionParameter.getKey() + " and values in map: " + ((CategoricalSelectionParameter) selectionParameter).getValueMap().toString() );
                value = ((CategoricalSelectionParameter) selectionParameter).getValueMap().get(category);
                System.out.println("\t\tParameter: " + selectionParameter.getKey() + ", category: " + category + " and value: " + value);

                weight = selectionParameter.getWeight();
                numerator += value*weight;
                denominator += weight;
            }
            else if (selectionParameter instanceof ContinuousSelectionParameter) {
                value = continuousSelectionParameterValues.get(selectionParameter.getKey());
                System.out.println("\t\tParameter: " + selectionParameter.getKey() + " has value: " + value);
                weight = selectionParameter.getWeight();
                numerator += value*weight;
                denominator += weight;
            }
        }
        return numerator/denominator;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public void setAprioriValue(double value) {
        this.aprioriValue = value;
    }

    public void setAprioriWeight(double weight) {
        this.aprioriWeight = weight;
    }

    public void setSelectionParameters(List<SelectionParameter> selectionParameters) {
        this.selectionParameters = selectionParameters;
    }

    public void addSelectionParameter(SelectionParameter selectionParameter) {
        this.selectionParameters.add(selectionParameter);
    }

    public void addSelectionParameters(List<SelectionParameter> selectionParametersToAdd) {
        for(SelectionParameter selectionParameter : selectionParametersToAdd) {
            this.selectionParameters.add(selectionParameter);
        }
    }

    public boolean hasChildren() {
        if (this.children.size() > 0) {
            return true;
        }
        else {
            return false;
        }
    }
}
