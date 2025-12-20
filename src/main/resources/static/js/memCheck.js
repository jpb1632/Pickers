/**
 * check.js
 */
$(function(){
	$("#memId").on("change keyup",function(){
		$.ajax({
			type : "post",
			url : "/login/memIdCheck",
			data : {"memId" : $("#memId").val()},
			dataType : "text",
			success : function(result){
				if(result == "1"){
					$("#idCheck").text("사용중인 아이디입니다.");
					$("#idCheck").css("color","red");
				}else{
					$("#idCheck").text("사용가능한 아이디입니다.");
					$("#idCheck").css("color","blue");
				}
			},
			error:function(){
				alert("서버오류");
			}
		});
	});
	
	$("#memEmail").on("change keyup", function(){
		$.ajax({
			type:"post",
			url:"/checkRest/userEmailCheck",
			dataType:"text",
			data:{"userEmail":$("#memEmail").val()},
			success:function(result){
				if(result == "1"){
					$("#emialCheck").text("사용중인 이메일입니다.");
					$("#emialCheck").css("color","red");
				}else{
					$("#emialCheck").text("사용가능한 이메일입니다.");
					$("#emialCheck").css("color","blue");
				}
			},
			error:function(){
				alert("서버 오류");
			}
		});
	});
	
});