<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var='root' value='${pageContext.request.contextPath}/'/>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>미니 프로젝트</title>
<!-- Bootstrap CDN -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js"></script>
</head>
<body>

<c:import url="/WEB-INF/views/include/top_menu.jsp"/>

<div class="container" style="margin-top:100px">
	<div class="row">
		<div class="col-sm-3"></div>
		<div class="col-sm-6">
			<div class="card shadow">
				<div class="card-body">
					<!-- 폼 태그에 id 추가 -->
					<form:form id="writeForm" modelAttribute="writeContentBean" enctype="multipart/form-data">
						<form:hidden path="content_board_idx"/>
						<div class="form-group">
							<form:label path="content_subject">タイトル</form:label>
							<form:input path="content_subject" class='form-control'/>
							<form:errors path='content_subject' style='color:red' />
						</div>
						<div class="form-group">
							<form:label path="content_text">内容</form:label>
							<form:textarea path="content_text" class="form-control" rows="10" style="resize:none"/>
							<form:errors path='content_text' style='color:red'/>
						</div>
						<div class="form-group">
							<form:label path="upload_file">添付画像</form:label>
							<form:input type='file' path='upload_file' class="form-control" accept="image/*"/>
						</div>
						<div class="form-group">
							<div class="text-right">
								<button type="button" class='btn btn-primary' id="submitBtn">作成する</button>
							</div>
						</div>
					</form:form>
				</div>
			</div>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom_info.jsp"/>

<script>
$(document).ready(function() {
    // 버튼 클릭 시 폼을 AJAX로 전송
    $('#submitBtn').on('click', function() {
        var formData = new FormData($('#writeForm')[0]); // 폼 데이터를 가져와서 FormData로 변환

        $.ajax({
            url: '${root}board/write_pro', // 転送するURL
            type: 'POST',
            data: formData, // フォームデータ転送
            processData: false, // デフォルトでデータをクエリ ストリングに変換するのを防止
            contentType: false, // デフォルトで設定されるContent-Typeを無効にする
            enctype: 'multipart/form-data',
            success: function(response) {
            	alert('保存されました')
                window.location.href = '${root}board/main?board_info_idx=${writeContentBean.content_board_idx}'; // 成功するとリストページに移動
            },
            error: function(xhr, status, error) {
                alert("保存できませんでした");
            }
        });	
    });
});
</script>

</body>
</html>
    