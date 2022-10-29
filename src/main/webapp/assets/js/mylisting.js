$(document).ready(function() {
	var currentPath = $(location).attr('pathname');
	if (!currentPath.includes('home')) {
		console.log('Checking if this is active session!')
		isUserLoggedIn();
	}
});

$('#delete--confirm--button').click(function() {
	$('#delete--confirm--modal').modal('hide');
	var propertyId = this.getAttribute('currentPropertyId');
	del(propertyId);
	return false;
});

function deleteListing(element) {
	var currentId = element.getAttribute('propertyid');
	var confirmOption = document.getElementById('delete--confirm--button');
	confirmOption.setAttribute('currentPropertyId', currentId);
	$('#delete--confirm--modal').modal('show');
	return false;
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

/*function del(propertyId) {
	var rawPropertyId = atob(propertyId);
	var url = "/property/delete/" + rawPropertyId;
	var promise = httpDeleteAsync(url);
	promise.then(function(response) {
		location.reload(true);
	});

}*/

function del(propertyId) {
	var rawPropertyId = atob(propertyId);
	var url = "/property/delete/" + rawPropertyId;
	setTimeout(function() {
		$.when(httpDelete(url)).then(function(response) {
			console.log(response);
			location.reload(true);
		}, 1000);
	});

}



$('#user--logout').click(function() {
	logout();
	return false;
});

$('#user--logout').click(function() {
	logout();
	return false;
});

function book(propertyId, book_btn) {
	var params = {};
	params.propertyId = propertyId;
	params.clientId = getUserId();
	params.bookingStatus = "O";

	var url = "/bookings/";
	setTimeout(function() {
		response = httpPost(url, params, true);
		console.log(response)
		if (response.errorCode == 0) {
			window.setTimeout(function() {
				alert("Booking done successfully")
				//book_btn.innerHTML = "Booked"

			}, 1000);
		} else {

			window.setTimeout(function() {
				alert(response.error);

			}, 1000);
		}
	});
}

function mylisting() {
	var ownerId = getUserId();
	var url = "/property/owner/" + ownerId;
	var promise = httpGetAsync(url);
	var currentIndex = 1;
	promise.then(function(response) {
		var data = JSON.parse(response);
		if (data.length == 0) {
			$('#no--listing--info').attr('style','display:block')
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
				var imageIndex = (currentIndex % 10) + 1;
				var deleteOption = childElement.getElementsByClassName('delete--listing')[0];
				var city = childElement.getElementsByClassName("property--City")[0];
				deleteOption.setAttribute('propertyId', btoa(currentData.propertyId));
				image.src = "assets/img/property-" + imageIndex + ".jpg";
				address.innerHTML = currentData.propertyName;
				area.innerHTML = currentData.propertyArea + "ft <sup>2</sup>";
				price.innerHTML = currentData.propertyCategory + " | " + currentData.propertyType + " | INR " + formatNumber(currentData.propertyPrice);
				bed.innerHTML = currentData.propertyBed;
				city.innerHTML = currentData.propertyCity;
				childElement.setAttribute('style', 'display:block');
				/*var book_btn = childElement.getElementsByClassName("link-a badge");
				childElement.addEventListener("click", function() {
					book(currentData.propertyId, book_btn);
				});
				if (currentData.propertyStatus === 'B') {
					$(book_btn).html('Booked');
					$(book_btn).attr('disabled', true);
				}*/
				currentIndex = currentIndex + 1;
			});

	});
}




$(document).ready(function() {
	mylisting();
});