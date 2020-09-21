package eu.agentsunited.topicselectionengine.topicselection;

import eu.agentsunited.topicselectionengine.DataController;
import eu.agentsunited.topicselectionengine.exception.DatabaseException;
import eu.agentsunited.topicselectionengine.exception.NotFoundException;
import eu.agentsunited.topicselectionengine.topicselection.model.*;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        this.addTopicAgent(Domain.CHRONICPAIN, new TopicAgent(Domain.CHRONICPAIN, this.dataController, new TopicStructureChronicPain(this.dataController)));
        this.addTopicAgent(Domain.COGNITION, new TopicAgent(Domain.COGNITION, this.dataController, new TopicStructureCognition(this.dataController)));
        this.addTopicAgent(Domain.PEER, new TopicAgent(Domain.PEER, this.dataController, new TopicStructurePeer(this.dataController)));
        this.addTopicAgent(Domain.PHYSICALACTIVITY, new TopicAgent(Domain.PHYSICALACTIVITY, this.dataController, new TopicStructurePhysicalActivity(this.dataController)));
        this.addTopicAgent(Domain.SOCIAL, new TopicAgent(Domain.SOCIAL, this.dataController, new TopicStructureSocial(this.dataController)));
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
        List<String> agentVariables = new ArrayList<String>();
        agentVariables.add("coachRasmusEnabled");
        agentVariables.add("coachOliviaEnabled");
        agentVariables.add("coachHelenEnabled");
        agentVariables.add("coachEmmaEnabled");
        agentVariables.add("coachCarlosEnabled");

        List<TopicNode> availableTopics = new ArrayList<TopicNode>();
        for (String agentVariable : agentVariables) {
            Object object = this.dataController.getVariable(agentVariable);
            if (object != null) {
                if (agentVariable.equals("coachRasmusEnabled") && object.toString().equals("true")) {
                    availableTopics.add(this.topicAgents.get(Domain.CHRONICPAIN).selectNewTopic());
                }
                if (agentVariable.equals("coachOliviaEnabled") && object.toString().equals("true")) {
                    availableTopics.add(this.topicAgents.get(Domain.PHYSICALACTIVITY).selectNewTopic());
                }
                if (agentVariable.equals("coachHelenEnabled") && object.toString().equals("true")) {
                    availableTopics.add(this.topicAgents.get(Domain.COGNITION).selectNewTopic());
                }
                if (agentVariable.equals("coachEmmaEnabled") && object.toString().equals("true")) {
                    availableTopics.add(this.topicAgents.get(Domain.SOCIAL).selectNewTopic());
                }
                if (agentVariable.equals("coachCarlosEnabled") && object.toString().equals("true")) {
                    availableTopics.add(this.topicAgents.get(Domain.PEER).selectNewTopic());
                }
            }
        }
        for (TopicNode tn : availableTopics) {
            logger.info("TOPIC NODE: " + tn.getTopic().getTopicName().toString() + " "  +tn.getLastSelectionValue());
        }

        if (availableTopics.size() == 0) {
            logger.info("NO AVAILABLE TOPICS");
            TopicNode defaultNode = new TopicNode(TopicName.DEFAULT, 1.0, 1.0);
            defaultNode.setLastSelectionValue(1.0);
            availableTopics.add(defaultNode);
        }

        double highestProbability = -1;
        TopicNode selectedNode = null;
        for (TopicNode node : availableTopics) {
            if (node.getLastSelectionValue() >= highestProbability) {
                selectedNode = node;
                highestProbability = node.getLastSelectionValue();
            }
        }

        logger.info("Last selection value: " + selectedNode.getLastSelectionValue());
        Topic topic = selectedNode.getTopic();
        return topic;
    }

    public Topic getNewTopicForDomain(Domain domain) throws NotFoundException {
        this.activeTopicAgent = this.getTopicAgentForDomain(domain);
        TopicNode topicNode = null;
        try {
            topicNode = activeTopicAgent.selectNewTopic();
        }
        catch (Exception e) {
            throw new NotFoundException("No TopicAgent found for domain: " + domain);
        }
        Topic topic = topicNode.getTopic();
        return topic;
    }

    /**
     * Returns the application's {@link ServiceManager} that is governing this {@link UserService}.
     * @return the application's {@link ServiceManager} that is governing this {@link UserService}.
     */
    public ServiceManager getServiceManager() {
        return serviceManager;
    }

    public DataController getDataController() {
        return dataController;
    }

    public void setAuthToken(String authToken) {
        this.dataController.setAuthToken(authToken);
    }
}
