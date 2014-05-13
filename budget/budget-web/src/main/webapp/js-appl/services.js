    var budgetServices = angular.module('budgetServices', ['ngResource']);
     
    budgetServices.factory('Statement', ['$resource',
        function($resource) {
          return $resource('phones/:phoneId.json', {}, {
    query: {method:'GET', params:{phoneId:'phones'}, isArray:true}
    });
    }]);