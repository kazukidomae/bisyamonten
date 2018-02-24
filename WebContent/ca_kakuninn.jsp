<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	//Cookieに現在格納されているユーザーIDを格納。
	String cookUserID = "";
	// ログイン中のユーザーの表示内容を格納。
	String mes = "";

	// リクエストから取得したユーザーの氏を格納する変数。
	String surname = "";
	// リクエストから取得したユーザーの名を格納する変数。
	String name = "";
	// リクエストから取得した郵便番号を格納する変数。
	String postalcode = "";
	// リクエストから取得した都道府県を格納する変数。
	String prefectures = "";
	// リクエストから取得した市区町村を格納する変数。
	String city = "";
	// リクエストから取得した住所詳細情報を格納する変数。
	String streetAddress = "";
	// リクエストから取得した電話番号を格納する変数。
	String phoneNumber = "";

	// リクエストからの値がnullでない場合は、各個人情報のデータをリクエストから取得し、変数に格納する。
	if(request.getAttribute("surname") != null)
	{
		surname = (String)request.getAttribute("surname");
	}
	if(request.getAttribute("name") != null)
	{
		name = (String)request.getAttribute("name");
	}
	if(request.getAttribute("postalcode") != null)
	{
		postalcode = (String)request.getAttribute("postalcode");
	}
	if(request.getAttribute("prefectures") != null)
	{
		prefectures = (String)request.getAttribute("prefectures");
	}
	if(request.getAttribute("city") != null)
	{
		city = (String)request.getAttribute("city");
	}
	if(request.getAttribute("streetAddress") != null)
	{
		streetAddress = (String)request.getAttribute("streetAddress");
	}
	if(request.getAttribute("phoneNumber") != null)
	{
		phoneNumber = (String)request.getAttribute("phoneNumber");
	}

	// 現在ブラウザでCookieが何かしら格納されているかを調べ、格納されていたらユーザーIDが入っているCookieを探す。
	if(request.getCookies() != null)
	{
		// 保存されているCookieを取得する。
		Cookie cookies[] = request.getCookies();
		for(int i=0;i<cookies.length;i++)
		{
			if(cookies[i].getName().equals("UserID"))
			{
				// クッキーにユーザーID情報が格納されていた場合は、クッキーからユーザーID情報を取得する。
				cookUserID = cookies[i].getValue();
			}
		}
		// ユーザーIDのcookieに何も値が入っていない場合、直近30分以内にログインしていないものとみなし、ログイン画面へ誘導するメッセージを出力する。
		if(cookUserID.equals(""))
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
			mes = "ようこそ:　" + cookUserID + "さん";
		}
	}
	else
	{
		// Cookie自体が存在しない場合は、ログインしていないとみなし、強制的にログイン画面へ遷移させる。
		%>
			<jsp:forward page="login.jsp" />
		<%
	}


%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="./css/nav.css" rel="stylesheet" type="text/css" />
	<link href="./css/reset.css" rel="stylesheet" type="text/css" />
	<link href="./css/kakuninn.css" rel="stylesheet" type="text/css" />

	<title>痛車屋 美車紋店  個人情報の変更確認</title>
</head>
	<body>
		<!-- ヘッダー -->
		<header>
		<!-- ロゴ -->
		<a href="top.html"><img src="image/rogo.png"></a>
		<!-- 現在ログイン中のユーザー表示 -->
		<p id="loginuser"><%= mes %></p>
		<!-- 仮ログアウトボタン -->
		<form action="MainPage" method="post">
			<input type="submit" id="logout" name="S_Logout" value="ログアウト" />
		</form>
			<!-- グローバルナビ -->
			<nav class="navigation_wrapper">
			<!-- デザイン用ボックス -->
				<div></div>
				<!-- メニュー -->
				<ul class="navigation">
					<li><a href="top.html">トップ</a></li>
					<li><a href="design.jsp">デザイン</a></li>
					<li><a href="#">ギャラリー</a></li>
					<li><a href="mypage.jsp">マイページ</a></li>
				</ul>
			</nav>
		</header>
		<div id="wapper">
			<h1>以下の情報であなたのユーザー情報を更新します。よろしければ「変更」ボタンをクリックしてください。</h1>
			<ul>
				<li>ユーザーの氏:<%= surname %></li>
				<li>ユーザーの名:<%= name %></li>
				<li>郵便番号:<%= postalcode %></li>
				<li>住所の都道府県:<%= prefectures %></li>
				<li>住所の市区町村:<%= city %></li>
				<li>住所の町域以下:<%= streetAddress %></li>
				<li>電話番号:<%= phoneNumber %></li>
			</ul>
			<form action="AcountInfo" method="post">

				<!-- ボタン -->
				<div id="button">
					<input type="submit" name="S_backtoCA" value="設定画面へ戻る" />
					<input type="submit" name="S_CAtoDB" value="変更" />
				</div>

				<!--  設定画面へ戻った際に先ほど入力した個人情報をデフォルト値として表示させるためのhiddenタグ。 -->
				<input type="hidden" name="surname" value="<%= surname %>" />
				<input type="hidden" name="name" value="<%= name %>" />
				<input type="hidden" name="postalcode" value="<%= postalcode %>" />
				<input type="hidden" name="prefectures" value="<%= prefectures %>" />
				<input type="hidden" name="city" value="<%= city %>" />
				<input type="hidden" name="streetAddress" value="<%= streetAddress %>" />
				<input type="hidden" name="phoneNumber" value="<%= phoneNumber %>" />
			</form>
		</div>
	</body>
</html>