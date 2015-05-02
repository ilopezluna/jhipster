'use strict';

angular.module('jhipsterApp')
    .factory('ItemSearch', function ($resource) {
        return $resource('api/_search/items/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    })
    .factory('WallSearch', function ($resource) {
        return $resource('api/_search/items/wall/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });;
