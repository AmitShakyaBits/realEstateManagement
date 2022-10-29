$(document).ready(function() {
	var currentPath = $(location).attr('pathname');
	if (!currentPath.includes('home')) {
		console.log('Checking if this is active session!')
		isUserLoggedIn();
	}
});

$(document).ready(function() {
	listHelper();
});

$('#search--form').click(function() {
	search();
	$('.box-collapse > span')[0].click();
	return false;
});


function search() {
	var keywordInput = document.getElementById('search--keyword');
	var cityInput = document.getElementById('search--city');
	var typeInput = document.getElementById('search--type');
	var numBedInput = document.getElementById('search--num--beds');
	var params = {};
	if (keywordInput.value)
		params.propertyName = keywordInput.value;
	if (cityInput.value)
		params.propertyCity = cityInput.value;
	if (typeInput.value)
		params.propertyType = typeInput.value;
	if (numBedInput.value)
		params.propertyBed = parseInt(numBedInput.value);
	$('#no--search--info').attr('style', 'display:');
	var url = "/property/search";
	setTimeout(function() {
		$.when(httpPost(url, params, true)).then(function(response) {
			removeOldElementsWithoutRefresh();
			listProperties(response);

		});
	});
}

function removeOldElementsWithoutRefresh() {
	var regex = "div[id*='child--element'i]";
	const elements = [document.querySelectorAll(regex)];
	console.log(elements.length);
	for (let i = 0; i < elements[0].length; i++) {
		elements[0][i].remove();
	}
}

function book(propertyId, book_btn) {
	var params = {};
	params.propertyId = propertyId;
	params.clientId = getUserId();
	params.bookingStatus = "O";
	var statusModal = document.getElementById('booking--status');

	var url = "/bookings/";
	setTimeout(function() {
		response = httpPost(url, params, true);
		if (response.errorCode == 0) {
			window.setTimeout(function() {
				statusModal.innerHTML = 'Booked successfully.'
				$('#booking--confirm--modal').modal('show');
			}, 1000);
		} else {

			window.setTimeout(function() {
				statusModal.innerHTML = 'Booking failed due to ' + response.error;
				$('#booking--confirm--modal').modal('show');
			}, 1000);
		}
	});
}

$('#my--transactions').click(function() {
	listTransactions();
	$('#show--transcation--modal').modal('show')
	return false;
});

function listTransactions() {
	var userId = getUserId();
	var params = {};
	params.clientId = userId;
	var url = "/transaction/list";
	var promise = httpPostAsync(url, params);
	promise.then(function(response) {
		var table = document.getElementById('transaction--table');
		$('#transaction--table > tr').remove();
		$.each(response,
			function(idx, currentData) {
				var tr;
				if (currentData.transactionId) {
					tr = document.createElement('tr');
					var td1 = document.createElement('td');
					var td2 = document.createElement('td');
					var td3 = document.createElement('td');
					var td4 = document.createElement('td');
					var td5 = document.createElement('td');
					var td6 = document.createElement('td');
					td1.innerText = currentData.transactionId;
					td2.innerText = currentData.bookingId;
					td3.innerText = currentData.propertyName;
					td4.innerText = currentData.propertyType;
					td5.innerText = currentData.propertyCategory;
					td6.innerText = 'INR ' + formatNumber(currentData.propertyPrice);
					table.appendChild(tr);
					tr.appendChild(td1);
					tr.appendChild(td2);
					tr.appendChild(td3);
					tr.appendChild(td4);
					tr.appendChild(td5);
					tr.appendChild(td6);

				}

			});
	});
}

function listProperties(data) {
	var currentIndex = 1;
	if (data.length == 0) {
		$('#no--search--info').attr('style', 'display:block');
		return false;
	}
	$.each(data,
		function(idx, currentData) {
			var baseElement = document.getElementById("base--element");
			var cloneElement = baseElement.cloneNode(true);
			cloneElement.id = "child--element" + currentIndex;
			baseElement.parentNode.appendChild(cloneElement);
			var childElement = document.getElementById("child--element" + currentIndex);
			var image = childElement.getElementsByClassName("property--image")[0];
			var address = childElement.getElementsByClassName("property--address")[0];
			var area = childElement.getElementsByClassName("property--area")[0];
			var bed = childElement.getElementsByClassName("property--bed")[0];
			var price = childElement.getElementsByClassName("property--price")[0];
			var city = childElement.getElementsByClassName("property--City")[0];
			var imageIndex = (currentIndex % 10) + 1;
			image.src = "assets/img/property-" + imageIndex + ".jpg";
			address.innerHTML = currentData.propertyName;
			area.innerHTML = currentData.propertyArea + "ft <sup>2</sup>";
			price.innerHTML = currentData.propertyCategory + " | " + currentData.propertyType + " | INR " + formatNumber(currentData.propertyPrice);
			bed.innerHTML = currentData.propertyBed;
			city.innerHTML = currentData.propertyCity;
			childElement.setAttribute('style', 'display:block');
			var book_btn = childElement.getElementsByClassName("link-a badge");
			childElement.addEventListener("click", function() {
				book(currentData.propertyId, book_btn);
			});
			if (currentData.propertyStatus === 'B') {
				$(book_btn).html('Booked');
				/*$(book_btn).attr('disabled', true);*/
			}
			currentIndex = currentIndex + 1;

		});
}
function listHelper(data) {
	var url = "/property";
	var promise = httpGetAsync(url);

	promise.then(function(response) {
		var data = JSON.parse(response);
		listProperties(data);

	});
}
