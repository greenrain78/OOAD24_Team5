
var OrderResponse  = JSON.parse(localStorage.getItem('OrderResponse'));

$(document).ready(function(){
    $(".code").text(OrderResponse.code.code);
    $(".coordinates").text(OrderResponse.info.id+" ("+OrderResponse.x + ", " + OrderResponse.y+")");
});