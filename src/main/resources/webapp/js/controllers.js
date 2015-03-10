'use strict';

/* Controllers */

var shopCtrl = angular.module('shop.controllers', []);

shopCtrl.controller('ItemListCtrl', [ '$scope', 'Item', function($scope, Item) {
	$scope.items = Item.query();

	$scope.buy = function(items) {
		Item.update(items);
	};
} ]);

shopCtrl.directive("statusSection", function() {
	return {
		restrict : 'E',
		templateUrl : "includes/status-section.html"
	};
});

shopCtrl.directive("buySection", function() {
	return {
		restrict : 'E',
		templateUrl : "includes/buy-section.html"
	};
});

shopCtrl.directive("header", function() {
	return {
		restrict : 'E',
		templateUrl : "includes/header.html"
	};
});
