(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('GoodsDetailController', GoodsDetailController);

    GoodsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Goods', 'Categories', 'Photos'];

    function GoodsDetailController($scope, $rootScope, $stateParams, entity, Goods, Categories, Photos) {
        var vm = this;

        vm.goods = entity;
        vm.photosForGoods = {};
        console.log("photo: 1");
        Photos.query().$promise.then(function(photos) {
            console.log("photo: 2");
            var photosForGoods = {};
            photos.forEach(function (item) {
                console.log("photo:", item);

                if(! photosForGoods[item.goods.id]) {
                    photosForGoods[item.goods.id] = [];
                }

                photosForGoods[item.goods.id].push(item);
            });

            vm.photosForGoods = photosForGoods;

        });


        var unsubscribe = $rootScope.$on('nikrossShopApp:goodsUpdate', function(event, result) {
            vm.goods = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
