(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('GoodsDialogController', GoodsDialogController);

    GoodsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Goods', 'Categories'];

    function GoodsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Goods, Categories) {
        var vm = this;

        vm.goods = entity;
        vm.clear = clear;
        vm.save = save;
        vm.categories = Categories.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.goods.id !== null) {
                Goods.update(vm.goods, onSaveSuccess, onSaveError);
            } else {
                Goods.save(vm.goods, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('nikrossShopApp:goodsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
