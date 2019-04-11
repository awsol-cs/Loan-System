(function (module) {
    mifosX.controllers = _.extend(module, {
        ViewFormulaController: function (scope, routeParams, resourceFactory, location, creditRuleServices) {
            scope.formula = [];

            resourceFactory.formulaResource.getScoreFormula({formulaId: routeParams.id}, function(data)
            {
                scope.formula = data;
            });

            scope.routeTo = function(){
                location.path('/editformula/'+ routeParams.id);
            };
        }
    });
    mifosX.ng.application.controller('ViewFormulaController', ['$scope', '$routeParams', 'ResourceFactory', '$location', 'creditRuleServices', mifosX.controllers.ViewFormulaController]).run(function ($log) {
        $log.info("ViewFormulaController initialized");
    });
}(mifosX.controllers || {}));