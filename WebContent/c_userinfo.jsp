<%@page import="java.util.ArrayList"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	// ここから　変数宣言・初期化スペース
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



%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="./css/input.css" rel="stylesheet" type="text/css" />
<title>ユーザー情報変更</title>
</head>
<body>
	<form action="UserInfo" method="post">
		<div id="info">
		<%
			// 入力エラーメッセージをこの部分に表示する。
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
					<input type="text" class="lbox" name="userid"  placeholder="ID" value="<%= userid %>" size="20"/>
				</dd>
			</dl>
			<dl>
				<dt>ユーザ名</dt>
				<dd>
					<input type="text" class="lbox" name="username"  placeholder="ユーザ名" value="<%= username %>" size="20"/>
				</dd>
			</dl>
			<dl>
				<dt>TwitterID</dt>
				<dd>
					<input type="text" class="lbox" name="twitter"  placeholder="TwitterID" value="<%= twiId %>" size="20"/>
				</dd>
			</dl>
			<dl>
				<dt>メールアドレス</dt>
				<dd>
					<input type="text" class="lbox" name="mail"  placeholder="mail" value="<%= mail %>" size="20"/>
				</dd>
			</dl>
			<dl>
				<dt>秘密の質問①</dt>
				<dd>
					<select class="lbox" name="secQ1">
						<option value="0">秘密の質問①を選択してください</option>
						<%= sQuestion1 %>
					</select>
				</dd>
			</dl>
			<dl>
				<dt></dt>
				<dd>
					<input type="text" class="lbox" name="secA1"  placeholder="秘密の質問①の答えを入力してください" size="20" value="<%= outSQ1 %>" />
				</dd>
			</dl>
			<dl>
				<dt>秘密の質問②</dt>
				<dd>
					<select class="lbox" name="secQ2">
						<option value="0">秘密の質問②を選択してください</option>
						<%= sQuestion2 %>
					</select>
				</dd>
			</dl>
			<dl>
				<dt></dt>
				<dd>
					<input type="text" class="lbox" name="secA2"  placeholder="秘密の質問②の答えを入力してください" size="20" value="<%= outSQ2 %>" />
				</dd>
			</dl>
			<div class="btn">
				<input type="submit" name="S_CUKakuninn" value="変更確認"/>
			</div>
		</div>
		<!-- 秘密の質問の一覧リストをhiddenのリクエストとして追加  -->

	</form>
</body>
</html>