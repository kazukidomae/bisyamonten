<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// ここから　変数宣言・初期化スペース
	// セッションに格納されているユーザーIDを格納する変数。
	String UserID = "";
	// メインページに表示させるユーザーID情報やログインページへのリンクを格納する。
	String mes = "";
	// ここまで　変数宣言・初期化スペース

	// ユーザーID情報が格納されたセッションが存在するか調べる。
	if(request.getCookies() != null)
	{
		// 保存されているCookieを取得する。
		Cookie cookies[] = request.getCookies();
		for(int i=0;i<cookies.length;i++)
		{
			if(cookies[i].getName().equals("UserID"))
			{
				// クッキーにユーザーID情報が格納されていた場合は、クッキーからユーザーID情報を取得する。
				UserID = cookies[i].getValue();
			}
		}
		// ユーザーIDのcookieに何も値が入っていない場合、直近30分以内にログインしていないものとみなし、ログイン画面へ誘導するメッセージを出力する。
		if(UserID.equals(""))
		{
			// mes = "<p>サービスをご利用の際は、ログインをお願いします。</p><p><a href=\"login.jsp\">ログイン画面へ戻る</a></p>";
			// 自動的にログイン画面へ遷移させる。
			%>
				<jsp:forward page="login.jsp" />
			<%
		}
		// ユーザーIDのcookieに何か値が入っている場合、直近30分以内にログインしたとみなし、メインページの画面を表示する。
		else
		{
			mes = "<p>現在ログイン中のユーザー:　" + UserID + "さん</p>";
			// ページ上にログアウトボタンを追加。
			mes = mes + "<form action=\"MainPage\" method=\"POST\">";
			mes = mes + "<input type=\"submit\" name=\"S_Mypage\" value=\"マイページ\">";
			mes = mes + "<input type=\"submit\" name=\"S_Logout\" value=\"ログアウト\" />";
			mes = mes + "</form>";
		}
		// 最後にログインしてから30分以内(セッションがまだ存在している)の場合
		// 現在ログイン中のユーザーIDを出力する。(セッションの場合)
		// UserID = (String)session.getAttribute("UserID");

	}
	else
	{
		// 最後にログインしてから30分経過、もしくはログインしていないのにアクセスした場合
		// ログインページに移動するよう促すメッセージを出力。
		// mes = "<p>サービスをご利用の際は、ログインをお願いします。</p><p><a href=\"login.jsp\">ログイン画面へ戻る</a></p>";
		// 自動的にログイン画面へ遷移させる。
		%>
			<jsp:forward page="login.jsp" />
		<%
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>痛車暴走族メインページ</title>
</head>
<body>
	<%= mes %>
</body>
</html>