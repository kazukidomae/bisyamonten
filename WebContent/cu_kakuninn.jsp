<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// ここから　変数宣言・初期化スペース

	// Cookieに現在格納されているユーザーIDを格納。
	String cookUserID = "";
	// ログイン中のユーザーの表示内容を格納。
	String mes = "";

	// リクエストから取得したユーザーIDを格納。
	String userid = "";
	// リクエストから取得したユーザーネームを格納。
	String username = "";
	// リクエストから取得したツイッターIDを格納。
	String twiId = "";
	// リクエストから取得したメールアドレスを格納。
	String mail = "";

	// セッションから取得した秘密の質問の一覧アレイリスト。
	// 質問1用
	ArrayList<Object> sQuestionList1 = null;
	// 質問2用
	ArrayList<Object> sQuestionList2 = null;
	// セッションから取得した秘密の質問のID一覧アレイリスト。
	// 質問1用
	ArrayList<Object> sQuestionID1 = null;
	// 質問2用
	ArrayList<Object> sQuestionID2 = null;

	// ユーザーが選択した秘密の質問のIDを格納する配列。
	int[] usSQ = null;
	// ユーザーの秘密の質問に対する回答データ。
	// 質問1
	String secA1 = "";
	// 質問2
	String secA2 = "";

	// ユーザーが選んだ秘密の質問の質問内容(ページに表示させる)。
	// 質問1
	String secQ1 = "";
	// 質問2
	String secQ2 = "";

	// ここまで　変数宣言・初期化スペース

	// リクエストからの値がnullでない場合は、各個人情報のデータをリクエストから取得し、変数に格納する。
	if(request.getAttribute("userid") != null)
	{
		userid = (String)request.getAttribute("userid");
	}
	if(request.getAttribute("username") != null)
	{
		username = (String)request.getAttribute("username");
	}
	if(request.getAttribute("twiId") != null)
	{
		twiId = (String)request.getAttribute("twiId");
	}
	if(request.getAttribute("mail") != null)
	{
		mail = (String)request.getAttribute("mail");
	}

	// 秘密の質問の回答を取得。
	// 質問1
	if(request.getAttribute("secA1") != null)
	{
		secA1 = (String)request.getAttribute("secA1");
	}
	// 質問2
	if(request.getAttribute("secA2") != null)
	{
		secA2 = (String)request.getAttribute("secA2");
	}

	// リクエストからユーザーが選んだ秘密の質問のIDを取得。
	if(request.getAttribute("usSQ") != null)
	{
		usSQ = (int[])request.getAttribute("usSQ");
	}

	// セッションからの値がnullでなければ、セッションから秘密の質問内容一覧を取得する。
	// 質問1用。
	if(session.getAttribute("sQuestionList1") != null)
	{
		sQuestionList1 = (ArrayList<Object>)session.getAttribute("sQuestionList1");
	}
	// 質問2用。
	if(session.getAttribute("sQuestionList2") != null)
	{
		sQuestionList2 = (ArrayList<Object>)session.getAttribute("sQuestionList2");
	}
	// セッションからの値がnullでなければ、秘密の質問ID一覧のアレイリストをリクエストから取得する。
	// 質問1用。
	if(session.getAttribute("sQuestionID1") != null)
	{
		sQuestionID1 = (ArrayList<Object>)session.getAttribute("sQuestionID1");
	}
	// 質問2用。
	if(session.getAttribute("sQuestionID2") != null)
	{
		sQuestionID2 = (ArrayList<Object>)session.getAttribute("sQuestionID2");
	}

	// 質問のIDと内容のリストが正常に取得できた場合は、ユーザーが選んだ質問の番号と同じ番号の質問の内容を表示用の変数に格納する。
	// 質問1
	if(sQuestionID1 != null && sQuestionList1 != null && usSQ != null)
	{
		// リストに格納されている質問の番号とユーザーが選んだ質問の番号を個別に比較。
		for(int i=0;i<sQuestionID1.size();i++)
		{
			// ユーザーが選んだ秘密の質問番号と比較中のリストの番号が一致
			if( usSQ[1] == Integer.parseInt((String)sQuestionID1.get(i)) )
			{
				// 秘密の質問1の内容表示用変数に該当の質問を格納。
				secQ1 = (String)sQuestionList1.get(i);
			}
		}
	}
	// 質問2
	if(sQuestionID2 != null && sQuestionList2 != null && usSQ != null)
	{
		// リストに格納されている質問の番号とユーザーが選んだ質問の番号を個別に比較。
		for(int i=0;i<sQuestionID2.size();i++)
		{
			// ユーザーが選んだ秘密の質問番号と比較中のリストの番号が一致
			if( usSQ[2] == Integer.parseInt((String)sQuestionID2.get(i)) )
			{
				// 秘密の質問2の内容表示用変数に該当の質問を格納。
				secQ2 = (String)sQuestionList2.get(i);
			}
		}
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
	<title>痛車屋　美車紋店　ユーザー情報の変更確認</title>
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
				<li>ID:<%= userid %></li>
				<li>ユーザ名:<%= username %></li>
				<li>TwitterID:<%= twiId %></li>
				<li>メールアドレス:<%= mail %></li>
				<li>秘密の質問1選択:<%= secQ1 %></li>
				<li>秘密の質問1の答え:<%= secA1 %></li>
				<li>秘密の質問2選択:<%= secQ2 %></li>
				<li>秘密の質問2の答え:<%= secA2 %></li>
			</ul>
			<form action="UserInfo" method="post">

				<!-- ボタン -->
				<input type="submit" name="S_backtoCU" value="設定画面へ戻る" />
				<input type="submit" name="S_CUtoDB" value="変更" />

				<!--  設定画面へ戻った際に先ほど入力した個人情報をデフォルト値として表示させるためのhiddenタグ。 -->
				<input type="hidden" name="userid" value="<%= userid %>" />
				<input type="hidden" name="username" value="<%= username %>" />
				<input type="hidden" name="twiId" value="<%= twiId %>" />
				<input type="hidden" name="mail" value="<%= mail %>" />

				<!-- 各秘密の質問の答えをデフォルト値として表示させるためのhiddenタグ。 -->
				<input type="hidden" name="secA1" value="<%= secA1 %>" />
				<input type="hidden" name="secA2" value="<%= secA2 %>" />
				<input type="hidden" name="secQ1" value="<%= usSQ[1] %>" />
				<input type="hidden" name="secQ2" value="<%= usSQ[2] %>" />
			</form>
		</div>
</body>
</html>