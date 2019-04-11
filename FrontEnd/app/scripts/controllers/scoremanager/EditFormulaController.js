(function (module) {
    mifosX.controllers = _.extend(module, {
        EditFormulaController: function (scope, routeParams, resourceFactory, location, creditRuleServices) {
            scope.rules = [];
            scope.formulaData = [];
            scope.formulaname = "";
            scope.formula = "";

            scope.addText = function(index){
                var tagData = scope.rules[index].tag;
                scope.formula = scope.formula.concat(" " + tagData + " ");
                document.getElementById("formula").focus();
            }

            scope.submit = function(){
                var data = {
                    "formulaName":scope.formulaname,
                    "formula":scope.formula,
                    "status":scope.formulaData.status
                };

                resourceFactory.formulaResource.editScoreFormula({formulaId: routeParams.id}, data, function(data)
                {
                    location.path('/viewformula/'+ routeParams.id);
                });
            }

            scope.getFormulaData = function(){
                resourceFactory.scoreManagerResource.getScoreRuleList({
                }, function (data) {
                    scope.rules = data.filter(function(rule){
                        return rule.status != "Disabled";
                    });
                });

                resourceFactory.formulaResource.getScoreFormula({formulaId: routeParams.id}, function(data)
                {
                    scope.formulaData = data;
                    scope.formula = scope.formulaData.formula;
                    scope.formulaname = scope.formulaData.formulaName;
                });
            };

            scope.routeTo = function(){
                location.path('/viewformula/'+ routeParams.id);
            };

            scope.getFormulaData();
        }
    });
    mifosX.ng.application.controller('EditFormulaController', ['$scope', '$routeParams', 'ResourceFactory', '$location', 'creditRuleServices', mifosX.controllers.EditFormulaController]).run(function ($log) {
        $log.info("EditFormulaController initialized");
    });
}(mifosX.controllers || {}));