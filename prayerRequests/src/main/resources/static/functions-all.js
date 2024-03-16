var ViewService = {
	gotoPage: function(page) {
		window.location = page;
	},
	
	logError: function(message, print=false) {
		console.error(message);
		if (print == true) { alert(message); }
	},

	logWarning: function(message, print=false) {
		console.warn(message);
		if (print == true) { alert(message); }
	},
	
	log: function(message, print=false) {
		console.log(message);
		if (print == true) { alert(message); }
	}
}

