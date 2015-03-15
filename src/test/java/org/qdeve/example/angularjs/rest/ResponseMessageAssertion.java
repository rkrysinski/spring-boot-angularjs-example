package org.qdeve.example.angularjs.rest;

import org.qdeve.example.angularjs.repo.SaveStatus;

public class ResponseMessageAssertion {
	private ResponseMessage responseMessage;

	public ResponseMessageAssertion(ResponseMessage responseMessage) {
		this.responseMessage = responseMessage;
	}
	
	public static ResponseMessageAssertion assertThat(ResponseMessage responseMessage) {
		return new ResponseMessageAssertion(responseMessage);
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

	public ResponseMessageAssertion hasNotItemsFor(SaveStatus status) {
		if (responseMessage.getUpdateStatus().get(status) != null) {
			throw new AssertionError("Excpeting that given ResponseMessage does not contain " 
					+ status + " items, while in provided ResponseMessage was: " 
					+ responseMessage.getUpdateStatus().get(status) + ".");			
		}
		return this;
	}
}
