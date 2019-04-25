(function (module) {
    mifosX.controllers = _.extend(module, {
        NavTabsController: function (scope, resourceFactory, localStorageService, $rootScope, location) {
            scope.dashboardClass = true;
            scope.clientClass = false;
            scope.loanProductClass = false;
            scope.reportClass = false;
            scope.otherClass = false;
            scope.otherHref = "";
            scope.otherName = "";
            scope.show = false;

            scope.$watch(function () {
                return location.path();
            }, function () {
                if(location.path().includes("dashboard") || location.path().includes("home")){
                    scope.dashboardClass = true;
                    scope.clientClass = false;
                    scope.loanProductClass = false;
                    scope.reportClass = false;
                    scope.otherClass = false;
                }else if(location.path().includes("report")
                    || location.path().includes("xbrl")){
                    scope.dashboardClass = false;
                    scope.clientClass = false;
                    scope.loanProductClass = false;
                    scope.reportClass = true;
                    scope.otherClass = false;
                }else if(location.path().includes("client")
                    || location.path().includes("familymember")
                    || location.path().includes("creditscore")){
                    scope.dashboardClass = false;
                    scope.clientClass = true;
                    scope.loanProductClass = false;
                    scope.reportClass = false;
                    scope.otherClass = false;
                }else if(location.path().includes("loanproduct")){
                    scope.dashboardClass = false;
                    scope.clientClass = false;
                    scope.loanProductClass = true;
                    scope.reportClass = false;
                    scope.otherClass = false;
                }else{
                    scope.show = true;
                    scope.dashboardClass = false;
                    scope.clientClass = false;
                    scope.loanProductClass = false;
                    scope.reportClass = false;
                    scope.otherClass = true;
                    scope.otherHref = "#" + location.path();
                    scope.checkLocationForName();
                }
            });

            scope.checkLocationForName = function(){
                if(location.path().includes("template")){
                    scope.otherName = "Template";
                }else if(location.path().includes("standinginstruction")){
                    scope.otherName = "Standing Instruction";
                }else if(location.path().includes("listaccounttransactions")){
                    scope.otherName = "Account Transaction";
                }else if(location.path().includes("admin")
                    || location.path().includes("global")
                    || location.path().includes("configuration")
                    || location.path().includes("checker")
                    || location.path().includes("externalservice")
                    || location.path().includes("twofactorconfig")
                    || location.path().includes("scoremanager")
                    || location.path().includes("rule")
                    || location.path().includes("formula")
                    || location.path().includes("workflow")){
                    scope.otherName = "Admin";
                }else if(location.path().includes("survey")){
                    scope.otherName = "Survey";
                }else if(location.path().includes("loan") 
                    || location.path().includes("adjustrepaymentschedule")
                    || location.path().includes("guarantor")
                    || location.path().includes("loanforeclosure")){
                    scope.otherName = "Loan";
                }else if(location.path().includes("organization") 
                    || location.path().includes("currconfig")
                    || location.path().includes("managefund")
                    || location.path().includes("holiday")
                    || location.path().includes("workingday")
                    || location.path().includes("passwordpreference")
                    || location.path().includes("aymenttype")
                    || location.path().includes("bulkloan")
                    || location.path().includes("teller")
                    || location.path().includes("Teller")
                    || location.path().includes("viewallprovisionings")
                    || location.path().includes("provisioningcriteria")
                    || location.path().includes("entitydatatablechecks")
                    || location.path().includes("bulkimport")){
                    scope.otherName = "Organization";
                }else if(location.path().includes("system")
                    || location.path().includes("code")
                    || location.path().includes("datatable")
                    || location.path().includes("job")
                    || location.path().includes("viewhistory")
                    || location.path().includes("hook")
                    || location.path().includes("entitytoentitymapping")
                    || location.path().includes("accountnumber")
                    || location.path().includes("audit")){
                    scope.otherName = "System";
                }else if(location.path().includes("charge")){
                    scope.otherName = "Charges";
                }else if(location.path().includes("floatingrate")){
                    scope.otherName = "Floating Rates";
                }else if(location.path().includes("savingproduct")){
                    scope.otherName = "Saving Product";
                }else if(location.path().includes("shareproduct")){
                    scope.otherName = "Share Product";
                }else if(location.path().includes("fixeddepositproduct")){
                    scope.otherName = "Fixed Deposit Product";
                }else if(location.path().includes("recurringdepositproduct")){
                    scope.otherName = "Recurring Deposit Product";
                }else if(location.path().includes("product")
                    || location.path().includes("productmix")
                    || location.path().includes("tax")
                    || location.path().includes("dividends")){
                    scope.otherName = "Product";
                }else if(location.path().includes("interestratechart")){
                    scope.otherName = "Interest Rate Charts";
                }else if(location.path().includes("office")){
                    scope.otherName = "Office";
                }else if(location.path().includes("task")){
                    scope.otherName = "Task";
                }else if(location.path().includes("search")){
                    scope.otherName = "Search";
                }else if(location.path().includes("user")){
                    scope.otherName = "User";
                }else if(location.path().includes("employee")){
                    scope.otherName = "Employee";
                }else if(location.path().includes("accounting")
                    || location.path().includes("glaccount")
                    || location.path().includes("freqposting")
                    || location.path().includes("transaction")
                    || location.path().includes("journalentry")
                    || location.path().includes("accounts_closure")
                    || location.path().includes("accounting_rules")
                    || location.path().includes("accrule")
                    || location.path().includes("accrual")
                    || location.path().includes("openingbalances")
                    || location.path().includes("provisioningentries")
                    || location.path().includes("provisioningentry")
                    || location.path().includes("createclosure")
                    || location.path().includes("financial")){
                    scope.otherName = "Accounting";
                }else if(location.path().includes("saving")
                    || location.path().includes("onholdtransactions")
                    || location.path().includes("viewguarantortransactions")){
                    scope.otherName = "Saving";
                }else if(location.path().includes("group")
                    || location.path().includes("meeting")
                    || location.path().includes("addrole")
                    || location.path().includes("transferclients")
                    || location.path().includes("assignstaff")){
                    scope.otherName = "Group";
                }else if(location.path().includes("accounttransfer")){
                    scope.otherName = "Account Transfer";
                }else if(location.path().includes("depositaccount")){
                    scope.otherName = "Deposit Account";
                }else if(location.path().includes("center")){
                    scope.otherName = "Center";
                }else if(location.path().includes("sheet")){
                    scope.otherName = "Collection";
                }else if(location.path().includes("profile")){
                    scope.otherName = "profile";
                }else if(location.path().includes("help")){
                    scope.otherName = "help";
                }else if(location.path().includes("shareaccount")){
                    scope.otherName = "Share Account";
                }else if(location.path().includes("address")){
                    scope.otherName = "Address";
                }else if(location.path().includes("smscampaign")){
                    scope.otherName = "SMS Campaign";
                }else if(location.path().includes("notification")){
                    scope.otherName = "Notification";
                }else if(location.path().includes("adhocquery")){
                    scope.otherName = "adhocquery";
                }
            }
        }
    });
    mifosX.ng.application.controller('NavTabsController', ['$scope', 'ResourceFactory', 'localStorageService', '$rootScope', '$location', mifosX.controllers.NavTabsController]).run(function ($log) {
        $log.info("NavTabsController initialized");
    });
}(mifosX.controllers || {}));
