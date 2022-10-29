$('#registerPropertyButton').click(function() {
	addProperty();
	return false;
});

$(document).ready(function() {
	console.log('Checking if this is active session!')
	isUserLoggedIn();
});


function addProperty() {

	var name = document.getElementById("property--name").value;
	var street = document.getElementById("property--street").value;
	var city = document.getElementById("property--city").value;
	var state = document.getElementById("property--state").value;
	var zipcode = document.getElementById("property--zipcode").value;
	var country = document.getElementById("property--country").value;
	var areainsft = document.getElementById("property--area").value;
	var price = document.getElementById("property--price").value;
	var numBeds = document.getElementById("property--beds").value;
	var description = document.getElementById("property--description").value;
	/*var availableFrom = document.getElementById("property--available--from").value;*/
	var category = document.querySelector("input[type='radio'][name=property--category]:checked").value;
	var type = document.querySelector("input[type='radio'][name=property--type]:checked").value;

	var submit = document.getElementById("registerPropertyButton");
	var status = document.getElementById("register--status");
	status.setAttribute('style', 'display:block');
	status.innerHTML = "Please wait <i class='fa fa-spinner'></i>";

	var params = {};
	params.propertyName = name;
	params.propertyTown = street;
	params.propertyCity = city;
	params.propertyState = state;
	params.propertyCategory = category;

	params.propertyType = type;
	params.propertyPrice = price;
	params.propertyArea = areainsft;
	params.propertyBed = numBeds;

	params.propertyZipCode = zipcode;
	params.propertyDescription = description;
	/*params.propertyAvailableDate = availableFrom;*/
	params.propertyCountry = country;
	//Read current logged in userId from cookie
	params.propertyOwnerId = getUserId();

	var url = "/property/register";
	setTimeout(function() {
		$.when(httpPost(url, params, true)).then(function(response) {
			//submit.innerHTML = 'Signup';
			if (response.errorCode == 0) {
				window.setTimeout(function() {
					status.innerHTML = "Property is registered successfully."

				}, 1000);
			} else {

				window.setTimeout(function() {
					status.innerHTML = response.error;

				}, 1000);
			}
		});
	});
}