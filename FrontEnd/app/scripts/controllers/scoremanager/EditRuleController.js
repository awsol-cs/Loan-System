(function (module) {
    mifosX.controllers = _.extend(module, {
        EditRuleController: function (scope, routeParams, resourceFactory, location, creditRuleServices) {
            scope.rule = [];
            scope.type = "0";
            scope.ruleName = "";
            scope.weightedValue = "";
            scope.tag = "";
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
            var forms = [
                "",
                "range.tpl.html",
                "choice.tpl.html"
            ];
            scope.ruleTypeDataList = [];
            scope.displayedForms = [];
            scope.date = new Date();
    
            scope.addForm = function(formIndex) {
              scope.displayedForms.push(forms[formIndex]);
            };

            scope.changeType = function(formIndex){
                scope.displayedForms = [];
                scope.ruleTypeDataList = [];
                scope.displayedForms.push(forms[formIndex]);
            };

            scope.removeRow = function(index){
                scope.ruleTypeDataList.splice(index, 1)
                scope.displayedForms.splice(index, 1);
            };

            scope.submit = function(){
                var data = {
                    "ruleName":scope.ruleName,
                    "tag":scope.tag,
                    "weightedValue":scope.weightedValue,
                    "ruleType":scope.type,
                    "ruleTypeDataList":scope.ruleTypeDataList,
                    "status":scope.rule.status,
                };
                resourceFactory.ruleResource.editScoreRule({ruleId: routeParams.id},data, function(data){
                    location.path('/scoremanager/');
                });
            }

            scope.getRuleData = function(){
                resourceFactory.ruleResource.getScoreRule({ruleId: routeParams.id}, function(data)
                {
                    scope.rule = data;
                    scope.ruleName = data.ruleName
                    scope.weightedValue = data.weightedValue;
                    scope.type = data.ruleType;
                    scope.tag = data.tag;
                    for(i = 0; i < data.ruleTypeDataList.length; i++){
                        scope.displayedForms.push(forms[scope.type]);
                    }
                    scope.ruleTypeDataList = data.ruleTypeDataList;
                });
            };

            scope.routeTo = function(){
                location.path('/viewrule/'+ routeParams.id);
            };

            scope.getRuleData();
        }
    });
    mifosX.ng.application.controller('EditRuleController', ['$scope', '$routeParams', 'ResourceFactory', '$location', 'creditRuleServices', mifosX.controllers.EditRuleController]).run(function ($log) {
        $log.info("EditRuleController initialized");
    });
}(mifosX.controllers || {}));