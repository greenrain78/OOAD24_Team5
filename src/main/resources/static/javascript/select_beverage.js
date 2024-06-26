var beverage = [];
var count=0;
var count_limit=5;
var cur_item_code=0;
var OrderRequest={"itemCode":0,"cardNumber":"","quantity":0};
var closest_DVM=null;
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

async function get_closest_DVM(item_code){
	var avail_DVM = [];
	const response = await fetch('/client/item/'+item_code)
	.then(response => response.json())
	.then(data => {
		avail_DVM = data.map(item => ({...item, needsUpdate: false}));
		closest_DVM=avail_DVM[0];
		for(var i=0;i<avail_DVM.length;i++){
			if(closest_DVM.item_num==0){
				if(avail_DVM[i].item_num>0){
					closest_DVM=avail_DVM[i];
				}
			}
			else{
				if(avail_DVM[i].distance<closest_DVM.distance && avail_DVM[i].item_num>0){
					closest_DVM=avail_DVM[i];
				}
			}
		}
	})
	.catch(error => console.error(error));
}

$(document).ready(function(){

	fetch_beverage();

	$(".drink-item").on("click",function(){

		cur_item_code = $(this).attr("id");
		$("#item_info").text(beverage[cur_item_code-1].name+" "+beverage[cur_item_code-1].price+"원");
		if(beverage[cur_item_code-1].quantity==0){
			get_closest_DVM(cur_item_code);
			if(closest_DVM==null || closest_DVM.item_num==0){
				$("#closest_DVM").attr('class','visible');
				$("#closest_DVM").text("해당 음료를 구매할 수 있는 자판기가 없습니다");
			}
			else{
				$("#prepay_notice").attr('class','visible');
				$("#closest_DVM").text("가장 가까운 자판기 id: "+closest_DVM.id+" 위치: "+closest_DVM.x+","+closest_DVM.y);
				$("#closest_DVM").attr('class','visible');
			}
			count=0;
			if(count_limit>closest_DVM.item_num){
				$("#counter-value").text(count+"/"+closest_DVM.item_num);
			}
			else{
				$("#counter-value").text(count+"/"+count_limit);
			}
		}
		else{
			$("#prepay_notice").attr('class','hidden');
			count=0;
			if(count_limit>beverage[cur_item_code-1].quantity){
				$("#counter-value").text(count+"/"+beverage[cur_item_code-1].quantity);
			}
			else{
				$("#counter-value").text(count+"/"+count_limit);
			}
		}

		$("#total").text(0);
		$("#select_form").attr("class","popup");
		change_position($(".popup"));
		$("#select_form").fadeIn();
	})

	$(".button_cancel").on("click",function(){
		$("#prepay_notice").attr("class","hidden");
		$("#closest_DVM").attr("class","hidden");
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
		else if(beverage[cur_item_code-1].quantity==0 && closest_DVM!=null && closest_DVM.quantity!=0 && count_limit>closest_DVM.item_num){
			if($(this).attr("id")=="add"){
				if(count<closest_DVM.item_num) count++;
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

		if(count_limit>beverage[cur_item_code-1].quantity && beverage[cur_item_code-1].quantity!=0){
			$("#counter-value").text(count+"/"+beverage[cur_item_code-1].quantity);
		}
		else if(beverage[cur_item_code-1].quantity==0 && closest_DVM!=null && closest_DVM.quantity!=0 && count_limit>closest_DVM.item_num){
			$("#counter-value").text(count+"/"+closest_DVM.item_num);
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