package eu.agentsunited.topicselectionengine.controller.model;

import java.util.List;

public class DialogueParams {

    private String protocol;
    private List<DialogueParticipant> participants;

    public DialogueParams (String protocol, List<DialogueParticipant> participants) {
        this.protocol = protocol;
        this.participants = participants;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public List<DialogueParticipant> getParticipants() {
        return this.participants;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setParticipants(List<DialogueParticipant> participants) {
        this.participants = participants;
    }
}
