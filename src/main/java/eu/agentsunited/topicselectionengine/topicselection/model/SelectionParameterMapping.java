package eu.agentsunited.topicselectionengine.topicselection.model;

import eu.agentsunited.topicselectionengine.DataController;
import eu.agentsunited.topicselectionengine.exception.DatabaseException;
import eu.agentsunited.topicselectionengine.topicselection.ServiceManager;
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
            if(parameterName.equals("cpUserCompletedWeek3")) {
                if (this.getValueForVariable("cpUserCompletedWeek3").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("cogUserCompletedCaaSEnabling")) {
                if (this.getValueForVariable("esmHelenCompleted").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("peerUserCompletedIntroductionEnabling") || parameterName.equals("peerUserCompletedIntroductionLimiting")) {
                if (this.getValueForVariable("peerUserCompletedIntroduction").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("peerUserCompletedCaaSEnabling")) {
                if (this.getValueForVariable("esmCarlosCompleted").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paUserCompletedIntroductionEnabling") || parameterName.equals("paUserCompletedIntroductionLimiting")) {
                if (this.getValueForVariable("paUserCompletedIntroduction").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paUserHasGoalType")) {
                if (!this.getValueForVariable("paCurrentGoalType").equals("null")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paUserHasShortTermGoalEnabling") || parameterName.equals("paUserHasShortTermGoalLimiting")) {
                if (!(this.getValueForVariable("paShortTermStepsGoal").equals("null")) || !(this.getValueForVariable("paShortTermMinutesGoal").equals("null")) ) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paUserHasLongTermGoal")) {
                if (!(this.getValueForVariable("paLongTermStepsGoal").equals("null")) || !(this.getValueForVariable("paLongTermMinutesGoal").equals("null")) ) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paUserHasWeightMeasurementEnabling") || parameterName.equals("paUserHasWeightMeasurementLimiting")) {
                if (!(this.getValueForVariable("paUserWeightValue").equals("null"))) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paUserCompletedFeedbackEnabling")) {
                if (!(this.getValueForVariable("paUserCompletedFeedbackSteps").equals("true") || this.getValueForVariable("paUserCompletedFeedbackMinutes").equals("true"))
                        || (!this.getValueForVariable("paUserCompletedFeedbackWeightMeasurement").equals("true"))) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paDataLimiting")) {
                if (!(this.getValueForVariable("StepsThisWeek").equals("null")) || !(this.getValueForVariable("MinutesThisWeek").equals("null"))
                        && this.getValueForVariable("paUserCompletedFeedbackWeightMeasurement").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("socUserCompletedIntroductionEnabling") || parameterName.equals("socUserCompletedIntroductionLimiting")) {
                if (this.getValueForVariable("socUserCompletedIntroduction").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("socUserCompletedCaaSEnabling") || parameterName.equals("socUserCompletedCaaSLimiting")) {
                if (this.getValueForVariable("esmEmmaCompleted").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("socUserCompletedFeedbackCaaSEnabling")) {
                if (this.getValueForVariable("socUserCompletedFeedbackCaaS").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }

            //Parameters for the example topic script
            if(parameterName.equals("paUserCompletedIntroduction")) {
                if (this.getValueForVariable("paUserCompletedIntroduction").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paUserCompletedGoalSetting")) {
                if (this.getValueForVariable("paUserCompletedGoalSetting").equals("true")) {
                    valueMap.put(parameterName, "true");
                }
                else {
                    valueMap.put(parameterName, "false");
                }
            }
            if(parameterName.equals("paUserCompletedFeedback")) {
                if (this.getValueForVariable("paUserCompletedFeedback").equals("true")) {
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
