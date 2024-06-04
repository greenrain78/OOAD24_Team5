var OrderRequest = localStorage.getItem('OrderRequest');
const prepay_notice = localStorage.getItem('prepay_notice');

$(document).ready(function(){
    $(".submit-button").on("click",function(){
        let inputValue = document.getElementById('input').value.toString();
        OrderRequest.cardNumber=inputValue;
        let response=null;
        if(prepay_notice){
            response = fetch('/payment/prepay', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(OrderRequest)
            });
            while(response==null){
                console.log("waiting for response");
            }
            console.log(response);
        }
        else{
            console.log(OrderRequest)
            fetch('/payment/pay', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(OrderRequest)
            })
            .then(response => response.json())
            .then(data => {
                console.log(data);
                response = data;
            });
            while(response==null){
                console.log("waiting for response");
            }
            console.log(response);
        }
    });

    $(".cancel-button").on("click",function(){
        location.href = "menu.html"
    });

});