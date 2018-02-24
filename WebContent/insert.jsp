<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// 入力エラーメッセージの内容を格納する変数。
	String InsErrMsg = "";

	// 入力エラーメッセージの内容をリクエストから取得。
	if(request.getAttribute("InsErrMsg") != null)
	{
		InsErrMsg = (String)request.getAttribute("InsErrMsg");
	}
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link href="./css/login_regist.css" rel="stylesheet" type="text/css" />
	<title>会員登録</title>
</head>
<body>
	<div id="wrap">
		<form action="Login" method="post">
			<div id="tbl">
				<p class="inserrmsg">
					<%= InsErrMsg %>
				</p>
				<dl>
					<dd>
						<input type="text" name="InUserMail"  placeholder="mail"/>
					</dd>
				</dl>
				<dl>
					<dd>
						<input type="text" name="InUserID"  placeholder="ID" size="20"/>
					</dd>
				</dl>
				<dl>
					<dd>
						<input type="text" name="InUserName"  placeholder="ユーザ名" size="20"/>
					</dd>
				</dl>
				<dl>
					<dd>
						<input type="password" name="InUserPass"  placeholder="password" size="20"/>
					</dd>
				</dl>
				<div class="btn">
					<input type="submit" name="S_Insert" value="会員登録"/>
				</div>
			</div>
		</form>

		<div class="btn">
			<input type="button" onclick="location.href='login.jsp'"value="ログイン">
		</div>
	</div>
</body>
</html>