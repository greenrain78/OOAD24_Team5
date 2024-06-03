$(document).ready(function(){
    $(".submit-button").on("click",function(){
        let inputValue = document.getElementById('input').value;
        fetch('/payment/pickup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: inputValue.toString() 
        });
    });

    $(".cancel-button").on("click",function(){
        location.href = "menu.html"
    });
});