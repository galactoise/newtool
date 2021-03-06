package com._3po_labs.alexa_model_tester.messagequeue;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.amazon.speech.json.SpeechletRequestEnvelope;

public class AlexaRequestQueue {

	private Map<String, SpeechletRequestEnvelope> alexaRequests;
	
	private Set<Object> registeredObjects;
	
	private AlexaRequestQueue() {
		alexaRequests = new HashMap<String, SpeechletRequestEnvelope>();
		registeredObjects = new HashSet<Object>();
	}

	private static class SingletonHolder {
		private static final AlexaRequestQueue INSTANCE = new AlexaRequestQueue();
	}

	public static AlexaRequestQueue getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public SpeechletRequestEnvelope getAlexaResponse(String userId, String applicationId){
		String hash = hash(userId, applicationId);
		
		synchronized(alexaRequests){
			return alexaRequests.remove(hash);
		}
	}
	
	public void addAlexaResponse(String userId, String applicationId, SpeechletRequestEnvelope sre){
		String hash = hash(userId, applicationId);
		
		synchronized(alexaRequests){
			alexaRequests.put(hash, sre);
		}
	}
	
	private String hash(String userId, String applicationId){
		return userId + applicationId;
	}
	
	public void registerThread(Object object){
		registeredObjects.add(object);
	}
	
	public void deregisterThread(Object object){
		registeredObjects.remove(object);
	}
	
	public void notifyAllThreads(){
		for(Object object : registeredObjects){
			object.notifyAll();
		}
	}
}
