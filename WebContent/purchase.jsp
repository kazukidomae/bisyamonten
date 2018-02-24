<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	// 登録済み住所表示エリアに表示するHTMLタグなどを格納する文字列型変数。
	String mes = "";

	// リクエストから登録済み住所データが取得可能な場合は、登録済み住所データを表示する。
	if(request.getAttribute("tourokuzumiOk") != null)
	{
		// 登録済み住所データをまとめて取得し、表示用変数にデータを追加。
		mes = "<input type=\"radio\" name=\"seleadd\" value=\"1\" />";
		mes = mes + "<p>〒";
		mes = mes + (String)session.getAttribute("postalcode");
		mes = mes + "<br />";
		mes = mes + (String)session.getAttribute("prefectures");
		mes = mes + (String)session.getAttribute("city");
		mes = mes + (String)session.getAttribute("streetAddress");
		mes = mes + "<br />";
		mes = mes + (String)session.getAttribute("phoneNumber");
		mes = mes + "</p>";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>美車紋店　購入ページ</title>
	<form action="" method="post">
		<div>
			<%= mes %>
		</div>
		<div>
			<input type="radio" name="seleadd" value="2" />
			<p>
				登録外の住所へ配送
			</p>
		</div>
	</form>
</head>
<body>

</body>
</html>