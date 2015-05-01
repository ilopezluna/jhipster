'use strict';

angular.module('jhipsterApp')
    .factory('Item', function ($resource) {
        return $resource('api/items/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createdAt = new Date(data.createdAt);
                    data.updatedAt = new Date(data.updatedAt);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
