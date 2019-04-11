(function (module) {
    mifosX.controllers = _.extend(module, {
        StaticController: function (scope, routeParams, resourceFactory, location, route) {
            
        }
    });
    mifosX.ng.application.controller('StaticController', ['$scope', '$routeParams', 'ResourceFactory', '$location', '$route', mifosX.controllers.StaticController]).run(function ($log) {
        $log.info("StaticController initialized");
    });
}(mifosX.controllers || {}));