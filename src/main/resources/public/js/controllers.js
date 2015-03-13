'use strict';

var shopCtrl = angular.module('shop.controllers');

shopCtrl.controller('ItemListCtrl', [ '$scope', 'Item', function($scope, Item) {
	
	clearMessages($scope);
	
	setUpItems($scope);
	
	$scope.buy = function(items) {
		clearMessages($scope);
		
		if (nothingToBuy(items)) {
			return;
		}
		
		items.forEach(function(item) {
			item.count = item.toBuy;
		});	
		
		Item.update(items).$promise
		.then(function(data) {
				$scope.updateStatus = data.updateStatus;
			}, function(error) {
				$scope.error = error;
			}
		).finally(function(data) {
			setUpItems($scope);
		});
	};
	
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
	
	function nothingToBuy(items) {
		var itemsToBuy = 0
		items.forEach(function(item) {
			itemsToBuy += item.toBuy;
		});
		return itemsToBuy == 0;
	}
	
	function clearMessages($scope) {
		$scope.error = "";
		$scope.updateStatus = "";
	}
		
} ]);
