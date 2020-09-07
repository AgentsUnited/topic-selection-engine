package eu.agentsunited.topicselectionengine.topicselection.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that extends a {@link SelectionParameter} and represents a selection parameter
 * with categorical values.
 *
 * @author Tessa Beinema
 */
public class CategoricalSelectionParameter extends SelectionParameter {

    private Map<String, Double> valueMap;

    public CategoricalSelectionParameter(String key, double weight) {
        super(key, weight);
        this.valueMap = new HashMap<String, Double>();
    }

    public Map<String, Double> getValueMap() {
        return this.valueMap;
    }

    public void setValueMap(Map<String, Double> valueMap) {
        this.valueMap = valueMap;
    }

    public void addToValueMap(String category, double value) {
        this.valueMap.put(category, value);
    }
}
