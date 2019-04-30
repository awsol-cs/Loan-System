(function (module) {
    mifosX.controllers = _.extend(module, {
        StaticController: function (scope, routeParams, resourceFactory, location, route) {
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
            /* END AIS scope variables */
        }
    });
    mifosX.ng.application.controller('StaticController', ['$scope', '$routeParams', 'ResourceFactory', '$location', '$route', mifosX.controllers.StaticController]).run(function ($log) {
        $log.info("StaticController initialized");
    });
}(mifosX.controllers || {}));