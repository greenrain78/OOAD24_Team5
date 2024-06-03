//let beverage = ["콜라","사이다","녹차","홍차","밀크티","탄산수","보리차","캔커피","물","에너지드링크","유자차","식혜","아이스티","딸기주스","오렌지주스","포도주스","이온음료","아메리카노","핫초코","카페라떼"]
var beverage = [];
var count=0;
var count_limit=5;
var cur_item_code=0;

async function fetch_beverage(){
	fetch('/management/items')
	.then(response => response.json())
	.then(data => {
		console.log(data);
		beverage = data.map(item => ({...item, needsUpdate: false}));
	})
	.catch(error => console.error(error));
}

$(document).ready(function(){

	fetch_beverage();

	$(".drink-item").each(function(){	
		var item_code=$(this).attr("id");
		$(this).children().last().text(beverage[item_code-1].name);
	})

	$(".drink-item").on("click",function(){
		cur_item_code = $(this).attr("id");
		$("#item_info").text(beverage[cur_item_code-1].name+" "+beverage_cost[cur_item_code-1].price+"원");
		count=0;
		$("#counter-value").text(count+"/"+count_limit);
		$("#total").text(0);
		$("#select_form").attr("class","popup");
		change_position($(".popup"));
		$("#select_form").fadeIn();
	})

	$(".button_cancel").on("click",function(){
		$("#select_form").fadeOut();
	})

	$(".counter-button").on("click",function(){
		if($(this).attr("id")=="add"){
			if(count<count_limit) count++;
		}else{
			if(count>0) count--;
		}
		$("#counter-value").text(count+"/"+count_limit);
		$("#total").text(count*beverage_cost[cur_item_code-1]);
	})

	$("#home-icon").on("click",function(){
		location.href="menu.html";
	});
})

function change_position(obj){
	var l=($(window).width()-obj.width())/2;
	var t=($(window).height()-obj.height())/2;
	obj.css({top:t,left:l});
}