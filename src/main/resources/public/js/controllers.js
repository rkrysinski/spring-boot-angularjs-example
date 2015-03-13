'use strict';

var shopCtrl = angular.module('shop.controllers');

shopCtrl.controller('ItemListCtrl', [ '$scope', 'Item', function($scope, Item) {
	
	$scope.error        = "";
	$scope.updateStatus = "";
	$scope.toBuyItems   = undefined;
	
	$scope.buy = function(items) {
		clearMessages($scope);
		prepareItemsToBuy($scope, items);
		doBuy($scope);
	};
	
	setUpItems($scope);
	
	function doBuy($scope) {
		if ($scope.toBuyItems) {
			Item.update($scope.toBuyItems).$promise
			.then(function(data) {
					$scope.updateStatus = data.updateStatus;
				}, function(error) {
					$scope.error = error;
				}
			).finally(function(data) {
				setUpItems($scope);
			});		
		}
	}
	
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
	
	function prepareItemsToBuy($scope, items) {
		var countToBuy = 0
		var toBuyItems = angular.copy(items);
		toBuyItems.forEach(function(item) {
			item.count = item.toBuy;
			countToBuy += item.toBuy;
		});
		
		if (countToBuy == 0) {
			toBuyItems = undefined;
		}
		$scope.toBuyItems = toBuyItems;
	}
	
	function clearMessages($scope) {
		$scope.error = "";
		$scope.updateStatus = "";
	}
		
} ]);
