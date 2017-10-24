<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="UTF-8"%>
<%
	String rootPath = request.getContextPath();
	
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	test iemi
	<label>IMEI:</label>
	<div>
		<ul id="imeis">
		</ul>
	</div>

<!-- jQuery 2.2.3 -->
</body>
<script src="<%=rootPath %>/plugins/jQuery/jquery-2.2.3.min.js"></script>
<script src="<%=rootPath %>/pages/test/js/barcode.js"></script>
<script type="text/javascript">

$(function(){
	/* $('#imei').change(function(){
		value = $('#imei').val();
		$('#imeis').append('<li>'+value+'</li>');
	}); */
	/* $("#imei").startListen({
		letter : true,
		number : true,
		check  : false,
		show : function(code){
			alert(code);
		}
	}); */
	
	/* var code = "";  
    var lastTime,nextTime;  
    var lastCode,nextCode; 
    $(this).keypress(function(e) {
    	console.log('0.'+code);
        nextCode = e.which;  
        nextTime = new Date().getTime();  
        if(lastCode == null && lastTime == null) {   
        } 
        if(lastCode != null && lastTime != null && nextTime - lastTime <= 10) { 
        	console.log('1.'+code);
        	if(code=='' && lastCode != ''){
        		code += String.fromCharCode(lastCode);
        	}
            code += String.fromCharCode(nextCode);  
        } else if(lastCode != null && lastTime != null && nextTime - lastTime > 1000) {  
        	console.log('2.'+code);
        	code = "";//超时清空 
        }  
        lastCode = nextCode;  
        lastTime = nextTime;  
    });  
    $(this).keyup(function(e) {  
    	console.log('3.'+code);
        if(e.which == 13 && code != '') {  
    		$('#imeis').append('<li>'+code+'</li>');
            code = "";//回车输入后清空  
        }  
    }); */
    
	barcode.show = function(code){
    	$('#imeis').append('<li>'+code+'</li>');
    }
}); 
</script>
</html>