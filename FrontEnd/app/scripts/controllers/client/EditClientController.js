(function (module) {
    mifosX.controllers = _.extend(module, {
        EditClientController: function (scope, routeParams, resourceFactory, location, http, dateFilter, API_VERSION, Upload, $rootScope) {
            scope.offices = [];
            scope.date = {};
            scope.restrictDate = new Date();
            scope.savingproducts = [];
            scope.clientId = routeParams.id;
            scope.showSavingOptions = 'false';
            scope.opensavingsproduct = 'false';
            scope.showNonPersonOptions = false;
            scope.clientPersonId = 1;
            scope.formData = {};
            scope.formData.kyc = {};
            scope.formData.client = {};
            resourceFactory.csClientResource.get({clientId: routeParams.id, template:'true', staffInSelectedOfficeOnly:true}, function (data) {
                scope.offices = data.officeOptions;
                scope.staffs = data.staffOptions;
                scope.savingproducts = data.savingProductOptions;
                scope.genderOptions = data.genderOptions;
                scope.clienttypeOptions = data.clientTypeOptions;
                scope.clientClassificationOptions = data.clientClassificationOptions;
                scope.clientNonPersonConstitutionOptions = data.clientNonPersonConstitutionOptions;
                scope.clientNonPersonMainBusinessLineOptions = data.clientNonPersonMainBusinessLineOptions;
                scope.clientLegalFormOptions = data.clientLegalFormOptions;
                scope.clientEducationalAttainmentOptions = data.clientEducationalAttainmentOptions;
                scope.clientResidenceOwnershipOptions = data.clientResidenceOwnershipOptions;
                scope.clientEmploymentOptions = data.clientEmploymentOptions;
                scope.clientSelfEmployedOptions = data.clientSelfEmployedOptions;
                scope.clientRankOptions = data.clientRankOptions;
                scope.clientRelationshipOfOfficerOptions = data.clientRelationshipOfOfficerOptions;
                scope.clientMaritalStatusOptions = data.clientMaritalStatusOptions;
                scope.clientRelatedToOfficerOptions = data.clientRelatedToOfficerOptions;
                scope.officeId = data.officeId;
                scope.formData.client = {
                    firstname: data.firstname,
                    lastname: data.lastname,
                    middlename: data.middlename,
                    active: data.active,
                    accountNo: data.accountNo,
                    staffId: data.staffId,
                    externalId: data.externalId,
                    isStaff:data.isStaff,
                    mobileNo: data.mobileNo,
                    savingsProductId: data.savingsProductId,
                    genderId: data.gender.id,
                    fullname: data.fullname,
                    clientNonPersonDetails : {
                        incorpNumber: data.clientNonPersonDetails.incorpNumber,
                        remarks: data.clientNonPersonDetails.remarks
                    }
                };

                if(data.gender){
                    scope.formData.client.genderId = data.gender.id;
                }

                if(data.clientType){
                    scope.formData.client.clientTypeId = data.clientType.id;
                }

                if(data.clientClassification){
                    scope.formData.client.clientClassificationId = data.clientClassification.id;
                }

                if(data.legalForm){
                    scope.displayPersonOrNonPersonOptions(data.legalForm.id);
                    scope.formData.client.legalFormId = data.legalForm.id;
                }

                if(data.clientNonPersonDetails.constitution){
                    scope.formData.client.clientNonPersonDetails.constitutionId = data.clientNonPersonDetails.constitution.id;
                }

                if(data.clientNonPersonDetails.mainBusinessLine){
                    scope.formData.client.clientNonPersonDetails.mainBusinessLineId = data.clientNonPersonDetails.mainBusinessLine.id;
                }

                if (data.savingsProductId != null) {
                    scope.opensavingsproduct = 'true';
                    scope.showSavingOptions = 'true';
                } else if (data.savingProductOptions.length > 0) {
                    scope.showSavingOptions = 'true';
                }

                if (data.dateOfBirth) {
                    var dobDate = dateFilter(data.dateOfBirth, scope.df);
                    scope.date.dateOfBirth = new Date(dobDate);
                }

                if (data.clientNonPersonDetails.incorpValidityTillDate) {
                    var incorpValidityTillDate = dateFilter(data.clientNonPersonDetails.incorpValidityTillDate, scope.df);
                    scope.date.incorpValidityTillDate = new Date(incorpValidityTillDate);
                }

                var actDate = dateFilter(data.activationDate, scope.df);
                scope.date.activationDate = new Date(actDate);
                if (data.active) {
                    scope.choice = 1;
                    scope.showSavingOptions = 'false';
                    scope.opensavingsproduct = 'false';
                }

                if (data.timeline.submittedOnDate) {
                    var submittedOnDate = dateFilter(data.timeline.submittedOnDate, scope.df);
                    scope.date.submittedOnDate = new Date(submittedOnDate);
                }
                scope.formData.kyc = data.kycInfo;
                scope.formData.kyc.maritalStatusId = data.kycInfo.maritalStatus.id;
                scope.formData.kyc.educationalAttainmentId = data.kycInfo.educationalAttainment.id;
                scope.formData.kyc.residenceOwnershipId = data.kycInfo.residenceOwnership.id;
                scope.formData.kyc.employmentId = data.kycInfo.employment.id;
                scope.formData.kyc.selfEmployedId = data.kycInfo.selfEmployed.id;
                scope.formData.kyc.rankId = data.kycInfo.rank.id;
                scope.formData.kyc.relatedToOfficerId = data.kycInfo.relatedToOfficer.id;
                scope.formData.kyc.relationshipOfOfficerId = data.kycInfo.relationshipOfOfficer.id;

                scope.allowOtherEducationalAttainment = 
                    data.kycInfo.educationalAttainment.name == "Others" ? true : false;
                scope.allowMonthlyRent = 
                    data.kycInfo.residenceOwnership.name == "Rented" ? true : false;
                scope.allowOtherEmployment = 
                    data.kycInfo.employment.name == "Others" ? true : false;
                scope.allowSelfEmployed = 
                    data.kycInfo.employment.name == "Self-Employed" ? true : false;
                scope.allowRelatedToOfficer = 
                    data.kycInfo.relatedToOfficer.name == "Yes" ? true : false;

                delete scope.formData.kyc.maritalStatus;
                delete scope.formData.kyc.educationalAttainment;
                delete scope.formData.kyc.residenceOwnership;
                delete scope.formData.kyc.employment;
                delete scope.formData.kyc.selfEmployed;
                delete scope.formData.kyc.rank;
                delete scope.formData.kyc.relatedToOfficer;
                delete scope.formData.kyc.relationshipOfOfficer;
            });

            scope.displayPersonOrNonPersonOptions = function (legalFormId) {
                if(legalFormId == scope.clientPersonId || legalFormId == null) {
                    scope.showNonPersonOptions = false;
                }else {
                    scope.showNonPersonOptions = true;
                }
            };

            scope.submit = function () {
                this.formData.client.locale = scope.optlang.code;
                this.formData.client.dateFormat = scope.df;
                if (scope.choice === 1) {
                    if (scope.date.activationDate) {
                        this.formData.client.activationDate = dateFilter(scope.date.activationDate, scope.df);
                    }
                }
                if(scope.date.dateOfBirth){
                    this.formData.client.dateOfBirth = dateFilter(scope.date.dateOfBirth,  scope.df);
                }

                if(scope.date.submittedOnDate){
                    this.formData.client.submittedOnDate = dateFilter(scope.date.submittedOnDate,  scope.df);
                }

                if(scope.date.incorpValidityTillDate){
                    this.formData.client.clientNonPersonDetails.locale = scope.optlang.code;
                    this.formData.client.clientNonPersonDetails.dateFormat = scope.df;
                    this.formData.client.clientNonPersonDetails.incorpValidityTillDate = dateFilter(scope.date.incorpValidityTillDate,  scope.df);
                }

                if(this.formData.client.legalFormId == scope.clientPersonId || this.formData.client.legalFormId == null) {
                    delete this.formData.client.fullname;
                }else {
                    delete this.formData.client.firstname;
                    delete this.formData.client.middlename;
                    delete this.formData.client.lastname;
                }
                this.formData.kyc.locale = scope.optlang.code;

                resourceFactory.csClientResource.update({'clientId': routeParams.id}, this.formData, function (data) {
                    location.path('/viewclient/' + routeParams.id);
                });
            };


            scope.allowOtherEducationalAttainment = false;
            scope.allowMonthlyRent = false;
            scope.allowOtherEmployment = false;
            scope.allowSelfEmployed = false;
            scope.allowRelatedToOfficer = false;

            scope.checkDropdown = function(idChecker, isComaker){
                scope.clientEducationalAttainmentOptions.forEach(function(option){
                    if(!isComaker){
                        if(option.id == idChecker){
                            if(option.name == "Others"){
                                scope.allowOtherEducationalAttainment = true;
                                return;
                            }
                            scope.allowOtherEducationalAttainment = false;
                            delete scope.formData.kyc.othersEducationalAttainment;
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
                            delete scope.formData.kyc.rentedResidenceOwnership;
                        }
                    }
                    
                });
                scope.clientEmploymentOptions.forEach(function(option){
                    if(!isComaker){
                        if(option.id == idChecker){
                            if(option.name == "Others"){
                                scope.allowOtherEmployment = true;
                                scope.allowSelfEmployed = false;
                                delete scope.formData.kyc.selfEmployedId;
                                delete scope.formData.kyc.yearsInOperation;
                                delete scope.formData.kyc.noOfEmployees;
                                return;
                            }else if(option.name == "Self-Employed"){
                                scope.allowOtherEmployment = false;
                                scope.allowSelfEmployed = true;
                                delete scope.formData.kyc.otherEmployment;
                                return;
                            }
                            scope.allowOtherEmployment = false;
                            scope.allowSelfEmployed = false;
                            delete scope.formData.kyc.otherEmployment;
                            delete scope.formData.kyc.selfEmployedId;
                            delete scope.formData.kyc.yearsInOperation;
                            delete scope.formData.kyc.noOfEmployees;
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
                            delete scope.formData.kyc.nameOfOfficer;
                            delete scope.formData.kyc.contactNumberOfficer;
                            delete scope.formData.kyc.relationshipOfOfficerId;
                        }
                    }
                });
            }

        }
    });
    mifosX.ng.application.controller('EditClientController', ['$scope', '$routeParams', 'ResourceFactory', '$location', '$http', 'dateFilter', 'API_VERSION', 'Upload', '$rootScope', mifosX.controllers.EditClientController]).run(function ($log) {
        $log.info("EditClientController initialized");
    });
}(mifosX.controllers || {}));
