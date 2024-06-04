var OrderRequest = JSON.parse(localStorage.getItem('OrderRequest'));
const prepay_notice = localStorage.getItem('prepay_notice');

var OrderResponse;

async function PaymentRequest(prepay_notice){
    if(prepay_notice==true){//prepayment
        let response = await fetch('/payment/prepay', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(OrderRequest)
        });
        console.log(response);

    }
    else{//payment
        
        let response = await fetch('/payment/pay', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(OrderRequest)
        })
        .then(response => {
            if(!response.ok){
                alert("결제에 실패했습니다. 다시 시도해주세요.");
            }
            else{
                response.json()
            }
            })
        .then(data => {
            console.log(data);
            OrderResponse = data;
            localStorage.setItem('OrderResponse', JSON.stringify(OrderResponse));
        });
        console.log(response);
    }
};

$(document).ready(function(){
    $(".submit-button").on("click",function(){
        let inputValue = $("#input").val().toString();
        console.log(inputValue);
        OrderRequest.cardNumber=inputValue;
        console.log(OrderRequest);
        console.log(prepay_notice);
        console.log(JSON.stringify(OrderRequest));
        PaymentRequest(prepay_notice);
    });

    $(".cancel-button").on("click",function(){
        location.href = "menu.html"
    });

});