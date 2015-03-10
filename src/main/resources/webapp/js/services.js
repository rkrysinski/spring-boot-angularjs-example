var services = angular.module('shop.services', [ 'ngResource' ]);

services.factory('Item', [ '$resource', function($resource) {
	return $resource('/item/', {}, {
		query : {
			method : 'GET',
			isArray : true,
			transformResponse : function(data) {
				var returnValue = angular.fromJson(data);
				returnValue.forEach(function(item) {
					item.toBuy = "";
				});
				return returnValue;
			}
		},
		update: {
			method: 'PUT',
			isArray : true,
			transformRequest : function(data) {
				var returnValue = angular.toJson(data);
//				data.forEach(function(item) {
//					item.toBuy = "";
//				});
				console.log(returnValue);
				return returnValue;
			}
		}
	});
} ]);