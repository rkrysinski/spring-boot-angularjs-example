'use strict';

var app = angular.module('shop.controllers', []);

app.directive("shopSection", function() {
	return {
		restrict : 'E',
		templateUrl : "includes/shop-section.html"
	};
});

app.directive("orderStatusSection", function() {
	return {
		restrict : 'E',
		templateUrl : "includes/order-status-section.html"
	};
});

app.directive("itemList", function() {
	return {
		restrict : 'E',
		scope: {
			items: '=items',
			kindOfOperation: '@kind'
		},
		templateUrl : "includes/item-list.html"
	};
});

app.directive("buySection", function() {
	return {
		restrict : 'E',
		templateUrl : "includes/buy-section.html"
	};
});

app.directive("header", function() {
	return {
		restrict : 'E',
		templateUrl : "includes/header.html"
	};
});
