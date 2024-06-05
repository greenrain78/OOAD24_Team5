var beverage = [];
var count=0;
var count_limit=5;
var cur_item_code=0;
var OrderRequest={"itemCode":0,"cardNumber":"","quantity":0};
async function fetch_beverage(){
	const response = await fetch('/items')
	.then(response => response.json())
	.then(data => {
		console.log(data);
		beverage = data.map(item => ({...item, needsUpdate: false}));
		$(document).ready(function(){
			$(".drink-item").each(function(){	
				var item_code=$(this).attr("id");
				$(this).children().last().text(beverage[parseInt(item_code)-1].name);
			})
		})
	})
	.catch(error => console.error(error));
}

$(document).ready(function(){

	fetch_beverage();

	$(".drink-item").on("click",function(){
		cur_item_code = $(this).attr("id");
		$("#item_info").text(beverage[cur_item_code-1].name+" "+beverage[cur_item_code-1].price+"원");
		if(beverage[cur_item_code-1].quantity==0){
			$("#prepay_notice").attr('class','visible');
		}
		else{
			$("#prepay_notice").attr('class','hidden');
		}
		count=0;
		if(count_limit>beverage[cur_item_code-1].quantity){
			$("#counter-value").text(count+"/"+beverage[cur_item_code-1].quantity);
		}
		else{
			$("#counter-value").text(count+"/"+count_limit);
		}
		$("#total").text(0);
		$("#select_form").attr("class","popup");
		change_position($(".popup"));
		$("#select_form").fadeIn();
	})

	$(".button_cancel").on("click",function(){
		$("#select_form").fadeOut();
	})

	$(".counter-button").on("click",function(){
		if(count_limit>beverage[cur_item_code-1].quantity && beverage[cur_item_code-1].quantity!=0){
			if($(this).attr("id")=="add"){
				if(count<beverage[cur_item_code-1].quantity) count++;
			}else{
				if(count>0) count--;
			}
		}
		else{
			if($(this).attr("id")=="add"){
				if(count<count_limit) count++;
			}else{
				if(count>0) count--;
			}
		}

		if(count_limit>beverage[cur_item_code-1].quantity){
			$("#counter-value").text(count+"/"+beverage[cur_item_code-1].quantity);
		}
		else{
			$("#counter-value").text(count+"/"+count_limit);
		}
		$("#total").text(count*beverage[cur_item_code-1].price);
	})

	$("#home-icon").on("click",function(){
		location.href="menu.html";
	});

	$(".button_purchase").on("click",function(){
		if(count==0){
			alert("수량을 선택해주세요.");
		}
		else{
			OrderRequest.itemCode=cur_item_code;
			OrderRequest.quantity=count;
			localStorage.setItem('OrderRequest',JSON.stringify(OrderRequest));
			if(beverage[cur_item_code-1].quantity==0){
				localStorage.setItem('prepay_notice',true);
			}
			else{
				localStorage.setItem('prepay_notice',false);
			}
			location.href="card_input.html";
		}
	});
})

function change_position(obj){
	var l=($(window).width()-obj.width())/2;
	var t=($(window).height()-obj.height())/2;
	obj.css({top:t,left:l});
}