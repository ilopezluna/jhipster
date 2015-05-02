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
                $scope.items = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Item.update($scope.item,
                function () {
                    $scope.loadAll();
                    $('#saveItemModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Item.get({id: id}, function(result) {
                $scope.item = result;
                $('#saveItemModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Item.get({id: id}, function(result) {
                $scope.item = result;
                $('#deleteItemConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Item.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteItemConfirmation').modal('hide');
                    $scope.clear();
                });
        };

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
