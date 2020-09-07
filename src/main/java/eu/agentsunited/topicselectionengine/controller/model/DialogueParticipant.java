package eu.agentsunited.topicselectionengine.controller.model;

public class DialogueParticipant {

    private String player;
    private String name;

    public DialogueParticipant(String player, String name) {
        this.player = player;
        this.name = name;
    }

    public String getPlayer() {
        return this.player;
    }

    public String getName() {
        return this.name;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public void setName(String name) {
        this.name = name;
    }
}
