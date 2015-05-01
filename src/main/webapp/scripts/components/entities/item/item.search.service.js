'use strict';

angular.module('jhipsterApp')
    .factory('ItemSearch', function ($resource) {
        return $resource('api/_search/items/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
