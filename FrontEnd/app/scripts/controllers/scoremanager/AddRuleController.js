(function (module) {
    mifosX.controllers = _.extend(module, {
        AddRuleController: function (scope, resourceFactory, location, creditRuleServices) {
            scope.type = "0";
            scope.attribute = "";
            scope.weightedvalue = "";
            scope.tag = "";
            scope.types = [
                {
                    "value":"1",
                    "type":'Range'
                },
                {
                    "value":"2",
                    "type":'Choice'  
                }
            ];
            var forms = [
                "",
                "range.tpl.html",
                "choice.tpl.html"
            ];
            scope.typeData = [];
            scope.displayedForms = [];
            scope.date = new Date();
    
            scope.addForm = function(formIndex) {
              scope.displayedForms.push(forms[formIndex]);
            };

            scope.changeType = function(formIndex){
                scope.displayedForms = [];
                scope.typeData = [];
                scope.displayedForms.push(forms[formIndex]);
            };

            scope.removeRow = function(index){
                scope.typeData.splice(index, 1)
                scope.displayedForms.splice(index, 1);
            };

            scope.submit = function(){
                var data = {
                    "ruleName":scope.attribute,
                    "tag":scope.tag,
                    "weightedValue":scope.weightedvalue,
                    "ruleType":scope.type,
                    "ruleTypeDataList":scope.typeData,
                    "status":"Enabled",
                };
                console.log(data);
                resourceFactory.scoreManagerResource.addScoreRule(data, function(data){
                    location.path('/scoremanager/');
                });
            }
        }
    });
    mifosX.ng.application.controller('AddRuleController', ['$scope', 'ResourceFactory', '$location', 'creditRuleServices', mifosX.controllers.AddRuleController]).run(function ($log) {
        $log.info("AddRuleController initialized");
    });
}(mifosX.controllers || {}));