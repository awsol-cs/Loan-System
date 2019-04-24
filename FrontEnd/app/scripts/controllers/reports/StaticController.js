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



        }
    });
    mifosX.ng.application.controller('StaticController', ['$scope', '$routeParams', 'ResourceFactory', '$location', '$route', mifosX.controllers.StaticController]).run(function ($log) {
        $log.info("StaticController initialized");
    });
}(mifosX.controllers || {}));