
var OrderResponse = JSON.parse(localStorage.getItem('OrderResponse'));

$(document).ready(function(){
    console.log(OrderResponse);
    var text = "음료 "+ OrderResponse.name+" " + OrderResponse.quantity + "개가 구매완료되었습니다! 맛있게 드세요!";
    $("#text").text(text);

    $(".btn").on("click",function(){
        location.href = "menu.html"
    });

});