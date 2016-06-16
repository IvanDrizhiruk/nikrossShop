'use strict';

describe('Controller Tests', function() {

    describe('Goods Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGoods, MockCategories;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGoods = jasmine.createSpy('MockGoods');
            MockCategories = jasmine.createSpy('MockCategories');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Goods': MockGoods,
                'Categories': MockCategories
            };
            createController = function() {
                $injector.get('$controller')("GoodsDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nikrossShopApp:goodsUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
