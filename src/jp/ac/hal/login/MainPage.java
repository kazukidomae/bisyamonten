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
 * Servlet implementation class MainPage
 */
@WebServlet("/MainPage")
public class MainPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	/* (非 Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストの文字コードを設定。
		request.setCharacterEncoding("UTF-8");

		// ここから　変数宣言・初期化スペース

		// 遷移先のjspファイル名を格納する変数。
		String DisPage = "";
		// ブラウザからCookieを取得する際に現在ブラウザ上に保存されているCookieの一覧リストを格納するための変数。
		// Cookieに保存されているユーザーIDを取得し、格納するための変数。
		String cookUserID = "";

		// 現在ブラウザで保持しているCookieからユーザーIDのデータを取得する。
		Cookie[] cookie_Li = request.getCookies();
		// Cookieのうち、ユーザーIDを示すCookieのデータだけを取り出す。
		for(Cookie i:cookie_Li)
		{
			if(i.getName().equals("UserID"))
			{
				// ユーザーIDのデータを取得。
				cookUserID = i.getValue();
			}
		}


		// データベース接続情報の設定
		String url = "jdbc:mysql:///hew02?user=root&password=" + "&useUnicode=yes&characterEncoding=utf8";
		// データベースアクセスオブジェクト作成
		LoginDao UserData = new LoginDao(url);

		// セッションを格納するためのオブジェクトを定義。
		HttpSession session = request.getSession();

		// エラーフラグ。処理途中で例外が発生した場合はtrueとなる。
		boolean errFlg = false;

		// ユーザー情報を格納するChangeAcountクラス。
		ChangeAcount CA = new ChangeAcount();

		// フォワード先ページで表示するタグや文章などを格納するメッセージ変数。
		String mes = "";

		// フォワード先で表示させるページのタイトルを格納する。
		String comp_title = "";
		// フォワード先で表示させるページの本文を格納する。
		String comp_mes = "";

		// 入力チェックの際に使うパターンマッチング用の文字列をまとめて格納するアレイリスト。
		ArrayList<String> InPattern = new ArrayList<String>();
		// アレイリストに入力チェックに使うパターンをまとめて追加。
		// INSERT INTOの文法。
		InPattern.add("^insert\\s+into\\s+.+\\(.+\\)\\s*values\\s*\\(.+\\)\\s*;*$");
		// UPDATEの文法。
		InPattern.add("^update\\s+.+\\s+set\\s+.+;*$");
		// DELETEの文法。
		InPattern.add("^delete\\s+from\\s+.+;*$");
		// 不正な文字列の入力チェック用オブジェクト。
		InCheck check = new InCheck(InPattern);

		// データベースから読み出したデータを格納する専用のアレイリスト。
		ArrayList<Object> dbres = new ArrayList<Object>();

		// データベースの更新処理で扱う、更新対象のカラム名を格納するアレイリスト。
		ArrayList<String> upd_col = new ArrayList<String>();
		// データベースの更新処理で扱う、更新対象となるデータを格納するアレイリスト。
		ArrayList<Object> upd_data = new ArrayList<Object>();
		// データベースの更新処理の結果を格納するフラグ。　false:失敗　true:成功
		boolean upd_res = false;

		// ここまで　変数宣言・初期化スペース

		// 処理途中で例外が発生した場合は、error.jspへ遷移させる。
		try
		{
			// トップページまたはマイページからログアウトボタンが押された場合の処理。
			if(request.getParameter("S_Logout") != null)
			{
				// ユーザーIDが格納されているCookieを破棄する処理。
				// 現在ブラウザ側で保持しているCookieを取得。
				Cookie cookies[] = request.getCookies();
				for(int i=0;i<cookies.length;i++)
				{
					if(cookies[i].getName().equals("UserID"))
					{
						// ユーザーIDのCookieだけを破棄する。
						cookies[i].setMaxAge(0);
						// レスポンスに破棄処理後のCookieを追加。
						response.addCookie(cookies[i]);
					}
				}

				// ユーザーIDが格納されているセッションが存在したら削除する。
				// if(session.getAttribute("UserID") != null)
				//{
				//	session.removeAttribute("UserID");
				//}

				// ページのフォワード先をログイン画面に設定。
				DisPage = "login.jsp";
			}

			// トップページから「マイページ」ボタンが押された場合の処理。
			else if(request.getParameter("S_Mypage") != null)
			{
				// ページのフォワード先をマイページ画面に設定。
				DisPage = "mypage.jsp";
			}
			// マイページから「トップページ」ボタンが押された場合の処理。
			else if(request.getParameter("S_Toppage") != null)
			{
				// ページのフォワード先をトップページ画面に設定。
				DisPage = "mainpage.jsp";
			}

			// マイページから「個人情報変更ページへ」ボタンが押された場合、もしくは「ユーザー情報変更ページへ」ボタンが押された場合の処理。
			else if(request.getParameter("S_ChangeAcount") != null || request.getParameter("S_ChangeUser") != null)
			{

				// 取得したユーザーIDを元に個人情報データをDBから取得する。

				// 押されたボタンにより処理を分岐させる。
				// 「個人情報変更ページへ」の場合
				if(request.getParameter("S_ChangeAcount") != null)
				{
					// ページのフォワード先をc_acount.jspに設定する。
					DisPage = "c_acount.jsp";
					// DBから個人情報データを取得。
					CA.readAcountInfo(UserData, cookUserID, 1);
					// データベースから取得した各個人情報のデータ(7種類)をリクエストに追加する。
					// ユーザーの氏
					request.setAttribute("surname", CA.getSurname());
					// ユーザーの名
					request.setAttribute("name", CA.getFirstName());
					// 郵便番号
					request.setAttribute("postalcode", CA.getPostalcode());
					// 都道府県
					request.setAttribute("prefectures", CA.getPrefectures());
					// 市区町村
					request.setAttribute("city", CA.getCity());
					// 住所詳細情報
					request.setAttribute("streetAddress", CA.getStreetAddress());
					// 電話番号
					request.setAttribute("phoneNumber", CA.getPhoneNumber());

				}
				// 「ユーザー情報変更ページへ」の場合
				else
				{

					// 秘密の質問クラスからインスタンスを生成。引数には質問の数(現在2種類)を入れる。
					SecretQuestion sec = new SecretQuestion(2);

					// ページのフォワード先をc_userinfo.jspに設定する。
					DisPage = "c_userinfo.jsp";
					// DBから個人情報データを取得。
					CA.readAcountInfo(UserData, cookUserID, 2);
					// データベースから取得した各個人情報のデータ(7種類)をリクエストに追加する。
					// ユーザーID。
					request.setAttribute("userid", CA.getUserid());
					// ユーザーネーム。
					request.setAttribute("username", CA.getUsername());
					// ツイッターID。
					request.setAttribute("twiId", CA.getTwiId());
					// メールアドレス。
					request.setAttribute("mail", CA.getMail());

					// 秘密の質問一覧をデータベースから取得し、秘密の質問アレイリストに格納する。

					// 秘密の質問1用。
					ArrayList<Object> sQuestionList1 = UserData.selectInTable("t_secretquestion","F_Secretquestioncontent","where F_Questionclassification = 1 order by F_SecretquestionID","");
					// データベースからの取得結果がnullでない場合は、データベースから取得した秘密の質問をアレイリストごとセッションに追加する(入力エラー時でも問題無く表示)。
					if(sQuestionList1 != null)
					{
						session.setAttribute("sQuestionList1", sQuestionList1);

						// 秘密の質問の内容を取得できたら、ついでに秘密の質問のID一覧を取得する。
						ArrayList<Object> sQuestionID1 = UserData.selectInTable("t_secretquestion", "F_SecretquestionID", "where F_Questionclassification = 1 order by F_SecretquestionID", "");
						// データベースからの取得結果がnullでない場合は、取得した秘密の質問のID一覧をアレイリストごとセッションに追加する。
						if(sQuestionID1 != null)
						{
							session.setAttribute("sQuestionID1", sQuestionID1);
						}
					}

					// 秘密の質問2用。
					ArrayList<Object> sQuestionList2 = UserData.selectInTable("t_secretquestion","F_Secretquestioncontent","where F_Questionclassification = 2 order by F_SecretquestionID","");
					// データベースからの取得結果がnullでない場合は、データベースから取得した秘密の質問をアレイリストごとセッションに追加する。
					if(sQuestionList2 != null)
					{
						session.setAttribute("sQuestionList2", sQuestionList2);

						// 秘密の質問の内容を取得できたら、ついでに秘密の質問のID一覧を取得する。
						ArrayList<Object> sQuestionID2 = UserData.selectInTable("t_secretquestion", "F_SecretquestionID", "where F_Questionclassification = 2 order by F_SecretquestionID", "");
						// データベースからの取得結果がnullでない場合は、取得した秘密の質問のID一覧をアレイリストごとセッションに追加する。
						if(sQuestionID2 != null)
						{
							session.setAttribute("sQuestionID2", sQuestionID2);
						}
					}

					// 現在データベース上に格納されているユーザーが選んだ秘密の質問番号と回答を取得し、フォワード先でデフォルト値として表示。
					// DBからのデータ読み出し処理
					sec.readSecretQuestion(UserData, cookUserID);
					// DBからの読み出し結果をリクエストに追加
					request.setAttribute("usSQ", sec.getUsSQ());

					// DBからの秘密の質問ユーザー回答データをリクエストに追加。
					request.setAttribute("usSQAnswer", sec.getUsSQAnswer());

					// セッションの有効期限を無期限に設定。
					session.setMaxInactiveInterval(-1);
				}
			}

			// マイページから「購入ページへ」ボタンが押された場合の処理。
			else if(request.getParameter("S_Purchase") != null)
			{
				// 登録済み住所の表示に必要なデータが全てデータベースから読み込めた場合にtrueとなるフラグ。
				boolean tourokuzumiOk = false;
				// 郵便番号が空白もしくは未入力以外の場合にtrueとなるフラグ。
				boolean posOk = false;
				// 都道府県が空白もしくは未入力以外の場合にtrueとなるフラグ。
				boolean prefOk = false;
				// 市区町村が空白もしくは未入力以外の場合にtrueとなるフラグ。
				boolean citOk = false;
				// 住所詳細情報が空白もしくは未入力以外の場合にtrueとなるフラグ。
				boolean saOk = false;
				// 電話番号が空白もしくは未入力以外の場合にtrueとなるフラグ。
				boolean phoOk = false;

				// クッキーから取得したユーザーIDを利用し、ユーザーの個人情報を一斉読み出し。
				CA.readAcountInfo(UserData, cookUserID, 1);
				// データベースから取得した住所の値が空白の場合(DBとの接続に失敗した場合)または"未入力"の場合は、「登録外の住所に配送」のみを表示する。
				// 郵便番号
				if( !( CA.getPostalcode().equals("") ) && !( CA.getPostalcode().equals("未入力") ) )
				{
					posOk = true;
				}
				// 都道府県
				if( !( CA.getPrefectures().equals("") ) && !( CA.getPrefectures().equals("未入力") ) )
				{
					prefOk = true;
				}
				// 市区町村
				if( !( CA.getCity().equals("") ) && !( CA.getCity().equals("未入力") ) )
				{
					citOk = true;
				}
				// 住所詳細情報
				if( !( CA.getStreetAddress().equals("") ) && !( CA.getStreetAddress().equals("未入力") ) )
				{
					saOk = true;
				}
				// 電話番号
				if( !( CA.getPhoneNumber().equals("") ) && !( CA.getPhoneNumber().equals("未入力") ) )
				{
					phoOk = true;
				}

				// 上記の5項目が全て空白か未入力以外の場合は、購入画面で「登録済みの住所に配送」ボタンを表示させる。
				if(posOk == true && prefOk == true && citOk == true && saOk == true && phoOk == true)
				{
					tourokuzumiOk = true;
				}

				// 登録済み住所が表示可能な場合、セッションに登録済み住所の表示を有効期限無期限で追加。
				if(tourokuzumiOk == true)
				{
					// リクエストに登録済み住所表示可能フラグを追加。
					request.setAttribute("tourokuzumiOk", tourokuzumiOk);
					// セッションの有効期限を無期限に設定。
					session.setMaxInactiveInterval(-1);
					// セッションに登録済み住所の各データを追加。
					session.setAttribute("postalcode", CA.getPostalcode());
					session.setAttribute("prefectures", CA.getPrefectures());
					session.setAttribute("city", CA.getCity());
					session.setAttribute("streetAddress", CA.getStreetAddress());
					session.setAttribute("phoneNumber", CA.getPhoneNumber());
				}

				// 遷移先ページをpurchase.jspに設定。
				DisPage = "purchase.jsp";

			}

			// マイページからモーダルでパスワードの変更処理が行われた場合の処理。
			else if(request.getParameter("pass_change") != null)
			{
				// ここから　変数宣言・初期化スペース
				// 現在のパスワードの入力内容を受け取る変数。
				String pass_now = "";
				// 新しいパスワードの入力内容を受け取る変数。
				String pass_new = "";
				// 新しいパスワードの再入力を受け取る変数。
				String pass_renew = "";

				// パスワードの入力チェックで1つでもエラーがあればtrue、何もエラーが無ければfalseとなるフラグ。
				boolean pass_inerr = false;

				// ここまで　変数宣言・初期化スペース

				// リクエストから各入力フォームの入力内容を受け取る。
				if(request.getParameter("now_pass") != null)
				{
					pass_now = request.getParameter("now_pass");
				}
				if(request.getParameter("new_pass") != null)
				{
					pass_new = request.getParameter("new_pass");
				}
				if(request.getParameter("renew_pass") != null)
				{
					pass_renew = request.getParameter("renew_pass");
				}

				// 現在のパスワードの入力チェック。
				if(pass_now.equals(""))
				{
					// エラーフラグをtrue。
					pass_inerr = true;
					comp_mes = comp_mes + "現在のパスワードが入力されていません！<br />";
				}
				else if(check.strMatching(pass_now) == true)
				{
					// SQLインジェクション対策。
					pass_inerr = true;
					comp_mes = comp_mes + "現在のパスワードに使用できない文字列が含まれています。<br />";
				}

				// 新しいパスワードの入力チェック。
				if(pass_new.equals(""))
				{
					// エラーフラグをtrue。
					pass_inerr = true;
					comp_mes = comp_mes + "新しいパスワードが入力されていません！<br />";
				}
				else if(check.strMatching(pass_new) == true)
				{
					// SQLインジェクション対策。
					pass_inerr = true;
					comp_mes = comp_mes + "新しいパスワードに使用できない文字列が含まれています。<br />";
				}

				// 新しいパスワードを再入力の入力チェック。
				if(pass_renew.equals(""))
				{
					// エラーフラグをtrue。
					pass_inerr = true;
					comp_mes = comp_mes + "新しいパスワードの再入力を行っていません！<br />";
				}

				// 続いて、新しいパスワードとパスワードの再入力が一致しているかチェック。
				if( !( pass_new.equals(pass_renew)) )
				{
					// 新パスと再パスが異なった場合はエラーフラグをtrue。
					pass_inerr = true;
					comp_mes = comp_mes + "新しいパスワードと再入力されたパスワードが異なります！もう一度入力してください。<br />";
				}

				// この時点で入力エラーが1つも無ければ、続いてDBのパスワードとの比較処理を行う。
				if(pass_inerr == false)
				{
					// Cookieに格納されているユーザーIDを元に、データベースから格納されているパスワードを読み出す。
					dbres = UserData.selectInTable("t_user", "F_Password", "WHERE F_UserID = ?", cookUserID);
					if(dbres != null && dbres.size() >= 1)
					{
						// データベースからパスワードの取得結果が1以上ある場合、取得した結果の0番要素に入っているパスワードと一致しているか判定する。
						if(pass_now.equals( (String)dbres.get(0) ))
						{
							// 現在のパスワードがデータベース上に格納されているパスワードと一致した場合は、新しいパスワードへの変更処理を行う。
							upd_col.add("F_Password");
							// 更新データ
							upd_data.add(1);
							upd_data.add(pass_new);

							upd_res = UserData.updateInTable("t_user", upd_col, upd_data, "WHERE F_UserID = \"" + cookUserID + "\"");
							if(upd_res == true)
							{
								// データベースの更新成功
								// リクエストにパスワード変更完了の表示用メッセージを追加。
								comp_mes = "パスワードの変更が正常に完了しました。<br />";
							}
							else
							{
								// データベースの更新失敗
								comp_mes = "申し訳ありません。パスワードの変更処理に失敗しました。<br />";
							}
						}
						else
						{
							// 現在のパスワードがデータベース上のパスワードと違う場合は、その旨を伝えるエラーメッセージを表示させる。
							comp_mes = comp_mes + "入力された現在のパスワードは間違っています。もう一度入力してください。<br />";
						}
					}
					else
					{
						// データベースからパスワードを正常に取得できなかった場合
						comp_mes = comp_mes + "申し訳ありません。パスワードの変更処理に失敗しました。<br />";
					}
				}

				// 遷移先ページをmypage.jspに設定。
				DisPage = "mypage.jsp";
				request.setAttribute("comp_mes", comp_mes);
			}

			// ホームページ上からマイページボタンが押された場合に、ユーザー情報とアカウント情報をデータベースから取得し、リクエストで送るための処理。
			else if(request.getParameter("gotomypage") != null)
			{
				// ページのフォワード先をc_acount.jspに設定する。
				DisPage = "mypage.jsp";

				// DBからの個人情報の取得処理。
				// DBから個人情報データを取得。
				CA.readAcountInfo(UserData, cookUserID, 1);
				// データベースから取得した各個人情報のデータ(7種類)をリクエストに追加する。
				// ユーザーの氏
				request.setAttribute("surname", CA.getSurname());
				// ユーザーの名
				request.setAttribute("name", CA.getFirstName());
				// 郵便番号
				request.setAttribute("postalcode", CA.getPostalcode());
				// 都道府県
				request.setAttribute("prefectures", CA.getPrefectures());
				// 市区町村
				request.setAttribute("city", CA.getCity());
				// 住所詳細情報
				request.setAttribute("streetAddress", CA.getStreetAddress());
				// 電話番号
				request.setAttribute("phoneNumber", CA.getPhoneNumber());

				// DBからのユーザー情報の取得処理。
				// 秘密の質問クラスからインスタンスを生成。引数には質問の数(現在2種類)を入れる。
				SecretQuestion sec = new SecretQuestion(2);
				// DBから個人情報データを取得。
				CA.readAcountInfo(UserData, cookUserID, 2);
				// データベースから取得した各個人情報のデータ(7種類)をリクエストに追加する。
				// ユーザーID。
				request.setAttribute("userid", CA.getUserid());
				// ユーザーネーム。
				request.setAttribute("username", CA.getUsername());
				// ツイッターID。
				request.setAttribute("twiId", CA.getTwiId());
				// メールアドレス。
				request.setAttribute("mail", CA.getMail());

				// 秘密の質問一覧をデータベースから取得し、秘密の質問アレイリストに格納する。

				// 秘密の質問1用。
				ArrayList<Object> sQuestionList1 = UserData.selectInTable("t_secretquestion","F_Secretquestioncontent","where F_Questionclassification = 1 order by F_SecretquestionID","");
				// データベースからの取得結果がnullでない場合は、データベースから取得した秘密の質問をアレイリストごとセッションに追加する(入力エラー時でも問題無く表示)。
				if(sQuestionList1 != null)
				{
					session.setAttribute("sQuestionList1", sQuestionList1);

					// 秘密の質問の内容を取得できたら、ついでに秘密の質問のID一覧を取得する。
					ArrayList<Object> sQuestionID1 = UserData.selectInTable("t_secretquestion", "F_SecretquestionID", "where F_Questionclassification = 1 order by F_SecretquestionID", "");
					// データベースからの取得結果がnullでない場合は、取得した秘密の質問のID一覧をアレイリストごとセッションに追加する。
					if(sQuestionID1 != null)
					{
						session.setAttribute("sQuestionID1", sQuestionID1);
					}
				}

				// 秘密の質問2用。
				ArrayList<Object> sQuestionList2 = UserData.selectInTable("t_secretquestion","F_Secretquestioncontent","where F_Questionclassification = 2 order by F_SecretquestionID","");
				// データベースからの取得結果がnullでない場合は、データベースから取得した秘密の質問をアレイリストごとセッションに追加する。
				if(sQuestionList2 != null)
				{
					session.setAttribute("sQuestionList2", sQuestionList2);

					// 秘密の質問の内容を取得できたら、ついでに秘密の質問のID一覧を取得する。
					ArrayList<Object> sQuestionID2 = UserData.selectInTable("t_secretquestion", "F_SecretquestionID", "where F_Questionclassification = 2 order by F_SecretquestionID", "");
					// データベースからの取得結果がnullでない場合は、取得した秘密の質問のID一覧をアレイリストごとセッションに追加する。
					if(sQuestionID2 != null)
					{
						session.setAttribute("sQuestionID2", sQuestionID2);
					}
				}

				// 現在データベース上に格納されているユーザーが選んだ秘密の質問番号と回答を取得し、フォワード先でデフォルト値として表示。
				// DBからのデータ読み出し処理
				sec.readSecretQuestion(UserData, cookUserID);
				// DBからの読み出し結果をリクエストに追加
				request.setAttribute("usSQ", sec.getUsSQ());

				// DBからの秘密の質問ユーザー回答データをリクエストに追加。
				request.setAttribute("usSQAnswer", sec.getUsSQAnswer());

				// セッションの有効期限を無期限に設定。
				session.setMaxInactiveInterval(-1);

			}
		}
		catch(Exception e)
		{
			// エラーフラグをtrueとする。
			errFlg = true;
			// ページ遷移先を「error.jsp」とする。
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
