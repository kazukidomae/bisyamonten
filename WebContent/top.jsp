<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	//セッションに格納されているユーザーIDを格納する変数。
	String UserID = "";
	// メインページに表示させるユーザーID情報やログインページへのリンクを格納する。
	String mes = "";

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
			mes = "ようこそ:　" + UserID + "さん";
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
<!doctype html>
<html>

	<head>
		<meta charset="UTF-8">

		<!-- css読み込み -->
		<link href="./css/reset.css" rel="stylesheet" type="text/css" />
		<link rel="stylesheet" href="css/style.css">
		<link rel="stylesheet" href="css/jquery.skippr.css">
		<link rel="stylesheet" href="./css/nav.css" type="text/css" />

		<!-- javascript読み込み -->
		<script src="js/jquery-1.11.0.min.js"></script>
		<script src="js/jquery.skippr.js"></script>
		<title>痛車屋 美車紋店</title>
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
					<li><a href="top.jsp">トップ</a></li>
					<li><a href="design.jsp">デザイン</a></li>
					<li><a href="gallery">ギャラリー</a></li>
					<li><a href="mypage">マイページ</a></li>
				</ul>
			</nav>
		</header>

		<!-- スライダーエリア -->
		<div class="slider">
			<div id="theTarget">
				<div style="background-image: url(image/slider1.png)"></div>
				<div style="background-image: url(image/slider2.png)"></div>
				<div style="background-image: url(images/image3.jpg)"></div>
			</div>
		</div>

		<!-- 更新情報カラム -->
		<div class="news">
			<p></p>
		</div>

		<!-- Twitter -->
		<div class="twitter">
			<a class="twitter-timeline"  href="https://twitter.com/search?q=%40bisyamounten" data-widget-id="821239604820877313">
				@bisyamountenに関するツイート
			</a>
			<script>
				!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+"://platform.twitter.com/widgets.js";fjs.parentNode.insertBefore(js,fjs);}}(document,"script","twitter-wjs");
			</script>
		</div>

		<!-- フッター -->
		<footer>
			<p>&copy;2017 湘南爆走族</p>
		</footer>

		<!-- スライドスクリプト  -->
		<script>
		$(document).ready(function(){
		            $("#theTarget").skippr();
		        });

		$("#theTarget").skippr({
		    // スライドタイプ ("fade" or "slclasse")
		    transition :'fade',
		    // 次のスライドまでの移行時間(単位ミリ秒)
		    speed : 1000,
		    // イージングの種類(http://easings.net/ja)
		    easing :'easeOutQuart',
		    // ナヴ・タイプ("block" or "bubble")
		    navType :'bubble',
		    // 子要素の種類("div" or "img")
		    childrenElementType :'div',
		    // 矢印の表示有無(trueで表示)
		    arrows :false,
		    // スライドショーの自動再生
		    autoPlay :true,
		    // 自動再生時のスライド移行時間(単位ミリ秒)
		    autoPlayDuration : 10000,
		    // 矢印キーの有効化
		    keyboardOnAlways :false,
		    // 一枚目のスライドに戻る矢印の表示の有無
		    hclassePrevious :false,
		});
		</script>
	</body>
</html>
