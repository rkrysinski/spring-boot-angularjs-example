'use strict';

/* Controllers */

var shopCtrl = angular.module('shop.controllers', []);

shopCtrl.controller('ItemListCtrl', ['$scope', 'Item',
function($scope, Item) {
	$scope.items = Item.query();
}]);
