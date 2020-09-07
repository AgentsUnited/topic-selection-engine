package eu.agentsunited.topicselectionengine.topicselection.model;

/**
 * Class that extends a {@link SelectionParameter} and represents a selection parameter
 * with continuous values.
 *
 * @author Tessa Beinema
 */
public class ContinuousSelectionParameter extends SelectionParameter  {

    public ContinuousSelectionParameter(String key, double weight) {
        super(key, weight);
    }
}
