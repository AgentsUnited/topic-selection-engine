package eu.councilofcoaches.coachingengine.topicselection.model;

import eu.councilofcoaches.coachingengine.DataController;
import eu.councilofcoaches.coachingengine.exception.DatabaseException;
import eu.councilofcoaches.coachingengine.topicselection.ServiceManager;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class with which for each {@Link SelectionParameter} its value can be retrieved.
 * Links @SelectionParameter to variables retrieved from the variable variable store.
 *
 * @author Tessa Beinema
 */
public class SelectionParameterMapping {

    public static final Logger logger = ServiceManager.getLogger(SelectionParameterMapping.class);

    private DataController dataController;

    public SelectionParameterMapping(DataController dataController) {
        this.dataController = dataController;
    }

    public Map<String, Object> loadVariables(Collection<String> variables) throws DatabaseException, IOException {
        return this.dataController.getVariables(variables);
    }

    /**
     * Method to retrieve the value for a variable.
     *
     * @param variableName
     * @return A String value for the variable.
     * @throws DatabaseException
     * @throws IOException
     */
    public String getValueForVariable(String variableName) throws DatabaseException, IOException {
        String result = "";
        Object value = this.dataController.getVariable(variableName);
        if (value == null) {
            result = "null";
        }
        else {
            result = value.toString();
        }
        return result;
    }

    /**
     * Method that returns the values for {@Link CategoricalSelectionParameter}s.
     * @param parameterNames A list with parameter names
     * @return A map of parameter names to their @String values.
     * @throws DatabaseException
     * @throws IOException
     */
    public Map<String, String> getCategoricalSelectionParameterValues(List<String> parameterNames) throws DatabaseException, IOException {
        Map<String, String> valueMap = new HashMap<String, String>();
        for(String parameterName : parameterNames) {
            if(parameterName.equals("paUserCompletedIntroductionEnabling") || parameterName.equals("paUserCompletedIntroductionLimiting")) {
                if (this.getValueForVariable("paUserCompletedIntroduction").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paUserHasLongTermGoal")) {
                if (this.getValueForVariable("paUserHasLongTermGoal").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
        }
        return valueMap;
    }

    /**
     * Method that returns the values for {@Link ContinousSelectionParameter}s.
     * @param parameterNames A list with parameter names
     * @return A map of parameter names to their Double values.
     * @throws DatabaseException
     * @throws IOException
     */
    public Map<String, Double> getContinuousSelectionParameterValues(List<String> parameterNames) {
        Map<String, Double> valueMap = new HashMap<String,Double>();
        for(String parameterName : parameterNames) {
            valueMap.put(parameterName, 1.0);
        }
        return valueMap;
    }
}
