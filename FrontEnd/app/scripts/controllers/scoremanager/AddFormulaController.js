(function (module) {
    mifosX.controllers = _.extend(module, {
        AddFormulaController: function (scope, resourceFactory, location, creditRuleServices) {
            scope.rules = [];
            scope.type = "0";
            scope.formulaname = "";
            scope.formula = "";
            scope.tag = "";
            scope.focusData = true;

            scope.addText = function(index){
                var tagData = scope.rules[index].tag;
                scope.formula = scope.formula.concat(" " + tagData + " ");
                document.getElementById("formula").focus();
            }


            scope.submit = function(){
                var data = {
                    "formulaName":scope.formulaname,
                    "formula":scope.formula,
                    "status":"Enabled"
                };

                resourceFactory.scoreFormulaResource.addScoreFormula(data, function(data)
                {
                    location.path('/scoremanager');
                });
            }

            scope.initPage = function(){
                resourceFactory.scoreManagerResource.getScoreRuleList({
                }, function (data) {
                    scope.rules = data.filter(function(rule){
                        return rule.status != "Disabled";
                    });
                });
            };
            scope.initPage();
        }
    });
    mifosX.ng.application.controller('AddFormulaController', ['$scope', 'ResourceFactory', '$location', 'creditRuleServices', mifosX.controllers.AddFormulaController]).run(function ($log) {
        $log.info("AddFormulaController initialized");
    });
}(mifosX.controllers || {}));