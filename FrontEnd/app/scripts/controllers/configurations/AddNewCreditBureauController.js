(function (module) {
    mifosX.controllers = _.extend(module, {
        AddNewCreditBureauController: function ($scope, http,API_VERSION,$rootScope,resourceFactory,dateFilter, $routeParams, location) {
            $scope.formData = {};
            $scope.creditbureaus=[];

            $scope.cancel={};
            $scope.localcountry={};

            $scope.cancel = function () {
                location.path("#/externalservicesCB/CreditBureau");
            };


            $scope.submit = function () {

                resourceFactory.addCreditBureau.save($scope.formData,function (data) {
                    location.path('/externalservicesCB/CreditBureau');
                });

            };

        }
    });
    mifosX.ng.application.controller('AddNewCreditBureauController', ['$scope','$http','API_VERSION','$rootScope', 'ResourceFactory', 'dateFilter','$routeParams', '$location', mifosX.controllers.AddNewCreditBureauController]).run(function ($log) {
        $log.info("AddNewCreditBureauController initialized");
    });
}(mifosX.controllers || {}));