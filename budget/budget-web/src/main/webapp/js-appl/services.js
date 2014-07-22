    var budgetServices = angular.module('budgetServices', ['ngResource']);
     
    budgetServices.factory('AccountService', ['$resource',
  	     function($resource) {
    	
    	   var AccountService =  {
                   data: {
                       balanceSheet : {},
                       statements : []
                   },
                   balanceSheet : function() {
                	   return $resource('/budget-web/rest/balancesheet/:id', {}, {
               	     	  query: {method:'GET', isArray:false}
               	       });
                   },
                   statement : function() {
                	   return $resource('/budget-web/rest/statement/:id', {}, {
                	     	  query: {method:'GET', isArray:true},
                	       });
                    },
                    saveStatement : function(data) {
                    	AccountService.statement().save(data);
                    	
                    	//can be improved?
                    	AccountService.data.balanceSheet = AccountService.balanceSheet().query();
                    }
           };
    	   
    	   AccountService.data.balanceSheet = AccountService.balanceSheet().query();
    	   AccountService.data.statements = AccountService.statement().query();
           return AccountService;
    	
  	}]);
    
    //deprecated
    budgetServices.factory('Account', ['$resource',
	     function($resource) {
	       return $resource('/budget-web/rest/accounts/:id', {}, {
	     	  query: {method:'GET', isArray:true}
	       });
	}]);
    
    budgetServices.factory('Statement', ['$resource',
        function($resource) {
          return $resource('/budget-web/rest/statements/:id', {}, {
              //query: {method:'GET', params:{phoneId:'phones'}, isArray:true}
        	  query: {method:'GET', isArray:true}
          });
    }]);