'use strict';

angular.module('shop.filters', []).filter('reason', function() {
	return function(error) {
		
		var retValue = "";
		if (error) {
			retValue = "Unable to connect to our server. Either it is down or there is a problem with connection. ";
			retValue += "Please try again later, and if the problem persists please contact our support.";
			if (error.data) {
				retValue = "Internal error: " + error.data.message;
				retValue += ". Please contact our shop support if problem persists.";
			}
		}
		return retValue;
	};
});