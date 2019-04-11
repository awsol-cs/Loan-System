(function (module) {
    mifosX.services = _.extend(module, {

        creditRuleServices: function () {
          var rules = [];
          var formulas = [];
          var date = new Date();

          var addRule = function(newObj) {
              rules.push(newObj);
          };

          var getRules = function(){
              return rules;
          };

          var setRules = function(data){
              rules = data;
          };

          var getRuleIndex = function(index){
              return rules[index];
          };

          var getRulesEnabled = function(){
            var data = [];
            rules.forEach(function(rule){
              if(rule.status === "Enabled"){
                data.push(rule);
              }
            });
              return data;
          }

          var setRuleIndex = function(index, data){
            rules[index] = data;
          };

          var addFormula = function(newObj) {
              formulas.push(newObj);
          };

          var getFormulas = function(){
            return formulas;
          };

          var getFormulasEnabled = function(){
            var data = [];
            formulas.forEach(function(formula){
              if(formula.status === "Enabled"){
                data.push(formula);
              }
            });
              return data;
          }

          var getFormulaIndex = function(index){
              return formulas[index];
          };

          var setFormulaIndex = function(index, data){
            formulas[index] = data;
          };

          var setFormulas = function(data){
            formulas = data;
          }

          var getFormulaEnabled = function(){
            var data = null;
            formulas.forEach(function(formula){
              if(formula.status === "Enabled"){
                data = formula;
                return;
              }
            });
            return data;
          }

          var initService = function(){
            var data = [
              {
                "ruleName":"Age",
                "tag":"age",
                "weightedValue":"4",
                "ruleType":"1",
                "ruleTypeDataList":[
                    {
                      "minValue":0,
                      "maxValue":17,
                      "relativeValue":-1
                    },
                    {
                      "minValue":18,
                      "maxValue":23,
                      "relativeValue":1
                    },
                    {
                      "minValue":24,
                      "maxValue":30,
                      "relativeValue":2
                    },
                    {
                      "minValue":31,
                      "maxValue":59,
                      "relativeValue":5
                    },
                    {
                      "minValue":60,
                      "maxValue":999,
                      "relativeValue":3
                    }
                  ],
                "createdDate":date,
                "createdBy":"mifos",
                "status":"Enabled",
                "updatedBy":"mifos" 
              },
              {
                "ruleName":"Residence Information",
                "tag":"ri",
                "weightedValue":"3",
                "ruleType":"2",
                "ruleTypeDataList":[
                    {
                      "name":"Makati",
                      "relativeValue":10
                    },
                    {
                      "name":"Manila",
                      "relativeValue":-10
                    }
                  ],
                "createdDate":date,
                "createdBy":"mifos",
                "status":"Enabled",
                "updatedBy":"mifos" 
              },
              {
                "ruleName":"Gender",
                "tag":"gender",
                "weightedValue":"1",
                "ruleType":"2",
                "ruleTypeDataList":[
                    {
                      "name":"Male",
                      "relativeValue":100
                    },
                    {
                      "name":"Female",
                      "relativeValue":500
                    }
                  ],
                "createdDate":date,
                "createdBy":"mifos",
                "status":"Enabled",
                "updatedBy":"mifos" 
              }, 
              {
                "ruleName":"Gross Annual Income",
                "tag":"gai",
                "weightedValue":"1",
                "ruleType":"1",
                "ruleTypeDataList":[
                    {
                      "minValue":0,
                      "maxValue":12000,
                      "relativeValue":0
                    },
                    {
                      "minValue":12001,
                      "maxValue":20000,
                      "relativeValue":10
                    },
                    {
                      "minValue":20001,
                      "maxValue":30000,
                      "relativeValue":50
                    },
                    {
                      "minValue":30001,
                      "maxValue":999999,
                      "relativeValue":100
                    }
                  ],
                "createdDate":date,
                "createdBy":"mifos",
                "status":"Enabled",
                "updatedBy":"mifos" 
              }
            ];
            rules = data;
          };
          initService();
          return{
            addRule:addRule,
            getRules:getRules,
            getRuleIndex:getRuleIndex,
            setRuleIndex:setRuleIndex,
            addFormula:addFormula,
            getFormulas:getFormulas,
            getFormulaIndex:getFormulaIndex,
            setFormulaIndex:setFormulaIndex,
            setFormulas:setFormulas,
            getFormulaEnabled:getFormulaEnabled,
            getFormulasEnabled:getFormulasEnabled,
            getRulesEnabled:getRulesEnabled
          }
        }
    });

    mifosX.ng.services.service('creditRuleServices', [mifosX.services.creditRuleServices]).run(function ($log) {
        $log.info("creditRuleServices initialized");

    });

}(mifosX.services || {}));
