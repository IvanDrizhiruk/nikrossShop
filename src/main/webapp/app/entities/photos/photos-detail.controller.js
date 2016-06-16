(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('PhotosDetailController', PhotosDetailController);

    PhotosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Photos', 'Goods'];

    function PhotosDetailController($scope, $rootScope, $stateParams, entity, Photos, Goods) {
        var vm = this;

        vm.photos = entity;

        var unsubscribe = $rootScope.$on('nikrossShopApp:photosUpdate', function(event, result) {
            vm.photos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
