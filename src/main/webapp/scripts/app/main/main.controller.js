'use strict';

angular.module('jhipsterApp')
    .controller('MainController', function ($scope, Principal, geolocation) {
        geolocation.getLocation().then( function(data){
            $scope.coords = {lat:data.coords.latitude, long:data.coords.longitude};
            $scope.map = { center: { latitude: $scope.coords.lat, longitude: $scope.coords.long }, zoom: 18 };
        });

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });
    });
