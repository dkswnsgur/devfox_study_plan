	<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>게시판</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <h1>게시판</h1>

    <!-- 등록 폼 -->
    <form id="postForm">
        <input type="text" name="title" id="postTitle" placeholder="제목" required>
        <textarea name="content" id="postContent" placeholder="내용" required></textarea>
        <button type="submit" id="submitButton">등록</button>
    </form>

    <!-- 수정 폼 (처음에는 숨김 처리) -->
    <form id="updateForm" style="display:none;">
        <input type="hidden" name="id" id="updatePostId">
        <input type="text" name="title" id="updatePostTitle" placeholder="제목" required>
        <textarea name="content" id="updatePostContent" placeholder="내용" required></textarea>
        <button type="submit" id="updateButton">수정</button>
        <button type="button" id="cancelUpdateButton">취소</button>
    </form>

    <ul id="postList">
        <!-- 게시물 리스트가 여기에 표시됩니다 -->
    </ul>

    <script>
        $(document).ready(function() {
            // 게시물 불러오기
            function loadPosts() {
                $.ajax({
                    url: '/AjaxBoard/posts',
                    method: 'GET',
                    dataType: 'json',
                    success: function(data) {
                        console.log(data); // 데이터 확인
                        $('#postList').empty();
                        data.forEach(function(post) {
                            $('#postList').append(
                                '<li>' + post.title + ': ' + post.content +
                                ' <button class="editBtn" data-id="' + post.id + '">수정</button>' +
                                ' <button class="deleteBtn" data-id="' + post.id + '">삭제</button></li>'
                            );
                        });
                    }
                });
            }

            // 게시물 등록
            $('#postForm').on('submit', function(e) {
                e.preventDefault();
                $.ajax({
                    url: '/AjaxBoard/posts',
                    method: 'POST',
                    data: $(this).serialize(),
                    success: function() {
                        loadPosts();
                        $('#postForm')[0].reset();
                    }
                });
            });

            // 게시물 삭제
            $(document).on('click', '.deleteBtn', function() {
                var postId = $(this).data('id');
                $.ajax({
                    url: '/AjaxBoard/posts/' + postId,
                    method: 'DELETE',
                    success: function() {
                        loadPosts();
                    }
                });
            });

            // 게시물 수정 버튼 클릭
            $(document).on('click', '.editBtn', function() {
                var postId = $(this).data('id');
                var postItem = $(this).parent();
                var postTitle = postItem.text().split(': ')[0];
                var postContent = postItem.text().split(': ')[1];

                // 폼에 값 설정 (수정 폼으로 전환)
                $('#updatePostId').val(postId);
                $('#updatePostTitle').val(postTitle);
                $('#updatePostContent').val(postContent);

                // 폼 전환 (등록 폼 숨기고 수정 폼 표시)
                $('#postForm').hide();
                $('#updateForm').show();
            });

         // 게시물 수정 처리
           $('#updateForm').on('submit', function(e) {
    e.preventDefault();
    var postId = $('#updatePostId').val();
    
    $.ajax({
        url: '/AjaxBoard/posts/' + postId,
        method: 'PUT',
        contentType: 'application/json; charset=UTF-8',
        data: JSON.stringify({
            title: $('#updatePostTitle').val(),
            content: $('#updatePostContent').val()
        }),
        success: function() {
            loadPosts();
            $('#updateForm')[0].reset();
            $('#updateForm').hide();
            $('#postForm').show();
        },
        error: function(xhr, status, error) {
            console.log('Error:', xhr.responseText);
        }
    });
});

            // 수정 취소 버튼 클릭 처리
            $('#cancelUpdateButton').on('click', function() {
                $('#updateForm')[0].reset();
                $('#updateForm').hide();
                $('#postForm').show();
            });

            loadPosts();
        });
    </script>
</body>
</html>