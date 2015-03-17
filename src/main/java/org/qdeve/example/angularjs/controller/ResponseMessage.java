package org.qdeve.example.angularjs.controller;

import java.util.List;
import java.util.Map;

import org.qdeve.example.angularjs.dao.SaveStatus;
import org.qdeve.example.angularjs.model.Item;

/**
 * Class that encapsulates the response to the browser.
 */
public class ResponseMessage {

	private Map<SaveStatus, List<Item>> updateStatus;
	
	public ResponseMessage() {}
	
	public ResponseMessage(Map<SaveStatus, List<Item>> updateResult) {
		this.updateStatus = updateResult;
	}

	public static ResponseMessage fromResult(Map<SaveStatus, List<Item>> updateResult) {
		return new ResponseMessage(updateResult);
	}
	
	public Map<SaveStatus, List<Item>> getUpdateStatus() {
		return updateStatus;
	}

	public void setUpdateStatus(Map<SaveStatus, List<Item>> updateStatus) {
		this.updateStatus = updateStatus;
	}
	
}
