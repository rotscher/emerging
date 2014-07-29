

var budgetControllers = angular.module('budgetControllers', []);

budgetControllers.controller('CurrentStatementListCtrl', ['$scope', 'AccountService', function($scope, AccountService) {
	
	$scope.data = AccountService.data;
	
	//balance is inverting the current value
	$scope.balance = function(data) {
		data.balanced = !data.balanced;
		AccountService.saveStatement(data);
	};
	
	$scope.saveStatement = function(stmt) {
		alert(stmt.amount);
		AccountService.saveStatement(stmt);
	};
}]);

budgetControllers.controller('ArchivedStatementListCtrl', function ($scope, $http) {
    $http.get('data/archived-statements.json').success(function(data) {
        $scope.archivedStmt = data;
    });
});