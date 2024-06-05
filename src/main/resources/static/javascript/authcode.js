
var PickupResponse;

async function PickupRequest(inputValue){
    let response = await fetch('/payment/pickup', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: inputValue.toString() 
    });
    if(response.status==400){
        alert("인증코드가 없거나 오류가 발생했습니다. 다시 시도해주세요.");
        return;
    }
    PickupResponse = await response.json();
    localStorage.setItem('PickupResponse', JSON.stringify(PickupResponse));
    console.log(PickupResponse);
    //location.href = "Pickupresult.html";
}

$(document).ready(function(){
    $(".submit-button").on("click",function(){
        PickupRequest($("#input").val());

    });

    $(".cancel-button").on("click",function(){
        location.href = "menu.html"
    });
});