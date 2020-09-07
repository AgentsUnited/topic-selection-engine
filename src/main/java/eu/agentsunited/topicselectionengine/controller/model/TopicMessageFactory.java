package eu.agentsunited.topicselectionengine.controller.model;

import java.util.List;

public class TopicMessageFactory {

    public static TopicMessage generateTopicMessage(String cmd, String topic, List<DialogueParticipant> participants, List<UtteranceParams> utteranceParams) {
        TopicMessage topicMessage = new TopicMessage(cmd, topic, participants, utteranceParams);

        return topicMessage;
    }
}
