(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('GoodsDetailController', GoodsDetailController);

    GoodsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Goods', 'Categories'];

    function GoodsDetailController($scope, $rootScope, $stateParams, entity, Goods, Categories) {
        var vm = this;

        vm.goods = entity;

        var unsubscribe = $rootScope.$on('nikrossShopApp:goodsUpdate', function(event, result) {
            vm.goods = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
