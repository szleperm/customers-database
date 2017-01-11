'use strict';

/**
 * @ngdoc function
 * @name customersApiClientApp.controller:DatabaseCtrl
 * @description
 * # DatabaseCtrl
 * Controller of the customersApiClientApp
 */
angular.module('customersDatabaseApp')
  .controller('DatabaseCtrl', function ($scope, Customer) {
    $scope.form = {};
    var self = this;
    self.customer = new Customer();
    self.customers = [];
    
    self.getAllCustomers = function(){
      self.customers = Customer.query();  
    };
    
    self.createCustomer = function(){
      self.customer.$save(function(){
         self.getAllCustomers(); 
      });
    };
    
    self.updateCustomer = function(){
      self.customer.$update(function(){
         self.getAllCustomers(); 
      });
    };
    
    self.deleteCustomer = function(identity){
      var cust = Customer.get({id:identity}, function(){
        cust.$delete(function(){
           self.getAllCustomers(); 
        });  
      });
    };
    
    self.getAllCustomers();
    
    self.submit = function(){
      if(self.customer.id === null || typeof self.customer.id === 'undefined'){
          self.createCustomer();
      }else {
          self.updateCustomer();
      }
      self.reset();
    };
    
    self.edit = function(id){
        for(var i = 0; i < self.customers.length; i++){
            if(self.customers[i].id === id){
                self.customer = angular.copy(self.customers[i]);
                        break;
            }
        }
    };
    
    self.remove = function(id){
      if(self.customer.id === id){
          self.reset();
      }
      self.deleteCustomer(id);
    };
    
    self.reset = function(){
      self.customer = new Customer();
      $scope.form.customerForm.$setPristine();
    };
    
    $scope.itemsByPage=10;
  });
