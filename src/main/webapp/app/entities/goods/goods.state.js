(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('goods', {
            parent: 'entity',
            url: '/goods',
            data: {
                authorities: ['ROLE_ADMIN'],
                pageTitle: 'nikrossShopApp.goods.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/goods/goods.html',
                    controller: 'GoodsController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('goods');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('goods-detail', {
            parent: 'entity',
            url: '/goods/{id}',
            data: {
                authorities: [],
                pageTitle: 'nikrossShopApp.goods.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/goods/goods-detail.html',
                    controller: 'GoodsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('goods');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Goods', function($stateParams, Goods) {
                    return Goods.get({id : $stateParams.id}).$promise;
                }]
            }
        })
        .state('goods.new', {
            parent: 'goods',
            url: '/new',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goods/goods-dialog.html',
                    controller: 'GoodsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                article: null,
                                title: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('goods', null, { reload: true });
                }, function() {
                    $state.go('goods');
                });
            }]
        })
        .state('goods.edit', {
            parent: 'goods',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goods/goods-dialog.html',
                    controller: 'GoodsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Goods', function(Goods) {
                            return Goods.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('goods', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('goods.delete', {
            parent: 'goods',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_ADMIN']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/goods/goods-delete-dialog.html',
                    controller: 'GoodsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Goods', function(Goods) {
                            return Goods.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('goods', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
