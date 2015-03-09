var services = angular.module('shop.services', ['ngResource']);

services.factory('Item', ['$resource',
  function($resource){
    return $resource('/item/', {}, {
      query: {method:'GET', isArray:true}
    });
  }
]);