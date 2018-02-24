<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

// ページ上に表示させる大見出しの内容。
	String comp_title = "";
	// ページ上に表示させる本文の内容。
	String comp_mes = "";

	// リクエストから大見出しのデータを取得。
	if(request.getAttribute("comp_title") != null)
	{
		comp_title = (String)request.getAttribute("comp_title");
	}
	// リクエストから本文のデータを取得。
	if(request.getAttribute("comp_mes") != null)
	{
		comp_mes = (String)request.getAttribute("comp_mes");
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>痛車屋　美車紋店</title>
</head>
<body>
	<h1><%= comp_title %></h1>
	<p><%= comp_mes %></p>
	<p><a href="mypage.jsp">マイページへ戻る</a></p>
</body>
</html>