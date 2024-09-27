<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<c:set var="root" value="${pageContext.request.contextPath }/"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>미니 프로젝트</title>
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
                    <form:form id="modifyForm" action='${root }board/modify_pro' method='post' modelAttribute="modifyContentBean" enctype="multipart/form-data">
                        <form:hidden path="content_idx"/>
                        <form:hidden path="content_board_idx"/>
                        <input type='hidden' name='page' value='${page}'/>
                        <div class="form-group">
                            <form:label path="content_writer_name">作成者</form:label>
                            <form:input path="content_writer_name" class='form-control' readonly="true"/>
                        </div>
                        <div class="form-group">
                            <form:label path="content_date">作成日</form:label>
                            <form:input path="content_date" class='form-control' readonly='true'/>
                        </div>
                        <div class="form-group">
                            <form:label path="content_subject">タイトル</form:label>
                            <form:input path="content_subject" class='form-control'/>
                            <form:errors path="content_subject" style='color:red'/>
                        </div>
                        <div class="form-group">
                            <form:label path="content_text">内容</form:label>
                            <form:textarea path="content_text" class="form-control" rows="10" style="resize:none"/>
                            <form:errors path="content_text" style='color:red'/>
                        </div>
                        <div class="form-group">
                            <label for="board_file">添付画像</label>
                            <c:if test="${modifyContentBean.content_file != null }">
                                <img src="${root }upload/${modifyContentBean.content_file}" width="100%"/>    
                                <form:hidden path="content_file"/>
                            </c:if>
                            <form:input path="upload_file" type='file' class="form-control" accept="image/*"/>
                        </div>
                        <div class="form-group">
                            <div class="text-right">
                                <form:button class='btn btn-primary' id="modifyButton">修正完了</form:button>
                                <a href="${root }board/read?board_info_idx=${board_info_idx}&content_idx=${content_idx}&page=${page}" class="btn btn-info">キャンセル</a>
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
    $('#modifyButton').on('click', function(e) {
        e.preventDefault(); // 基本フォームの提出防止

        var formData = new FormData($('#modifyForm')[0]); // FormDataオブジェクト作成
        
        // AJAX 요청
        $.ajax({
            url: '${root}board/modify_pro', // 修正処理URL
            type: 'POST',
            data: formData,
            processData: false, // jQueryがデータを処理しないように設定
            contentType: false, // content-typeを自動で設定しないように設定
            success: function(response) {
                if (response.status === 'success') { // サーバーがJSONオブジェクトに返却する場合
                    alert("投稿が更新されました。");
                } else {
                	alert("投稿が更新されました。");
                    window.location.href = '${root}board/main?board_info_idx=${board_info_idx}';
                }
            },
            error: function() {
                alert("サーバーとの通信中にエラーが発生しました。");
            }
        });
    });
});
</script>

</body>
</html>
    