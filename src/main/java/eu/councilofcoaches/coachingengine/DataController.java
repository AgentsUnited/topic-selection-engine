package eu.councilofcoaches.coachingengine;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu.councilofcoaches.coachingengine.exception.DatabaseException;
import eu.councilofcoaches.coachingengine.topicselection.ServiceManager;
import eu.woolplatform.utils.http.HttpClient;
import eu.woolplatform.utils.http.HttpClientException;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Class defining a data controller is used for retrieving the values for variables from a WOOL Web Service.
 */
public class DataController {

    public static final Logger logger = ServiceManager.getLogger(DataController.class);
    private String userId;
    private String authToken;
    private String woolWebServiceURL;

    public DataController(String userId, String authToken, String woolWebServiceURL) {
        this.userId = userId;
        this.authToken = authToken;
        this.woolWebServiceURL = woolWebServiceURL;
    }

    public Object getVariable(String variableName) throws DatabaseException, IOException {
        Object variableValue = null;
        String url = woolWebServiceURL + "variables" + "?names=" + variableName;
        String response;
        HttpClient client = new HttpClient(url);
        try {
            response = client.addHeader("X-Auth-Token", this.authToken).readString();
            logger.info(response);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> result = mapper.readValue(response, HashMap.class);
            variableValue = result.get(variableName);
        }
        catch (HttpClientException ex) {
            throw new DatabaseException("Connecting to the WOOL Web Service returned error response: " + ex.getMessage());
        }
        finally {
            client.close();
        }
        return variableValue;
    }

    public Map<String, Object> getVariables(Collection<String> variableNames) throws DatabaseException, IOException {
        Map<String, Object> variableValues = null;
        String token = "";
        String url = woolWebServiceURL + "variables" + "?names=";
        for (String variableName : variableNames) {
            url += "variableName" + "%20";
        }
        String response;
        HttpClient client = new HttpClient(url);
        try {
            response = client.addHeader("X-Auth-Token", this.authToken).readString();
        }
        catch (HttpClientException ex) {
            throw new DatabaseException("Connecting to the WOOL Web Service returned error response: " + ex.getMessage());
        }
        finally {
            client.close();
        }
        return variableValues;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
