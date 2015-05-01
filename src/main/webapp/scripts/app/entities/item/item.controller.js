'use strict';

angular.module('jhipsterApp')
    .controller('ItemController', function ($scope, Item, ItemSearch, ParseLinks) {
        $scope.items = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Item.query({page: $scope.page, per_page: 20}, function(result, headers) {
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

        $scope.clear = function () {
            $scope.item = {name: null, description: null, createdAt: null, updatedAt: null, price: null, status: null, latitude: null, longitude: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
