<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="./css/login_regist.css" rel="stylesheet" type="text/css" />
<title>美車紋店 ログイン</title>
</head>
<body>
	<div id="wrap">
		<form action="Login" method="post">
			<div id="tbl">
				<dl>
					<dd>
						<!-- ユーザーIDまたは登録メールアドレスの入力フォーム -->
						<input type="text" name="UserId"  placeholder="ID or mail" size="20"/>
					</dd>
				</dl>
				<dl>
					<dd>
						<!-- パスワードの入力フォーム -->
						<input type="password" name="UserPass"  placeholder="password" size="20"/>
					</dd>
				</dl>
				<div class="btn">
					<!-- ログインボタン -->
					<input type="submit" name="S_Login" value="ログイン"/>
				</div>
				<dl>
					<dd>
							<a class="forget" href="pass_forget.html">パスワードを忘れた方はこちら</a>
					</dd>
				</dl>
			</div>
		</form>

		<div class="btn">
			<!-- 新規会員登録ページへのリンク -->
			<input type="button" onclick="location.href='insert.jsp'" value="新規会員登録">
		</div>
	</div>
</body>
</html>