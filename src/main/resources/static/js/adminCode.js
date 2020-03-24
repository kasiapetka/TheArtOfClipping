var today = new Date();
var dateInOneYear = new Date();
dateInOneYear.setDate(today.getDate() + 365);

var dd = today.getDate();
var mm = today.getMonth()+1; //January is 0!
var yyyy = today.getFullYear();
if(dd<10){
    dd='0'+dd
}
if(mm<10){
    mm='0'+mm
}
today = yyyy+'-'+mm+'-'+dd;

var dd2 = dateInOneYear.getDate();
var mm2 = dateInOneYear.getMonth()+1; //January is 0!
var yyyy2 = dateInOneYear.getFullYear();
if(dd2<10){
    dd2='0'+dd2
}
if(mm2<10){
    mm2='0'+mm2
}
dateInOneYear = yyyy2+'-'+mm2+'-'+dd2;

document.getElementById("end").setAttribute("max", dateInOneYear);
document.getElementById("beg").setAttribute("max", dateInOneYear);
document.getElementById("beg").setAttribute("min", today);
document.getElementById("end").setAttribute("min", today);