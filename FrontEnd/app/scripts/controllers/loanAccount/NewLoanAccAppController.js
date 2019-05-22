(function (module) {
    mifosX.controllers = _.extend(module, {
        NewLoanAccAppController: function (scope, routeParams, resourceFactory, location, dateFilter, uiConfigService, WizardHandler) {
            scope.previewRepayment = false;
            scope.clientId = routeParams.clientId;
            scope.groupId = routeParams.groupId;
            scope.restrictDate = new Date();
            scope.formData = {};
            scope.formData.loan = {};
            scope.loandetails = {};
            scope.chargeFormData = {}; //For charges
            scope.collateralFormData = {}; //For collaterals
            scope.inparams = {resourceType: 'template', activeOnly: 'true'};
            scope.date = {};
            scope.formDat = {};
            scope.datatables = [];
            scope.noOfTabs = 1;
            scope.step = '-';
            scope.formData.loan.datatables = [];
            scope.formDat.datatables = [];
            scope.tf = "HH:mm";
            scope.loanApp = "LoanApp";
            scope.customSteps = [];
            scope.tempDataTables = [];
            scope.disabled = true;

            scope.date.first = new Date();

            if (scope.clientId) {
                scope.inparams.clientId = scope.clientId;
                scope.formData.loan.clientId = scope.clientId;
            }


            if (scope.groupId) {
                scope.inparams.groupId = scope.groupId;
                scope.formData.loan.groupId = scope.groupId;
            }

            if (scope.clientId && scope.groupId) {
                scope.inparams.templateType = 'jlg';
            }
            else if (scope.groupId) {
                scope.inparams.templateType = 'group';
            }
            else if (scope.clientId) {
                scope.inparams.templateType = 'individual';
            }

            scope.inparams.staffInSelectedOfficeOnly = true;

            resourceFactory.loanResource.get(scope.inparams, function (data) {
                scope.products = data.productOptions;

                if (data.clientName) {
                    scope.clientName = data.clientName;
                }
                if (data.group) {
                    scope.groupName = data.group.name;
                }
            });

            scope.loanProductChange = function (loanProductId) {
                // _.isUndefined(scope.datatables) ? scope.tempDataTables = [] : scope.tempDataTables = scope.datatables;
                // WizardHandler.wizard().removeSteps(1, scope.tempDataTables.length);
                scope.inparams.productId = loanProductId;
                // scope.datatables = [];
                resourceFactory.loanResource.get(scope.inparams, function (data) {
                    scope.loanaccountinfo = data;
                    scope.previewClientLoanAccInfo();
                    scope.datatables = data.datatables;
                    scope.handleDatatables(scope.datatables);
                    scope.disabled = false;
                });

                resourceFactory.loanResource.get({resourceType: 'template', templateType: 'collateral', productId: loanProductId, fields: 'id,loanCollateralOptions'}, function (data) {
                    scope.collateralOptions = data.loanCollateralOptions || [];
                });
            }

            scope.goNext = function(form){
                WizardHandler.wizard().checkValid(form);
            }

            scope.handleDatatables = function (datatables) {
                if (!_.isUndefined(datatables) && datatables.length > 0) {
                    scope.formData.loan.datatables = [];
                    scope.formDat.datatables = [];
                    scope.noOfTabs = datatables.length + 1;
                    angular.forEach(datatables, function (datatable, index) {
                        scope.updateColumnHeaders(datatable.columnHeaderData);
                        angular.forEach(datatable.columnHeaderData, function (colHeader, i) {
                            if (_.isEmpty(scope.formDat.datatables[index])) {
                                scope.formDat.datatables[index] = {data: {}};
                            }

                            if (_.isEmpty(scope.formData.loan.datatables[index])) {
                                scope.formData.loan.datatables[index] = {
                                    registeredTableName: datatable.registeredTableName,
                                    data: {locale: scope.optlang.code}
                                };
                            }

                            if (datatable.columnHeaderData[i].columnDisplayType == 'DATETIME') {
                                scope.formDat.datatables[index].data[datatable.columnHeaderData[i].columnName] = {};
                            }
                        });
                    });
                }
            };

            scope.updateColumnHeaders = function(columnHeaderData) {
                var colName = columnHeaderData[0].columnName;
                if (colName == 'id') {
                    columnHeaderData.splice(0, 1);
                }

                colName = columnHeaderData[0].columnName;
                if (colName == 'client_id' || colName == 'office_id' || colName == 'group_id' || colName == 'center_id' || colName == 'loan_id' || colName == 'savings_account_id') {
                    columnHeaderData.splice(0, 1);
                }
            };
            //Wizard is creating new scope on every step. So resetting the variable here
            scope.resetPreviewFlag = function() {
                scope.previewRepayment =  !scope.previewRepayment;
            };

            scope.previewClientLoanAccInfo = function () {
                scope.previewRepayment = false;
                scope.charges = scope.loanaccountinfo.charges || [];
                scope.formData.loan.disbursementData = scope.loanaccountinfo.disbursementDetails || [];
                scope.collaterals = [];

                if (scope.loanaccountinfo.calendarOptions) {
                    scope.formData.loan.syncRepaymentsWithMeeting = true;
                    scope.formData.loan.syncDisbursementWithMeeting = true;
                }
                scope.multiDisburseLoan = scope.loanaccountinfo.multiDisburseLoan;
                scope.formData.loan.productId = scope.loanaccountinfo.loanProductId;
                scope.formData.loan.fundId = scope.loanaccountinfo.fundId;
                scope.formData.loan.principal = scope.loanaccountinfo.principal;
                scope.formData.loan.loanTermFrequency = scope.loanaccountinfo.termFrequency;
                scope.formData.loan.loanTermFrequencyType = scope.loanaccountinfo.termPeriodFrequencyType.id;
                scope.loandetails.loanTermFrequencyValue = scope.loanaccountinfo.termPeriodFrequencyType.value;
                scope.formData.loan.numberOfRepayments = scope.loanaccountinfo.numberOfRepayments;
                scope.formData.loan.repaymentEvery = scope.loanaccountinfo.repaymentEvery;
                scope.formData.loan.repaymentFrequencyType = scope.loanaccountinfo.repaymentFrequencyType.id;
                scope.loandetails.repaymentFrequencyValue = scope.loanaccountinfo.repaymentFrequencyType.value;
                scope.formData.loan.interestRatePerPeriod = scope.loanaccountinfo.interestRatePerPeriod;
                scope.formData.loan.amortizationType = scope.loanaccountinfo.amortizationType.id;
                scope.formData.loan.isEqualAmortization = scope.loanaccountinfo.isEqualAmortization;
                scope.loandetails.amortizationValue = scope.loanaccountinfo.amortizationType.value;
                scope.formData.loan.interestType = scope.loanaccountinfo.interestType.id;
                scope.loandetails.interestValue = scope.loanaccountinfo.interestType.value;
                scope.formData.loan.interestCalculationPeriodType = scope.loanaccountinfo.interestCalculationPeriodType.id;
                scope.loandetails.interestCalculationPeriodValue = scope.loanaccountinfo.interestCalculationPeriodType.value;
                scope.formData.loan.allowPartialPeriodInterestCalcualtion = scope.loanaccountinfo.allowPartialPeriodInterestCalcualtion;
                scope.formData.loan.inArrearsTolerance = scope.loanaccountinfo.inArrearsTolerance;
                scope.formData.loan.graceOnPrincipalPayment = scope.loanaccountinfo.graceOnPrincipalPayment;
                scope.formData.loan.graceOnInterestPayment = scope.loanaccountinfo.graceOnInterestPayment;
                scope.formData.loan.graceOnArrearsAgeing = scope.loanaccountinfo.graceOnArrearsAgeing;
                scope.formData.loan.transactionProcessingStrategyId = scope.loanaccountinfo.transactionProcessingStrategyId;
                scope.loandetails.transactionProcessingStrategyValue = scope.formValue(scope.loanaccountinfo.transactionProcessingStrategyOptions,scope.formData.loan.transactionProcessingStrategyId,'id','name');
                scope.formData.loan.graceOnInterestCharged = scope.loanaccountinfo.graceOnInterestCharged;
                scope.formData.loan.fixedEmiAmount = scope.loanaccountinfo.fixedEmiAmount;
                scope.formData.loan.maxOutstandingLoanBalance = scope.loanaccountinfo.maxOutstandingLoanBalance;

                if (scope.loanaccountinfo.isInterestRecalculationEnabled && scope.loanaccountinfo.interestRecalculationData.recalculationRestFrequencyDate) {
                    scope.date.recalculationRestFrequencyDate = new Date(scope.loanaccountinfo.interestRecalculationData.recalculationRestFrequencyDate);
                }
                if (scope.loanaccountinfo.isInterestRecalculationEnabled && scope.loanaccountinfo.interestRecalculationData.recalculationCompoundingFrequencyDate) {
                    scope.date.recalculationCompoundingFrequencyDate = new Date(scope.loanaccountinfo.interestRecalculationData.recalculationCompoundingFrequencyDate);
                }

                if(scope.loanaccountinfo.isLoanProductLinkedToFloatingRate) {
                    scope.formData.loan.isFloatingInterestRate = false ;
                }

                scope.loandetails = angular.copy(scope.formData.loan);
                scope.loandetails.productName = scope.formValue(scope.products,scope.formData.loan.productId,'id','name');
            };

            scope.$watch('formData.loan',function(newVal){
                scope.loandetails = angular.extend(scope.loandetails,newVal);
            },true);

            scope.formValue = function(array,model,findattr,retAttr){
                findattr = findattr ? findattr : 'id';
                retAttr = retAttr ? retAttr : 'value';
                console.log(findattr,retAttr,model);
                return _.find(array, function (obj) {
                    return obj[findattr] === model;
                })[retAttr];
            };

            scope.addCharge = function () {
                if (scope.chargeFormData.chargeId) {
                    resourceFactory.chargeResource.get({chargeId: this.chargeFormData.chargeId, template: 'true'}, function (data) {
                        data.chargeId = data.id;
                        scope.charges.push(data);
                        scope.chargeFormData.chargeId = undefined;
                    });
                }
            }

            scope.deleteCharge = function (index) {
                scope.charges.splice(index, 1);
            }


            scope.addTranches = function () {
                scope.formData.loan.disbursementData.push({
                });
            };
            scope.deleteTranches = function (index) {
                scope.formData.loan.disbursementData.splice(index, 1);
            }

            scope.syncRepaymentsWithMeetingchange = function () {
                if (!scope.formData.loan.syncRepaymentsWithMeeting) {
                    scope.formData.loan.syncDisbursementWithMeeting = false;
                }
            };

            scope.syncDisbursementWithMeetingchange = function () {
                if (scope.formData.loan.syncDisbursementWithMeeting) {
                    scope.formData.loan.syncRepaymentsWithMeeting = true;
                }
            };

            scope.addCollateral = function () {
                if (scope.collateralFormData.collateralIdTemplate && scope.collateralFormData.collateralValueTemplate) {
                    scope.collaterals.push({type: scope.collateralFormData.collateralIdTemplate.id, name: scope.collateralFormData.collateralIdTemplate.name, value: scope.collateralFormData.collateralValueTemplate, description: scope.collateralFormData.collateralDescriptionTemplate});
                    scope.collateralFormData.collateralIdTemplate = undefined;
                    scope.collateralFormData.collateralValueTemplate = undefined;
                    scope.collateralFormData.collateralDescriptionTemplate = undefined;
                }
            };

            scope.deleteCollateral = function (index) {
                scope.collaterals.splice(index, 1);
            };

            scope.previewRepayments = function () {
                // Make sure charges and collaterals are empty before initializing.
                delete scope.formData.loan.charges;
                delete scope.formData.loan.collateral;
                if(_.isUndefined(scope.formData.loan.datatables) || (!_.isUndefined(scope.formData.loan.datatables) && scope.formData.loan.datatables.length == 0)) {
                    delete scope.formData.loan.datatables;
                }

                var reqFirstDate = dateFilter(scope.date.first, scope.df);
                var reqSecondDate = dateFilter(scope.date.second, scope.df);
                var reqThirdDate = dateFilter(scope.date.third, scope.df);
                var reqFourthDate = dateFilter(scope.date.fourth, scope.df);
                if (scope.charges.length > 0) {
                    scope.formData.loan.charges = [];
                    for (var i in scope.charges) {
                        scope.formData.loan.charges.push({ chargeId: scope.charges[i].chargeId, amount: scope.charges[i].amount, dueDate: dateFilter(scope.charges[i].dueDate, scope.df) });
                    }
                }

                if (scope.formData.loan.disbursementData.length > 0) {
                    for (var i in scope.formData.loan.disbursementData) {
                        scope.formData.loan.disbursementData[i].expectedDisbursementDate = dateFilter(scope.formData.loan.disbursementData[i].expectedDisbursementDate, scope.df);
                    }
                }

                if (scope.collaterals.length > 0) {
                    scope.formData.loan.collateral = [];
                    for (var i in scope.collaterals) {
                        scope.formData.loan.collateral.push({type: scope.collaterals[i].type, value: scope.collaterals[i].value, description: scope.collaterals[i].description});
                    }
                    ;
                }

                if (this.formData.loan.syncRepaymentsWithMeeting) {
                    this.formData.loan.calendarId = scope.loanaccountinfo.calendarOptions[0].id;
                    scope.syncRepaymentsWithMeeting = this.formData.loan.syncRepaymentsWithMeeting;
                }
                delete this.formData.loan.syncRepaymentsWithMeeting;

                this.formData.loan.interestChargedFromDate = reqThirdDate;
                this.formData.loan.repaymentsStartingFromDate = reqFourthDate;
                this.formData.loan.locale = scope.optlang.code;
                this.formData.loan.dateFormat = scope.df;
                this.formData.loan.loanType = scope.inparams.templateType;
                this.formData.loan.expectedDisbursementDate = reqSecondDate;
                this.formData.loan.submittedOnDate = reqFirstDate;
                if(this.formData.loan.interestCalculationPeriodType == 0){
                    this.formData.loan.allowPartialPeriodInterestCalcualtion = false;
                }
                resourceFactory.loanResource.save({command: 'calculateLoanSchedule'}, this.formData.loan, function (data) {
                    scope.repaymentscheduleinfo = data;
                    scope.previewRepayment = true;
                    scope.formData.loan.syncRepaymentsWithMeeting = scope.syncRepaymentsWithMeeting;
                });

            }

            uiConfigService.appendConfigToScope(scope);

            //return input type
            scope.fieldType = function (type) {
                var fieldType = "";
                if (type) {
                    if (type == 'CODELOOKUP' || type == 'CODEVALUE') {
                        fieldType = 'SELECT';
                    } else if (type == 'DATE') {
                        fieldType = 'DATE';
                    } else if (type == 'DATETIME') {
                        fieldType = 'DATETIME';
                    } else if (type == 'BOOLEAN') {
                        fieldType = 'BOOLEAN';
                    } else {
                        fieldType = 'TEXT';
                    }
                }
                return fieldType;
            };

            scope.submit = function () {
                // if (WizardHandler.wizard().getCurrentStep() != scope.noOfTabs) {
                //     WizardHandler.wizard().next();
                //     return;
                // }
                // Make sure charges and collaterals are empty before initializing.
                // delete scope.formData.loan.charges;
                // delete scope.formData.loan.collateral;
                // var reqFirstDate = dateFilter(scope.date.first, scope.df);
                // var reqSecondDate = dateFilter(scope.date.second, scope.df);
                // var reqThirdDate = dateFilter(scope.date.third, scope.df);
                // var reqFourthDate = dateFilter(scope.date.fourth, scope.df);
                // var reqFifthDate = dateFilter(scope.date.fifth, scope.df);

                // if (scope.charges.length > 0) {
                //     scope.formData.loan.charges = [];
                //     for (var i in scope.charges) {
                //         scope.formData.loan.charges.push({ chargeId: scope.charges[i].chargeId, amount: scope.charges[i].amount, dueDate: dateFilter(scope.charges[i].dueDate, scope.df) });
                //     }
                // }

                // if (scope.formData.loan.disbursementData.length > 0) {
                //     for (var i in scope.formData.loan.disbursementData) {
                //         scope.formData.loan.disbursementData[i].expectedDisbursementDate = dateFilter(scope.formData.loan.disbursementData[i].expectedDisbursementDate, scope.df);
                //     }
                // }
                // if (scope.collaterals.length > 0) {
                //     scope.formData.loan.collateral = [];
                //     for (var i in scope.collaterals) {
                //         scope.formData.loan.collateral.push({type: scope.collaterals[i].type, value: scope.collaterals[i].value, description: scope.collaterals[i].description});
                //     }
                //     ;
                // }

                // if (this.formData.loan.syncRepaymentsWithMeeting) {
                //     this.formData.loan.calendarId = scope.loanaccountinfo.calendarOptions[0].id;
                // }
                // delete this.formData.loan.syncRepaymentsWithMeeting;
                // this.formData.loan.interestChargedFromDate = reqThirdDate;
                // this.formData.loan.repaymentsStartingFromDate = reqFourthDate;
                // this.formData.loan.locale = scope.optlang.code;
                // this.formData.loan.dateFormat = scope.df;
                // this.formData.loan.loanType = scope.inparams.templateType;
                // this.formData.loan.expectedDisbursementDate = reqSecondDate;
                // this.formData.loan.submittedOnDate = reqFirstDate;
                // this.formData.loan.createStandingInstructionAtDisbursement = scope.formData.loan.createStandingInstructionAtDisbursement;
                // if (scope.date.recalculationRestFrequencyDate) {
                //     var restFrequencyDate = dateFilter(scope.date.recalculationRestFrequencyDate, scope.df);
                //     scope.formData.loan.recalculationRestFrequencyDate = restFrequencyDate;
                // }
                // if (scope.date.recalculationCompoundingFrequencyDate) {
                //     var restFrequencyDate = dateFilter(scope.date.recalculationCompoundingFrequencyDate, scope.df);
                //     scope.formData.loan.recalculationCompoundingFrequencyDate = restFrequencyDate;
                // }
                // if(this.formData.loan.interestCalculationPeriodType == 0){
                //     this.formData.loan.allowPartialPeriodInterestCalcualtion = false;
                // }
                // if (!_.isUndefined(scope.datatables) && scope.datatables.length > 0) {
                //     angular.forEach(scope.datatables, function (datatable, index) {
                //         scope.columnHeaders = datatable.columnHeaderData;
                //         angular.forEach(scope.columnHeaders, function (colHeader, i) {
                //             scope.dateFormat = scope.df + " " + scope.tf
                //             if (scope.columnHeaders[i].columnDisplayType == 'DATE') {
                //                 if (!_.isUndefined(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName])) {
                //                     scope.formData.loan.datatables[index].data[scope.columnHeaders[i].columnName] = dateFilter(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName],
                //                         scope.dateFormat);
                //                     scope.formData.loan.datatables[index].data.dateFormat = scope.dateFormat;
                //                 }
                //             } else if (scope.columnHeaders[i].columnDisplayType == 'DATETIME') {
                //                 if (!_.isUndefined(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName].date) && !_.isUndefined(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName].time)) {
                //                     scope.formData.loan.datatables[index].data[scope.columnHeaders[i].columnName] = dateFilter(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName].date, scope.df)
                //                         + " " + dateFilter(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName].time, scope.tf);
                //                     scope.formData.loan.datatables[index].data.dateFormat = scope.dateFormat;
                //                 }
                //             }
                //         });
                //     });
                // } else {
                //     delete scope.formData.loan.datatables;
                // }
                // resourceFactory.loanResource.save(this.formData.loan, function (data) {
                //     location.path('/viewloanaccount/' + data.loanId);
                // });
            };

            scope.cancel = function () {
                if (scope.groupId) {
                    location.path('/viewgroup/' + scope.groupId);
                } else if (scope.clientId) {
                    location.path('/viewclient/' + scope.clientId);
                }
            }

            scope.allowCoMaker = false;

            scope.checkLoanPrincipal = function(loanPrincipal){
                if(loanPrincipal > scope.loanaccountinfo.proposedPrincipal){
                    scope.allowCoMaker = true;
                }else {
                    scope.allowCoMaker = false;
                }
            }
        }
    });
    mifosX.ng.application.controller('NewLoanAccAppController', ['$scope', '$routeParams', 'ResourceFactory', '$location', 'dateFilter', 'UIConfigService', 'WizardHandler', mifosX.controllers.NewLoanAccAppController]).run(function ($log) {
        $log.info("NewLoanAccAppController initialized");
    });
}(mifosX.controllers || {}));
