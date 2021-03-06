package org.qdeve.example.angularjs.integration.controller;

import static org.hamcrest.Matchers.contains;

import org.hamcrest.MatcherAssert;

import java.util.List;
import java.util.Map;

import org.qdeve.example.angularjs.controller.ResponseMessage;
import org.qdeve.example.angularjs.dao.SaveStatus;
import org.qdeve.example.angularjs.model.Item;

public class ResponseMessageAssertion {
	private ResponseMessage responseMessage;

	public ResponseMessageAssertion(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public static ResponseMessageAssertion assertThat(ResponseMessage responseMessage) {
		return new ResponseMessageAssertion(responseMessage);
	}
	
	public static ResponseMessageAssertion assertThat(Map<SaveStatus, List<Item>> mapResult) {
		return new ResponseMessageAssertion(new ResponseMessage(mapResult));
	}

	public ResponseMessageAssertion hasResponseItemsFor(SaveStatus status) {
		if (responseMessage.getUpdateStatus().get(status) == null 
				|| responseMessage.getUpdateStatus().get(status).isEmpty()) {
			throw new AssertionError("Excpeting that given ResponseMessage contains " 
					+ status + " items, while in provided ResponseMessage was: " 
					+ responseMessage.getUpdateStatus().get(status) + ".");			
		}
		return this;
	}

	public ResponseMessageAssertion responseAndContentNotNull() {
		if (responseMessage == null || responseMessage.getUpdateStatus() == null) {
			throw new AssertionError("Excpeting that given ResponseMessage is not null, " 
					+ "while provided ResponseMessage is.");
		}
		return this;
	}

	public ResponseMessageAssertion hasNoItemsFor(SaveStatus status) {
		if (responseMessage.getUpdateStatus().get(status) != null) {
			throw new AssertionError("Excpeting that given ResponseMessage does not contain " 
					+ status + " items, while in provided ResponseMessage was: " 
					+ responseMessage.getUpdateStatus().get(status) + ".");			
		}
		return this;
	}

	public ResponseMessageAssertion hasExatlyGivenItemsFor(SaveStatus state, List<Item> content) {
		hasResponseItemsFor(state);
		List<Item> stateList = responseMessage.getUpdateStatus().get(state);
		MatcherAssert.assertThat(stateList, contains(content.toArray()));
		return this;
	}
}
