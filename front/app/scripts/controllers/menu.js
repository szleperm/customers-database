'use strict';

/**
 * @ngdoc function
 * @name customersApiClientApp.controller:MenuCtrl
 * @description
 * # MenuCtrl
 * Controller of the customersApiClientApp
 */
angular.module('customersDatabaseApp')
  .controller('MenuCtrl', function ($rootScope, $scope, $location) {
   $scope.isActive = function(route){
       return route === $location.path();
   };
  });
