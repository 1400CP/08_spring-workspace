<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

    <jsp:include page="../common/header.jsp"/>

    <div class="content">
        <br><br>
        <div class="innerOuter">
            <h2>회원가입</h2>
            <br>

            <form action="insert.me" method="post" onsubmit="" id="enrollForm">
                <div class="form-group">
                    <label for="userId">* ID :</label>
                    <input type="text" class="form-control" id="userId" name="userId" placeholder="Please Enter ID" required>
                    <!-- <div id="checkResult" style="font-size:0.8em"></div>  원래는 안 보인다 -->
                    <div id="checkResult" style="font-size:0.8em; display:none"></div>
                    
                    <br>
                    <label for="userPwd">* Password :</label>
                    <input type="password" class="form-control" id="userPwd" name="userPwd" placeholder="Please Enter Password" required><br>
                    
                    <label for="checkPwd">* Password Check :</label>
                    <input type="password" class="form-control" id="checkPwd" placeholder="Please Enter Password" required><br>
                    
                    <label for="userName">* Name :</label>
                    <input type="text" class="form-control" id="userName" name="userName" placeholder="Please Enter Name" required><br>
                    
                    <label for="email"> &nbsp; Email :</label>
                    <input type="email" class="form-control" id="email" name="email" placeholder="Please Enter Email"><br>
                    
                    <label for="age"> &nbsp; Age :</label>
                    <input type="number" class="form-control" id="age" name="age" placeholder="Please Enter Age"><br>
                    
                    <label for="phone"> &nbsp; Phone :</label>
                    <input type="tel" class="form-control" id="phone" name="phone" placeholder="Please Enter Phone (-없이)"><br>
                    
                    <label for="address"> &nbsp; Address :</label>
                    <input type="text" class="form-control" id="address" name="address" placeholder="Please Enter Address"><br>
                    
                    <label for=""> &nbsp; Gender : </label> &nbsp;&nbsp;
                    <input type="radio" name="gender" id="Male" value="M">
                    <label for="Male">남자</label> &nbsp;&nbsp;
                    <input type="radio" name="gender" id="Female" value="F">
                    <label for="Female">여자</label><br>
                    
                </div>
                <br>
                <div class="btns" align="center">
                    <button id="enrollBtn" type="submit" class="btn btn-primary" disabled>회원가입</button>
                    <button type="reset" class="btn btn-danger"> 초기화</button>
                </div>
            </form>
        </div>
        
        <script>
        	$(function(){
        		// 아이디 입력하는 input 요소객체 변수에 담아두기
        		const $idInput = $("#enrollForm input[name=userId]")
        		// jquery 형태로 const를 만들 때는 이렇게 만든다
        		$idInput.keyup(function(){ // 키보드가 눌렸다가 떼어지는 것
        			// console.log($idInput.val())
        			
        			// 우선 최소 5글자 이상으로 입력됐을 때면 ajax 요청해서 중복체크
        			if($idInput.val().length >= 5){
        				$.ajax({
        					url:"idCheck.me",
        					data:{
        						checkId:$idInput.val()
        					},
        					success:function(result){
        						if(result === "NNNNN"){
        							// 사용 불가, 빨간색 메시지 출력
        							$("#checkResult").show();
        							$("#checkResult").css("color","red").text("중복된 아이디가 존재합니다. 다시 입력해 주세요");
        							
        							// 버튼 비활성화
        							$("#enrollForm :submit").attr("disabled", true);
        							
        						}else{
        							// 사용 가능, 초록색 메시지 출력
        							$("#checkResult").show();
        							$("#checkResult").css("color","green").text("아이디 생성이 가능합니다.");
        							
        							// 버튼 활성화
        							$("#enrollForm :submit").removeAttr(); // 속성 제거
        						}
        					},
        					error:function(){
        						console.log("아이디 중복체크용 ajax 통신 실패");
        					},
        					
        				})
        				
        			}else{
        				// 5글자 미만에는 상태 메시지를 지워야 한다.
        				// 버튼 비활성화도 해야 한다.
        				$("#checkResult").hide();
        				$("#enrollForm :submit").attr("disabled", true);
        			}
        		})
        	})
        </script>
        
      
        <br><br>
    </div>

   <jsp:include page="../common/footer.jsp"/>

</body>
</html>