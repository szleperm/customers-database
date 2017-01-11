'use strict';

/**
 * @ngdoc overview
 * @name customersApiClientApp
 * @description
 * # customersApiClientApp
 *
 * Main module of the application.
 */
angular
  .module('customersDatabaseApp', [
    'angular-loading-bar',
    'smart-table',
    'ngAnimate',
    'ngAria',
    'ngCookies',
    'ngMessages',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/about', {
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl',
        controllerAs: 'about'
      })
      .when('/database', {
        templateUrl: 'views/database.html',
        controller: 'DatabaseCtrl',
        controllerAs: 'database'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
