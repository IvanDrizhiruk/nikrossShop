(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('CategoriesController', CategoriesController);

    CategoriesController.$inject = ['$scope', '$state', 'Categories'];

    function CategoriesController ($scope, $state, Categories) {
        var vm = this;
        
        vm.categories = [];

        loadAll();

        function loadAll() {
            Categories.query(function(result) {
                vm.categories = result;
            });
        }
    }
})();
