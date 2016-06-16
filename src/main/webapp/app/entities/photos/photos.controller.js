(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('PhotosController', PhotosController);

    PhotosController.$inject = ['$scope', '$state', 'Photos'];

    function PhotosController ($scope, $state, Photos) {
        var vm = this;
        
        vm.photos = [];

        loadAll();

        function loadAll() {
            Photos.query(function(result) {
                vm.photos = result;
            });
        }
    }
})();
