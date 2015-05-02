'use strict';

angular.module('jhipsterApp')
    .controller('WallController', function ($scope, Item, ItemSearch, WallSearch, ParseLinks, geolocation) {
        geolocation.getLocation().then( function(data){
            $scope.coords = {lat:data.coords.latitude, long:data.coords.longitude};
            $scope.map = { center: { latitude: $scope.coords.lat, longitude: $scope.coords.long }, zoom: 18 };
        });

        $scope.items = [];
        $scope.page = 1;

        $scope.loadAll = function() {
            Item.query({page: $scope.page, per_page: 100}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.items.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 1;
            $scope.items = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.search = function () {
            ItemSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.items = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.wall = function () {
            WallSearch.query({query: $scope.coords.lat + "," + $scope.coords.long}, function(result) {
                $scope.items = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.clear = function () {
            $scope.item = {name: null, description: null, createdAt: null, updatedAt: null, price: null, status: null, latitude: $scope.coords.lat, longitude: $scope.coords.long, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
