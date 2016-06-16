(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('CategoriesDetailController', CategoriesDetailController);

    CategoriesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Categories', 'Goods'];

    function CategoriesDetailController($scope, $rootScope, $stateParams, entity, Categories, Goods) {
        var vm = this;

        vm.categories = entity;

        var unsubscribe = $rootScope.$on('nikrossShopApp:categoriesUpdate', function(event, result) {
            vm.categories = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
