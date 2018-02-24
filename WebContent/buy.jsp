<%@page import="jp.ac.hal.login.UsestickerBean"%>
<%@page import="java.util.ArrayList"%>
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


	ArrayList<UsestickerBean> usestickerBeans = (ArrayList<UsestickerBean>)request.getAttribute("usestickerBeans");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="./css/reset.css" rel="stylesheet" type="text/css" />
<link href="./css/buy.css" rel="stylesheet" type="text/css" />
<link href="./css/nav.css" rel="stylesheet" type="text/css" />
<link href="./css/footer.css" rel="stylesheet" type="text/css" />
<title>購入｜美車紋店</title>
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
				<li><a href="gallery">ギャラリー</a></li>
				<li><a href="mypage">マイページ</a></li>
			</ul>
		</nav>
	</header>
	<!-- 商品購入 -->
	<div id="wapper">
		<!-- 購入ステッカー一覧 -->
		<div id="price">
			<div id="viewer"><img src="./mygallery/nodata.png" id="cap<%=  usestickerBeans.get(0).getWorkid() %>"></div>
			<div id="subtotal">
				<p>購入商品</p>
				<table>
					<tbody>
						<tr>
							<th class="r1">ステッカー名</th>
							<th class="r1">SSサイズ</th>
							<th class="r1">Sサイズ</th>
							<th class="r1">Mサイズ</th>
							<th class="r1">Lサイズ</th>
							<th class="r1">LLサイズ</th>
						</tr>
					</tbody>
				</table>
				<!-- 購入商品一覧 -->
				<div id="detail">
				<table>
					<tbody>
						<!-- 使用ステッカー枚数表示 -->
						<% int sumSS=0; %>
						<% int sumS=0; %>
						<% int sumM=0; %>
						<% int sumL=0; %>
						<% int sumLL=0; %>
						<% for( UsestickerBean usestickerBean : usestickerBeans ){%>
						<tr>
							<th class="r1"><%=usestickerBean.getStickerName() %></th>
							<td class="r2"><%=usestickerBean.getNumberofsheetsSS() %></td>
							<td class="r2"><%=usestickerBean.getNumberofsheetsS() %></td>
							<td class="r2"><%=usestickerBean.getNumberofsheetsM() %></td>
							<td class="r2"><%=usestickerBean.getNumberofsheetsL() %></td>
							<td class="r2"><%=usestickerBean.getNumberofsheetsLL() %></td>

							<% sumSS = sumSS+usestickerBean.getNumberofsheetsSS(); %>
							<% sumS = sumS+usestickerBean.getNumberofsheetsS(); %>
							<% sumM = sumM+usestickerBean.getNumberofsheetsM(); %>
							<% sumL = sumL+usestickerBean.getNumberofsheetsL(); %>
							<% sumLL = sumLL+usestickerBean.getNumberofsheetsLL(); %>
						</tr>
						<%} %>
					</tbody>
				</table>
				</div>
				<table>
					<tbody>
						<tr>
							<th class="r1">合計</th>
							<td class="r2"><%= sumSS%></td>
							<td class="r2"><%= sumS%></td>
							<td class="r2"><%= sumM%></td>
							<td class="r2"><%= sumL%></td>
							<td class="r2"><%= sumLL%></td>
						</tr>
					</tbody>
				</table>
				<table>
					<tbody>
						<tr>
							<th class="r1">合計金額</th>
							<td class="r2"><%= sumSS = sumSS*usestickerBeans.get(0).getPriceSS() %></td>
							<td class="r2"><%= sumS = sumS* usestickerBeans.get(0).getPriceS()%></td>
							<td class="r2"><%= sumM = sumM* usestickerBeans.get(0).getPriceM()%></td>
							<td class="r2"><%= sumL = sumL* usestickerBeans.get(0).getPriceL()%></td>
							<td class="r2"><%= sumLL = sumLL* usestickerBeans.get(0).getPriceLL()%></td>
						</tr>
					</tbody>
				</table>
				<table>
					<tbody>
						<tr>
							<th>計</th>
							<td class="r2"><%= sumSS+sumS+sumM+sumL+sumLL %></td>
						</tr>
					</tbody>
				</table>
			</div>
			<div>
				<a href="mypage"><input type="button" name="return" value="戻る" ></a>
			</div>
		</div>

		<!-- 購入画面に表示される購入予定の痛車の画像を、該当の痛車のキャプチャ画像が保存されている場合はそちらの画像に変更させる。存在しない場合はノーデータの画像とする。 -->
		<script type="text/javascript">
			cap_openname = "cap" + <%= usestickerBeans.get(0).getWorkid() %>;
			cap_openurl = window.localStorage.getItem(cap_openname);
			// 痛車のキャプチャ画像が上手く読み込めた場合は該当の痛車のキャプチャ画像を、読み込めなかった場合は読み込み失敗用の画像を表示させる。
			cap_ele = document.getElementById("cap" + <%= usestickerBeans.get(0).getWorkid() %>);
			if(cap_openurl != null)
			{
				// 画像の読み込みが成功した場合は、該当の痛車のキャプチャ画像をページ上に表示させる。
				cap_ele.src = cap_openurl;
			}
		</script>

		<!-- 購入手続き -->
		<div id="shipping">
			<form action="Purchase" method="post" id="form">
				<!-- 支払い方法 -->
				<div id="pay">
					<p>お支払方法</p>
					<table>
						<tr>
							<td><input type="radio" name="pay" value="WebMoney" checked/></td>
							<td>WebMoney</td>
							<td><input type="text" maxlength='16' name="wmnum" placeholder="プリペイド番号"/></td>
						</tr>
						<tr>
							<td><input type="radio" name="pay" value="credit"/></td>
							<td>クレジットカード</td>
							<td><input type="text" maxlength='16' name="creditnum" placeholder="カード番号"/></td>
						</tr>
					</table>
				</div>

				<!-- 配送先 -->
				<div id="address">
					<dl>
						<!-- 登録済み住所表示処理 -->
						<dt><input type="radio" name="deli" value="address" checked/></dt>
						<dd>登録済みの住所に配送</dd>
						<dt><input type="radio" name="deli" value="newaddress"/></dt>
						<dd>登録外の住所に配送</dd>
					</dl>
					<dl>
						<dt class="add">姓</dt>
						<dd><input type="text" name="sumname" placeholder="姓"/></dd>

						<dt class="add">名</dt>
						<dd><input type="text" name="name" placeholder="名"/></dd>

						<dt class="add">〒</dt>
						<dd><input type="text" name="code" placeholder="1234567"/></dd>
						<dt class="add">住所</dt>
						<dd>
							<select name="prefect">
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
						</dd>
						<dd><input type="text" name="pality" placeholder="市町村区"/></dd>
						<dd><input type="text" name="city" placeholder="地区町村 ◌丁目◌番地 マンション名 部屋番号"/></dd>
						<dt class="add">電話番号</dt>
						<dd><input type="text" name="tell" placeholder="12345678910"/></dd>
					</dl>
					<div>
						<input type="submit" name="con" value="確認">
						<input type="hidden" name="workID" value="<%=usestickerBeans.get(0).getWorkid() %>">
					</div>
				</div>
			</form>
		</div>
	</div>
	<!-- フッター -->
	<footer>
		<p>&copy;2017 湘南爆走族</p>
	</footer>
</body>
</html>