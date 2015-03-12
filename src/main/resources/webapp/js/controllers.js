'use strict';

/* Controllers */

var shopCtrl = angular.module('shop.controllers', []);


shopCtrl.controller('ItemListCtrl', [ '$scope', 'Item', function($scope, Item) {
	
	function setUpItems($scope) {
		Item.query().$promise.then(function(data) {
			data.forEach(function(data) {
				data.toBuy = "";
			});
			$scope.items = data;
		}, function(error) {
			$scope.error = error;
		});	
	}
	
	$scope.error = "";
	$scope.message = "";

	setUpItems($scope);
	
	$scope.buy = function(items) {
		items.forEach(function(item) {
			item.count = item.toBuy;
		});		
		Item.update(items).$promise
		.then(function(data) {
				$scope.message = "You have ordered items successfully.";
				$scope.error = "";
			}, function(error) {
				$scope.message = "";
				$scope.error = error;
			}
		).finally(function(data) {
			setUpItems($scope);
		});
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
