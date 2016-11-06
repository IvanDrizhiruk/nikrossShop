(function() {
    'use strict';

    angular
        .module('nikrossShopApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'Categories', 'Goods'];

    function HomeController ($scope, Principal, LoginService, $state, Categories, Goods) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        init();

        function init () {
            getAccount();

            Goods.query().$promise.then(function(goods) {

                var categorizedGoods = {};
                goods.forEach(function (item) {
                    item.categories.forEach(function (category) {
                        console.log(category)
                        var storedCategory = categorizedGoods[category.id];

                        if (!storedCategory) {
                            storedCategory = category;
                            categorizedGoods[category.id] = storedCategory;
                            storedCategory.goods=[];
                        }

                        storedCategory.goods.push(item);
                    })
                });

                vm.categorizedGoods = categorizedGoods;
            });
        }

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
