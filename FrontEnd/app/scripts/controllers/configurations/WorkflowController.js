(function (module) {
    mifosX.controllers = _.extend(module, {
        WorkflowController: function ($scope, resourceFactory, routeParams, location) {
            $scope.models = {
                selected: null,
                lists: {"dataForm": [
                    {
                        label: "Create Client"
                    },
                    {
                        label: "Create Loan"
                    },
                    {
                        label: "Add Guarantor"
                    },
                    {
                        label: "Check Credit Bureau"
                    },
                    {
                        label: "Calculate Credit Score"
                    },
                    {
                        label: "Upload Document"
                    },
                    {
                        label: "Disburse Loan"
                    },
                    {
                        label: "Add Co-Maker"
                    },
                    {
                        label: "Loan Approval"
                    }
                ]}
            };

            $scope.cancel = function(){
                location.path("/system");
            };
        }


    });
    mifosX.ng.application.controller('WorkflowController', ['$scope','ResourceFactory', '$routeParams', '$location', mifosX.controllers.WorkflowController]).run(function ($log) {
        $log.info("WorkflowController initialized");
    });

}
(mifosX.controllers || {}));