package eu.councilofcoaches.coachingengine.controller;

import eu.councilofcoaches.coachingengine.Application;
import eu.councilofcoaches.coachingengine.exception.NotFoundException;
import eu.councilofcoaches.coachingengine.topicselection.UserService;
import eu.councilofcoaches.coachingengine.topicselection.model.Domain;
import eu.councilofcoaches.coachingengine.topicselection.model.Topic;
import eu.councilofcoaches.coachingengine.topicselection.model.TopicName;
import eu.councilofcoaches.coachingengine.controller.model.DialogueParticipant;
import eu.councilofcoaches.coachingengine.controller.model.TopicMessage;
import eu.councilofcoaches.coachingengine.controller.model.TopicMessageFactory;
import eu.councilofcoaches.coachingengine.controller.model.UtteranceParams;
import eu.woolplatform.utils.AppComponents;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ServiceController {
	@Autowired
	Application application;

	private Logger logger = AppComponents.getLogger(getClass().getSimpleName());

	@RequestMapping(value="/hello", method= RequestMethod.GET)
	public String hello() {
		return "Hello";
	}


	@RequestMapping(value="get-new-topic", method=RequestMethod.GET)
	public ResponseEntity<TopicMessage> getNewTopic(
		HttpServletRequest request,
		HttpServletResponse response,
		@RequestParam(value="user", required=true) String userId,
		@RequestHeader(value="X-Auth-Token") String authToken)
			throws NotFoundException {
			return doGetNewTopic(userId, authToken);
	}


	private ResponseEntity<TopicMessage> doGetNewTopic(String userId, String authToken) throws NotFoundException {
		ResponseEntity<TopicMessage> httpReply = null;
		try {
			UserService userService = application.getServiceManager().getActiveUserService(userId, authToken);
			userService.setAuthToken(authToken);
			Topic topic = userService.getNewTopic();
			//Topic topic = new Topic(Domain.PHYSICAL_ACTIVITY, TopicName.GOALSETTING);
			TopicMessage topicMessage = this.generateTopicMessage(topic);
			httpReply = new ResponseEntity<TopicMessage>(topicMessage, HttpStatus.OK);
		}
		catch (Exception e) {
			throw new NotFoundException("No topic could be returned." + e.toString());
		}
		return httpReply;
	}

	@RequestMapping(value="get-new-topic-for-domain", method=RequestMethod.GET)
	public ResponseEntity<TopicMessage> getNewTopicForDomain(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value="user", required=true) String userId,
			@RequestHeader(value="X-Auth-Token") String authToken,
			@RequestParam(value="domain", required=true) String domain)
			throws NotFoundException {
		return doGetNewTopicForDomain(userId, authToken, domain);
	}

	private ResponseEntity<TopicMessage> doGetNewTopicForDomain(String userId, String authToken, String stringDomain) throws NotFoundException {
		ResponseEntity<TopicMessage> httpReply = null;
		try {
			Domain domain = Domain.valueOf(stringDomain);
			UserService userService = application.getServiceManager().getActiveUserService(userId, authToken);
			userService.setAuthToken(authToken);
			Topic topic = userService.getNewTopicForDomain(domain);
			TopicMessage topicMessage = this.generateTopicMessage(topic);
			httpReply = new ResponseEntity<TopicMessage>(topicMessage, HttpStatus.OK);
		}
		catch (Exception e) {
			throw new NotFoundException("No topic could be returned.");
		}
		return httpReply;
	}


	private TopicMessage generateTopicMessage(Topic topic) {
		String cmd = "new";
		String topicText = topic.getTopicName().toString();

		List<DialogueParticipant> participants = new ArrayList<DialogueParticipant>();

		String participant1 = "Olivia";
		String participant2 = "Francois";
		String participant3 = "Bob";

		participants.add(new DialogueParticipant("Agent", participant1));
		participants.add(new DialogueParticipant("Agent", participant2));
		participants.add(new DialogueParticipant("User", participant3));

		List<String> participant1CMV = new ArrayList<String>();
		participant1CMV.add("goal_setting+");
		participant1CMV.add("physical_activity+");

		List<String> participant1CAV = new ArrayList<String>();
		participant1CAV.add("steps-");

		List<String> participant1CP = new ArrayList<String>();
		participant1CP.add("intro < direct");
		participant1CP.add("calories < minutes");

		UtteranceParams filstantiatorParamsParticipant1 = new UtteranceParams(participant1, participant1CMV, participant1CAV, participant1CP, "authoritative < socratic");

		List<String> participant2CAV = new ArrayList<String>();
		participant2CAV.add("steps-");

		List<String> participant2CMV = new ArrayList<String>();
		participant2CMV.add("goal_setting+");
		participant2CMV.add("physical_activity+");

		List<String> participant2CP = new ArrayList<String>();
		participant2CP.add("intro < direct");
		participant2CP.add("calories < minutes");
		UtteranceParams filstantiatorParamsParticipant2 = new UtteranceParams(participant2, participant2CMV, participant2CAV, participant2CP, "authoritative < socratic");

		List<UtteranceParams> filstantiatorParams = new ArrayList<UtteranceParams>();
		filstantiatorParams.add(filstantiatorParamsParticipant1);
		filstantiatorParams.add(filstantiatorParamsParticipant2);

		TopicMessage topicMessage = TopicMessageFactory.generateTopicMessage(cmd, topicText, participants, filstantiatorParams);

		return topicMessage;
	}
}
