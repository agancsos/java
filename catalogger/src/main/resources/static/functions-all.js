var menu = null;


// https://dev.to/iamafro/how-to-create-a-custom-context-menu--5d7p
/////////////////////////////////////////////////////////////////////////
const toggleMenu = command => {
	if (menu) {
  		menu.style.display = command === "show" ? "block" : "none";
	}
};

const setPosition = ({ top, left }) => {
	if (menu) {
  		menu.style.left = `30%`;
  		menu.style.top = `${top}px`;
  		toggleMenu("show");
	}
};

function setOverlay() {
	document.getElementById("overlay").style.hidden = false;
	document.getElementById("overlay").style.zIndex = "99";
}

function removeOverlay() {
	document.getElementById("overlay").style.hidden = true;
	document.getElementById("overlay").style.zIndex = "0";
	document.getElementById("loader").style.hidden = true;
	document.getElementById("loader").style.display = "none";
}

function showMenuFor(el, e) {
	if (menu) { return; }
	menu = document.querySelector("#context-menu-" + el.id);
	if (menu) {
		setPosition({left: el.pageX, top: el.pageY});
	}
	if (e) {
		e.stopPropagation();
	}
	return false;
}

function changeTimerType(el) {
	const type     = el.value;
    const payload  = { "type":type, "op":"schedule" };
    var form = document.createElement('form');
    form.style.visibility = 'hidden';
    form.method = 'POST';
    $.each(Object.keys(payload), function(index, key) {
        var input = document.createElement('input');
        input.name = key;
        input.value = payload[key];
        form.appendChild(input)
    });
    document.body.appendChild(form);
    form.submit();
}

function changeScheduleType(el) {
    const type     = el.value;
    const payload  = { "scheduleType":type, "op":"schedule" };
    var form = document.createElement('form');
    form.style.visibility = 'hidden';
    form.method = 'POST';
    $.each(Object.keys(payload), function(index, key) {
        var input = document.createElement('input');
        input.name = key;
        input.value = payload[key];
        form.appendChild(input)
    });
    document.body.appendChild(form);
    form.submit();
}

window.addEventListener("click", e => {
	if(menu) {
		toggleMenu("hide");
		const type     = e.target.childNodes[0].data;
		var pair     = e.target.id.replace("menu-option-", "").split("-");
		if (pair.length != 2) { return; }
		const op       = pair[0];
		const key      = pair[1]; 
		const payload  = { "type":type, "key":key, "op":op };
		var form = document.createElement('form');
		form.style.visibility = 'hidden';
		form.method = 'POST';
		form.action = "./home?sel_id=" + key;
		$.each(Object.keys(payload), function(index, key) {
			var input = document.createElement('input');
    		input.name = key;
    		input.value = payload[key];
    		form.appendChild(input)
		});
		document.body.appendChild(form);
		form.submit();
		menu = null;
	}
});

window.addEventListener("contextmenu", e => {
  	e.preventDefault();
	e.stopPropagation();
  	return false;
});
/////////////////////////////////////////////////////////////////////////////////////////

// https://www.w3schools.com/html/html5_draganddrop.asp
/////////////////////////////////////////////////////////////////////////////////////////
function startDrag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function allowDrop(ev) {
    ev.preventDefault();
}

function endDrag(ev) {
	var form        = document.createElement('form');
	const sourceKey = ev.dataTransfer.getData("text");
	const targetKey = ev.target.id; 
	const payload  = { "type":"move", "sourceKey":sourceKey, "targetKey": targetKey, "op":"object" };
    form.style.visibility = 'hidden';
    form.method = 'POST';
    form.action = "./home.php?sel_id=" + sourceKey;
    $.each(Object.keys(payload), function(index, key) {
        var input = document.createElement('input');
        input.name = key;
        input.value = payload[key];
        form.appendChild(input)
    });
    document.body.appendChild(form);
    form.submit();
    ev.stopPropagation();
}

function startTaskDrag(ev) {
    ev.dataTransfer.setData("text", ev.target.id);
}

function allowTaskDrop(ev) {
    ev.preventDefault();
}

function endTaskDrag(ev) {
    var form              = document.createElement('form');
    const taskType        = ev.dataTransfer.getData("text");
	const sourceKey       = taskType.split("_")[1]
    const payload         = { "type":"add", "@type":taskType.split("_")[0], "op":"payload", "op2":"PAYLOAD" };
    form.style.visibility = 'hidden';
    form.method = 'POST';
    form.action = "./home.php?sel_id=" + sourceKey;
    $.each(Object.keys(payload), function(index, key) {
        var input = document.createElement('input');
        input.name = key;
        input.value = payload[key];
        form.appendChild(input)
    });
    document.body.appendChild(form);
    form.submit();
    ev.stopPropagation();
}

/////////////////////////////////////////////////////////////////////////////////////////

document.addEventListener("load", e => {
	e.stopPropagation();
	return false;
});


function goto(path) {
	window.location = path;
}

function copyText(text) {
	alert(text);
}

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
	},

	removeFields: function(prefix) {
		let tags = ["input", "label", "select"];
		for (const i in tags) {
			var fields = document.getElementsByTagName(tags[i]);
			for(const j in fields) {
				var field = fields[j];
				if (field == null) { continue; }
				if (field.remove) {
					field.remove();
				}
			}
		}
	}, 

	removeForm: function(id) {
		var form = document.getElementById(id);
		if (form) {
			form.remove();
		}
		var script = document.getElementById('reload-script');
		if (script) {
			script.remove();
		}
	},

	setText: function(id, text) {
		var div = document.getElementById(id);
		div.innerHTML = text;
		var script = document.getElementById('reload-script2');
        script.remove();
	},

	setValue: function(id, value) {
		var el1 = document.getElementById(id);
		el1.value = value;
	}
}


