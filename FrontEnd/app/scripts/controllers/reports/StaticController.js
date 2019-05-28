(function (module) {
    mifosX.controllers = _.extend(module, {
        StaticController: function (scope, routeParams, resourceFactory, location, route, $rootScope, http, $sce, API_VERSION) {

                function formatDate(date) {
                var d = new Date(date),
                    month = '' + (d.getMonth() + 1),
                    day = '' + d.getDate(),
                    year = d.getFullYear();

                if (month.length < 2) month = '0' + month;
                if (day.length < 2) day = '0' + day;

                return [year, month, day].join('-');
            }


           scope.getCurrentDocument = function() {
           	   scope.isVisible = false;
               var fileName = scope.currentDocument.split('.');
			   var reportURL = $rootScope.hostUrl + API_VERSION + "/cs_reports/PDF?file="+fileName[0];
               
                var jsonData = {
                   "id" : parseInt(scope.selectedOffice),
                   "startDate" : formatDate(scope.one),
                   "endDate" : formatDate(scope.two),
                   "subReportParam" : [
                        "Balance-Sheet-1",
                        "Balance-Sheet-2"
                    ]
               };

                   if(scope.currentDocument.includes(".pdf")){
                       scope.isVisible = scope.isVisible = true;
                     // Allow untrusted urls for the ajax request.
                       // http://docs.angularjs.org/error/$sce/insecurl
                       reportURL = $sce.trustAsResourceUrl(reportURL);
                       reportURL = $sce.valueOf(reportURL);
                       http.post(reportURL, jsonData, {responseType: 'arraybuffer'}).
                         success(function(data, status, headers, config) {
                           var contentType = headers('Content-Type');
                           var file = new Blob([data], {type: "application/pdf"});
                           var fileContent = URL.createObjectURL(file);

                           scope.pdf = {
                               src: $sce.trustAsResourceUrl(fileContent),
                           };

                         });


                   }
                   else {
                        var link = document.createElement("a");
                    link.href = '../../../static/' + scope.currentDocument;
                    link.style = "visibility:hidden";
                    //link.download = '../../../static' + scope.currentDocument;
                   }
		   		
		   };
		   /*
		   scope.pdf = {
		        src: '../../../static/SEC-Form-LCFS1.pdf',
		   };*/

			/* START AIS scope variables */

			const monthNames = ["January", "February", "March", "April", "May", "June",
			  "July", "August", "September", "October", "November", "December"
			];

			scope.baseURL = "";
		   	scope.SEC = "SECURITIES AND EXCHANGE COMMISSION";
            scope.AIS ="ANNUAL INFORMATION STATEMENT";
            scope.AISDay = new Date().getDate();
            scope.AISMonth = monthNames[new Date().getMonth()];
            scope.AISYear = new Date().getFullYear();
            scope.CorpName;
            scope.IndustryClass;
            scope.PrincipalAddr;
            scope.SECRegNo;
            scope.SECCOA;
            scope.SECRegDate;
            scope.SECAnivDate;
            scope.ContactPerson;
            scope.ContactInformation;
            scope.Designation;
            scope.EmailAddr;
            scope.SRCOption1 = false;
            scope.SRCOption2 = false;
            scope.SRCOption3 = false;
            scope.SRCOption4 = false;
            scope.SRCOption5 = false;
            scope.PNoteATBICURR = "PHP";
            scope.PNoteATBIST = "";
            scope.PNoteATBILT = "";
            scope.RAgreeATBICURR = "PHP";
            scope.RAgreeATBIST = "";
            scope.RAgreeATBILT = "";
            scope.COAATBICURR = "PHP";
            scope.COAATBIST = "";
            scope.COAATBILT = "";
            scope.COPATBICURR = "PHP";
            scope.COPATBIST = "";
            scope.COPATBILT = "";
            scope.OTH1ATBI = "";
            scope.OTH1ATBICURR = "";
            scope.OTH1ATBIST = "";
            scope.OTH1ATBILT = "";
            scope.OTH2ATBI = "";
            scope.OTH2ATBICURR = "";
            scope.OTH2ATBIST = "";
            scope.OTH2ATBILT = "";
            scope.OTH3ATBI = "";
            scope.OTH3ATBICURR = "";
            scope.OTH3ATBIST = "";
            scope.OTH3ATBILT = "";
            scope.ATBISUMST = "";
            scope.ATBISUMLT = "";
            scope.PNoteTICURR="PHP";
            scope.PNoteTIMonthST = "";
            scope.PNoteTIMonthLT = "";
            scope.PNoteTIOAST = "";
            scope.PNoteTIOALT = "";
            scope.RAgreeTICURR="PHP";
            scope.RAgreeTIMonthST = "";
            scope.RAgreeTIMonthLT = "";
            scope.RAgreeTIOAST = "";
            scope.RAgreeTIOALT = "";
            scope.COATICURR="PHP";
            scope.COATIMonthST = "";
            scope.COATIMonthLT = "";
            scope.COATIOAST = "";
            scope.COATIOALT = "";
            scope.COPTICURR="PHP";
            scope.COPTIMonthST = "";
            scope.COPTIMonthLT = "";
            scope.COPTIOAST = "";
            scope.COPTIOALT = "";
            scope.OTH1TI = "";
            scope.OTH1TICURR="";
            scope.OTH1TIMonthST = "";
            scope.OTH1TIMonthLT = "";
            scope.OTH1TIOAST = "";
            scope.OTH1TIOALT = "";
            scope.OTH2TI = "";
            scope.OTH2TICURR="";
            scope.OTH2TIMonthST = "";
            scope.OTH2TIMonthLT = "";
            scope.OTH2TIOAST = "";
            scope.OTH2TIOALT = "";
            scope.OTH3TI = "";
            scope.OTH3TICURR="";
            scope.OTH3TIMonthST = "";
            scope.OTH3TIMonthLT = "";
            scope.OTH3TIOAST = "";
            scope.OTH3TIOALT = "";
            scope.TISUMMonthST = "";
            scope.TISUMMonthLT = "";
            scope.TISUMOAST = "";
            scope.TISUMOALT = "";
            scope.SECDocNo;
            scope.SECPageNo;
            scope.SECBookNo;
            scope.SECSerialNo;
            scope.Instruction_1 = "This form shall be Executed and filed by an issuer of commercial paper exempt under SRC Rule 9.2 and 17.1(2)(C)    on or before January 30 of each year.";
            scope.Instruction_2 = "A fee of ten Thousand Pesos (P10,000.00) plus 1% thereof as Legal Research Fee (LRF) in Cash, Cashier's or Manager's check shall be paid upon filing.";
            scope.pagraph_1 = "Pursuant to the provisions of the Securities Regulation Code, the issuer has caused this information statement to be signed on its behalf by the undersigned officers on ";
            scope.pagraph_Annex_1 = "Any evidence of indebtedness issued by a financial institution itself that has been duly licensed by the Bangko Sentral ng Pilipinas to engage in banking/quasi-banking activity shall be exempt from registration under Section 8.1 of the Code; provided that the purchase and sale of such security shall not be considered exempt from the coverage of the provisions of the Code on antifraud, civil liability or others.";
            scope.pagraph_Annex_3 = "Evidence of indebtedness issued to the following primary institutional lenders:  banks, including their trust accounts wherein the bank-trustee is granted discretionary powers in the investment disposition of the trust funds, investment houses including their trust accounts wherein the investment house-trustee is granted discretionary powers in the investment disposition of the trust funds, trust companies, financing companies, investment companies, pre-need companies, non-stock savings and loan associations, building and loan associations, venture capital corporations, insurance companies, government financial institutions, pawnshops, pension and retirement funds approved by the BIR, educational assistance funds established by the national government, and other entities that may be classified as primary institutional lenders by the BSP, in consultation with the SEC; provided all such evidence of indebtedness shall only be negotiated or assigned to any of the aforementioned primary institutional lenders or the Development Bank of the Philippines with respect to private development banks in relation with their rediscounting privileges; provided further that in case of non-banks without underwriting licenses, such negotiation or assignment shall be through banks or non-banks licensed to be an underwriter or a securities dealer; provided finally, that in no case shall said instrument be negotiated or assigned to non-qualified investors;";
            scope.UnsignedOfficerLoc = "";
            scope.PresidentSignature = "";
            scope.TreasurerSignature = "";
            scope.PresidentName = "";
            scope.TreasurerName = "";
            scope.DocumentNumber = "";
            scope.PageNumber = "";
            scope.BookNumber = "";
            scope.SerialNumber = "";
            scope.hideTable = false
            scope.hideAISReport = true; 
            scope.hideHeader = true;
            /* END AIS scope variables */

            scope.SumOfAtbiSt = function(){
            	var arrayATBIST = [];
				isNaN(parseFloat(scope.PNoteATBIST)) ? arrayATBIST.push(0) : arrayATBIST.push(parseFloat(scope.PNoteATBIST));
				isNaN(parseFloat(scope.RAgreeATBIST)) ? arrayATBIST.push(0) : arrayATBIST.push(parseFloat(scope.RAgreeATBIST));
				isNaN(parseFloat(scope.COAATBIST)) ? arrayATBIST.push(0) : arrayATBIST.push(parseFloat(scope.COAATBIST));
				isNaN(parseFloat(scope.COPATBIST)) ? arrayATBIST.push(0) : arrayATBIST.push(parseFloat(scope.COPATBIST));
				isNaN(parseFloat(scope.OTH1ATBIST)) ? arrayATBIST.push(0) : arrayATBIST.push(parseFloat(scope.OTH1ATBIST));
				isNaN(parseFloat(scope.OTH2ATBIST)) ? arrayATBIST.push(0) : arrayATBIST.push(parseFloat(scope.OTH2ATBIST));
				isNaN(parseFloat(scope.OTH3ATBIST)) ? arrayATBIST.push(0) : arrayATBIST.push(parseFloat(scope.OTH3ATBIST));

				scope.ATBISUMST = scope.sum(arrayATBIST).toString();
            }

            scope.SumOfAtbiLt = function(){
            	var arrayATBILT = [];
				isNaN(parseFloat(scope.PNoteATBILT)) ? arrayATBILT.push(0) : arrayATBILT.push(parseFloat(scope.PNoteATBILT));
				isNaN(parseFloat(scope.RAgreeATBILT)) ? arrayATBILT.push(0) : arrayATBILT.push(parseFloat(scope.RAgreeATBILT));
				isNaN(parseFloat(scope.COAATBILT)) ? arrayATBILT.push(0) : arrayATBILT.push(parseFloat(scope.COAATBILT));
				isNaN(parseFloat(scope.COPATBILT)) ? arrayATBILT.push(0) : arrayATBILT.push(parseFloat(scope.COPATBILT));
				isNaN(parseFloat(scope.OTH1ATBILT)) ? arrayATBILT.push(0) : arrayATBILT.push(parseFloat(scope.OTH1ATBILT));
				isNaN(parseFloat(scope.OTH2ATBILT)) ? arrayATBILT.push(0) : arrayATBILT.push(parseFloat(scope.OTH2ATBILT));
				isNaN(parseFloat(scope.OTH3ATBILT)) ? arrayATBILT.push(0) : arrayATBILT.push(parseFloat(scope.OTH3ATBILT));

				scope.ATBISUMLT = scope.sum(arrayATBILT).toString();
            }

            scope.SumOfTiMonthSt = function(){
            	var arrayTiMonthSt = [];
            	isNaN(parseFloat(scope.PNoteTIMonthST)) ? arrayTiMonthSt.push(0) : arrayTiMonthSt.push(parseFloat(scope.PNoteTIMonthST));
            	isNaN(parseFloat(scope.RAgreeTIMonthST)) ? arrayTiMonthSt.push(0) : arrayTiMonthSt.push(parseFloat(scope.RAgreeTIMonthST));
            	isNaN(parseFloat(scope.COATIMonthST)) ? arrayTiMonthSt.push(0) : arrayTiMonthSt.push(parseFloat(scope.COATIMonthST));
            	isNaN(parseFloat(scope.COPTIMonthST)) ? arrayTiMonthSt.push(0) : arrayTiMonthSt.push(parseFloat(scope.COPTIMonthST));
            	isNaN(parseFloat(scope.OTH1TIMonthST)) ? arrayTiMonthSt.push(0) : arrayTiMonthSt.push(parseFloat(scope.OTH1TIMonthST));
            	isNaN(parseFloat(scope.OTH2TIMonthST)) ? arrayTiMonthSt.push(0) : arrayTiMonthSt.push(parseFloat(scope.OTH2TIMonthST));
            	isNaN(parseFloat(scope.OTH3TIMonthST)) ? arrayTiMonthSt.push(0) : arrayTiMonthSt.push(parseFloat(scope.OTH3TIMonthST));

            	scope.TISUMMonthST = scope.sum(arrayTiMonthSt).toString();
            	
            }

            scope.SumOfTiMonthLt = function(){
            	var arrayTiMonthLt = [];
            	isNaN(parseFloat(scope.PNoteTIMonthLT)) ? arrayTiMonthLt.push(0) : arrayTiMonthLt.push(parseFloat(scope.PNoteTIMonthLT));
            	isNaN(parseFloat(scope.RAgreeTIMonthLT)) ? arrayTiMonthLt.push(0) : arrayTiMonthLt.push(parseFloat(scope.RAgreeTIMonthLT));
            	isNaN(parseFloat(scope.COATIMonthLT)) ? arrayTiMonthLt.push(0) : arrayTiMonthLt.push(parseFloat(scope.COATIMonthLT));
            	isNaN(parseFloat(scope.COPTIMonthLT)) ? arrayTiMonthLt.push(0) : arrayTiMonthLt.push(parseFloat(scope.COPTIMonthLT));
            	isNaN(parseFloat(scope.OTH1TIMonthLT)) ? arrayTiMonthLt.push(0) : arrayTiMonthLt.push(parseFloat(scope.OTH1TIMonthLT));
            	isNaN(parseFloat(scope.OTH2TIMonthLT)) ? arrayTiMonthLt.push(0) : arrayTiMonthLt.push(parseFloat(scope.OTH2TIMonthLT));
            	isNaN(parseFloat(scope.OTH3TIMonthLT)) ? arrayTiMonthLt.push(0) : arrayTiMonthLt.push(parseFloat(scope.OTH3TIMonthLT));

            	scope.TISUMMonthLT = scope.sum(arrayTiMonthLt).toString();
            	
            }

            scope.SumOfTiOaSt = function(){
            	var arrayTiOaSt = [];
            	isNaN(parseFloat(scope.PNoteTIOAST)) ? arrayTiOaSt.push(0) : arrayTiOaSt.push(parseFloat(scope.PNoteTIOAST));
            	isNaN(parseFloat(scope.RAgreeTIOAST)) ? arrayTiOaSt.push(0) : arrayTiOaSt.push(parseFloat(scope.RAgreeTIOAST));
            	isNaN(parseFloat(scope.COATIOAST)) ? arrayTiOaSt.push(0) : arrayTiOaSt.push(parseFloat(scope.COATIOAST));
            	isNaN(parseFloat(scope.COPTIOAST)) ? arrayTiOaSt.push(0) : arrayTiOaSt.push(parseFloat(scope.COPTIOAST));
            	isNaN(parseFloat(scope.OTH1TIOAST)) ? arrayTiOaSt.push(0) : arrayTiOaSt.push(parseFloat(scope.OTH1TIOAST));
            	isNaN(parseFloat(scope.OTH2TIOAST)) ? arrayTiOaSt.push(0) : arrayTiOaSt.push(parseFloat(scope.OTH2TIOAST));
            	isNaN(parseFloat(scope.OTH3TIOAST)) ? arrayTiOaSt.push(0) : arrayTiOaSt.push(parseFloat(scope.OTH3TIOAST));

            	scope.TISUMOAST = scope.sum(arrayTiOaSt).toString();
            	
            }

            scope.SumOfTiOaLt = function(){
            	var arrayTiOaLt = [];
            	isNaN(parseFloat(scope.PNoteTIOALT)) ? arrayTiOaLt.push(0) : arrayTiOaLt.push(parseFloat(scope.PNoteTIOALT));
            	isNaN(parseFloat(scope.RAgreeTIOALT)) ? arrayTiOaLt.push(0) : arrayTiOaLt.push(parseFloat(scope.RAgreeTIOALT));
            	isNaN(parseFloat(scope.COATIOALT)) ? arrayTiOaLt.push(0) : arrayTiOaLt.push(parseFloat(scope.COATIOALT));
            	isNaN(parseFloat(scope.COPTIOALT)) ? arrayTiOaLt.push(0) : arrayTiOaLt.push(parseFloat(scope.COPTIOALT));
            	isNaN(parseFloat(scope.OTH1TIOALT)) ? arrayTiOaLt.push(0) : arrayTiOaLt.push(parseFloat(scope.OTH1TIOALT));
            	isNaN(parseFloat(scope.OTH2TIOALT)) ? arrayTiOaLt.push(0) : arrayTiOaLt.push(parseFloat(scope.OTH2TIOALT));
            	isNaN(parseFloat(scope.OTH3TIOALT)) ? arrayTiOaLt.push(0) : arrayTiOaLt.push(parseFloat(scope.OTH3TIOALT));

            	scope.TISUMOALT = scope.sum(arrayTiOaLt).toString();
            }

            scope.sum = function(array){
            	return (array.reduce((a,b) => a+b, 0)).toFixed(2);
            }

         
            scope.generateAISForm = function(){
                scope.hideTable = true;
                scope.hideAISReport = false;
                scope.hideHeader = false;

                var jsonData = {
                	"subReportParam": [
				        "SEC_AIS_FORM_PAGE_1",
				        "SEC_AIS_FORM_PAGE_2_1",
				        "SEC_AIS_FORM_PAGE_2_2",
				        "SEC_AIS_FORM_PAGE_3",
				        "SEC_AIS_FORM_ANNEX"
				    ],
				    "corpName":scope.CorpName,
                    "industryClass":scope.IndustryClass,
                    "principalAddress":scope.PrincipalAddr,
                    "aisYear":scope.AISYear.toString(),
                    "secRegNo":scope.SECRegNo,
                    "secCoa": scope.SECCOA,
                    "secRegDate":scope.SECRegDate,
                    "secAnivDate":scope.SECAnivDate,
                    "contactPerson":scope.ContactPerson,
                    "contactInformation":scope.ContactInformation,
                    "designation":scope.Designation,
                    "emailAddress": scope.EmailAddr,
                    "pnoteAtbiCurr":scope.PNoteATBICURR,
                    "pnoteAtbiSt":scope.PNoteATBIST,
                    "pnoteAtbiLt":scope.PNoteATBILT,
                    "ragreeAtbiCurr":scope.RAgreeATBICURR,
                    "ragreeAtbiSt":scope.RAgreeATBIST,
                    "ragreeAtbiLt":scope.RAgreeATBILT,
                    "coaAtbiCurr":scope.COAATBICURR,
                    "coaAtbiSt":scope.COAATBIST,
                    "coaAtbiLt":scope.COATIOALT,
                    "copAtbiCurr":scope.COPATBICURR,
                    "copAtbiSt":scope.COPATBIST,
                    "copAtbiLt":scope.COPATBILT,
                    "oth1Atbi": (scope.OTH1ATBI == "") ? "" : scope.OTH1ATBI,
                    "oth1AtbiCurr": (scope.OTH1ATBI == "") ? "" : scope.OTH1ATBICURR,
                    "oth1AtbiSt": (scope.OTH1ATBI == "") ? "" : scope.OTH1ATBIST,
                    "oth1AtbiLt": (scope.OTH1ATBI == "") ? "" : scope.OTH1ATBILT,
                    "oth2Atbi": (scope.OTH2ATBI == "") ? "" : scope.OTH2ATBI,
                    "oth2AtbiCurr": (scope.OTH2ATBI == "") ? "" : scope.OTH2ATBICURR,
                    "oth2AtbiSt": (scope.OTH2ATBI == "") ? "" : scope.OTH2ATBIST,
                    "oth2AtbiLt": (scope.OTH2ATBI == "") ? "" : scope.OTH2ATBILT,
                    "oth3Atbi": (scope.OTH3ATBI == "") ? "" : scope.OTH3ATBI,
                    "oth3AtbiCurr": (scope.OTH3ATBI == "") ? "" : scope.OTH3ATBICURR,
                    "oth3AtbiSt": (scope.OTH3ATBI == "") ? "" : scope.OTH3ATBIST,
                    "oth3AtbiLt": (scope.OTH3ATBI == "") ? "" : scope.OTH3ATBILT,
                    "atbiSumSt":scope.ATBISUMST, // to be computed or converted
                    "atbiSumLt":scope.ATBISUMLT,
                    "pnoteTiCurr":scope.PNoteTICURR,
                    "pnoteTiMonthSt":scope.PNoteTIMonthST,
                    "pnoteTiMonthLt":scope.PNoteTIMonthLT,
                    "pnoteTiOaSt":scope.PNoteTIOAST,
                    "pnoteTiOaLt":scope.PNoteTIOALT,
                    "ragreeTiCurr":scope.RAgreeTICURR,
                    "ragreeTiMonthSt":scope.RAgreeTIMonthST,
                    "ragreeTiMonthLt":scope.RAgreeTIMonthLT,
                    "ragreeTiOaSt":scope.RAgreeTIOAST,
                    "ragreeTiOaLt":scope.RAgreeTIOALT,
                    "coaTiCurr":scope.COATICURR,
                    "coaTiMonthSt":scope.COATIMonthST,
                    "coaTiMonthLt":scope.COATIMonthLT,
                    "coaTiOaSt":scope.COATIOAST,
                    "coaTiOaLt":scope.COATIOALT,
                    "copTiCurr":scope.COPTICURR,
                    "copTiMonthSt":scope.COPTIMonthST,
                    "copTiMonthLt":scope.COPTIMonthLT,
                    "copTiOaSt":scope.COPTIOAST,
                    "copTiOaLt":scope.COPTIOALT,
                    "oth1Ti": (scope.OTH1TI == "") ? "" : scope.OTH1TI,
                    "oth1TiCurr": (scope.OTH1TI == "") ? "" : scope.OTH1TICURR,
                    "oth1TiMonthSt": (scope.OTH1TI == "") ? "" : scope.OTH1TIMonthST,
                    "oth1TiMonthLt": (scope.OTH1TI == "") ? "" : scope.OTH1TIMonthLT,
                    "oth1TiOaSt": (scope.OTH1TI == "") ? "" : scope.OTH1TIOAST,
                    "oth1TiOaLt": (scope.OTH1TI == "") ? "" : scope.OTH1TIOALT,
                    "oth2Ti": (scope.OTH2TI == "") ? "" : scope.OTH2TI,
                    "oth2TiCurr": (scope.OTH2TI == "") ? "" : scope.OTH2TICURR,
                    "oth2TiMonthSt": (scope.OTH2TI == "") ? "" : scope.OTH2TIMonthST,
                    "oth2TiMonthLt": (scope.OTH2TI == "") ? "" : scope.OTH2TIMonthLT,
                    "oth2TiOaSt": (scope.OTH2TI == "") ? "" : scope.OTH2TIOAST,
                    "oth2TiOaLt": (scope.OTH2TI == "") ? "" : scope.OTH2TIOALT,
                    "oth3Ti": (scope.OTH3TI == "") ? "" : scope.OTH3TI,
                    "oth3TiCurr": (scope.OTH3TI == "") ? "" : scope.OTH3TICURR,
                    "oth3TiMonthSt": (scope.OTH3TI == "") ? "" : scope.OTH3TIMonthST,
                    "oth3TiMonthLt": (scope.OTH3TI == "") ? "" : scope.OTH3TIMonthLT,
                    "oth3TiOaSt": (scope.OTH3TI == "") ? "" : scope.OTH3TIOAST,
                    "oth3TiOaLt": (scope.OTH3TI == "") ? "" : scope.OTH3TIOALT,
                    "tiSumMonthSt":scope.TISUMMonthST,
                    "tiSumMonthLt":scope.TISUMMonthLT,
                    "tiSumOaSt": scope.TISUMOAST,
                    "tiSumOaLt": scope.TISUMOALT,
                    "undersignedOfficerLoc":scope.UnsignedOfficerLoc,
                    "presidentName": scope.PresidentName,
                    "treasurerName": scope.TreasurerName,
                    "aisDay": scope.AISDay.toString(),
                    "aisMonth": (new Date().getMonth() + 1).toString(),
                    "docNo": scope.SECDocNo,
                    "pageNo": scope.SECPageNo,
                    "bookNo": scope.SECBookNo,
                    "serialNo": scope.SECSerialNo,
                    "presidentSignature": scope.PresidentSignature,
                    "treasurerSignature": scope.TreasurerSignature,
                    "srcOption1":(scope.SRCOption1) ? "/" : "",
                    "srcOption2":(scope.SRCOption2) ? "/" : "",
                    'srcOption3':(scope.SRCOption3) ? "/" : "",
                    "srcOption4":(scope.SRCOption4) ? "/" : "",
                    'srcOption5':(scope.SRCOption5) ? "/" : ""
                }

                var reportURL = $rootScope.hostUrl + API_VERSION + "/cs_reports/PDF?file=SEC_AIS_FORM"; 

                reportURL = $sce.trustAsResourceUrl(reportURL);
                reportURL = $sce.valueOf(reportURL);
                http.post(reportURL, jsonData, {responseType: 'arraybuffer'}).
                    success(function(data, status, headers, config) {
	                    var contentType = headers('Content-Type');
	                    var file = new Blob([data], {type: contentType});
	                    var fileContent = URL.createObjectURL(file);

	                    // Pass the form data to the iframe as a data url.
	                   	scope.baseURL = $sce.trustAsResourceUrl(fileContent);
                });
		    }
    	}
	});
    mifosX.ng.application.controller('StaticController', ['$scope', '$routeParams', 'ResourceFactory', '$location', '$route', '$rootScope', '$http', '$sce', 'API_VERSION', mifosX.controllers.StaticController]).run(function ($log) {
        $log.info("StaticController initialized");
    });
}(mifosX.controllers || {}));