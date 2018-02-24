<%@page import="jp.ac.hal.login.UsestickerBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="jp.ac.hal.login.UserBean"%>
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
	//購入方法取得
	String payment = (String)request.getAttribute("payment");
	//登録済情報取得
	UserBean address = (UserBean)request.getAttribute("address");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="./css/nav.css" rel="stylesheet" type="text/css" />
<link href="./css/reset.css" rel="stylesheet" type="text/css" />
<link href="./css/buy.css" rel="stylesheet" type="text/css" />
<link href="./css/footer.css" rel="stylesheet" type="text/css" />
<title>購入確認</title>
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
	<div id="wapper">
		<!-- 商品購入 -->
			<!-- 購入ステッカー一覧 -->
			<div id="price">
				<div id="viewer"><img src="./mygallery/nodata.png" id="cap<%= usestickerBeans.get(0).getWorkid() %>"></div>
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
							<% int sumSS=0; %>
							<% int sumS=0; %>
							<% int sumM=0; %>
							<% int sumL=0; %>
							<% int sumLL=0; %>
							<!-- 使用ステッカー枚数表示 -->
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
								<th class="r1">合計枚数</th>
								<td class="r2"><%= sumSS %></td>
								<td class="r2"><%= sumS %></td>
								<td class="r2"><%= sumM %></td>
								<td class="r2"><%= sumL %></td>
								<td class="r2"><%= sumLL %></td>
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
					<form action="Buy" method="post">
						<input type="submit" name="return" value="戻る" >
						<input type="hidden" name="workID" value="<%=usestickerBeans.get(0).getWorkid() %>">
					</form>
				</div>

				<!-- 購入確認画面に表示される購入予定の痛車の画像を、該当の痛車のキャプチャ画像が保存されている場合はそちらの画像に変更させる。存在しない場合はノーデータの画像とする。 -->
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

			</div>
			<div id="shipping">
				<div id="pay">
					<p>お支払方法</p>
					<table>
						<tr>
							<td>WebMoney</td>
							<td><%=payment %></td>
						</tr>
						<tr>
							<td>クレジットカード</td>
							<td><%=payment %></td>
						</tr>
					</table>
				</div>
				<div id="address">
					<dl>
						<dt class="add">姓</dt>
						<dd class="add"><%=address.getSurname() %></dd>
						<dt class="add">名</dt>
						<dd class="add"><%=address.getName() %></dd>
						<dt class="add">〒</dt>
						<dd class="add"><%=address.getPostalcode() %></dd>
						<dt class="add">住所</dt>
						<dd class="add"><%=address.getPrefectures() %></dd>
						<dd class="add"><%=address.getCity() %></dd>
						<dd class="add"><%=address.getStreetaddress() %></dd>
						<dt class="add">電話番号</dt>
						<dd class="add"><%=address.getPhonenumber() %></dd>
					</dl>
					<div>
						<a href="PurchaseComp"><input type="button" value="購入"></a>
					</div>
				</div>
			</div>
	</div>
	<!-- フッター -->
	<footer>
		<p>&copy;2017 湘南爆走族</p>
	</footer>
</body>
</html>