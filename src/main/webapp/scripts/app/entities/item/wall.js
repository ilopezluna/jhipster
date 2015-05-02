'use strict';

angular.module('jhipsterApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('wall', {
                parent: 'site',
                url: '/wall',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'jhipsterApp.item.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/item/wall.html',
                        controller: 'WallController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('item');
                        return $translate.refresh();
                    }]
                }
            });
    });
