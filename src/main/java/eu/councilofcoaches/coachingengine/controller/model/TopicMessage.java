package eu.councilofcoaches.coachingengine.controller.model;

import java.util.List;
import java.util.UUID;

public class TopicMessage {
    private String id;
    private String cmd;
    private String topic;
    private List<DialogueParticipant> participants;
    private List<UtteranceParams> utteranceParams;

    // -------------------- Constructors

    public TopicMessage(String cmd, String topic, List<DialogueParticipant> participants, List<UtteranceParams> utteranceParams) {
        this.id = UUID.randomUUID().toString();
        this.cmd = cmd;
        this.topic = topic;
        this.participants = participants;
        this.utteranceParams = utteranceParams;
    }

    // -------------------- Getters

    public String getId() {
        return this.id;
    }

    public String getCmd() {
        return this.cmd;
    }

    public String getTopic() {
        return topic;
    }

    public List<DialogueParticipant> getParticipants() {
        return participants;
    }

    public List<UtteranceParams> getUtteranceParams() {
        return utteranceParams;
    }

    // -------------------- Setters

    public void setId(String id) {
        this.id = id;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setParticipants(List<DialogueParticipant> participants) {
        this.participants = participants;
    }

    public void setUtteranceParams(List<UtteranceParams> utteranceParams) {
        this.utteranceParams = utteranceParams;
    }
}
