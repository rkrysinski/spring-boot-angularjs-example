'use strict';

angular.module('shop.filters', []).filter('reason', function() {
	return function(error) {
		return error ? error.data.message : "";
	};
});