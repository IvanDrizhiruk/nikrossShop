'use strict';

describe('Controller Tests', function() {

    describe('Categories Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCategories, MockGoods;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCategories = jasmine.createSpy('MockCategories');
            MockGoods = jasmine.createSpy('MockGoods');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Categories': MockCategories,
                'Goods': MockGoods
            };
            createController = function() {
                $injector.get('$controller')("CategoriesDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nikrossShopApp:categoriesUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
