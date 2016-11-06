(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('PhotosController', PhotosController);

    PhotosController.$inject = ['$scope', '$state', 'DataUtils', 'Photos'];

    function PhotosController ($scope, $state, DataUtils, Photos) {
        var vm = this;
        
        vm.photos = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Photos.query(function(result) {
                vm.photos = result;
            });
        }
    }
})();
