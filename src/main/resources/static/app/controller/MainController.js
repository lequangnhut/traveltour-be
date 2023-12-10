travel_app.controller('MainController', function ($scope) {

    $scope.logoutAuth = function () {
        window.location.href = '/manager/logout';
    }
});
