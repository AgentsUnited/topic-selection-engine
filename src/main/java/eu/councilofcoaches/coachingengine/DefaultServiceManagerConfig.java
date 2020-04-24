package eu.councilofcoaches.coachingengine;

import eu.councilofcoaches.coachingengine.topicselection.ServiceManager;
import eu.councilofcoaches.coachingengine.topicselection.ServiceManagerConfig;
import eu.councilofcoaches.coachingengine.topicselection.UserService;

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
