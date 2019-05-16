	$(function(){
			    //导航固定
			    var elm = $('#outnav'); 
			    var startPos = $(elm).offset().top; 
			    $.event.add(window, "scroll", function() { 
			        var p = $(window).scrollTop(); 
			        $(elm).css('position',((p) > startPos) ? 'fixed' : 'static'); 
			        $(elm).css('top',((p) > startPos) ? '0px' : ''); 
			    }); 
			   //导航下拉框
				$("#globhouse").hover(function(){
					$("#downmenu").show();
				},function(){
					$("#downmenu").hide();
				});
				
				
				var funll=$(document).width();
				
				$(".full_screen").css("width",funll+"px");
});