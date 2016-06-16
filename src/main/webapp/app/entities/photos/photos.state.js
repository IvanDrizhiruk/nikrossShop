(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('photos', {
            parent: 'entity',
            url: '/photos',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nikrossShopApp.photos.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/photos/photos.html',
                    controller: 'PhotosController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('photos');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('photos-detail', {
            parent: 'entity',
            url: '/photos/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'nikrossShopApp.photos.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/photos/photos-detail.html',
                    controller: 'PhotosDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('photos');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Photos', function($stateParams, Photos) {
                    return Photos.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('photos.new', {
            parent: 'photos',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photos/photos-dialog.html',
                    controller: 'PhotosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                thumbnail: null,
                                image: null,
                                type: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('photos', null, { reload: true });
                }, function() {
                    $state.go('photos');
                });
            }]
        })
        .state('photos.edit', {
            parent: 'photos',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photos/photos-dialog.html',
                    controller: 'PhotosDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Photos', function(Photos) {
                            return Photos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('photos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('photos.delete', {
            parent: 'photos',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/photos/photos-delete-dialog.html',
                    controller: 'PhotosDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Photos', function(Photos) {
                            return Photos.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('photos', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
