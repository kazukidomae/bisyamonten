<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// ログインの成功可否情報を取得する変数。
	int LoginOK = 0;
	// 表示する成功可否メッセージを格納する変数。
	String mes = "";
	// リクエストからログイン情報を取得。
	if(request.getAttribute("LoginOK") != null)
	{
		LoginOK = (Integer)request.getAttribute("LoginOK");
		// ログイン失敗の原因により表示するエラーメッセージを変化させる。
		switch(LoginOK)
		{
			case 0:
				mes = "<p>ログインに失敗しました。</p><p><a href=\"login.jsp\">ログイン画面へ戻る</a></p>";
				break;
			case 1:
				mes = "<p>ログインに成功しました！</p><p><a href=\"top.html\">トップページへ</a></p>";
				break;
			case 2:
				mes = "<p>ログインに失敗しました。</p><p><a href=\"login.jsp\">ログイン画面へ戻る</a></p>";
				break;
			case 3:
				mes = "<p>ログインに失敗しました。<br />警告:使用できない文字列が使われています。</p><p><a href=\"login.jsp\">ログイン画面へ戻る</a></p>";
				break;
			case 4:
				mes = "<p>ログインに失敗しました。</p><p><a href=\"login.jsp\">ログイン画面へ戻る</a></p>";
				break;
		}
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>ログイン結果</title>
</head>
<body>
	<h1>ログインの結果</h1>
	<%= mes %>
</body>
</html>