let travel_app = angular.module('travel_app', ['ngRoute']);

let BASE_API = 'http://localhost:3200/api/v1/'
let baseURL = '/page/';

travel_app.config(function ($routeProvider, $locationProvider) {
    $routeProvider
        /**
         * @Dashboards
         */
        .when('/manager/dashboard', {
            templateUrl: baseURL + 'dashboard/dashboard.html',
            controller: 'DashboardController'
        })
        /**
         * @Chat
         */
        .when('/manager/chat', {
            templateUrl: baseURL + 'chat/chat.html',
            controller: 'ChatController'
        })
        /**
         * @Tour
         */
        .when('/manager/tour', {
            templateUrl: baseURL + 'tour/tour.html',
            controller: ''
        })
        .otherwise({
            redirectTo: '/manager/dashboard'
        })

    if (window.history && window.history.pushState) {
        $locationProvider.html5Mode({
            enabled: true,
            requireBase: false
        });
    }
});
