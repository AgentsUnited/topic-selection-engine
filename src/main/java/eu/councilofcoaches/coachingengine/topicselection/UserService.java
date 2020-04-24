package eu.councilofcoaches.coachingengine.topicselection;

import eu.councilofcoaches.coachingengine.DataController;
import eu.councilofcoaches.coachingengine.exception.DatabaseException;
import eu.councilofcoaches.coachingengine.exception.NotFoundException;
import eu.councilofcoaches.coachingengine.topicselection.model.Domain;
import eu.councilofcoaches.coachingengine.topicselection.model.Topic;
import eu.councilofcoaches.coachingengine.topicselection.model.TopicName;
import eu.councilofcoaches.coachingengine.topicselection.model.TopicStructurePhysicalActivity;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * An {@link UserService} class models an instance for topic selection for one or more {@link TopicAgent}s, belonging to a specific userId's instance.
 *
 * @author Harm op den Akker
 * @author Tessa Beinema
 */
public class UserService {
    public static final Logger logger = ServiceManager.getLogger(UserService.class);

    public String userId;
    public ServiceManager serviceManager;
    public DataController dataController;

    private Map<Domain, TopicAgent> topicAgents;
    private TopicAgent activeTopicAgent;

    /**
     * Instantiates an {@link UserService} for a given user, identified
     * by the associated {@code accountId} and {@code userId}.
     * @param userId - A unique identifier of the current user this {@link UserService} is interacting with.
     * @param serviceManager the server's {@link ServiceManager} instance.
     */
    public UserService(String userId, String authToken, ServiceManager serviceManager, String woolWebServiceURL)     {
        this.userId = userId;
        this.serviceManager = serviceManager;

        this.topicAgents = new HashMap<Domain, TopicAgent>();
        this.dataController = new DataController(this.userId, authToken, woolWebServiceURL);
        this.addTopicAgent(Domain.PHYSICAL_ACTIVITY, new TopicAgent(Domain.PHYSICAL_ACTIVITY, this.dataController, new TopicStructurePhysicalActivity(this.dataController)));
    }

    private void addTopicAgent(Domain domain, TopicAgent agent) {
        topicAgents.put(domain, agent);
    }



    // ---------- Getters:

    /**
     * Returns the user identifier which this {@link UserService} is serving.
     * @return the user identifier which this {@link UserService} is serving.
     */
    public String getUserId() {
        return userId;
    }

    protected TopicAgent getTopicAgentForDomain(Domain domain) throws NotFoundException {
        if (!this.topicAgents.containsKey(domain)) {
            throw new NotFoundException("Topic agent not found for domain: " + domain);
        }
        return topicAgents.get(domain);
    }

    public Topic getNewTopic() throws NotFoundException, DatabaseException, IOException {
        Topic topic = this.topicAgents.get(Domain.PHYSICAL_ACTIVITY).selectNewTopic();
        /**try{
            for(Domain domain : this.topicAgents.keySet()) {
                topic = this.topicAgents.get(domain).selectNewTopic();
            }
        }
        catch (Exception e) {
            throw new NotFoundException("No TopicAgents found.");
        }*/
        return topic;
    }

    public Topic getNewTopicForDomain(Domain domain) throws NotFoundException {
        this.activeTopicAgent = this.getTopicAgentForDomain(domain);
        Topic topic = null;
        try {
            topic = activeTopicAgent.selectNewTopic();
        }
        catch (Exception e) {
            throw new NotFoundException("No TopicAgent found for domain: " + domain);
        }
        return topic;
    }

    /**
     * Returns the application's {@link ServiceManager} that is governing this {@link UserService}.
     * @return the application's {@link ServiceManager} that is governing this {@link UserService}.
     */
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setAuthToken(String authToken) {
        this.dataController.setAuthToken(authToken);
    }
}
