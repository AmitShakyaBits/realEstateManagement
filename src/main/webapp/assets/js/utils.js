function httpGet(url) {
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("GET", url, false); // false for synchronous request
	xmlHttp.send(null);
	return xmlHttp.responseText;
}

function httpDelete(url) {
	var xmlHttp = new XMLHttpRequest();
	xmlHttp.open("DELETE", url, false); // false for synchronous request
	xmlHttp.send(null);
	return xmlHttp.responseText;
}


function createCookie(cookieName, cookieValue, daysToExpire, path) {
	var date = new Date();
	date.setTime(date.getTime() + (daysToExpire * 24 * 60 * 60 * 1000));
	document.cookie = cookieName + "=" + cookieValue + "; path=/";
}

function deleteCookie(cookieName) {
	document.cookie = cookieName + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function httpPost(url, params, nJsonResponse) {
	var response;
	var json = JSON.stringify(params);
	var xhr = new XMLHttpRequest();
	xhr.open("POST", url, false);
	xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
	xhr.onload = function() {
		if ((xhr.readyState == 4 && xhr.status == "200") || (xhr.readyState == 4 && xhr.status == "201")) {
			if (nJsonResponse) {
				response = xhr.responseText;
				if (response != undefined) {
					response = response.trim();
					response = response.replace(/[\u{0080}-\u{FFFF}]/gu, "");
					if (IsJsonString(response))
						response = JSON.parse(response);
				}
			}
		} else {
			response = "";
		}
	}
	xhr.send(json);
	return response;
}

httpPostAsync = async (url, params) => {
	const settings = {
		method: 'POST',
		body: JSON.stringify(params),
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
		}
	};
	try {
		const fetchResponse = await fetch(url, settings);
		const data = await fetchResponse.json();
		return data;
	} catch (e) {
		console.log(e);
		return e;
	}
}

httpGetAsync = async (url, nJson) => {
	var data;
	const settings = {
		method: 'GET',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
		}
	};
	try {
		const fetchResponse = await fetch(url);
		if (nJson)
			data = await fetchResponse.json();
		else
			data = await fetchResponse.text();

		/*console.log(data);*/
		return data;
	} catch (e) {
		console.log(e);
		return e;
	}
}

httpDeleteAsync = async (url, nJson) => {
	alert('This is delete call!');
	var data;
	const settings = {
		method: 'DELETE',
		headers: {
			Accept: 'application/json',
			'Content-Type': 'application/json',
		}
	};
	try {
		const fetchResponse = await fetch(url);
		if (nJson)
			data = await fetchResponse.json();
		else
			data = await fetchResponse.text();

		
		return data;
	} catch (e) {
		console.log(e);
		return e;
	}
}

function formatNumber(number) {
	let commas = -1;
	for (let n3 = 3; n3 <= 12; n3 += 3) {
		commas++;
		const max = Math.pow(10, n3);
		if (number < max) {
			let numbers = String(number).split('');
			for (let i = 0; i < commas; i++) {
				numbers.splice(-(3 * (i + 1) + i), 0, ',');
			}
			return numbers.join('');
		}
	}

	throw new Error(`number: ${number} is too big`);
}

function IsJsonString(str) {
	try {
		var json = JSON.parse(str);
		return (typeof json === 'object');
	} catch (e) {
		return false;
	}
}

function createCookie(cookieName, cookieValue, daysToExpire, path) {
	var date = new Date();
	date.setTime(date.getTime() + (daysToExpire * 24 * 60 * 60 * 1000));
	document.cookie = cookieName + "=" + cookieValue + "; path=/";
}

function deleteCookie(cookieName) {
	document.cookie = cookieName + '=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;';
}

function getQueryParam(param) {
	var value;
	let searchParams = new URLSearchParams(window.location.search)
	if (searchParams.has(param)) {
		value = searchParams.get(param);
	}
	return value;
}

function getCookieValue(a) {
	var b = document.cookie.match('(^|;)\\s*' + a + '\\s*=\\s*([^;]+)');
	return b ? b.pop() : '';
}

function getUserId() {
	try {
		var t = getCookieValue('userinfo');
		t = t.split(/####/g);
		if (t[1] == undefined) {
			throw exception();
		}
		return t[1];
	} catch (e) {
		return undefined;
	}
}

function getUserEmailAddress() {
	try {
		var t = getCookieValue('userinfo');
		t = t.split(/####/g);
		if (t[0] == '') {
			throw exception();
		}
		return t[0];
	} catch (e) {
		return undefined;
	}
}

function isUserLoggedIn() {
	var emailAddress = getUserEmailAddress();
	if (emailAddress == undefined) {
		$('#login--confirm--modal').modal('show');
	}
}

$('#login--confirm--button').click(function() {
	window.location.href = "login";
	return false;
});

