(function (module) {
    mifosX.controllers = _.extend(module, {
        LoanTransferController: function (scope, routeParams, resourceFactory, location) {
            scope.clients = [];
            scope.client = {};
            scope.clientId = 0;
            scope.currentClientId = 0;
            scope.loanAccount = "";
            scope.balance = 0;
            scope.currentClient = "";
            scope.currentaccno = "";

            scope.init = function(){
                resourceFactory.LoanAccountResource.getLoanAccountDetails({loanId: routeParams.loanId}, function(data){
                    scope.loanAccount = data.loanProductName + "(" + data.accountNo + ")";
                    // scope.balance = !data.summary ? 0 : data.summary.totalOutstanding;
                    scope.currentClientId = data.clientId;
                    scope.currentClient = data.clientName;
                    scope.currentaccno = data.clientAccountNo;
                    resourceFactory.clientResource.getAllClients({
                    }, function (data) {
                        scope.clients = data.pageItems.filter(function(client){
                            return client.id != scope.currentClientId || !client.active;
                        });
                    });
                });
            };
            scope.init();

            scope.changeClient = function(client){
                scope.client = client;
                scope.clientId = client.id;
            }

            scope.submit = function(){
                var data = {
                    "loanId": routeParams.loanId,
                    "clientId": scope.clientId
                };
                resourceFactory.transferLoanResource.save(data, function (data) {
                    location.path('/viewclient/' + scope.currentClientId);
                });
            }

            scope.cancel = function(){
                location.path('/viewloanaccount/' + routeParams.loanId);
            }
        }
    });
    mifosX.ng.application.controller('LoanTransferController', ['$scope', '$routeParams', 'ResourceFactory', '$location', mifosX.controllers.LoanTransferController]).run(function ($log) {
        $log.info("LoanTransferController initialized");
    });
}(mifosX.controllers || {}));
