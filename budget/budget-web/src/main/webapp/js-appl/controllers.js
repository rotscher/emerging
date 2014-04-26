

var budgetControllers = angular.module('budgetControllers', []);

budgetControllers.controller('AccountListCtrl', function ($scope, $http) {
    $http.get('data/accounts.json').success(function(data) {
        $scope.accounts = data;
    });
  });

budgetControllers.controller('CurrentStatementListCtrl', function ($scope, $http) {
    $http.get('data/current-statements.json').success(function(data) {
        $scope.currentStmt = data;
    });
  });

budgetControllers.controller('ArchivedStatementListCtrl', function ($scope, $http) {
    $http.get('data/archived-statements.json').success(function(data) {
        $scope.archivedStmt = data;
    });
  });