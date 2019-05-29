(function (module) {
    mifosX.controllers = _.extend(module, {
        CreateClientController: function (scope, resourceFactory, location, http, dateFilter, API_VERSION, Upload, $rootScope, routeParams, WizardHandler) {

            scope.offices = [];
            scope.staffs = [];
            scope.savingproducts = [];
            scope.first = {};
            scope.first.date = new Date();
            scope.first.submitondate = new Date ();
            scope.formData = {};
            scope.formData.client = {};
            scope.formDat = {};
            scope.clientNonPersonDetails = {};
            scope.restrictDate = new Date();
            scope.showSavingOptions = false;
            scope.savings = {};
            scope.savings.opensavingsproduct = false;
            scope.forceOffice = null;
            scope.showNonPersonOptions = false;
            scope.clientPersonId = 1;
            //address
            scope.addressTypes=[];
            scope.countryOptions=[];
            scope.stateOptions=[];
            scope.addressTypeId={};
            entityname="ADDRESS";
            scope.addressArray=[];
            scope.formData.client.address=[];
            //familymembers
            scope.formData.client.familyMembers=[];
            scope.familyArray=[];
            scope.datatables = [];
            scope.noOfTabs = 1;
            scope.step = '-';
            scope.formData.client.datatables = [];
            scope.formDat.datatables = [];
            scope.tf = "HH:mm";
            scope.clientId = routeParams.clientId;
            scope.inparams = {resourceType: 'template', activeOnly: 'true', templateType: 'individual'};
            scope.loanAccount = {};
            scope.datatablesLoan = [];
            scope.loandetails = {};
            scope.date = {};
            scope.productId = null;
            scope.previewRepayment = false;
            scope.disabled = true;
            scope.interest = "";
            scope.noOfRepayment = "";
            scope.loanTerm = "";
            scope.loanPurpose = "";
            scope.enableLoanOption = false;
            scope.comaker = {};


            var requestParams = {staffInSelectedOfficeOnly:true};
            if (routeParams.groupId) {
                requestParams.groupId = routeParams.groupId;
            }
            if (routeParams.officeId) {
                requestParams.officeId = routeParams.officeId;
            }
            resourceFactory.csClientTemplateResource.get(requestParams, function (data) {
                scope.offices = data.officeOptions;
                scope.staffs = data.staffOptions;
                scope.formData.officeId = scope.offices[0].id;
                scope.savingproducts = data.savingProductOptions;
                scope.genderOptions = data.genderOptions;
                scope.clienttypeOptions = data.clientTypeOptions;
                scope.clientClassificationOptions = data.clientClassificationOptions;
                scope.clientNonPersonConstitutionOptions = data.clientNonPersonConstitutionOptions;
                scope.clientNonPersonMainBusinessLineOptions = data.clientNonPersonMainBusinessLineOptions;
                scope.clientEducationalAttainmentOptions = data.clientEducationalAttainmentOptions;
                scope.clientResidenceOwnershipOptions = data.clientResidenceOwnershipOptions;
                scope.clientEmploymentOptions = data.clientEmploymentOptions;
                scope.clientSelfEmployedOptions = data.clientSelfEmployedOptions;
                scope.clientRankOptions = data.clientRankOptions;
                scope.clientRelationshipOfOfficerOptions = data.clientRelationshipOfOfficerOptions;
                scope.clientMaritalStatusOptions = data.clientMaritalStatusOptions;
                scope.clientRelatedToOfficerOptions = data.clientRelatedToOfficerOptions;
                scope.clientLegalFormOptions = data.clientLegalFormOptions;
                scope.datatables = data.datatables;
                if (!_.isUndefined(scope.datatables) && scope.datatables.length > 0) {
                    scope.noOfTabs = scope.datatables.length + 1;
                    angular.forEach(scope.datatables, function (datatable, index) {
                        scope.updateColumnHeaders(datatable.columnHeaderData);
                        angular.forEach(datatable.columnHeaderData, function (colHeader, i) {
                            if (_.isEmpty(scope.formDat.datatables[index])) {
                                scope.formDat.datatables[index] = {data: {}};
                            }

                            if (_.isEmpty(scope.formData.datatables[index])) {
                                scope.formData.datatables[index] = {
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

                if (data.savingProductOptions.length > 0) {
                    scope.showSavingOptions = true;
                }
                if(routeParams.officeId) {
                    scope.formData.officeId = routeParams.officeId;
                    for(var i in data.officeOptions) {
                        if(data.officeOptions[i].id == routeParams.officeId) {
                            scope.forceOffice = data.officeOptions[i];
                            break;
                        }
                    }
                }
                if(routeParams.groupId) {
                    if(typeof data.staffId !== "undefined") {
                        scope.formData.staffId = data.staffId;
                    }
                }


                scope.enableAddress=data.isAddressEnabled;

                if(scope.enableAddress===true)
                {
                    scope.addressTypes=data.address[0].addressTypeIdOptions;
                    scope.countryOptions=data.address[0].countryIdOptions;
                    scope.stateOptions=data.address[0].stateProvinceIdOptions;
                    console.log(scope.stateOptions);
                    console.log(data.address[0].stateProvinceIdOptions);

                    resourceFactory.addressFieldConfiguration.get({entity:entityname},function(data){



                        for(var i=0;i<data.length;i++)
                        {
                            data[i].field='scope.'+data[i].field;
                            eval(data[i].field+"="+data[i].is_enabled);

                        }





                    })


                }


                scope.relationshipIdOptions=data.familyMemberOptions.relationshipIdOptions;
                scope.genderIdOptions=data.familyMemberOptions.genderIdOptions;
                scope.maritalStatusIdOptions=data.familyMemberOptions.maritalStatusIdOptions;
                scope.professionIdOptions=data.familyMemberOptions.professionIdOptions;


            });

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

            // address

            scope.addAddress=function()
            {
                scope.addressArray.push({});
            }

            scope.removeAddress=function(index)
            {
                scope.addressArray.splice(index,1);
            }




            // end of address


            // family members

            scope.addFamilyMember=function()
            {
                scope.familyArray.push({});
            }

            scope.removeFamilyMember=function(index)
            {
                scope.familyArray.splice(index,1);
            }


            // end of family members




            scope.displayPersonOrNonPersonOptions = function (legalFormId) {
                if(legalFormId == scope.clientPersonId || legalFormId == null) {
                    scope.showNonPersonOptions = false;
                }else {
                    scope.showNonPersonOptions = true;
                }
            };

            scope.changeOffice = function (officeId) {
                resourceFactory.clientTemplateResource.get({staffInSelectedOfficeOnly:true, officeId: officeId
                }, function (data) {
                    scope.staffs = data.staffOptions;
                    scope.savingproducts = data.savingProductOptions;
                });
            };

            scope.setChoice = function () {
                if (this.formData.active) {
                    scope.choice = 1;
                }
                else if (!this.formData.active) {
                    scope.choice = 0;
                }
            };
            if(routeParams.groupId) {
                scope.cancel = '#/viewgroup/' + routeParams.groupId
                scope.groupid = routeParams.groupId;
            }else {
                scope.cancel = "#/clients"
            }

            //return input type
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

            scope.dateTimeFormat = function (colHeaders) {
                angular.forEach(colHeaders, function (colHeader, i) {
                    if (colHeaders[i].columnDisplayType == 'DATETIME') {
                        return scope.df + " " + scope.tf;
                    }
                });
                return scope.df;
            };

            scope.submit = function () {
                var reqDate = dateFilter(scope.first.date, scope.df);

                this.formData.client.locale = scope.optlang.code;
                this.formData.client.active = this.formData.active || false;
                this.formData.client.dateFormat = scope.df;
                this.formData.client.activationDate = reqDate;

                if (!_.isUndefined(scope.datatables) && scope.datatables.length > 0) {
                    angular.forEach(scope.datatables, function (datatable, index) {
                        scope.columnHeaders = datatable.columnHeaderData;
                        angular.forEach(scope.columnHeaders, function (colHeader, i) {
                            scope.dateFormat = scope.df + " " + scope.tf
                            if (scope.columnHeaders[i].columnDisplayType == 'DATE') {
                                if (!_.isUndefined(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName])) {
                                    scope.formData.client.datatables[index].data[scope.columnHeaders[i].columnName] = dateFilter(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName],
                                        scope.dateFormat);
                                    scope.formData.client.datatables[index].data.dateFormat = scope.dateFormat;
                                }
                            } else if (scope.columnHeaders[i].columnDisplayType == 'DATETIME') {
                                if (!_.isUndefined(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName].date) && !_.isUndefined(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName].time)) {
                                    scope.formData.client.datatables[index].data[scope.columnHeaders[i].columnName] = dateFilter(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName].date, scope.df)
                                        + " " + dateFilter(scope.formDat.datatables[index].data[scope.columnHeaders[i].columnName].time, scope.tf);
                                    scope.formData.client.datatables[index].data.dateFormat = scope.dateFormat;
                                }
                            }
                        });
                    });
                } else {
                    delete scope.formData.client.datatables;
                }

                if (routeParams.groupId) {
                    this.formData.client.groupId = routeParams.groupId;
                }

                if (routeParams.officeId) {
                    this.formData.client.officeId = routeParams.officeId;
                }

                if (scope.first.submitondate) {
                    reqDate = dateFilter(scope.first.submitondate, scope.df);
                    this.formData.client.submittedOnDate = reqDate;
                }

                if (scope.first.dateOfBirth) {
                    this.formData.client.dateOfBirth = dateFilter(scope.first.dateOfBirth, scope.df);
                }

                if (this.formData.client.legalFormId == scope.clientPersonId || this.formData.legalFormId == null) {
                    delete this.formData.client.fullname;
                } else {
                    delete this.formData.client.firstname;
                    delete this.formData.client.middlename;
                    delete this.formData.client.lastname;
                }

                if(scope.first.incorpValidityTillDate) {
                    this.formData.client.clientNonPersonDetails.locale = scope.optlang.code;
                    this.formData.client.clientNonPersonDetails.dateFormat = scope.df;
                    this.formData.client.clientNonPersonDetails.incorpValidityTillDate = dateFilter(scope.first.incorpValidityTillDate, scope.df);
                }

                if (!scope.savings.opensavingsproduct) {
                    this.formData.client.savingsProductId = null;
                }

                if(scope.enableAddress===true)
                {
                    for(var i=0;i<scope.addressArray.length;i++)
                    {
                        var temp=new Object();
                        if(scope.addressArray[i].addressTypeId)
                        {
                            temp.addressTypeId=scope.addressArray[i].addressTypeId;
                        }
                        if(scope.addressArray[i].street)
                        {
                            temp.street=scope.addressArray[i].street;
                        }
                        if(scope.addressArray[i].addressLine1)
                        {
                            temp.addressLine1=scope.addressArray[i].addressLine1;
                        }
                        if(scope.addressArray[i].addressLine2)
                        {
                            temp.addressLine2=scope.addressArray[i].addressLine2;
                        }
                        if(scope.addressArray[i].addressLine3)
                        {
                            temp.addressLine3=scope.addressArray[i].addressLine3;
                        }
                        if(scope.addressArray[i].townVillage)
                        {
                            temp.townVlage=scope.addressArray[i].townVillage;
                        }
                        if(scope.addressArray[i].city)
                        {
                            temp.city=scope.addressArray[i].city;
                        }
                        if(scope.addressArray[i].countyDistrict)
                        {
                            temp.countyDistrict=scope.addressArray[i].countyDistrict;
                        }
                        if(scope.addressArray[i].countryId)
                        {
                            temp.countryId=scope.addressArray[i].countryId;
                        }
                        if(scope.addressArray[i].stateProvinceId)
                        {
                            temp.stateProvinceId=scope.addressArray[i].stateProvinceId;
                        }
                        if(scope.addressArray[i].postalCode)
                        {
                            temp.postalCode=scope.addressArray[i].postalCode;
                        }
                        if(scope.addressArray[i].latitude)
                        {
                            temp.latitude=scope.addressArray[i].latitude;
                        }
                        if(scope.addressArray[i].longitude)
                        {
                            temp.longitude=scope.addressArray[i].longitude;
                        }
                        if(scope.addressArray[i].isActive)
                        {
                            temp.isActive=scope.addressArray[i].isActive;

                        }
                        scope.formData.client.address.push(temp);
                    }
                }


                // family array

                for(var i=0;i<scope.familyArray.length;i++)
                {
                    var temp=new Object();
                    if(scope.familyArray[i].relationshipId)
                    {
                        temp.relationshipId=scope.familyArray[i].relationshipId;
                    }
                    if(scope.familyArray[i].firstName)
                    {
                        temp.firstName=scope.familyArray[i].firstName;
                    }
                    if(scope.familyArray[i].middleName)
                    {
                        temp.middleName=scope.familyArray[i].middleName;
                    }
                    if(scope.familyArray[i].lastName)
                    {
                        temp.lastName=scope.familyArray[i].lastName;
                    }
                    if(scope.familyArray[i].qualification)
                    {
                        temp.qualification=scope.familyArray[i].qualification;
                    }
                    if(scope.familyArray[i].mobileNumber)
                    {
                        temp.mobileNumber=scope.familyArray[i].mobileNumber;
                    }
                    if(scope.familyArray[i].age)
                    {
                        temp.age=scope.familyArray[i].age;
                    }
                    if(scope.familyArray[i].isDependent)
                    {
                        temp.isDependent=scope.familyArray[i].isDependent;
                    }
                    if(scope.familyArray[i].genderId)
                    {
                        temp.genderId=scope.familyArray[i].genderId;
                    }
                    if(scope.familyArray[i].professionId)
                    {
                        temp.professionId=scope.familyArray[i].professionId;
                    }
                    if(scope.familyArray[i].maritalStatusId)
                    {
                        temp.maritalStatusId=scope.familyArray[i].maritalStatusId;
                    }
                    if(scope.familyArray[i].dateOfBirth)
                    {

                        temp.dateOfBirth=dateFilter(scope.familyArray[i].dateOfBirth, scope.df);
                    }

                    temp.locale = scope.optlang.code;
                    temp.dateFormat = scope.df;
                    scope.formData.client.familyMembers.push(temp);
                }
                scope.saveLoanAccount();
                scope.formData.client.active = true;
                scope.formData.kyc.locale = scope.optlang.code;
                if(scope.formData.comaker){
                    scope.formData.comaker.kyc.locale = scope.optlang.code;
                    scope.formData.comaker.info.locale = scope.optlang.code;
                    if (scope.comaker.dateOfBirth) {
                        this.formData.comaker.info.dateFormat = scope.df;
                        this.formData.comaker.info.dateOfBirth = dateFilter(scope.comaker.dateOfBirth, scope.df);
                    }
                }
                resourceFactory.csClientResource.save(scope.formData, function (data) {
                    location.path('/viewclient/' + data.clientId);
                });
            };

            scope.saveLoanAccount = function () {
                delete scope.loanAccount.charges;
                delete scope.loanAccount.collateral;
                var reqFirstDate = dateFilter(scope.first.submitondate, scope.df);
                var reqSecondDate = dateFilter(scope.date.second, scope.df);
                var reqThirdDate = dateFilter(scope.date.third, scope.df);
                var reqFourthDate = dateFilter(scope.date.fourth, scope.df);

                if (scope.charges.length > 0) {
                    scope.loanAccount.charges = [];
                    for (var i in scope.charges) {
                        scope.loanAccount.charges.push({ chargeId: scope.charges[i].chargeId, amount: scope.charges[i].amount, dueDate: dateFilter(scope.charges[i].dueDate, scope.df) });
                    }
                }

                if (scope.loanAccount.disbursementData.length > 0) {
                    for (var i in scope.loanAccount.disbursementData) {
                        scope.loanAccount.disbursementData[i].expectedDisbursementDate = dateFilter(scope.loanAccount.disbursementData[i].expectedDisbursementDate, scope.df);
                    }
                }
                if (scope.collaterals.length > 0) {
                    scope.loanAccount.collateral = [];
                    for (var i in scope.collaterals) {
                        scope.loanAccount.collateral.push({type: scope.collaterals[i].type, value: scope.collaterals[i].value, description: scope.collaterals[i].description});
                    }
                    ;
                }

                if (this.loanAccount.syncRepaymentsWithMeeting) {
                    this.loanAccount.calendarId = scope.loanaccountinfo.calendarOptions[0].id;
                }
                delete this.loanAccount.syncRepaymentsWithMeeting;
                this.loanAccount.interestChargedFromDate = reqThirdDate;
                this.loanAccount.repaymentsStartingFromDate = reqFourthDate;
                this.loanAccount.locale = scope.optlang.code;
                this.loanAccount.dateFormat = scope.df;
                this.loanAccount.loanType = scope.inparams.templateType;
                this.loanAccount.expectedDisbursementDate = reqSecondDate;
                this.loanAccount.submittedOnDate = reqFirstDate;
                this.loanAccount.createStandingInstructionAtDisbursement = scope.loanAccount.createStandingInstructionAtDisbursement;
                if (scope.date.recalculationRestFrequencyDate) {
                    var restFrequencyDate = dateFilter(scope.date.recalculationRestFrequencyDate, scope.df);
                    scope.loanAccount.recalculationRestFrequencyDate = restFrequencyDate;
                }
                if (scope.date.recalculationCompoundingFrequencyDate) {
                    var restFrequencyDate = dateFilter(scope.date.recalculationCompoundingFrequencyDate, scope.df);
                    scope.loanAccount.recalculationCompoundingFrequencyDate = restFrequencyDate;
                }
                if(this.loanAccount.interestCalculationPeriodType == 0){
                    this.loanAccount.allowPartialPeriodInterestCalcualtion = false;
                }
                if(scope.datatablesLoan){
                    this.loanAccount.datatables = scope.formDatatablesData();
                }
                scope.formData.loan = this.loanAccount;
                // resourceFactory.loanResource.save(this.loanAccount, function (data) {
                //     location.path('/viewclient/' + data.clientId);
                // });
            }

            resourceFactory.loanResource.get(scope.inparams, function (data) {
                scope.products = data.productOptions;
            });

            scope.formDatatablesData = function(){
                var datatables = [];
                scope.datatablesLoan.forEach(function(datatable){
                    var tableData = {};
                    tableData.data = {};
                    tableData.registeredTableName = datatable.registeredTableName;
                    datatable.columnHeaderData.forEach(function(table){
                        if(!table.isColumnNullable && !table.isColumnPrimaryKey){
                            var valData = table.columnDisplayType == "CODELOOKUP" ? table.columnValues[0].id : table.columnName;
                            var valData = table.columnDisplayType == "INTEGER" ? 0 : valData;
                            var columnName = table.columnName;
                            tableData.data.locale = scope.optlang.code;
                            tableData.data[columnName] = valData;
                        }
                    });
                    datatables.push(tableData);
                });
                return datatables;
            }

            scope.loanProductChange = function (loanProductId) {
                scope.inparams.productId = loanProductId;
                resourceFactory.loanResource.get(scope.inparams, function (data) {
                    scope.loanaccountinfo = data;
                    scope.previewClientLoanAccInfo();
                    scope.datatablesLoan = data.datatables;
                    scope.interest = data.interestRatePerPeriod + "%";
                    scope.noOfRepayment = data.numberOfRepayments;
                    scope.loanTerm = data.termFrequency + " " + data.termPeriodFrequencyType.value;
                    scope.disabled = false;
                });

                resourceFactory.loanResource.get({resourceType: 'template', templateType: 'collateral', productId: loanProductId, fields: 'id,loanCollateralOptions'}, function (data) {
                    scope.collateralOptions = data.loanCollateralOptions || [];
                });
            };

            scope.previewClientLoanAccInfo = function () {
                scope.previewRepayment = false;
                scope.charges = scope.loanaccountinfo.charges || [];
                scope.loanAccount.disbursementData = scope.loanaccountinfo.disbursementDetails || [];
                scope.collaterals = [];

                if (scope.loanaccountinfo.calendarOptions) {
                    scope.loanAccount.syncRepaymentsWithMeeting = true;
                    scope.loanAccount.syncDisbursementWithMeeting = true;
                }
                scope.multiDisburseLoan = scope.loanaccountinfo.multiDisburseLoan;
                scope.loanAccount.productId = scope.loanaccountinfo.loanProductId;
                scope.loanAccount.fundId = scope.loanaccountinfo.fundId;
                scope.loanAccount.principal = scope.loanaccountinfo.principal;
                scope.loanAccount.loanTermFrequency = scope.loanaccountinfo.termFrequency;
                scope.loanAccount.loanTermFrequencyType = scope.loanaccountinfo.termPeriodFrequencyType.id;
                scope.loandetails.loanTermFrequencyValue = scope.loanaccountinfo.termPeriodFrequencyType.value;
                scope.loanAccount.numberOfRepayments = scope.loanaccountinfo.numberOfRepayments;
                scope.loanAccount.repaymentEvery = scope.loanaccountinfo.repaymentEvery;
                scope.loanAccount.repaymentFrequencyType = scope.loanaccountinfo.repaymentFrequencyType.id;
                scope.loandetails.repaymentFrequencyValue = scope.loanaccountinfo.repaymentFrequencyType.value;
                scope.loanAccount.interestRatePerPeriod = scope.loanaccountinfo.interestRatePerPeriod;
                scope.loanAccount.amortizationType = scope.loanaccountinfo.amortizationType.id;
                scope.loanAccount.isEqualAmortization = scope.loanaccountinfo.isEqualAmortization;
                scope.loandetails.amortizationValue = scope.loanaccountinfo.amortizationType.value;
                scope.loanAccount.interestType = scope.loanaccountinfo.interestType.id;
                scope.loandetails.interestValue = scope.loanaccountinfo.interestType.value;
                scope.loanAccount.interestCalculationPeriodType = scope.loanaccountinfo.interestCalculationPeriodType.id;
                scope.loandetails.interestCalculationPeriodValue = scope.loanaccountinfo.interestCalculationPeriodType.value;
                scope.loanAccount.allowPartialPeriodInterestCalcualtion = scope.loanaccountinfo.allowPartialPeriodInterestCalcualtion;
                scope.loanAccount.inArrearsTolerance = scope.loanaccountinfo.inArrearsTolerance;
                scope.loanAccount.graceOnPrincipalPayment = scope.loanaccountinfo.graceOnPrincipalPayment;
                scope.loanAccount.graceOnInterestPayment = scope.loanaccountinfo.graceOnInterestPayment;
                scope.loanAccount.graceOnArrearsAgeing = scope.loanaccountinfo.graceOnArrearsAgeing;
                scope.loanAccount.transactionProcessingStrategyId = scope.loanaccountinfo.transactionProcessingStrategyId;
                scope.loandetails.transactionProcessingStrategyValue = scope.formValue(scope.loanaccountinfo.transactionProcessingStrategyOptions,scope.loanAccount.transactionProcessingStrategyId,'id','name');
                scope.loanAccount.graceOnInterestCharged = scope.loanaccountinfo.graceOnInterestCharged;
                scope.loanAccount.fixedEmiAmount = scope.loanaccountinfo.fixedEmiAmount;
                scope.loanAccount.maxOutstandingLoanBalance = scope.loanaccountinfo.maxOutstandingLoanBalance;

                if (scope.loanaccountinfo.isInterestRecalculationEnabled && scope.loanaccountinfo.interestRecalculationData.recalculationRestFrequencyDate) {
                    scope.date.recalculationRestFrequencyDate = new Date(scope.loanaccountinfo.interestRecalculationData.recalculationRestFrequencyDate);
                }
                if (scope.loanaccountinfo.isInterestRecalculationEnabled && scope.loanaccountinfo.interestRecalculationData.recalculationCompoundingFrequencyDate) {
                    scope.date.recalculationCompoundingFrequencyDate = new Date(scope.loanaccountinfo.interestRecalculationData.recalculationCompoundingFrequencyDate);
                }

                if(scope.loanaccountinfo.isLoanProductLinkedToFloatingRate) {
                    scope.loanAccount.isFloatingInterestRate = false ;
                }

                scope.loandetails = angular.copy(scope.loanAccount);
                scope.loandetails.productName = scope.formValue(scope.products,scope.loanAccount.productId,'id','name');
            };

            scope.changePreviewRepayment = function() {
                scope.previewRepayment = !scope.previewRepayment;
            }

            scope.previewRepayments = function () {
                // Make sure charges and collaterals are empty before initializing.
                var reqFirstDate = dateFilter(scope.first.submitondate, scope.df);
                var reqSecondDate = dateFilter(scope.date.second, scope.df);
                var prevRepayData = {};

                prevRepayData.locale = "en";
                prevRepayData.dateFormat = scope.df;
                prevRepayData.loanType = scope.inparams.templateType;
                prevRepayData.expectedDisbursementDate = reqSecondDate;
                prevRepayData.submittedOnDate = reqFirstDate;
                prevRepayData.productId = scope.inparams.productId;
                prevRepayData.principal = scope.loanAccount.principal;
                prevRepayData.loanTermFrequency = scope.loanAccount.loanTermFrequency;
                prevRepayData.loanTermFrequencyType = scope.loanAccount.loanTermFrequencyType;
                prevRepayData.numberOfRepayments = scope.loanAccount.numberOfRepayments;
                prevRepayData.repaymentEvery = scope.loanAccount.repaymentEvery;
                prevRepayData.repaymentFrequencyType = scope.loanAccount.repaymentFrequencyType;
                prevRepayData.interestRatePerPeriod = scope.loanAccount.interestRatePerPeriod;
                prevRepayData.amortizationType = scope.loanAccount.amortizationType;
                prevRepayData.interestType = scope.loanAccount.interestType;
                prevRepayData.interestCalculationPeriodType = scope.loanAccount.interestCalculationPeriodType;
                prevRepayData.transactionProcessingStrategyId = scope.loanAccount.transactionProcessingStrategyId;
                if(prevRepayData.interestCalculationPeriodType == 0){
                    this.loanAccount.allowPartialPeriodInterestCalcualtion = false;
                }
                resourceFactory.loanResource.save({command: 'calculateLoanSchedule'}, prevRepayData, function (data) {
                    scope.repaymentscheduleinfo = data;
                    scope.previewRepayment = true;
                    scope.formData.syncRepaymentsWithMeeting = scope.syncRepaymentsWithMeeting;
                });

            }

            scope.formValue = function(array,model,findattr,retAttr){
                findattr = findattr ? findattr : 'id';
                retAttr = retAttr ? retAttr : 'value';
                return _.find(array, function (obj) {
                    return obj[findattr] === model;
                })[retAttr];
            };

            scope.enabledLoan = function($index){
                if($index == scope.datatables.length - 1){
                    scope.enableLoanOption = true;
                }
            }

            scope.allowOtherEducationalAttainment = false;
            scope.allowMonthlyRent = false;
            scope.allowOtherEmployment = false;
            scope.allowSelfEmployed = false;
            scope.allowRelatedToOfficer = false;
            scope.allowOtherEducationalAttainmentComaker = false;
            scope.allowMonthlyRentComaker = false;
            scope.allowOtherEmploymentComaker = false;
            scope.allowSelfEmployedComaker = false;
            scope.allowRelatedToOfficerComaker = false;
            scope.allowCoMaker = false;

            scope.checkDropdown = function(idChecker, isComaker){
                scope.clientEducationalAttainmentOptions.forEach(function(option){
                    if(!isComaker){
                        if(option.id == idChecker){
                            if(option.name == "Others"){
                                scope.allowOtherEducationalAttainment = true;
                                return;
                            }
                            scope.allowOtherEducationalAttainment = false;
                        }
                    }else{
                        if(option.id == idChecker){
                            if(option.name == "Others"){
                                scope.allowOtherEducationalAttainmentComaker = true;
                                return;
                            }
                            scope.allowOtherEducationalAttainmentComaker = false;
                        }
                    }
                });
                scope.clientResidenceOwnershipOptions.forEach(function(option){
                    if(!isComaker){
                        if(option.id == idChecker){
                            if(option.name == "Rented"){
                                scope.allowMonthlyRent = true;
                                return;
                            }
                            scope.allowMonthlyRent = false;
                        }
                    }else{
                        if(option.id == idChecker){
                            if(option.name == "Rented"){
                                scope.allowMonthlyRentComaker = true;
                                return;
                            }
                            scope.allowMonthlyRentComaker = false;
                        }
                    }
                    
                });
                scope.clientEmploymentOptions.forEach(function(option){
                    if(!isComaker){
                        if(option.id == idChecker){
                            if(option.name == "Others"){
                                scope.allowOtherEmployment = true;
                                scope.allowSelfEmployed = false;
                                return;
                            }else if(option.name == "Self-Employed"){
                                scope.allowOtherEmployment = false;
                                scope.allowSelfEmployed = true;
                                return;
                            }
                            scope.allowOtherEmployment = false;
                            scope.allowSelfEmployed = false;
                        }
                    }else{
                        if(option.id == idChecker){
                            if(option.name == "Others"){
                                scope.allowOtherEmploymentComaker = true;
                                scope.allowSelfEmployedComaker = false;
                                return;
                            }else if(option.name == "Self-Employed"){
                                scope.allowOtherEmploymentComaker = false;
                                scope.allowSelfEmployedComaker = true;
                                return;
                            }
                            scope.allowOtherEmploymentComaker = false;
                            scope.allowSelfEmployedComaker = false;
                        }
                    }
                    
                });
                scope.clientRelatedToOfficerOptions.forEach(function(option){
                    if(!isComaker){
                        if(option.id == idChecker){
                            if(option.name == "Yes"){
                                scope.allowRelatedToOfficer = true;
                                return;
                            }
                            scope.allowRelatedToOfficer = false;
                        }
                    }else{
                        if(option.id == idChecker){
                            if(option.name == "Yes"){
                                scope.allowRelatedToOfficerComaker = true;
                                return;
                            }
                            scope.allowRelatedToOfficerComaker = false;
                        }
                    }
                });
            }

            scope.checkLoanPrincipal = function(loanPrincipal){
                if(loanPrincipal > scope.loanaccountinfo.proposedPrincipal){
                    scope.allowCoMaker = true;
                }else {
                    scope.allowCoMaker = false;
                }
            }
        }
    });
    mifosX.ng.application.controller('CreateClientController', ['$scope', 'ResourceFactory', '$location', '$http', 'dateFilter', 'API_VERSION', 'Upload', '$rootScope', '$routeParams', 'WizardHandler', mifosX.controllers.CreateClientController]).run(function ($log) {
        $log.info("CreateClientController initialized");
    });
}(mifosX.controllers || {}));
