var budgetApp = angular.module('budgetApp', [
                                            'ngRoute',
                                            'budgetControllers'
                                            ]);

budgetApp.config(['$routeProvider',
function($routeProvider) {
$routeProvider.
when('/overview', {
templateUrl: 'partials/overview.html',
controller: 'AccountListCtrl'
}).
when('/phones/:phoneId', {
templateUrl: 'partials/phone-detail.html',
controller: 'PhoneDetailCtrl'
}).
otherwise({
redirectTo: '/overview'
});
}]);