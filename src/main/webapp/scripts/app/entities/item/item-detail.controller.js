'use strict';

angular.module('jhipsterApp')
    .controller('ItemDetailController', function ($scope, $stateParams, Item) {
        $scope.item = {};
        $scope.load = function (id) {
            Item.get({id: id}, function(result) {
              $scope.item = result;
            });
        };
        $scope.load($stateParams.id);
    });
