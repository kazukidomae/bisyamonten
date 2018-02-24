<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	// ここから　変数宣言・初期化スペース
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
	// 入力エラーメッセージの一覧を格納する文字列型アレイリスト。
	ArrayList<String> errmsg = new ArrayList<String>();
	// 入力項目名を順にまとめた配列変数。
	String ca_inname[] = {"ユーザーの氏","ユーザーの名","郵便番号","住所の都道府県","住所の市区町村","住所の町域以下","電話番号"};
	// ここまで　変数宣言・初期化スペース

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
				errmsg.add("必須項目の" + ca_inname[i] + "が未入力です！");
			}
			// 不正な値が使われている入力項目が見つかった場合
			else if(caCheck.get(i) == 2)
			{
				errmsg.add("必須項目の" + ca_inname[i] + "に不正な文字列が使われています。再入力してください。");
			}
		}
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>痛車屋　美車紋店　アカウント情報変更</title>
</head>
<body>
	<h1>アカウント情報追加・変更画面</h1>
	<%
		// 入力エラーメッセージをこの部分に表示する。
		for(int i=0;i<errmsg.size();i++)
		{
			%><p><%=
			errmsg.get(i)
			%></p><%
		}
	%>
	<form action="AcountInfo" method="post">
		<label>ユーザーの氏</label>
		<input type="text" name="surname" value="<%= surname %>" /><br />
		<label>ユーザーの名</label>
		<input type="text" name="name" value="<%= name %>" /><br />
		<label>郵便番号</label>
		<input type="text" name="postalcode" value="<%= postalcode %>" /><br />
		<label>都道府県</label>
		<input type="text" name="prefectures" value="<%= prefectures %>" /><br />
		<label>市区町村</label>
		<input type="text" name="city" value="<%= city %>" /><br />
		<label>住所詳細情報</label>
		<input type="text" name="streetAddress" value="<%= streetAddress %>" /><br />
		<label>電話番号</label>
		<input type="text" name="phoneNumber" value="<%= phoneNumber %>" /><br />
		<input type="submit" name="S_CAKakuninn" value="変更確認" />
	</form>
</body>
</html>