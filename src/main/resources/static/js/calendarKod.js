$(document).ready(function(){
  $(".day-wrapper").slick({
  infinite: false,
  speed: 300,
  slidesToShow: 7,
  nextArrow: $("#next"),
  prevArrow: $("#prev")
  });
});

var url = new URL(window.location.href);
var query_string = url.search;
var search_params = new URLSearchParams(query_string);
var urlDate = search_params.get('date');



var dayNArray = new Array("Sun","Mon","Tue","Wed","Thu","Fri","Sat");
var monthArray = new Array("Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec");
var currDate = new Date();
var date = new Date();
var dateInOneYear = new Date();
dateInOneYear.setDate(date.getDate() + 365);
var date2=new Date();
var d= new Date();

if(urlDate !== ""){
	var tab = urlDate.split('-');
	date.setFullYear(parseInt(tab[2]));
	date.setMonth(parseInt(tab[1])-1);
	date.setDate(parseInt(tab[0]));

	if(date > dateInOneYear){
		date = dateInOneYear;
	}
	document.getElementById("date").innerHTML= date.toDateString();
}

var forward = 0;
var ifForwardLast = false;

for (var i=0;i<dayNArray.length;i++){

	date2.setDate(d.getDate() + i);
	var currDayOfWeek = date2.getDay();
	var currMonth = date2.getMonth();

	$('#dayN'+i).html(dayNArray[currDayOfWeek]);
	console.log("name "+dayNArray[currDayOfWeek]);

	$('#day'+i).html(date2.getDate());
	console.log("day "+date2.getDate());

	$('#month'+i).html(monthArray[currMonth]);
	console.log("mth  "+monthArray[currMonth]);
	$('#year'+i).html(date2.getFullYear());
}

function moveForward(){
	console.log("mF "+forward);

	if(forward < 52){
		if(ifForwardLast==false){
			forward++;
		}
		for (var i=0;i<dayNArray.length;i++){

		var d =   currDate;

	 	var dd = d.getDate();
	    var mm = d.getMonth();
	    var yyyy = d.getFullYear();
	    var auxDate= new Date(yyyy, mm, dd + (7*forward) + i);

		var currDay = auxDate.getDay();
		var currMonth = auxDate.getMonth();

		$('#dayN'+i).html(dayNArray[currDay]);
		$('#day'+i).html(auxDate.getDate());
		$('#month'+i).html(monthArray[currMonth]);
		$('#year'+i).html(auxDate.getFullYear());
		}

		forward++;
		ifForwardLast = true;
	}
}

function moveBackward(){
	console.log("Mb "+forward);

	if(forward>0){

		if(ifForwardLast == true){
			forward--;
		}

		forward --;

		for (var i=0;i<dayNArray.length;i++){
			var d =  currDate;
			var dd = d.getDate();
			var mm = d.getMonth();
			var yyyy = d.getFullYear();
			var auxDate= new Date(yyyy, mm, dd + (7*forward ) + i);
			var currDay = auxDate.getDay();
			var currMonth = auxDate.getMonth();

			$('#dayN'+i).html(dayNArray[currDay]);
			$('#day'+i).html(auxDate.getDate());
			$('#month'+i).html(monthArray[currMonth]);
			$('#year'+i).html(auxDate.getFullYear());
			ifForwardLast= false;
		}
		
	}
}

function returnDate(nb)
{
    var dayNb = $('#day'+nb).html();
    var month = $('#month'+nb).html();
    var year = $('#year'+nb).html();
    var monthNb;

    for(var i=0;i<monthArray.length;i++){
        if(monthArray[i] === month){
            monthNb=i+1;
        }
    }

    var date = dayNb+"-"+monthNb+"-"+year;

    var url = new URL(window.location.href);
    var query_string = url.search;
    var search_params = new URLSearchParams(query_string);

    // new value of "id" is set to "101"
    search_params.set('date', date.toString());

    // change the search property of the main url
    url.search = search_params.toString();

    // the new url string
    var new_url = url.toString();
    console.log(new_url);

    $('#urlId'+nb).attr('href', new_url);
}


// function returnDate(nb){
// 	var dayNb = $('#day'+nb).html();
// 	var month = $('#month'+nb).html();
// 	var year = $('#year'+nb).html();
// 	var monthNb;
//
// 	for(var i=0;i<monthArray.length;i++){
// 		if(monthArray[i] === month){
// 			monthNb=i+1;
// 		}
// 	}

    // var date = "/"+dayNb+"-"+monthNb+"-"+year;
    // var _href = href.toString();
    // var res = _href.split("/");
    //
    // var id = res[4];
    // console.log(id);
    // var dupa = res[0];
    // console.log(dupa);
    // var filipWKlapkachIBezKoszulki = res[2];
    // console.log(filipWKlapkachIBezKoszulki);
    //
    // $('#urlId'+nb).attr("href",date);




    // var date = dayNb+"-"+monthNb+"-"+year;
    // var href = $('#urlId'+nb).attr("href");
    // $('#urlId'+nb).attr(href+date);

// 	var date = dayNb+"-"+monthNb+"-"+year;
// 	$('#urlId'+nb).attr("href",date);
// }

