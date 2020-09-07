package eu.agentsunited.topicselectionengine;

import eu.agentsunited.topicselectionengine.topicselection.ServiceManager;
import eu.agentsunited.topicselectionengine.topicselection.ServiceManagerConfig;
import eu.agentsunited.topicselectionengine.topicselection.UserService;

public class DefaultServiceManagerConfig extends ServiceManagerConfig {

    private String woolWebServiceURL;

    public DefaultServiceManagerConfig(String woolWebServiceURL) {
        this.woolWebServiceURL = woolWebServiceURL;
    }

    @Override
    public UserService createUserService(String userId, String authToken, ServiceManager serviceManager) {
        return new UserService(userId, authToken, serviceManager, woolWebServiceURL);
    }
}
