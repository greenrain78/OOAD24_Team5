var OrderRequest = JSON.parse(localStorage.getItem('OrderRequest'));
const prepay_notice = localStorage.getItem('prepay_notice');

var OrderResponse="";

async function PaymentRequest(prepay_notice){
    if(prepay_notice=="true"){//prepayment
        let response = await fetch('/payment/prepay', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(OrderRequest)
        })
        if(response.status==400){
            alert("결제에 실패하였습니다. 다시 시도해주세요.");
            return;
        }
        OrderResponse = await response.json();
        localStorage.setItem('OrderResponse', JSON.stringify(OrderResponse));
        location.href = "PrepaymentResult.html";

    }
    else if(prepay_notice=="false"){//payment
        let response = await fetch('/payment/pay', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(OrderRequest)
        })
        if(response.status==400){
            alert("결제에 실패하였습니다. 다시 시도해주세요.");
            return;
        }
        OrderResponse = await response.json();
        localStorage.setItem('OrderResponse', JSON.stringify(OrderResponse));
        location.href = "PaymentResult.html";
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

    $(".main-page-button").on("click",function(){
        location.href = "menu.html"
    });

});