(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('PhotosDeleteController',PhotosDeleteController);

    PhotosDeleteController.$inject = ['$uibModalInstance', 'entity', 'Photos'];

    function PhotosDeleteController($uibModalInstance, entity, Photos) {
        var vm = this;

        vm.photos = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Photos.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
