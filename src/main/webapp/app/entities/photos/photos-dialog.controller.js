(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('PhotosDialogController', PhotosDialogController);

    PhotosDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Photos', 'Goods'];

    function PhotosDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Photos, Goods) {
        var vm = this;

        vm.photos = entity;
        vm.clear = clear;
        vm.save = save;
        vm.goods = Goods.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.photos.id !== null) {
                Photos.update(vm.photos, onSaveSuccess, onSaveError);
            } else {
                Photos.save(vm.photos, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nikrossShopApp:photosUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
