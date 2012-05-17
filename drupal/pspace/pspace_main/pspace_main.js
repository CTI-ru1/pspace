jQuery(document).ready(function() {
	
	/*
	jQuery(".statusimage").mouseover(function(){
		var image_id = this.id;
		//alert(image_id);
		jQuery("." + image_id).show(); 
	});

	jQuery(".statusimage").mouseout(function(){
		var image_id = this.id;
		//alert(image_id);
		jQuery("." + image_id).hide(); 
	});
	*/
	
	jQuery(".statusimage").click(function(){
		var image_id = this.id;
		//alert(image_id);
		jQuery("." + image_id).hide().slideDown(800); ; 
	});	
	
});


//----Validate----//
/*
	$("#ip").change(validateIP);

*/
	
//-----AJAX-----///
/*
	$.ajaxSetup ({
		cache: false
	});

	var ajax_load = "<img class='loading' src='img/load.gif' alt='loading...' />";

	$(".gbtn").click(function(){

	     if(! $("input#ip").val()){ //isempty?
		$("p.result").html("Missing IP Address!").css('color', 'red')
		.hide().slideDown(200); 
		return false;		
	     }
	     else if(! $('input:radio[name="os"]').is(':checked')){
		$("p.result").html("Missing OS!").css('color', 'red')
		.hide().slideDown(200); 
		return false;
	     }
	     else if(! validateIP()){
		$("p.result").html("Invalid IP Address!").css('color', 'red')
		.hide().slideDown(200); 
		return false;
	     }
	     else{
		$("p.result").html(ajax_load);

		$.ajax({
   		   type: "POST",
   		   url: "",
  		   data: $("#addserver").serialize(),
    		   error: function(){
			$("p.result").html("Something went wrong.. (Internet Connection?) ")	
			.hide()  
			.fadeIn(500);  

			return false; 
			},
    		   success: function(responseText) {

			var response = responseText.split(".");

			$("p.result").html(response[1]).css('color', response[0]=='1' ? 'green':'red')
			.hide().slideDown(200); 

			if (response[0]=='1')
			 resetForm($('#addserver')); 

			return false; 
			}
      		 });
	     }
	
	     return false;

	});

 */	
	
//-----Reset Form-----///
/*
	$(".bbtn").click(function(){
		resetForm($('#addserver')); 
	});

	function resetForm($form) {
	    $form.find('input:text, input:password, input:file, select,textarea').val('');
	    $form.find('input:radio, input:checkbox') .removeAttr('checked').removeAttr('selected');
	}
*/
