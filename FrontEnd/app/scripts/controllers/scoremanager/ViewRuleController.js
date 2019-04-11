(function (module) {
    mifosX.controllers = _.extend(module, {
        ViewRuleController: function (scope, routeParams, resourceFactory, location, creditRuleServices) {
            scope.rule = [];
            scope.type = "0";
            scope.typeName = "";
            scope.types = [
                {
                    "value":1,
                    "type":'Range'
                },
                {
                    "value":2,
                    "type":'Choice'  
                }
            ];

            scope.getRuleData = function(){
                resourceFactory.ruleResource.getScoreRule({ruleId: routeParams.id}, function(data)
                {
                    scope.rule = data;
                    scope.types.forEach(function(type){
                        if(type.value == scope.rule.ruleType){
                            scope.typeName = type.type;
                        }
                    });
                });
            };

            scope.routeTo = function(){
                location.path('/editrule/'+ routeParams.id);
            };

            scope.getRuleData();
        }
    });
    mifosX.ng.application.controller('ViewRuleController', ['$scope', '$routeParams', 'ResourceFactory', '$location', 'creditRuleServices', mifosX.controllers.ViewRuleController]).run(function ($log) {
        $log.info("ViewRuleController initialized");
    });
}(mifosX.controllers || {}));