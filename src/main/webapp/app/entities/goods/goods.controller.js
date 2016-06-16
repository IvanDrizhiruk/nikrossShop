(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('GoodsController', GoodsController);

    GoodsController.$inject = ['$scope', '$state', 'Goods'];

    function GoodsController ($scope, $state, Goods) {
        var vm = this;
        
        vm.goods = [];

        loadAll();

        function loadAll() {
            Goods.query(function(result) {
                vm.goods = result;
            });
        }
    }
})();
