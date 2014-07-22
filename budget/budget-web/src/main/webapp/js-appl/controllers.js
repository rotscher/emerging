

var budgetControllers = angular.module('budgetControllers', []);

budgetControllers.controller('CurrentStatementListCtrl', ['$scope', 'AccountService', function($scope, AccountService) {
	
	$scope.data = AccountService.data;
	
	//balance is inverting the current value
	$scope.balance = function(data1) {
		data1.balanced = !data1.balanced;
		AccountService.saveStatement(data1);
	};
}]);

budgetControllers.controller('ArchivedStatementListCtrl', function ($scope, $http) {
    $http.get('data/archived-statements.json').success(function(data) {
        $scope.archivedStmt = data;
    });
});