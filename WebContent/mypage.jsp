<%@page import="jp.ac.hal.login.GalleryBean"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	// ここから　変数宣言・初期化スペース
	// セッションに格納されているユーザーIDを格納する変数。
	String UserID = "";
	// メインページに表示させるユーザーID情報やログインページへのリンクを格納する。
	String mes = "";
	// 登録処理結果に関するメッセージを格納する変数。
	String comp_mes = "";
	// 退会処理で発生したエラーの種類を格納する変数。　　0:原因不明　1:パスワードが登録パスワードと異なる　2:パスワードが入力されていない　3:不正な文字列が使用されている
	int wd_error_reason = 0;
	// ギャラリー情報を格納するアレイリスト変数。
	ArrayList<GalleryBean> galleryBeans = new ArrayList<GalleryBean>();

	// ☆ユーザー情報変更タブで扱う情報を格納する変数☆
	// リクエストから取得したユーザーIDを格納する変数。
	String userid = "";
	// リクエストから取得したユーザーネームを格納する変数。
	String username = "";
	// リクエストから取得したツイッターIDを格納する変数。
	String twiId = "";
	// リクエストから取得したメールアドレスを格納する変数。
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

	// リクエストから取得した秘密の質問のユーザー選択情報。
	// 秘密の質問の種類(ID)。
	int[] usSQ = null;
	// 秘密の各質問に対する回答。
	String[] usSQAnswer = null;

	// リクエストから取得した、各個人情報の入力状況データを格納するアレイリスト。
	ArrayList<Integer> checkRes = null;
	// 入力エラーメッセージの一覧を格納する文字列型アレイリスト。
	ArrayList<String> errmsg = new ArrayList<String>();
	// 入力項目名を順にまとめた配列変数。
	String ca_inname[] = {"ID","ユーザ名","TwitterID","メールアドレス","秘密の質問1の答え","秘密の質問2の答え"};

	// HTML上に出力させる秘密の質問の項目一覧の文字列を格納する変数。
	// 質問1用
	String sQuestion1 = "";
	// 質問2用
	String sQuestion2 = "";

	// 秘密の質問1の画面上に出力させるユーザーの回答。
	String outSQ1 = "";
	// 秘密の質問2の画面上に出力させるユーザーの回答。
	String outSQ2 = "";

	// ☆個人情報変更タブ関連の変数
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
	// リクエストから取得した、各個人情報の入力状況データを格納するアレイリスト。
	ArrayList<Integer> caCheck = null;
	// 入力項目名を順にまとめた配列変数。
	String ca_inname_ac[] = {"ユーザーの氏","ユーザーの名","郵便番号","住所の都道府県","住所の市区町村","住所の町域以下","電話番号"};

	// ここまで　変数宣言・初期化スペース

	// ☆ユーザー情報変更タブで現在データベースに設定中のユーザー情報を表示させるための処理。
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

	// リクエストからの値がnullでなければ、質問の種類とその質問に対する回答をリクエストから取得。
	// 質問の種類
	if(request.getAttribute("usSQ") != null)
	{
		usSQ = (int[])request.getAttribute("usSQ");
	}
	// 質問に対する回答
	if(request.getAttribute("usSQAnswer") != null)
	{
		usSQAnswer = (String[])request.getAttribute("usSQAnswer");

		// 取得した回答の配列変数から質問1と質問2の回答を取得する。
		// 質問1の回答
		outSQ1 = usSQAnswer[1];
		// 質問2の回答
		outSQ2 = usSQAnswer[2];
	}
	// 質問1と2の回答の入力チェックに引っかかった場合に個別に送られるリクエストが存在する場合は、そちらを取得する。
	else if(request.getAttribute("secA1") != null || request.getAttribute("secA2") != null)
	{
		if(request.getAttribute("secA1") != null)
		{
			outSQ1 = (String)request.getAttribute("secA1");
		}
		if(request.getAttribute("secA2") != null)
		{
			outSQ2 = (String)request.getAttribute("secA2");
		}
	}

	// セッションからの値がnullでなければ、秘密の質問の内容一覧をリクエストから取得する。
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

	// リクエストからの質問内容と質問のIDの取得がどちらもnull以外の場合のみ、秘密の質問一覧アレイリストを取得し、画面に質問の一覧を表示させるためにHTMLコードを生成する。
	// 質問1部分
	if(sQuestionList1 != null && sQuestionID1 != null && usSQ != null)
	{
		// HTMLコードの生成処理。
		for(int i=0;i<sQuestionList1.size();i++)
		{
			// 質問1部分
			// DB上にユーザーの選択質問データがある場合、該当の質問をデフォルト値として選択させる。optionのvalue値は、秘密の質問のIDとする。
			sQuestion1 = sQuestion1 + "<option value=\"" + (String)sQuestionID1.get(i) + "\" ";
			// 現在処理中の質問1のIDがユーザーが質問1で選択した質問のIDと一致した場合は、タグにselectedを追加する。
			if( usSQ[1] == Integer.parseInt((String)sQuestionID1.get(i)) )
			{
				sQuestion1 = sQuestion1 + "selected";
			}
			sQuestion1 = sQuestion1 + ">" + (String)sQuestionList1.get(i) + "</option>";
		}
	}

	// 質問2部分
	if(sQuestionList2 != null && sQuestionID2 != null && usSQ != null)
	{
		// HTMLコードの生成処理。
		for(int i=0;i<sQuestionList2.size();i++)
		{
			// 質問2部分
			// DB上にユーザーの選択質問データがある場合、該当の質問をデフォルト値として選択させる。optionのvalue値は、秘密の質問のIDとする。
			sQuestion2 = sQuestion2 + "<option value=\"" + (String)sQuestionID2.get(i) + "\" ";
			// 現在処理中の質問1のIDがユーザーが質問1で選択した質問のIDと一致した場合は、タグにselectedを追加する。
			if( usSQ[2] == Integer.parseInt((String)sQuestionID2.get(i)) )
			{
				sQuestion2 = sQuestion2 + "selected";
			}
			sQuestion2 = sQuestion2 + ">" + (String)sQuestionList2.get(i) + "</option>";
		}
	}

	// リクエストからの値がnullでない場合は、リクエストから入力状況データのアレイリストを取得する。
	if(request.getAttribute("checkRes") != null)
	{
		checkRes = (ArrayList<Integer>)request.getAttribute("checkRes");
		// 入力した項目にエラーがあることを促すメッセージ。
		errmsg.add("以下の入力項目に誤りがあります。");
		for(int i=0;i<checkRes.size();i++)
		{
			// 空白・未入力の入力項目が見つかった場合
			if(checkRes.get(i) == 1)
			{
				errmsg.add("必須項目の" + ca_inname[i] + "が未入力です！");
			}
			// 不正な値が使われている入力項目が見つかった場合
			else if(checkRes.get(i) == 2)
			{
				errmsg.add("必須項目の" + ca_inname[i] + "に不正な文字列が使われています。再入力してください。");
			}
		}

		// 秘密の質問の種類が選択されていない(デフォルト値)の場合は、質問を選択するよう促すエラーメッセージを追加。
		if(usSQ != null) // nullの場合はスキップ。
		{
			// 秘密の質問の質問数分繰り返す。
			for(int i=1;i<usSQ.length;i++)
			{
				if(usSQ[i] == 0)
				{
					errmsg.add("秘密の質問" + i + "の種類が選択されていません！");
				}
			}
		}

	}
	// ☆ユーザー情報関連処理　ここまで

	// ☆個人情報変更関連処理　ここから
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

	// リクエストからの値がnullでない場合は、リクエストから入力状況データのアレイリストを取得する。
	if(request.getAttribute("caCheck") != null)
	{
		caCheck = (ArrayList<Integer>)request.getAttribute("caCheck");
		// 入力した項目にエラーがあることを促すメッセージ。
		errmsg.add("以下の入力項目に誤りがあります。");
		for(int i=0;i<caCheck.size();i++)
		{
			// 空白・未入力の入力項目が見つかった場合
			if(caCheck.get(i) == 1)
			{
				errmsg.add("必須項目の" + ca_inname_ac[i] + "が未入力です！");
			}
			// 不正な値が使われている入力項目が見つかった場合
			else if(caCheck.get(i) == 2)
			{
				errmsg.add("必須項目の" + ca_inname_ac[i] + "に不正な文字列が使われています。再入力してください。");
			}
		}
	}
	// ☆個人情報変更関連処理　ここまで

	//データベースからギャラリー情報を取得
	if(request.getAttribute("galleryBeans") != null)
	{
		galleryBeans = (ArrayList<GalleryBean>)request.getAttribute("galleryBeans");
	}
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
			mes = "ようこそ:　" + UserID + "さん";
			// ページ上にログアウトボタンを追加。
			// mes = mes + "<form action=\"MainPage\" method=\"POST\">";
			// mes = mes + "<input type=\"submit\" name=\"S_ChangeAcount\" value=\"個人情報変更ページへ\" />";
			// mes = mes + "<input type=\"submit\" name=\"S_ChangeUser\" value=\"ユーザー情報変更ページへ\" />";
			// mes = mes + "<input type=\"submit\" name=\"S_Purchase\" value=\"購入ページへ\" />";
			// mes = mes + "<input type=\"submit\" name=\"S_Toppage\" value=\"トップページ\" />";
			// mes = mes + "<input type=\"submit\" name=\"S_Logout\" value=\"ログアウト\" />";
			// mes = mes + "</form>";
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

	// リクエストから登録処理結果に関するメッセージが取得できれば取得。
	if(request.getAttribute("comp_mes") != null)
	{
		comp_mes = (String)request.getAttribute("comp_mes");
	}

	// リクエストから退会処理のエラーの種類が取得できれば取得する。
	if(request.getAttribute("wd_error_reason") != null)
	{
		wd_error_reason = (Integer)request.getAttribute("wd_error_reason");
	}
	// リクエストから退会処理でのエラーフラグが取得できれば取得し、退会処理に失敗した旨のエラーメッセージを表示させる。
	if(request.getAttribute("wd_error") != null)
	{
		comp_mes = "退会処理が正常に完了しませんでした。";
		// 発生したエラーの種類に応じて、エラーの詳細説明を追加する。
		switch(wd_error_reason)
		{
			case 1:
				comp_mes = comp_mes + "入力されたパスワードが登録パスワードと異なります！";
				break;
			case 2:
				comp_mes = comp_mes + "パスワードが未入力です！";
				break;
			case 3:
				comp_mes = comp_mes + "不正な文字列が入力されました。";
				break;
		}
	}

%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="robots" content="noindex,nofollow">
		<!-- ビューポートの設定 -->
		<meta name="viewport" content="width=device-width, initial-scale=1.0">

		<!-- css読み込み -->
		<link href="./css/reset.css" rel="stylesheet" type="text/css" />
		<link href="./css/modal.css" rel="stylesheet" type="text/css" />
		<link href="./css/nav.css" rel="stylesheet" type="text/css" />
		<link href="./css/mypage.css" rel="stylesheet" type="text/css" />
		<link href="./css/input.css" rel="stylesheet" type="text/css" />
		<link href="./css/gallery.css" rel="stylesheet" type="text/css" />
		<link href="./css/footer.css" rel="stylesheet" type="text/css" />

		<title>美車紋店 マイページ</title>
	</head>
	<body>
		<!-- ヘッダー -->
		<header>
			<!-- ロゴ -->
			<a href="top.html"><img src="image/rogo.png"></a>
			<!-- ログイン中ユーザー表示 -->
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
			<!-- ユーザー変更処理後、個人情報変更処理後、退会処理でのエラー発生時に表示するメッセージエリア。 -->
			<div id="message">
				<p><%= comp_mes %></p>
			</div>
		</header>

		<!-- コンテンツエリア -->
		<div class="wrapper">
		<!-- タブ -->
		<div id="tabmenu">
		<div id="tab">
			<a href="#tab1">マイギャラリー</a>
			<a href="#tab2">お気に入り</a>
			<a href="#tab3">アカウント情報変更</a>
			<a href="#tab4">個人情報変更</a>
			<a href="#tab5">パスワード変更</a>
			<a href="#tab6">退会</a>
		</div>
		<div id="tab_contents">
		<div id="tab_page">

			<!-- マイギャラリー -->
			<div id="tab1">
				<div class="thumbnail_wrapper">
					<!-- サムネイル表示 -->
					<div class="thumbnail">
					<% for( GalleryBean galleryBean : galleryBeans ){%>
						<ul>
							<!-- サムネイル表示 -->
							<li>
								<a class="modal-open" >
									<img src="mygallery/nodata.png" id="cap<%= galleryBean.getWorkid() %>">
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
								// 画像の読み込みが成功したら、マイギャラリーの作品一覧に表示されるリンク画像を痛車のキャプチャ画像に変更する。
								cap_ele.src = cap_openurl;
							}
						</script>
					<% } %>
					</div>
				</div>
			</div>

			<!-- お気に入り -->
			<div id="tab2">
				<div class="thumbnail_wrapper">
					<!-- サムネイル表示 -->
					<div class="thumbnail">
					<% for( GalleryBean galleryBean : galleryBeans ){%>
						<ul>
							<!-- サムネイル表示 -->
							<li>
								<a class="modal-open" >
									<img src="mygallery/<%=galleryBean.getWorkid() %>.png">

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
					<% } %>
					</div>
				</div>
			</div>

			<!-- アカウント情報変更 -->
			<div id="tab3">
				<form action="UserInfo" method="post">
					<div class="info">
					<!-- 入力エラーが見つかった場合はここにエラーの原因を表示する。 -->
					<%
						for(int i=0;i<errmsg.size();i++)
						{
							%><p><%=
							errmsg.get(i)
							%></p><%
						}
					%>
						<dl>
							<dt>ID</dt>
							<dd>
								<input type="text" class="lbox" name="userid" placeholder="ID"  value="<%= userid %>" size="20"/>
							</dd>
						</dl>
						<dl>
							<dt>ユーザ名</dt>
							<dd>
								<input type="text" class="lbox" name="username"placeholder="ユーザ名" value="<%= username %>" size="20"/>
							</dd>
						</dl>
						<dl>
							<dt>TwitterID</dt>
							<dd>
								<input type="text" class="lbox" name="twitter" placeholder="TwitterID"  value="<%= twiId %>" size="20"/>
							</dd>
						</dl>
						<dl>
							<dt>メールアドレス</dt>
							<dd>
								<input type="text" class="lbox" name="mail" placeholder="mail" value="<%= mail %>" size="20"/>
							</dd>
						</dl>

						<!-- 秘密の質問①入力 -->
						<dl>
							<dt>秘密の質問①</dt>
							<dd>
								<select class="lbox" name="secQ1">
									<option value="0">秘密の質問①を選択してください</option>
									<%= sQuestion1 %>
								</select>
							</dd>
						</dl>
						<!-- 秘密の質問②答え入力 -->
						<dl>
							<dt></dt>
							<dd>
								<input type="text" class="lbox" name="secA1"placeholder="秘密の質問①の答えを入力してください" size="20" value="<%= outSQ1 %>" />
							</dd>
						</dl>

						<!-- 秘密の質問②入力 -->
						<dl>
							<dt>秘密の質問②</dt>
							<dd>
								<select class="lbox" name="secQ2">
									<option value="0">秘密の質問②を選択してください</option>
									<%= sQuestion2 %>
								</select>
							</dd>
						</dl>
						<!-- 秘密の質問②答え入力 -->
						<dl>
							<dt></dt>
							<dd>
								<input type="text" class="lbox" name="secA2"placeholder="秘密の質問②の答えを入力してください" size="20" value="<%= outSQ2 %>" />
							</dd>
						</dl>

						<!-- 確認ボタン -->
						<!-- <input type="hidden" name="S_CUKakuninn">-->
						<div class="btn">
							<input type="submit" name="S_CUKakuninn" value="変更確認"/>
						</div>
					</div>
				</form>
			</div>

			<!-- 個人情報変更 -->
			<div id="tab4">
				<form action="AcountInfo" method="post">
					<div class="info">
						<%
							// 入力エラーメッセージをこの部分に表示する。
							for(int i=0;i<errmsg.size();i++)
							{
								%><p><%=
								errmsg.get(i)
								%></p><%
							}
						%>
						<!-- 氏名入力 -->
						<dl>
							<dt>姓</dt>
							<dd>
								<input type="text" class="lbox" name="surname" placeholder="姓" size="20" value="<%= surname %>"/>
							</dd>
						</dl>

						<dl>
							<dt>名</dt>
							<dd>
								<input type="text" class="lbox" name="name" placeholder="名" size="20" value="<%= name %>"/>
							</dd>
						</dl>

						<!-- 郵便番号入力 -->
						<dl>
							<dt>〒</dt>
							<dd>
								<input type="text" class="lbox" name="postalcode" placeholder="1234567" size="20" value="<%= postalcode %>"/>
							</dd>
						</dl>

						<!-- 住所入力 -->
						<dl>
							<dt>住所</dt>
							<dd>
								<select name="prefectures">
									<option value="">都道府県</option>
									<option value="北海道">北海道</option>
									<option value="青森県">青森県</option>
									<option value="岩手県">岩手県</option>
									<option value="宮城県">宮城県</option>
									<option value="秋田県">秋田県</option>
									<option value="山形県">山形県</option>
									<option value="福島県">福島県</option>
									<option value="茨城県">茨城県</option>
									<option value="栃木県">栃木県</option>
									<option value="群馬県">群馬県</option>
									<option value="埼玉県">埼玉県</option>
									<option value="千葉県">千葉県</option>
									<option value="東京都">東京都</option>
									<option value="神奈川県">神奈川県</option>
									<option value="新潟県">新潟県</option>
									<option value="富山県">富山県</option>
									<option value="石川県">石川県</option>
									<option value="福井県">福井県</option>
									<option value="山梨県">山梨県</option>
									<option value="長野県">長野県</option>
									<option value="岐阜県">岐阜県</option>
									<option value="静岡県">静岡県</option>
									<option value="愛知県">愛知県</option>
									<option value="三重県">三重県</option>
									<option value="滋賀県">滋賀県</option>
									<option value="京都府">京都府</option>
									<option value="大阪府">大阪府</option>
									<option value="兵庫県">兵庫県</option>
									<option value="奈良県">奈良県</option>
									<option value="和歌山県">和歌山県</option>
									<option value="鳥取県">鳥取県</option>
									<option value="島根県">島根県</option>
									<option value="岡山県">岡山県</option>
									<option value="広島県">広島県</option>
									<option value="山口県">山口県</option>
									<option value="徳島県">徳島県</option>
									<option value="香川県">香川県</option>
									<option value="愛媛県">愛媛県</option>
									<option value="高知県">高知県</option>
									<option value="福岡県">福岡県</option>
									<option value="佐賀県">佐賀県</option>
									<option value="長崎県">長崎県</option>
									<option value="熊本県">熊本県</option>
									<option value="大分県">大分県</option>
									<option value="宮崎県">宮崎県</option>
									<option value="鹿児島県">鹿児島県</option>
									<option value="沖縄県">沖縄県</option>
								</select>
								<br><br>
								<input type="text" class="lbox" name="city" placeholder="市町村区" size="20" value="<%= city %>"/>
								<input type="text" name="streetAddress" placeholder="地区町村 ◌丁目◌番地 マンション名 部屋番号" value="<%= streetAddress %>"/>
							</dd>
						</dl>

						<!-- 電話番号入力 -->
						<dl>
							<dt>電話番号</dt>
							<dd>
								<input type="text" class="lbox" name="phoneNumber" placeholder="12345678910" size="20" value="<%= phoneNumber %>"/>
							</dd>
						</dl>

						<!-- クレジットカード番号入力 -->
						<dl>
							<dt>クレジットカード番号</dt>
							<dd>
								<input type="text" class="lbox" name="credit" placeholder="1234567891011121" size="20"/>
							</dd>
						</dl>

						<!-- 確認ボタン -->
						<input type="hidden" name="S_CAKakuninn">
						<div class="btn">
							<input type="submit" name="S_CAKakuninn" value="変更確認"/>
						</div>
					</div>
				</form>
			</div>

			<!-- パスワード変更 -->
			<div id="tab5">
				<form action="MainPage" method="post">
					<div class="up">
						<dl>
							<dt>現在のパスワード</dt>
							<dd>
								<input type="password" name="now_pass"placeholder="現在のパスワード" size="20"/>
							</dd>
						</dl>
						<dl>
							<dt></dt>
							<dd>
								<a class="forget" href="pass_forget.html">パスワードを忘れた方はこちら</a>
							</dd>
						</dl>
						<dl>
							<dt>新しいパスワード</dt>
							<dd>
								<input type="password" class="lbox" name="new_pass"placeholder="新しいパスワード" size="20"/>
							</dd>
						</dl>
						<dl>
							<dt>新しいパスワードを再入力</dt>
							<dd>
								<input type="password" class="lbox" name="renew_pass"placeholder="新しいパスワード" size="20"/>
							</dd>
						</dl>
						<div class="btn">
							<input type="submit" name="pass_change" value="パスワード変更"/>
						</div>
					</div>
				</form>
			</div>

			<!-- 退会 -->
			<div id="tab6">
				<form action="Withdrawal" method="post">
					<div class="up">
						<p>退会しますか？</p>
						<br/>
						<p>削除する前に</p>
						<p>・個人情報について</p>
						<p>　退会後１ヶ月間、弊社にて適切に保管させていただき、その後責任を持って削除いたします。</p>
						<dl>
							<dd>
								<input type="password" class="lbox" name="now_pass" placeholder="パスワードを入力してください" size="20"/>
							</dd>
						</dl>
						<div class="btn">
							<input type="submit" name="quit" value="退会する"/>
						</div>
					</div>
				</form>
			</div>

		</div>
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
							<input type="image" name="favorite2" src="./images/drawer_img/star0.png" onClick="this.src='./images/drawer_img/star1.png'"/>
							<p id="Favorite"></p>
						</div>
					</div>
					<div class="button-link">
						<form action="Buy" method="POST">
							<input type="submit" id="buy_btn" value="購入">
							<input type="hidden" name="workID" value="" id="wor">
						</form>
						<form action="Release" method="POST">
							<input type="submit" id="gallery" value="ギャラリーに公開">
							<input type="hidden" name="workID" value="" id="Release">
						</form>
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
		<!-- <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"></script>  -->
		<script type="text/javascript" src="./script/jquery-3.0.0.min.js"></script>
		<script src="./script/mymodal.js"></script>
	</body>
</html>

