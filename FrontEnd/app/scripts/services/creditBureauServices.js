(function (module) {
    mifosX.services = _.extend(module, {

        creditBureauServices: function () {
          var negEvents = {};
          var createCreditBureauData = function(lastname, firstname){
            var data = {};
            data.firstName = "";
            data.middleName = "";
            data.lastName = "";
            data.gender = "";
            data.dateofbirth = "";
            data.civilstatus = "";
            data.nationality = "";
            var min=0; 
            var max=7;
            var ranNum = randomNumberGenerator(max, min);
            data.negativeEvents = [];
            var ctr;
            for(ctr = 0; ctr < ranNum; ctr++){
              var negativeEvent = {};
              var minPicker=1; 
              var maxPicker=3;
              var picker = cbPicker(randomNumberGenerator(maxPicker, minPicker));
              negativeEvent.provider = picker;
              var desc = descriptionPicker();
              negativeEvent.description = desc;
              negativeEvent.detail = desc.toUpperCase();
              var date = randomDate(new Date(2012, 0, 1), new Date(2017, 11, 31));
              negativeEvent.eventdate = date;
              negativeEvent.status = "AC";
              negativeEvent.statusdate = date;
              data.negativeEvents.push(negativeEvent);
            }

            var tempNegEvent = [];
            var fullName = lastname + '.' + firstname;
            if (typeof negEvents[fullName] === 'undefined') {
                // undefined
            } else {
                tempNegEvent = negEvents[fullName];
            }                
            negEvents[fullName] = data.negativeEvents.concat(tempNegEvent);
            data.negativeEvents = negEvents[fullName]

            return data;
          }
		  
          var randomNumberGenerator = function(max, min){
            return Math.floor(Math.random() * (+max - +min)) + +min;
          }

          var descriptionPicker = function(){
            var minPicker=1; 
            var maxPicker=9;
            var picker = randomNumberGenerator(maxPicker, minPicker);
            switch(picker){
              case 1:
                return "Fraud"
              case 2:
                return "Late Payment"
              case 3:
                return "Charge-Off"
              case 4:
                return "Debt Settlement"
              case 5:
                return "Bankruptcy"
              case 6:
                return "Foreclosures"
              case 7:
                return "Law Suit"
              case 8:
                return "Wage Garnishment"
            }
          }

          var randomDate = function(start, end) {
            var randDate = new Date(start.getTime() + Math.random() * (end.getTime() - start.getTime()));
              return (randDate.getMonth() + 1) + "/" + randDate.getDate() + "/" + randDate.getFullYear();
          }
		  
		  
		  var cbPicker = function(num){
			  switch(num){
				  case 1:
					  return "CIC"
				  case 2:
					  return "CIBI"
				  case 3:
					  return "CRIF"
			  }
		  }
          
          return{
            createCreditBureauData:createCreditBureauData,
          }
        }
    });

    mifosX.ng.services.service('creditBureauServices', [mifosX.services.creditBureauServices]).run(function ($log) {
        $log.info("creditBureauServices initialized");

    });

}(mifosX.services || {}));
