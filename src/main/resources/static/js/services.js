var services = angular.module('shop.services', [ 'ngResource' ]);

services.factory('Item', [ '$resource', function($resource) {
	return $resource('/items/', {}, {
		query : {
			method : 'GET',
			isArray : true,
		},
		update: {
			method: 'PUT',
		}
	});
} ]);
