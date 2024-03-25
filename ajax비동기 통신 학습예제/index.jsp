<%@ page contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="css/bootstrap.css">
	<link rel="stylesheet" href="css/custom.css">
	<title>Insert title here</title>
	
	<script type="text/javascript">
		<!-- Http웹 사이트에 요청을 보내는 인스턴스 request 생성 -->
		var request = new XMLHttpRequest();
		
		function searchFunction(){
			<!-- post방식으로 request  ./UserSearchServlet이란 페이지에 userName으로 + 뒤에 부분을 보낸다 UTF-8인코딩해서 -->
			request.open("Post", "./UserSearchServlet?userName=" + encodeURIComponent(document.getElementById("userName").value), true);		
			request.onreadystatechange = searchProcess;
			request.send(null);
		}
		
		function searchProcess(){
			
			var table = document.getElementById("ajaxTable");
			table.innerHTML = "";
			if(request.readyState == 4 && request.status ==200 )
			{
				var object = eval('(' + request.responseText + ')');
				var result = object.result;
				
				for(var i = 0; i < result.length; i++)
				{
					var row = table.insertRow(0);
					
					for(var j = 0; j < result[i].length; j++)
					{
						var cell = row.insertCell(j);
						cell.innerHTML = result[i][j].value;
					}
				}
			}
		}
		window.onload = function(){
			searchFunction();
		}
	</script>
	
	<!-- j쿼리 url -->
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src ="js/bootstrap.js"></script>

</head>
<body>
	<br>
	
	<div class="container">
		<div class="form-group row pull-right">
			<div class="col-xs-8"> 				<!-- 입력하는 공간의 id를 userName으로 설정   onkeyup 입력할 때 마자 searchFunction실행 -->
				<input class="form-control" type="text" size="20" id="userName" onkeyup="searchFunction();">
			</div>
			<div class="col-xs-2">
															<!-- 버튼을 눌렀을 searchFunction함수 실행 -->
				<button class="btn btn-primary" type="button" onclick="searchFunction();" >검색</button>
			</div>
		</div>
		<table class="table" style="text-align: center; border: 1px solid #dddddd">
			<thead>
				<tr>
					<th style="background-color: #fafafa; text-align: center;">이름</th>
					<th style="background-color: #fafafa; text-align: center;">나이</th>
					<th style="background-color: #fafafa; text-align: center;">성별</th>
					<th style="background-color: #fafafa; text-align: center;">이메일</th>
				</tr>
			</thead>
			<tbody id="ajaxTable">

			</tbody>
		</table>
	</div>
	
</body>
</html>