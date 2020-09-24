package eu.agentsunited.topicselectionengine.controller;

import eu.agentsunited.topicselectionengine.Application;
import eu.agentsunited.topicselectionengine.exception.NotFoundException;
import eu.agentsunited.topicselectionengine.topicselection.UserService;
import eu.agentsunited.topicselectionengine.topicselection.model.Domain;
import eu.agentsunited.topicselectionengine.topicselection.model.Topic;
import eu.agentsunited.topicselectionengine.topicselection.model.TopicName;
import eu.agentsunited.topicselectionengine.controller.model.DialogueParticipant;
import eu.agentsunited.topicselectionengine.controller.model.TopicMessage;
import eu.agentsunited.topicselectionengine.controller.model.TopicMessageFactory;
import eu.agentsunited.topicselectionengine.controller.model.UtteranceParams;
import eu.agentsunited.topicselectionengine.exception.DatabaseException;

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
import java.io.IOException;


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
		@RequestParam(value="user", required=true) String userId)
			throws NotFoundException {
			return doGetNewTopic(userId, request.getHeader("X-Auth-Token"));
	}


	private ResponseEntity<TopicMessage> doGetNewTopic(String userId, String authToken) throws NotFoundException {
		ResponseEntity<TopicMessage> httpReply = null;
		try {
			UserService userService = application.getServiceManager().getActiveUserService(userId, authToken);
			userService.setAuthToken(authToken);
			Topic topic = userService.getNewTopic();
			TopicMessage topicMessage = this.generateTopicMessage(userService, topic);
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
			@RequestParam(value="domain", required=true) String domain)
			throws NotFoundException {
		return doGetNewTopicForDomain(userId, request.getHeader("X-Auth-Token"), domain);
	}

	private ResponseEntity<TopicMessage> doGetNewTopicForDomain(String userId, String authToken, String stringDomain) throws NotFoundException {
		ResponseEntity<TopicMessage> httpReply = null;
		try {
			Domain domain = Domain.valueOf(stringDomain);
			UserService userService = application.getServiceManager().getActiveUserService(userId, authToken);
			userService.setAuthToken(authToken);
			Topic topic = userService.getNewTopicForDomain(domain);
			TopicMessage topicMessage = this.generateTopicMessage(userService, topic);
			httpReply = new ResponseEntity<TopicMessage>(topicMessage, HttpStatus.OK);
		}
		catch (Exception e) {
			throw new NotFoundException("No topic could be returned.");
		}
		return httpReply;
	}


	private TopicMessage generateTopicMessage(UserService userService, Topic topic) throws DatabaseException, IOException {
		String cmd = "new";
		String topicText = topic.getTopicName().toString();

		List<DialogueParticipant> participants = new ArrayList<DialogueParticipant>();

		String agentName = "";
		if(topic.getTopicDomain() == Domain.PHYSICALACTIVITY) {
			agentName = "Olivia";
		}
		else if (topic.getTopicDomain() == Domain.CHRONICPAIN) {
			agentName = "Rasmus";
		}
		else if (topic.getTopicDomain() == Domain.PEER) {
			agentName = "Carlos";
		}
		else if (topic.getTopicDomain() == Domain.SOCIAL) {
			agentName = "Emma";
		}
		else if (topic.getTopicDomain() == Domain.COGNITION) {
			agentName = "Helen";
		}
		else {
			agentName = "Default";
		}
		DialogueParticipant participant1 = new DialogueParticipant("Agent", agentName);
		participants.add(participant1);
		Object object = userService.getDataController().getVariable("userFirstName");

		DialogueParticipant participant2 = null;
		if (object != null) {
			participant2 = new DialogueParticipant("User", "User");
		}
		else {
			participant2 = new DialogueParticipant("User", "User");
		}
		participants.add(participant2);

		if(topic.getTopicDomain() == Domain.PEER && topic.getTopicName() == TopicName.INTRODUCTION) {
			participants.add(new DialogueParticipant("Agent", "Olivia"));
		}
		if(topic.getTopicDomain() == Domain.PHYSICALACTIVITY && topic.getTopicName() == TopicName.INTRODUCTION) {
			participants.add(new DialogueParticipant("Agent", "Emma"));
			participants.add(new DialogueParticipant("Agent", "Carlos"));
		}
		if(topic.getTopicDomain() == Domain.SOCIAL && topic.getTopicName() == TopicName.INTRODUCTION) {
			participants.add(new DialogueParticipant("Agent", "Olivia"));
			participants.add(new DialogueParticipant("Agent", "Carlos"));
		}

		List<String> participant1CMV = new ArrayList<String>();
		List<String> participant1CAV = new ArrayList<String>();
		List<String> participant1CP = new ArrayList<String>();
		if(topic.getTopicDomain() == Domain.PHYSICALACTIVITY && topic.getTopicName() == TopicName.GOALSETTING) {
			object = userService.getDataController().getVariable("paCurrentGoalType");
			if (object != null) {
				Object object2 = null;
				if (object.toString().equals("steps")) {
					participant1CAV.add("minutes-");
					object2 = userService.getDataController().getVariable("paLongTermStepsGoal");
					if (object2 != null && !object2.equals("null")) {
						participant1CMV.add("short_term_goal+");
						participant1CMV.add("steps+");
					}
					else {
						participants.add(new DialogueParticipant("Agent", "Emma"));
						participant1CMV.add("long_term_goal+");
						participant1CMV.add("steps+");
					}
				}
				else if (object.toString().equals("minutes")) {
					participant1CAV.add("steps-");
					object2 = userService.getDataController().getVariable("paLongTermMinutesGoal");
					if (object2 != null  && !object2.equals("null")) {
						participant1CMV.add("short_term_goal+");
						participant1CMV.add("minutes+");
					}
					else {
						participants.add(new DialogueParticipant("Agent", "Emma"));
						participant1CMV.add("long_term_goal+");
						participant1CMV.add("minutes+");
					}
				}
				else if (object.toString().equals("null")) {
					participant1CMV.add("goal_type+");
					topicText += "1";
				}

			}
			else {
				participant1CMV.add("goal_type+");
				topicText += "1";
			}
		}
		if(topic.getTopicDomain() == Domain.PHYSICALACTIVITY && topic.getTopicName() == TopicName.FEEDBACK) {
			object = userService.getDataController().getVariable("paUserCompletedFeedbackWeightMeasurement");
			if (object != null) {
				if (object.toString().equals("false") || object.toString().equals("null")) {
					participant1CMV.add("weight+");

					Object object2 = userService.getDataController().getVariable("paCurrentGoalType");
					if (object2 != null  && !object2.equals("null")) {
						if (object2.toString().equals("steps")) {
							participant1CAV.add("steps-");
						}
						else if (object2.toString().equals("minutes")) {
							participant1CAV.add("minutes-");
						}
					}
					participant1CAV.add("steps-");
				}
				else if (object.toString().equals("true")) {
					participant1CAV.add("weight-");
					Object object2 = userService.getDataController().getVariable("paCurrentGoalType");
					if (object2 != null  && !object2.equals("null")) {
						if (object2.toString().equals("steps")) {
							participant1CMV.add("steps+");
						}
						else if (object2.toString().equals("minutes")) {
							participant1CMV.add("minutes+");
						}
					}
				}
			}
			else {
				participant1CMV.add("weight+");
				Object object2 = userService.getDataController().getVariable("paCurrentGoalType");
				if (object2 != null  && !object2.equals("null")) {
					if (object2.toString().equals("steps")) {
						participant1CAV.add("steps-");
					}
					else if (object2.toString().equals("minutes")) {
						participant1CAV.add("minutes-");
					}
				}
			}
		}

		UtteranceParams filstantiatorParamsParticipant1 = new UtteranceParams(participant1.getName(), participant1CMV, participant1CAV, participant1CP, "authoritative < socratic");

		List<String> participant2CAV = new ArrayList<String>();
		//participant2CAV.add("steps-");
		participant2CAV = participant1CAV;

		List<String> participant2CMV = new ArrayList<String>();
		//participant2CMV.add("goal_setting+");
		//participant2CMV.add("physical_activity+");
		participant2CMV = participant1CMV;

		List<String> participant2CP = new ArrayList<String>();
		//participant2CP.add("intro < direct");
		//participant2CP.add("calories < minutes");
		participant2CP = participant1CP;
		UtteranceParams filstantiatorParamsParticipant2 = new UtteranceParams(participant2.getName(), participant2CMV, participant2CAV, participant2CP, "authoritative < socratic");

		List<UtteranceParams> filstantiatorParams = new ArrayList<UtteranceParams>();
		filstantiatorParams.add(filstantiatorParamsParticipant1);
		filstantiatorParams.add(filstantiatorParamsParticipant2);

		List<String> emptyCAV = new ArrayList<>();
		List<String> emptyCMV = new ArrayList<>();
		List<String> empty2CP = new ArrayList<>();

		if (participants.size() > 2) {
			for (int i = 2; i < participants.size(); i++) {
				UtteranceParams filstantiatorParamsParticipantN = new UtteranceParams(participants.get(i).getName(), emptyCMV, emptyCAV, empty2CP, "");
				filstantiatorParams.add(filstantiatorParamsParticipantN);
			}
		}

		TopicMessage topicMessage = TopicMessageFactory.generateTopicMessage(cmd, topicText, participants, filstantiatorParams);

		return topicMessage;
	}
}
