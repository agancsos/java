var RestHelpers = {
	invokeGet: function(path) {
		var curl = new XMLHttpRequest();
		var rst  = null;
		curl.onreadystatechange = function() {
			if (this.readyState == 4 && this.status == 200) {
				rst = JSON.parse(this.responseText);
			}
		};
		curl.open("GET", path, false);
		curl.setRequestHeader("Content-type", "application/json");
		curl.send();
		return rst;
	},

	invokePost: function(path, body) {
		var curl = new XMLHttpRequest();
        var rst  = null;
        curl.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                rst = JSON.parse(this.responseText);
            }
        };
        curl.open("POST", path, false);
        curl.setRequestHeader("Content-type", "application/json");
        curl.send(JSON.stringify(body));
        return rst;
	},
};

var FormHelpers = {
	getQueryParameters: function(url) {
		var rst   = {};
		var query = url.href.split("?");
		if (query.length == 1) {
			return {};
		}
		var pairs = query[1].split("&");
		for(var pair in pairs) {
			var comps = pairs[pair].split("=");
			rst[comps[0]] = comps[1];
		}
		return rst;
	},

	submitVote: function(e) {
		let pollId   = e.originalTarget.pollId;
		let optionId = e.originalTarget.id;
		let clientIP = RestHelpers.invokeGet("https://ipinfo.io").ip;
		RestHelpers.invokePost("http://localhost:8080/api/votes/add", {
			"pollId": pollId,
			"optionId": optionId,
			"sourceIp": clientIP"
		});
	},

	submitPoll: function(e) {
		let text     = document.getElementById("text").innerText;
		let options  = document.getElementById("option").innerText.split("\n");
		let options2 = {};
		for (let i in options) {
			options2[i] = options[i];
		}
		RestHelpers.invokePost("http://localhost:8080/api/polls/add", {
			"text": text,
			"options": options2
		});  
	},
};

