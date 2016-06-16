'use strict';

describe('Controller Tests', function() {

    describe('Photos Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPhotos, MockGoods;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPhotos = jasmine.createSpy('MockPhotos');
            MockGoods = jasmine.createSpy('MockGoods');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Photos': MockPhotos,
                'Goods': MockGoods
            };
            createController = function() {
                $injector.get('$controller')("PhotosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'nikrossShopApp:photosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
