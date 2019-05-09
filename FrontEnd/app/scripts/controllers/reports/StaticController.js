(function (module) {
    mifosX.controllers = _.extend(module, {
        StaticController: function (scope, routeParams, resourceFactory, location, route, http) {
           scope.getCurrentDocument = function() {
           	   scope.isVisible = false;

			   	if(scope.currentDocument.includes(".pdf")){
			   		scope.isVisible = scope.isVisible = true;
			   		scope.pdf = {
			   			src: '../../../static/' + scope.currentDocument,
			   		};
			   	}
			   	else {
			   	 	var link = document.createElement("a");
    				link.href = '../../../static/' + scope.currentDocument;
    				link.style = "visibility:hidden";
    				//link.download = '../../../static' + scope.currentDocument;
    				document.body.appendChild(link);
				    link.click();
				    document.body.removeChild(link);
			   	}

		   		
		   };
		   /*
		   scope.pdf = {
		        src: '../../../static/SEC-Form-LCFS1.pdf',
		   };*/


		   /* START AIS scope variables */

		   	scope.SEC = "SECURITIES AND EXCHANGE COMMISSION";
            scope.AIS ="ANNUAL INFORMATION STATEMENT";
            scope.AISDay = new Date().getDay();
            scope.AISMonth = new Date().getMonth();
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
            scope.PNoteATBIST = "0.00";
            scope.PNoteATBILT = "0.00";
            scope.RAgreeATBICURR = "PHP";
            scope.RAgreeATBIST = "0.00";
            scope.RAgreeATBILT = "0.00";
            scope.COAATBICURR = "PHP";
            scope.COAATBIST = "0.00";
            scope.COAATBILT = "0.00";
            scope.COPATBICURR = "PHP";
            scope.COPATBIST = "0.00";
            scope.COPATBILT = "0.00";
            scope.OTH1ATBI = "Others";
            scope.OTH1ATBICURR = "PHP";
            scope.OTH1ATBIST = "0.00";
            scope.OTH1ATBILT = "0.00";
            scope.OTH2ATBI = "Others";
            scope.OTH2ATBICURR = "PHP";
            scope.OTH2ATBIST = "0.00";
            scope.OTH2ATBILT = "0.00";
            scope.OTH3ATBI = "Others";
            scope.OTH3ATBICURR = "PHP";
            scope.OTH3ATBIST = "0.00";
            scope.OTH3ATBILT = "0.00";
            scope.ATBISUMST = "0.00";
            scope.ATBISUMLT = "0.00";
            scope.PNoteTICURR="PHP";
            scope.PNoteTIMonthST = "0.00";
            scope.PNoteTIMonthLT = "0.00";
            scope.PNoteTIOAST = "0.00";
            scope.PNoteTIOALT = "0.00";
            scope.RAgreeTICURR="PHP";
            scope.RAgreeTIMonthST = "0.00";
            scope.RAgreeTIMonthLT = "0.00";
            scope.RAgreeTIOAST = "0.00";
            scope.RAgreeTIOALT = "0.00";
            scope.COATICURR="PHP";
            scope.COATIMonthST = "0.00";
            scope.COATIMonthLT = "0.00";
            scope.COATIOAST = "0.00";
            scope.COATIOALT = "0.00";
            scope.COPTICURR="PHP";
            scope.COPTIMonthST = "0.00";
            scope.COPTIMonthLT = "0.00";
            scope.COPTIOAST = "0.00";
            scope.COPTIOALT = "0.00";
            scope.OTH1TI = "Others";
            scope.OTH1TICURR="PHP";
            scope.OTH1TIMonthST = "0.00";
            scope.OTH1TIMonthLT = "0.00";
            scope.OTH1TIOAST = "0.00";
            scope.OTH1TIOALT = "0.00";
            scope.OTH2TI = "Others";
            scope.OTH2TICURR="PHP";
            scope.OTH2TIMonthST = "0.00";
            scope.OTH2TIMonthLT = "0.00";
            scope.OTH2TIOAST = "0.00";
            scope.OTH2TIOALT = "0.00";
            scope.OTH3TI = "Others";
            scope.OTH3TICURR="PHP";
            scope.OTH3TIMonthST = "0.00";
            scope.OTH3TIMonthLT = "0.00";
            scope.OTH3TIOAST = "0.00";
            scope.OTH3TIOALT = "0.00";
            scope.TISUMMonthST = "0.00";
            scope.TISUMMonthLT = "0.00";
            scope.TISUMOAST = "0.00";
            scope.TISUMOALT = "0.00";
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
            /* END AIS scope variables */


            // add variable for signature of president and treasurer, unsigned_officer_Loc, compute function for summation, checkbox replace of true with / ;

            // function generate/call jaspersoft report generation 
            // prepare json request.

            // to update http post 
            scope.generateAISForm = function(){

                $http.post("url",{
                    "corpName":scope.CorpName,
                    "industryClass":scope.IndustryClass,
                    "principalAddress":scope.principalAddress,
                    "aisYear":scope.AISYear,
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
                    "oth3AtbiSt": (scope.OTH3ATBI == "") ? "" : scope.OTH3ABIST,
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
                    "undersignedOfficerLoc":scope.UnsignedOfficerLoc,
                    "presidentName": scope.PresidentName,
                    "treasurerName": scope.TreasurerName,
                    "aisDay": scope.AISDay,
                    "aisMonth": scope.AISMonth,
                    "docNo": scope.DocumentNumber,
                    "pageNo": scope.PageNumber,
                    "bookNo": scope.BookNumber,
                    "serialNo": scope.SerialNumber,
                    "presidentSignature": scope.PresidentSignature,
                    "treasurerSignature": scope.TreasurerSignature,
                    "srcOption1":(scope.SRCOption1) ? "/" : "",
                    "srcOption2":(scope.SRCOption2) ? "/" : "",
                    "srcOption3":(scope.SRCOption3) ? "/" : "",
                    "srcOption4":(scope.SRCOption4) ? "/" : "",
                    "srcOption5":(scope.SRCOption5) ? "/" : ""
                    
                });


            }
            
        }
    });
    mifosX.ng.application.controller('StaticController', ['$scope', '$routeParams', 'ResourceFactory', '$location', '$route', mifosX.controllers.StaticController]).run(function ($log) {
        $log.info("StaticController initialized");
    });
}(mifosX.controllers || {}));