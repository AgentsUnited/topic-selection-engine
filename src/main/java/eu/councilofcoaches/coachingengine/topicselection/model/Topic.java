package eu.councilofcoaches.coachingengine.topicselection.model;

/**
 * Class representing a (conversation) Topic.
 *
 * @author Tessa Beinema
 */
public class Topic {
    public Domain topicDomain;
    public TopicName topicName;

    public Topic(Domain topicDomain, TopicName topicName) {
        this.topicDomain = topicDomain;
        this.topicName = topicName;
    }

    public Domain getTopicDomain() {
        return this.topicDomain;
    }

    public TopicName getTopicName() {
        return this.topicName;
    }

    public void setTopicDomain(Domain domain) {
        this.topicDomain = domain;
    }

    public void setTopicName(TopicName topicName) {
        this.topicName = topicName;
    }
}
