var zephyrApp = angular.module('zephyrApp', [
    'ngRoute',
]);

zephyrApp.config(['$routeProvider',
function($routeProvider) {
$routeProvider.
    when('/index', {
        templateUrl: 'partials/i-partial.html'
    }).
    otherwise({
        redirectTo: '/index'
    });
}]);