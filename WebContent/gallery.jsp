<%@page import="jp.ac.hal.login.GalleryBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// セッションに格納されているユーザーIDを格納する変数。
	String UserID = "";
	// メインページに表示させるユーザーID情報やログインページへのリンクを格納する。
	String mes = "";
	// お気に入りの登録結果を伝えるメッセージを格納。
	String comp_mes = "";
	// リクエストから受け取ったお気に入りのDB処理結果を格納。
	boolean db_res = false;

	ArrayList<GalleryBean> galleryBeans = new ArrayList<GalleryBean>();

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

	// リクエストからお気に入りDB処理結果を取得。
	if(request.getAttribute("db_res") != null)
	{
		db_res = (Boolean)request.getAttribute("db_res");
		// DBの処理結果によりcomp_mesに表示させるメッセージを変更。
		if(db_res == true)
		{
			// 成功
			comp_mes = "お気に入りに追加しました。";
		}
		else
		{
			// 失敗
			comp_mes = "お気に入りの追加に失敗しました。";
		}
	}
	if(request.getAttribute("galleryBeans") != null)
	{
		galleryBeans = (ArrayList<GalleryBean>)request.getAttribute("galleryBeans");
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>美車紋店 ギャラリー</title>

<!-- css読み込み  -->
<link href="./css/gallery.css" rel="stylesheet" type="text/css" />
<link href="./css/reset.css" rel="stylesheet" type="text/css" />
<link href="./css/modal.css" rel="stylesheet" type="text/css" />
<link href="./css/nav.css" rel="stylesheet" type="text/css" />
<link href="./css/footer.css" rel="stylesheet" type="text/css" />


<!-- ビューポートの設定 -->
<!-- モーダルウィンドウ -->
<meta name="viewport" content="width=device-width, initial-scale=1.0">

</head>
<body>
	<script type="text/javascript">
		// 3Dキャプチャ画像の読み込み用変数をここで初期化。
		// キャプチャ画像のファイル名。
		var cap_openname;
		// キャプチャ画像の保存先URL。
		var cap_openurl;
		// imgタグにsrcを指定する際のエレメント取得でエレメントを格納する変数。
		var cap_ele;
	</script>
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
		<!-- お気に入りの登録結果を表示するメッセージエリア。 -->
		<div id="message">
			<p><%= comp_mes %></p>
		</div>
	</header>

	<div class="thumbnail_wrapper">
		<!-- サムネイル表示 -->
		<div class="thumbnail">
		<% for( GalleryBean galleryBean : galleryBeans ){%>
			<ul>
				<!-- サムネイル表示 -->
				<li>
					<a class="modal-open" >
						<img src="mygallery/1.png" id="cap<%= galleryBean.getWorkid() %>">

						<!-- 作品ID -->
						<input type="hidden" name="workId" value="<%= galleryBean.getWorkid() %>">
						<!-- ユーザーID -->
						<input type="hidden" name="userId" value="<%= galleryBean.getUserid() %>">
						<!-- ユーザーネーム -->
						<input type="hidden" name="userName" value="<%= galleryBean.getUsername() %>">
						<!-- 制作日時 -->
						<input type="hidden" name="date" value="<%= galleryBean.getWorkdata() %>">
						<!-- いいね数 -->
						<input type="hidden" name="nice" value="<%= galleryBean.getNice() %>">
						<!-- お気に入り -->
						<input type="hidden" name="favorite" value="<%= galleryBean.getFavorite() %>">
					</a>
				</li>
			</ul>

			<!-- ギャラリーの各作品エリアに該当作品のキャプチャ画像を表示するための処理。 -->
			<script type="text/javascript">
				cap_openname = "cap" + <%= galleryBean.getWorkid() %>;
				cap_openurl = window.localStorage.getItem(cap_openname);
				// 痛車のキャプチャ画像が上手く読み込めた場合は該当の痛車のキャプチャ画像を、読み込めなかった場合は読み込み失敗用の画像を表示させる。
				cap_ele = document.getElementById("cap" + <%= galleryBean.getWorkid() %>);
				if(cap_openurl != null)
				{
					// 画像の読み込み成功
					cap_ele.src = cap_openurl;
				}
				else
				{
					// 画像の読み込み失敗
					cap_ele.src = "mygallery/nodata.png";
				}
			</script>

		<% } %>
		</div>


		<!-- モーダルウィンドウ -->
		<div id="modal-content">
			<input type="button" id="modal-close" name="return" value="×">
			<div id="view" class="contentsize"><img src=""></div>
			<div id="info" class="contentsize">
				<div class="opus">
					<!-- ユーザID -->
					<pre id="Id"></pre>
					<!-- ユーザネーム -->
					<pre id="Name"></pre>
					<!-- 制作日時 -->
					<pre id="Data"></pre>
					<!-- カテゴリー表示処理結果 -->
					<pre id="data"></pre>
				</div>

				<div id="favorite">
					<!-- いいね数 -->
					<div class="favorite">
						<input type="image" id="Nicebutton" name="favorite1" src="./images/drawer_img/heart0.png" onClick="this.src='./images/drawer_img/heart1.png'"/>
						<input type="hidden" name="Nicecount" value="" id="Nc">
						<p id="Nice"></p>
					</div>
					<!-- お気に入り数-->
					<div class="favorite">
						<form action="AddFavorite" name="favform" id="favform" method="post">
							<input type="hidden" name="FrWorkID" id="frwork" value="">
						</form>
						<input type="image" id="Favoritebutton" name="favorite2" src="./images/drawer_img/star0.png" onclick="document.favform.submit()"/>
						<p id="Favorite"></p>
					</div>
				</div>

			</div>
		</div>
	</div>

	<!-- フッター -->
	<footer>
		<p>&copy;2017 湘南爆走族</p>
	</footer>

	<!-- JavaScriptの読み込み -->
	<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script> -->
	<script type="text/javascript" src="./script/jquery-3.0.0.min.js"></script>
	<script type="text/javascript" src="./script/modal.js"></script>

	<!--  -->

</body>
</html>