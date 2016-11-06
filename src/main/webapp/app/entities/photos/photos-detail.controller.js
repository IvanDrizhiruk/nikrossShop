(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('PhotosDetailController', PhotosDetailController);

    PhotosDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'Photos', 'Goods'];

    function PhotosDetailController($scope, $rootScope, $stateParams, DataUtils, entity, Photos, Goods) {
        var vm = this;

        vm.photos = entity;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('nikrossShopApp:photosUpdate', function(event, result) {
            vm.photos = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
