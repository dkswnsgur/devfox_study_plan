<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.Date"%>
<html >
<head>    
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css" rel="stylesheet">
	<title>Welcome</title>
</head>
<body>
<div class="container py-4">
   <%@ include file="menu.jsp"%>
    
	<%!String greeting = "도서 쇼핑몰에 오신 것을 환영합니다";
	String tagline = "Welcome to Web Market!";%>
	
    <div class="p-5 mb-4 bg-body-tertiary rounded-3">
      <div class="container-fluid py-5">
        <h1 class="display-5 fw-bold"><%=greeting%></h1>
        <p class="col-md-8 fs-4">BookMarket</p>      
      </div>
    </div>

    <div class="row align-items-md-stretch   text-center">
      <div class="col-md-12">
        <div class="h-100 p-5">
          <h3><%=tagline%></h3>     
          
          <%  
				Date day = new java.util.Date(); // 現在の日付と時刻を取得するDateオブジェクトの作成
				String am_pm; // AM/PMを保存する変数宣言
				int hour = day.getHours(); // 時間
				int minute = day.getMinutes(); // 分
				int second = day.getSeconds(); // 秒
				if (hour / 12 == 0) { // 12時を基準にAM/PM決定
					am_pm = "AM"; // 12時前ならAM
				} else {
					am_pm = "PM"; // 12時以降ならPM
					hour = hour - 12;
				}
				String CT = hour + ":" + minute + ":" + second + " " + am_pm; // 時、分、秒、AM/PM情報を文字列で生成
				out.println("현재 접속  시각: " + CT + "\n"); // 結果出力
			%>    
        </div>
      </div>   
 	</div> 
	
	<%@ include file="footer.jsp"%>
   
  </div>
</body>
</html>
  