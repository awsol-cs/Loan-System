(function (module) {
    mifosX.controllers = _.extend(module, {
        EditComakerController: function (scope, routeParams, resourceFactory, location, http, dateFilter, API_VERSION, Upload, $rootScope) {
            scope.formData = {};
            scope.formData.comaker = {};
            scope.comaker = {};

            resourceFactory.csCoMakerResource.get({loanId: routeParams.id}, function (data) {
                scope.formData.comaker = data;
                console.log(data);

                if (data.info.dateOfBirth) {
                    var dobDate = dateFilter(data.info.dateOfBirth, scope.df);
                    scope.comaker.dateOfBirth = new Date(dobDate);
                }

                scope.formData.comaker.kyc.maritalStatusId = data.kyc.maritalStatus.id;
                scope.formData.comaker.kyc.educationalAttainmentId = data.kyc.educationalAttainment.id;
                scope.formData.comaker.kyc.residenceOwnershipId = data.kyc.residenceOwnership.id;
                scope.formData.comaker.kyc.employmentId = data.kyc.employment.id;
                scope.formData.comaker.kyc.selfEmployedId = data.kyc.selfEmployed.id;
                scope.formData.comaker.kyc.rankId = data.kyc.rank.id;
                scope.formData.comaker.kyc.relatedToOfficerId = data.kyc.relatedToOfficer.id;
                scope.formData.comaker.kyc.relationshipOfOfficerId = data.kyc.relationshipOfOfficer.id;
                scope.formData.comaker.info.genderId = data.info.gender.id;

                scope.allowOtherEducationalAttainmentComaker = 
                    data.kyc.educationalAttainment.name == "Others" ? true : false;
                scope.allowMonthlyRentComaker = 
                    data.kyc.residenceOwnership.name == "Rented" ? true : false;
                scope.allowOtherEmploymentComaker = 
                    data.kyc.employment.name == "Others" ? true : false;
                scope.allowSelfEmployedComaker = 
                    data.kyc.employment.name == "Self-Employed" ? true : false;
                scope.allowRelatedToOfficerComaker = 
                    data.kyc.relatedToOfficer.name == "Yes" ? true : false;
                delete scope.formData.comaker.kyc.maritalStatus;
                delete scope.formData.comaker.kyc.educationalAttainment;
                delete scope.formData.comaker.kyc.residenceOwnership;
                delete scope.formData.comaker.kyc.employment;
                delete scope.formData.comaker.kyc.selfEmployed;
                delete scope.formData.comaker.kyc.rank;
                delete scope.formData.comaker.kyc.relatedToOfficer;
                delete scope.formData.comaker.kyc.relationshipOfOfficer;
                delete scope.formData.comaker.info.gender;
            });

            resourceFactory.LoanAccountResource.getLoanAccountDetails({loanId: routeParams.id, associations: 'all',exclude: 'guarantors,futureSchedule'}, function (data) {
                scope.loandetails = data;
            });
            var requestParams = {staffInSelectedOfficeOnly:true};
            resourceFactory.csClientTemplateResource.get(requestParams, function (data) {
                
                scope.genderOptions = data.genderOptions;
                scope.clientEducationalAttainmentOptions = data.clientEducationalAttainmentOptions;
                scope.clientResidenceOwnershipOptions = data.clientResidenceOwnershipOptions;
                scope.clientEmploymentOptions = data.clientEmploymentOptions;
                scope.clientSelfEmployedOptions = data.clientSelfEmployedOptions;
                scope.clientRankOptions = data.clientRankOptions;
                scope.clientRelationshipOfOfficerOptions = data.clientRelationshipOfOfficerOptions;
                scope.clientMaritalStatusOptions = data.clientMaritalStatusOptions;
                scope.clientRelatedToOfficerOptions = data.clientRelatedToOfficerOptions;
                

            });

            scope.submit = function(){
                scope.formData.comaker.info.locale = scope.optlang.code;
                scope.formData.comaker.kyc.locale = scope.optlang.code;
                scope.formData.comaker.info.dateFormat = scope.df;
                if(scope.comaker.dateOfBirth){
                    scope.formData.comaker.info.dateOfBirth = dateFilter(scope.comaker.dateOfBirth,  scope.df);
                }

                 resourceFactory.csCoMakerResource.update({comakerId: scope.formData.comaker.info.id}, scope.formData, function (data) {
                    location.path('/viewloanaccount/' + routeParams.id);
                });
            }

            scope.checkDropdown = function(idChecker){
                scope.clientEducationalAttainmentOptions.forEach(function(option){
                        if(option.id == idChecker){
                            if(option.name == "Others"){
                                scope.allowOtherEducationalAttainmentComaker = true;
                                return;
                            }
                            scope.allowOtherEducationalAttainmentComaker = false;
                            scope.formData.comaker.kyc.othersEducationalAttainment = "";
                        }
                });
                scope.clientResidenceOwnershipOptions.forEach(function(option){
                        if(option.id == idChecker){
                            if(option.name == "Rented"){
                                scope.allowMonthlyRentComaker = true;
                                return;
                            }
                            scope.allowMonthlyRentComaker = false;
                            scope.formData.comaker.kyc.rentedResidenceOwnership = ""
                        }
                    
                });
                scope.clientEmploymentOptions.forEach(function(option){
                        if(option.id == idChecker){
                            if(option.name == "Others"){
                                scope.allowOtherEmploymentComaker = true;
                                scope.allowSelfEmployedComaker = false;
                                scope.formData.comaker.kyc.selfEmployedId = "";
                                scope.formData.comaker.kyc.yearsInOperation = "";
                                scope.formData.comaker.kyc.noOfEmployees = "";
                                return;
                            }else if(option.name == "Self-Employed"){
                                scope.allowOtherEmploymentComaker = false;
                                scope.allowSelfEmployedComaker = true;
                                scope.formData.comaker.kyc.otherEmployment = "";
                                return;
                            }
                            scope.allowOtherEmploymentComaker = false;
                            scope.allowSelfEmployedComaker = false;
                            scope.formData.comaker.kyc.otherEmployment = "";
                            scope.formData.comaker.kyc.selfEmployedId = "";
                            scope.formData.comaker.kyc.yearsInOperation = "";
                            scope.formData.comaker.kyc.noOfEmployees = "";
                        }
                    
                });
                scope.clientRelatedToOfficerOptions.forEach(function(option){
                        if(option.id == idChecker){
                            if(option.name == "Yes"){
                                scope.allowRelatedToOfficerComaker = true;
                                return;
                            }
                            scope.allowRelatedToOfficerComaker = false;
                            scope.formData.comaker.kyc.nameOfOfficer = "";
                            scope.formData.comaker.kyc.contactNumberOfficer = "";
                            scope.formData.comaker.kyc.relationshipOfOfficerId = "";
                        }
                });
            }



        }
    });
    mifosX.ng.application.controller('EditComakerController', ['$scope', '$routeParams', 'ResourceFactory', '$location', '$http', 'dateFilter', 'API_VERSION', 'Upload', '$rootScope', mifosX.controllers.EditComakerController]).run(function ($log) {
        $log.info("EditComakerController initialized");
    });
}(mifosX.controllers || {}));
