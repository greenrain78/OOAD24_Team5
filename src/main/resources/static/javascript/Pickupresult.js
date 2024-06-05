
var PickupResponse = JSON.parse(localStorage.getItem('PickupResponse'));

$(document).ready(function(){
    console.log(PickupResponse);
    var text = "음료 "+ PickupResponse.name+" " + PickupResponse.quantity + "개가 구매완료되었습니다! 맛있게 드세요!";
    $("#text").text(text);

    $(".btn").on("click",function(){
        location.href = "menu.html"
    });

});