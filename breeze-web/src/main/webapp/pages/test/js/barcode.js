var barcode = {
		code: '',  
		lastTime: null,
		nextTime: null,  
		lastCode: null,
		nextCode: null, 
		show : function(){},
		keypress : function(e) {
	    	console.log('0.'+barcode.code);
	    	barcode.nextCode = e.which;  
	    	barcode.nextTime = new Date().getTime();  
	        if(barcode.lastCode == null && barcode.lastTime == null) {   
	        } 
	        if(barcode.lastCode != null && barcode.lastTime != null && barcode.nextTime - barcode.lastTime <= 20) { 
	        	console.log('1.'+barcode.code);
	        	if(barcode.code=='' && barcode.lastCode != ''){
	        		barcode.code += String.fromCharCode(barcode.lastCode);
	        	}
	        	barcode.code += String.fromCharCode(barcode.nextCode);  
	        } else if(barcode.lastCode != null && barcode.lastTime != null && barcode.nextTime - barcode.lastTime > 1000) {  
	        	console.log('2.'+barcode.code);
	        	barcode.code = "";//超时清空 
	        }  
	        barcode.lastCode = barcode.nextCode;  
	        barcode.lastTime = barcode.nextTime;  
	    },
	    keyup : function(e) {  
	    	console.log('3.'+barcode.code);
	        if(e.which == 13 && barcode.code != '') {  
	        	if(typeof barcode.show == 'function'){
	        		barcode.show(barcode.code);
				}
	        	barcode.code = "";//回车输入后清空  
	        }  
	    }
	}
//$(function(){
//	window.document.onkeypress = barcode.keypress;
//	window.document.onkeyup = barcode.keyup;
//}); 
window.onload = function(){
	window.document.onkeypress = barcode.keypress;
	window.document.onkeyup = barcode.keyup;
};