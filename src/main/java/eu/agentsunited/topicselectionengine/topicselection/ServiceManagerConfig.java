package eu.agentsunited.topicselectionengine.topicselection;

public abstract class ServiceManagerConfig {

    private static ServiceManagerConfig instance = null;

    public static ServiceManagerConfig getInstance() {
        return instance;
    }

    public static void setInstance(ServiceManagerConfig instance) {
        ServiceManagerConfig.instance = instance;
    }

    /**
     * Constructs a new configuration.
     */

    public ServiceManagerConfig() {
    }

    public abstract UserService createUserService(String userId, String authToken, ServiceManager serviceManager);
}
