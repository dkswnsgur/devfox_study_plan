<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var='root' value='${pageContext.request.contextPath }/'/>
<script>
	alert('保存されました')
	location.href = '${root}board/read?board_info_idx=${writeContentBean.content_board_idx}&content_idx=${writeContentBean.content_idx}&page=1'
</script>