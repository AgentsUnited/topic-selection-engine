package eu.agentsunited.topicselectionengine.controller.model;

import java.util.List;

public class UtteranceParams {

    private String participant;
    private UtteranceParamsParticipant parameters;

    public UtteranceParams (String participant, List<String> content_mandatory_values, List<String> content_avoid_values, List<String> content_preferences, String move_style_preferences) {
        this.participant = participant;
        this.parameters = new UtteranceParamsParticipant(content_mandatory_values, content_avoid_values, content_preferences, move_style_preferences);
    }

    public String getParticipant() {
        return this.participant;
    }

    public UtteranceParamsParticipant getParameters() {
        return this.parameters;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public void setParameters (UtteranceParamsParticipant parameters) {
        this.parameters = parameters;
    }
}
