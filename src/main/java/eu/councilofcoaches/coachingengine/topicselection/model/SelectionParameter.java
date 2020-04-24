package eu.councilofcoaches.coachingengine.topicselection.model;

/**
 * Class representing a feature that can be used as a component in calculating the
 * relevance for a {@link TopicNode}, which in turn can be used to decide on a topic.
 *
 * @author Tessa Beinema
 */
public class SelectionParameter {
    private String key;
    private double weight;

    public SelectionParameter(String key, double weight) {
        this.setKey(key);
        this.setWeight(weight);
    }

    public String getKey() {
        return key;
    }

    public double getWeight() {
        return weight;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
