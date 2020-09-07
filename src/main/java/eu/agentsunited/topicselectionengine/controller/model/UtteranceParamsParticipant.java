package eu.agentsunited.topicselectionengine.controller.model;

import java.util.List;

public class UtteranceParamsParticipant {
    private List<String> content_mandatory_values;
    private List<String> content_avoid_values;
    private List<String> content_preferences;
    private String move_style_preferences;

    public UtteranceParamsParticipant(List<String> content_mandatory_values, List<String> content_avoid_values, List<String> content_preferences, String move_style_preferences) {
        this.content_mandatory_values = content_mandatory_values;
        this.content_avoid_values = content_avoid_values;
        this.content_preferences = content_preferences;
        this.move_style_preferences = move_style_preferences;
    }

    public List<String> getContent_mandatory_values() {
        return this.content_mandatory_values;
    }

    public List<String> getContent_avoid_values() {
        return this.content_avoid_values;
    }

    public String getMove_style_preferences() {
        return this.move_style_preferences;
    }

    public List<String> getContent_preferences() {
        return this.content_preferences;
    }

    public void setContent_mandatory_values(List<String> content_mandatory_values) {
        this.content_mandatory_values = content_mandatory_values;
    }

    public void setContent_avoid_values(List<String> content_avoid_values) {
        this.content_avoid_values = content_avoid_values;
    }

    public void setContent_preferences(List<String> content_preferences) {
        this.content_preferences = content_preferences;
    }

    public void setMove_style_preferences(String move_style_preferences) {
        this.move_style_preferences = move_style_preferences;
    }

}
