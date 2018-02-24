package jp.ac.hal.login;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class UserInfo
 */
@WebServlet("/UserInfo")

public class UserInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// リクエストの文字コードを設定。
		request.setCharacterEncoding("UTF-8");

		// ここから　変数宣言・初期化スペース
		// セッションを格納するためのオブジェクト。
		HttpSession session = null;

		//　入力チェック用の正規表現文字列パターンを格納するアレイリスト変数。
		ArrayList<String> InPattern = new ArrayList<String>();
		// アレイリストに入力チェックに使うパターンをまとめて追加。
		// INSERT INTOの文法。
		InPattern.add("^insert\\s+into\\s+.+\\(.+\\)\\s*values\\s*\\(.+\\)\\s*;*$");
		// UPDATEの文法。
		InPattern.add("^update\\s+.+\\s+set\\s+.+;*$");
		// DELETEの文法。
		InPattern.add("^delete\\s+from\\s+.+;*$");

		// 入力チェックに使う対象文字列を格納するアレイリスト。
		ArrayList<String> checkStr = new ArrayList<String>();
		// テキストの入力チェック用のオブジェクト。
		CheckTextArea check = null;
		// 入力チェックの結果を数値で受け取るためのアレイリスト。
		ArrayList<Integer> checkRes = null;

		// フォワード先のjspファイル名を格納する変数。
		String DisPage = "";

		// エラーフラグ。処理途中で例外が発生した場合はtrueとなる。
		boolean errFlg = false;

		// リクエストから取得した入力データ群。
		// ID。
		String userid = "";
		// ユーザ名。
		String username = "";
		// ツイッターID。
		String twiId = "";
		// メールアドレス。
		String mail = "";
		// 秘密の質問回答1。
		String secA1 = "";
		// 秘密の質問回答2。
		String secA2 = "";

		// 秘密の質問1の種類を格納する変数。
		int cu_secQ1 = 0;
		// 秘密の質問2の種類を格納する変数。
		int cu_secQ2 = 0;

		// ChangeAcountクラスに格納する秘密の質問の質問IDデータを格納する変数。
		int[] setusSQ = new int[3];
		// ChangeAcountクラスに格納する秘密の質問の質問回答データを格納する変数。
		String[] setusSQAnswer = new String[3];
		// 配列変数の初期化。
		for(int i=0;i<3;i++)
		{
			setusSQ[i] = 0;
			setusSQAnswer[i] = "";
		}

		// 個人情報関連のデータを格納するオブジェクト。
		ChangeAcount ca = new ChangeAcount();
		// 秘密の質問関連の情報を格納する変数。質問数は2。
		SecretQuestion sq = new SecretQuestion(2);

		// データベース接続情報の設定
		//String url = "jdbc:mysql:///hew02?user=itasyayabisyamonten&password=syounanbakusou" + "&useUnicode=yes&characterEncoding=utf8";
		String url = "jdbc:mysql:///hew02?user=root&password=" + "&useUnicode=yes&characterEncoding=utf8";
		// データベースのアクセスオブジェクト。
		LoginDao dao = new LoginDao(url);

		// ここまで　変数宣言・初期化スペース

		// 例外処理。処理途中で例外が発生した場合、「error.jsp」へと遷移させる。
		try
		{
			// ユーザー情報変更ページから「変更確認」ボタンが押された場合の処理
			if(request.getParameter("S_CUKakuninn") != null)
			{
				// ここから　変数宣言・初期化スペース

				// ユーザー情報変更ページの全ての必須項目が入力されている場合にtrue、1つでも未入力ならfalseとなるフラグ。
				boolean cu_hissuok = false;
				// ユーザー情報変更ページのうちテキストボックス系の入力項目が全て入力されていればtrueとなるフラグ。
				boolean cu_txtok = false;

				// ここまで　変数宣言・初期化スペース

				// リクエストから、入力データ(秘密の質問の種類を除く)を取得し対象文字列アレイリストに追加。
				// ID。
				if(request.getParameter("userid") != null)
				{
					userid = request.getParameter("userid");
					checkStr.add(userid);
				}
				// ユーザー名。
				if(request.getParameter("username") != null)
				{
					username = request.getParameter("username");
					checkStr.add(username);
				}
				// ツイッターID。
				if(request.getParameter("twitter") != null)
				{
					twiId = request.getParameter("twitter");
					checkStr.add(twiId);
				}
				// メールアドレス。
				if(request.getParameter("mail") != null)
				{
					mail = request.getParameter("mail");
					checkStr.add(mail);
				}
				// 秘密の質問1の回答。
				if(request.getParameter("secA1") != null)
				{
					secA1 = request.getParameter("secA1");
					checkStr.add(secA1);
				}
				// 秘密の質問2の回答。
				if(request.getParameter("secA2") != null)
				{
					secA2 = request.getParameter("secA2");
					checkStr.add(secA2);
				}

				// テキストボックス形式の入力項目に対して全て入力チェックを実施。
				check = new CheckTextArea(checkStr);
				// チェック結果を数値型アレイリストで受け取る。
				checkRes = check.checkTextList(InPattern);

				// 入力チェックの結果が全て0(異常なし)かをチェック。
				cu_txtok = true;
				for(int i=0;i<checkRes.size();i++)
				{
					// 1つでも入力チェックで異常が見つかったデータがある場合、テキストボックスの入力チェックフラグをfalseとする。
					if(checkRes.get(i) != 0)
					{
						cu_txtok = false;
						// ループ強制脱出。
						break;
					}
				}

				// 続いて、秘密の質問の選んだ種類の入力チェックを行う。
				// 秘密の質問1の種類をリクエストから取得。
				if(request.getParameter("secQ1") != null)
				{
					cu_secQ1 = Integer.parseInt(request.getParameter("secQ1"));
				}
				// 秘密の質問2の種類をリクエストから取得。
				if(request.getParameter("secQ2") != null)
				{
					cu_secQ2 = Integer.parseInt(request.getParameter("secQ2"));
				}

				// この時点で、テキストボックスの入力チェックが全てOK、かつ秘密の質問1と秘密の質問2の種類がそれぞれ0以外(設定済み)の場合は、認証成功。
				if(cu_txtok == true && cu_secQ1 != 0 && cu_secQ2 != 0)
				{
					// 全項目認証成功フラグをtrueにする。
					cu_hissuok = true;
				}

				// この時点で全項目認証フラグがtrueの場合、変更確認画面へ遷移する。
				if(cu_hissuok == true)
				{
					DisPage = "cu_kakuninn.jsp";
				}
				else
				{
					// 認証に失敗した場合は、マイページ画面へ戻る。
					DisPage = "mypage.jsp";
					// リクエストに入力チェックデータのアレイリストを追加。
					request.setAttribute("checkRes", checkRes);
				}

				// リクエストに入力データを個別に追加。
				request.setAttribute("userid", userid);
				request.setAttribute("username", username);
				request.setAttribute("twiId", twiId);
				request.setAttribute("mail", mail);
				// 秘密の質問の回答群。
				request.setAttribute("secA1", secA1);
				request.setAttribute("secA2", secA2);

				// リクエストに秘密の質問の種類データを追加。
				int[] usSQ = {0,cu_secQ1,cu_secQ2};
				request.setAttribute("usSQ", usSQ);

			}

			// ユーザー情報変更確認ページから「設定画面へ戻る」ボタンが押された場合の処理。
			else if(request.getParameter("S_backtoCU") != null)
			{
				// リクエストからユーザー情報の各入力項目を取得し、設定画面に送るリクエストとして追加。
				if(request.getParameter("userid") != null)
				{
					userid = request.getParameter("userid");
					request.setAttribute("userid", userid);
				}
				if(request.getParameter("username") != null)
				{
					username = request.getParameter("username");
					request.setAttribute("username", username);
				}
				if(request.getParameter("twiId") != null)
				{
					twiId = request.getParameter("twiId");
					request.setAttribute("twiId", twiId);
				}
				if(request.getParameter("mail") != null)
				{
					mail = request.getParameter("mail");
					request.setAttribute("mail", mail);
				}

				// ユーザーの秘密の質問の各回答。
				if(request.getParameter("secA1") != null)
				{
					secA1 = request.getParameter("secA1");
					request.setAttribute("secA1", secA1);
				}
				if(request.getParameter("secA2") != null)
				{
					secA2 = request.getParameter("secA2");
					request.setAttribute("secA2", secA2);
				}

				// ユーザーの秘密の質問の選んだ質問ID。
				if(request.getParameter("secQ1") != null && request.getParameter("secQ2") != null)
				{
					cu_secQ1 = Integer.parseInt(request.getParameter("secQ1"));
					cu_secQ2 = Integer.parseInt(request.getParameter("secQ2"));
				}
				int usSQ[] = {0,cu_secQ1,cu_secQ2};
				request.setAttribute("usSQ", usSQ);

				// 転送先ページを「mypage.jsp」に設定。
				DisPage = "mypage.jsp";
			}

			// ユーザー情報変更確認ページから「変更」ボタンが押された場合の処理。
			else if(request.getParameter("S_CUtoDB") != null)
			{
				// ここから　変数宣言・初期化スペース
				// 更新対象のカラム名を一斉指定するアレイリスト。
				ArrayList<String> colLi = new ArrayList<String>();
				colLi.add("userid");
				colLi.add("username");
				colLi.add("twiId");
				colLi.add("mail");

				// 現在クッキーに格納されているユーザーIDを格納する変数。
				String userid_now = "";
				// 秘密の質問以外のデータベースの更新処理の成功フラグ。false:失敗　true:成功
				boolean update_suc = false;
				// 秘密の質問のデータベース更新処理の成功フラグ。false:失敗　true:成功
				boolean himitsu_suc = false;

				// フォワード先で表示させるページのタイトルを格納する。
				String comp_title = "";
				// フォワード先で表示させるページの本文を格納する。
				String comp_mes = "";

				// クッキーのユーザーIDの値の置き換えで使用する変数。
				Cookie nCookie = null;

				// ここまで　変数宣言・初期化スペース

				// リクエストからユーザーIDを取得。
				if(request.getParameter("userid") != null)
				{
					userid = request.getParameter("userid");
					// CAにユーザーIDをセット。
					ca.setUserid(userid);
				}
				// リクエストからユーザー名を取得。
				if(request.getParameter("username") != null)
				{
					username = request.getParameter("username");
					// CAにユーザー名をセット。
					ca.setUsername(username);
				}
				// リクエストからツイッターIDを取得。
				if(request.getParameter("twiId") != null)
				{
					twiId = request.getParameter("twiId");
					// CAにツイッターIDをセット。
					ca.setTwiId(twiId);
				}
				// リクエストからメールアドレスを取得。
				if(request.getParameter("mail") != null)
				{
					mail = request.getParameter("mail");
					// CAにメールアドレスをセット。
					ca.setMail(mail);
				}

				// リクエストからデータベースに格納予定の秘密の質問IDと回答を取得し、配列にセット。
				for(int i=1;i<=2;i++)
				{
					// 秘密の質問のID。
					if(request.getParameter("secQ" + String.valueOf(i)) != null)
					{
						setusSQ[i] = Integer.parseInt( request.getParameter("secQ" + String.valueOf(i)) );
					}
					// 秘密の質問の回答。
					if(request.getParameter("secA" + String.valueOf(i)) != null)
					{
						setusSQAnswer[i] = request.getParameter("secA" + String.valueOf(i));
					}
				}

				// 取得した秘密の質問IDと回答をSecretQuestionインスタンスにセットする。
				// 質問ID
				sq.setUsSQ(setusSQ);
				// 質問の回答
				sq.setUsSQAnswer(setusSQAnswer);


				// 現在ブラウザで保持しているCookieから、変更前のユーザーIDのデータを取得する。
				Cookie cookie_Li[] = request.getCookies();
				// Cookieのうち、ユーザーIDを示すCookieのデータだけを取り出す。
				for(Cookie i:cookie_Li)
				{
					if(i.getName().equals("UserID"))
					{
						// 現在のユーザーIDのデータを取得。
						userid_now = i.getValue();
						nCookie = i;
					}
				}

				// 現在のユーザーIDのデータを取得したら、現在のクッキーのユーザーIDの値を変更後のユーザーIDの値に置き換える。
				if(nCookie != null)
				{
					nCookie.setValue(userid);
					response.addCookie(nCookie);
				}

				// 現在格納されている、秘密の質問の内容一覧とID一覧のセッションを全て削除する。
				session = request.getSession();
				// 秘密の質問1のID一覧。
				session.removeAttribute("sQuestionID1");
				// 秘密の質問2のID一覧。
				session.removeAttribute("sQuestionID2");
				// 秘密の質問1の質問内容一覧。
				session.removeAttribute("sQuestionList1");
				// 秘密の質問2の質問内容一覧。
				session.removeAttribute("sQuestionList2");


				// データベースに入力済みのユーザー情報を格納。
				update_suc = ca.writeAcountInfo(dao, userid_now, colLi);

				// 続いて、データベースに秘密の質問IDとその質問の回答を格納。
				himitsu_suc = sq.writeSeqretQuestion(dao, userid);

				// ユーザー情報の更新処理が成功した場合は、更新が完了した旨をフォワード先で表示させる。
				if(update_suc == true && himitsu_suc == true)
				{
					// フォワード先の大見出し
					comp_title = "ユーザー情報の変更完了";
					// フォワード先の本文
					comp_mes = "ユーザー情報の変更が正常に完了しました。";
				}
				else
				{
					// フォワード先の大見出し
					comp_title = "ユーザー情報の変更失敗";
					// フォワード先の本文
					comp_mes = "申し訳ありません。ユーザー情報の変更処理が正常に行われませんでした。";
				}

				// リクエストに大見出し・本文の内容を追加。
				// request.setAttribute("comp_title", comp_title);
				request.setAttribute("comp_mes", comp_mes);

				// 転送先ページを「mypage.jsp」に設定。
				DisPage = "mypage.jsp";
			}
		}
		catch(Exception e)
		{
			// エラーフラグをtrueとする。
			errFlg = true;
			// 遷移先のページを「error.jsp」とする。
			DisPage = "error.jsp";
		}
		finally
		{
			// 転送処理
			RequestDispatcher disp = request.getRequestDispatcher(DisPage);
			disp.forward(request, response);
		}
	}

}
