'use strict';

/**
 * @ngdoc service
 * @name customersApiClientApp.customer
 * @description
 * # customer
 * Factory in the customersApiClientApp.
 */
angular.module('customersDatabaseApp')
  .factory('Customer', function ($resource) {
       
    return $resource('/customers-database/api/customers/:id',
        {id: '@id'},
        {
            update: {
                method: 'PUT'
            }
        }     
      );
   
  });
