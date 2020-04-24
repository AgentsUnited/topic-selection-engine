package eu.councilofcoaches.coachingengine.topicselection;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link ServiceManager} manages one or more services, each corresponding to a specific userId-password combination.
 *
 * @author Harm op den Akker
 * @author Tessa Beinema
 *
 */
public class ServiceManager {

    private final Logger logger = getLogger(ServiceManager.class);
    private List<UserService> activeUserServices = new ArrayList<>();

    /**
     * Creates an instance of an {@link ServiceManager}, that loads in a
     * predefined list of Wool dialogues.
     */
    public ServiceManager(String woolWebServiceURL) {
        logger.info("Initializing ServiceManager.");
        long startMS = System.currentTimeMillis();

        ServiceManagerConfig appConfig = ServiceManagerConfig.getInstance();

        long endMS = System.currentTimeMillis();
        long procTime = endMS - startMS;
        logger.info("ServiceManager initialized in "+procTime+"ms.");
    }

    private static ILoggerFactory loggerFactory =
            LoggerFactory.getILoggerFactory();

    public static Logger getLogger(Class<?> clazz) {
        return loggerFactory.getLogger(clazz.getName());
    }

    public static Logger getLogger(String name) {
        return loggerFactory.getLogger(name);
    }

    public static ILoggerFactory getLoggerFactory() {
        return loggerFactory;
    }

    public static void setLoggerFactory(ILoggerFactory loggerFactory) {
        ServiceManager.loggerFactory = loggerFactory;
    }

    /**
     * Returns an active {@link UserService} object for the given {@code accountId} and {@code userId}. Retrieves
     * from an internal list of active {@link UserService}s, or instantiates a new {@link UserService} if no {@link UserService} is active
     * for the given parameters.
     * @param userId the identifier of the specific user that is interacting with the {@link UserService}.
     * @return an {@link UserService} object that can handle the communication with the user.
     */
    public UserService getActiveUserService(String userId, String authToken) {

        for(UserService userService : activeUserServices) {
            if(userService.getUserId().equals(userId)) {
                return userService;
            }
        }

        logger.info("No active UserService for userId '"+userId+"' creating UserService instance.");

        // Initialize new userService
        ServiceManagerConfig appConfig = ServiceManagerConfig.getInstance();

        UserService newUserService;
        try {
            newUserService = appConfig.createUserService(userId, authToken, this);
        } catch (Exception ex) {
            String error = "Can't create userService: " + ex.getMessage();
            logger.error(error, ex);
            throw new RuntimeException(error, ex);
        }

        // Store as "active userService"
        activeUserServices.add(newUserService);
        return newUserService;
    }

    /**
     * Removes the given {@link UserService} from the set of active {@link UserService}s in
     * this {@link ServiceManager}.
     * @param userService the {@link UserService} to remove.
     * @return {@code true} if the given was successfully removed, {@code false} if
     * it was not present on the list of active {@link UserService}s in the first place.
     */
    public boolean removeUserService(UserService userService) {
        return activeUserServices.remove(userService);
    }
}
