<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var='root' value='${pageContext.request.contextPath }/'/>
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
					<div class="form-group">
						<label for="board_writer_name">作成者</label>
						<input type="text" id="board_writer_name" name="board_writer_name" class="form-control" value="${readContentBean.content_writer_name }" disabled="disabled"/>
					</div>
					<div class="form-group">
						<label for="board_date">作成日</label>
						<input type="text" id="board_date" name="board_date" class="form-control" value="${readContentBean.content_date }" disabled="disabled"/>
					</div>
					<div class="form-group">
						<label for="board_subject">タイトル</label>
						<input type="text" id="board_subject" name="board_subject" class="form-control" value="${readContentBean.content_subject }" disabled="disabled"/>
					</div>
					<div class="form-group">
						<label for="board_content">内容</label>
						<textarea id="board_content" name="board_content" class="form-control" rows="10" style="resize:none" disabled="disabled">${readContentBean.content_text }</textarea>
					</div>
					<c:if test="${readContentBean.content_file != null }">
					<div class="form-group">
						<label for="board_file">添付画像</label>
						<img src="${root }upload/${readContentBean.content_file}" width="100%"/>						
					</div>
					</c:if>
					<div class="form-group">
						<div class="text-right">
							<a href="${root }board/main?board_info_idx=${board_info_idx}&page=${page}" class="btn btn-primary">リストを見る</a>
							<c:if test="${loginUserBean.user_idx == readContentBean.content_writer_idx }">
							<a href="${root }board/modify?board_info_idx=${board_info_idx}&content_idx=${content_idx}&page=${page}" class="btn btn-info">修正する</a>
							<%-- <a href="${root }board/delete?board_info_idx=${board_info_idx}&content_idx=${content_idx}" class="btn btn-danger">削除する</a> --%>
							 <button class="btn btn-danger" id="deletePostBtn" data-content-idx="${content_idx}">削除する</button>
							</c:if>
						</div>
					</div>
    <c:forEach var="comment" items="${commentList}">
        <div class="card mb-2">
            <div class="card-body"> 
                <!-- コメント内容 -->
                <p><strong>${comment.comment_writer_name}:</strong> ${comment.comment_text}</p>
                <p class="text-muted" style="font-size:0.9rem;">${comment.comment_date}</p>

                <!-- コメント削除ボタン -->
                <c:if test="${loginUserBean != null && loginUserBean.user_name == comment.comment_writer_name}">
                    <div style="text-align: right;">
                    <%-- <form action="${root }board/deleteComment" method="post" style="float: right;">
                        <input type="hidden" name="comment_id" value="${comment.comment_id}" />
                        <button class="btn btn-danger" id="commitdeletePostBtn" data-content-idx="${content_idx}">削除</button> --%>
                        <button class="btn btn-danger deleteCommentBtn" data-comment-id="${comment.comment_id}">削除</button>
                    </form>
                    </div>
                </c:if>
            </div>
        </div>
    </c:forEach>
</div>
					<!-- コメント入力フォーム -->
					<c:if test="${loginUserBean != null}">
					<div class="form-group">
						<h5>コメント作成</h5>
						<%-- <form action="${root }board/addComment" method="post"> --%>
						<form id="commentForm">
						    <input type="hidden" name="comment_writer_name" value="${loginUserBean.user_name}" />
							<input type="hidden" name="content_idx" value="${content_idx}" />
							<input type="hidden" name="board_info_idx" value="${board_info_idx}" />
							<textarea name="comment_text" class="form-control" rows="3" placeholder="コメントを入力してください" required></textarea>
							<div class="text-right mt-2">
								<button type="button" id="submitCommentBtn" class="btn btn-success">コメントをつける</button>
							</div>
						</form>
					</div>
					</c:if>
				</div>
			</div>
		</div>
		<div class="col-sm-3"></div>
	</div>
</div>

<c:import url="/WEB-INF/views/include/bottom_info.jsp"/>

<script>
$(document).ready(function() {
    // 게시글 삭제 버튼 클릭 이벤트 처리
    $('#deletePostBtn').on('click', function() {
        var contentIdx = $(this).data('content-idx');  // 削除する投稿のIDを取得する
        var boardInfoIdx = ${board_info_idx}; // 掲示板情報ID取得

        if (confirm("本当に削除しますか？")) {  // 削除確認メッセージ
            $.ajax({
                url: '${root}board/delete',  // サーバの削除処理URL
                type: 'GET',
                data: { 
                    content_idx: contentIdx, // 削除する掲示文ID送信
                    board_info_idx: boardInfoIdx // 掲示板情報ID送信
                },
                success: function(response) {
                    if (response == 'success') {
                        // 削除に成功した場合は、リストページに移動
                        alert("文の削除に失敗しました。");
                    } else {
                    	alert("投稿が削除されました。");
                    	window.location.href = '${root}board/main?board_info_idx=${board_info_idx}&page=${page}'; // 成功するとリストページに移動
                    }
                },
                error: function() {
                    alert("サーバーとの通信中にエラーが発生しました。");
                }
            });
        }
    });
});


$(document).ready(function() {
    // 폼 제출 시 AJAX로 서버에 요청
    $('#submitCommentBtn').on('click', function(event) {
        event.preventDefault(); // 기본 폼 제출 동작 방지

        // 댓글 입력 폼 데이터를 가져오기
        var formData = $('#commentForm').serialize();

        $.ajax({
            url: '${root}board/addComment', // 요청할 URL
            type: 'POST',
            data: formData,
            success: function(response) {
                if (response.status === 'success') {
                    alert("コメントが正常に登録されました！");
                } else {
                	 alert("コメントが正常に登録されました！");
                	 window.location.href = '${root}board/read?board_info_idx=${board_info_idx}&content_idx=${content_idx}&page=1';
                }
            },
            error: function(xhr, status, error) {
                alert("서버와의 통신 중 오류가 발생했습니다.");
            }
        });
    });
});

$(document).ready(function() {
    // 댓글 삭제 버튼 클릭 이벤트 처리
    $('.deleteCommentBtn').on('click', function() {
        var commentId = $(this).data('comment-id');

        if (confirm("本当にコメントを削除しますか？")) {  // 削除確認メッセージ
            $.ajax({
                url: '${root}board/deleteComment',  // サーバの削除処理URL
                type: 'POST',
                data: { 
                	comment_id: commentId
                },
                success: function(response) {
                    if (response == 'success') {
                        // 削除に成功した場合は、リストページに移動
                        alert("コメントが削除されました11。");
                    } else {
                    	alert("コメントが削除されました。");
                    	window.location.href = '${root}board/read?board_info_idx=${board_info_idx}&content_idx=${content_idx}&page=1'; // 成功するとリストページに移動
                    }
                },
                error: function() {
                    alert("サーバーとの通信中にエラーが発生しました。");
                }
            });
        }
    });
});








</script>

</body>
</html>
    