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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
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

		// セッションオブジェクトの定義。
		HttpSession session = request.getSession();

		// 入力されたユーザーIDまたはメールアドレスを格納する変数。
		String strUserID = "";
		// 入力されたパスワードを格納する変数。
		String strUserPass = "";
		// 登録メールアドレスまたはユーザーIDがデータベースと一致した場合に格納される、該当ユーザーのユーザーナンバー。
		String strUserNum = "";

		// データベースから取得したユーザーIDの一覧リストを格納する変数。
		ArrayList<Object> UserID_Li = null;
		// データベースから取得したパスワードの一覧リストを格納する変数。
		ArrayList<Object> UserPass_Li = null;
		// データベースから取得した登録メールアドレスの一覧リストを格納する変数。
		ArrayList<Object> UserMail_Li = null;
		// データベースから取得したユーザーナンバーの一覧リストを格納する変数。
		ArrayList<Object> UserNum_Li = null;
		// データベースから取得した退会フラグの一覧リストを格納する変数。
		ArrayList<Object> wd_Li = null;

		// データベース中に入力されたユーザーID、またはメールアドレスと一致するものがあればtrueとなるフラグ。
		boolean UserID_find = false;
		// データベース中に入力されたパスワードと一致するものがあればtrueとなるフラグ。
		boolean UserPass_find = false;

		// ログインの成功可否情報を格納する変数(0:不明　1:ログイン成功　2:ログイン失敗　3:ログイン失敗(SQLの警告付き)　4:ログイン失敗(既に退会したユーザー))。フォワード処理で送信する。
		int LoginOK = 0;

		// SELECT文の後ろオプションとして追加するSQL文の内容を格納する。
		String OptSql = "";

		// ここから　新規ユーザー登録ページ関係の変数宣言・初期化スペース

		// 登録メールアドレスの入力チェックフラグ。正しく入力されている場合はtrueとする。
		boolean InUserMailOk = false;
		// 登録ユーザーIDの入力チェックフラグ。正しく入力されている場合はtrueとする。
		boolean InUserIdOk = false;
		// 登録ユーザー名の入力チェックフラグ。正しく入力されている場合はtrueとする。
		boolean InUserNameOk = false;
		// 登録パスワードの入力チェックフラグ。正しく入力されている場合はtrueとする。
		boolean InUserPassOk = false;

		// 登録の成功可否情報を格納する変数。新規ユーザー登録が正常に終了した場合は1、新規ユーザー登録に失敗した場合は2が格納される。
		int InsertInfo = 0;
		// 入力された情報のDB格納処理が成功したかどうかの情報。成功の場合は0、失敗の場合は0以外が格納される。
		int InsertDBOk = 0;
		// 新規登録に失敗し、ユーザー登録の入力フォーム画面に戻された際に画面上部に表示させるエラーメッセージの内容を格納する。
		String InsErrMsg = "";

		// 登録メールアドレス欄の入力値を格納する変数。
		String InUserMail = "";
		// 登録ユーザーID欄の入力値を格納する変数。
		String InUserID = "";
		// 登録ユーザー名欄の入力値を格納する変数。
		String InUserName = "";
		// 登録パスワード欄の入力値を格納する変数。
		String InUserPass = "";

		// 入力フォームに入力されたデータを、データ型情報と共に格納するアレイリスト。
		// 偶数の要素番号には次の要素番号に格納されたデータのデータ型を、奇数の要素番号には実際のデータが格納される。
		ArrayList<Object> InsertData = new ArrayList<Object>();

		// ここまで　新規ユーザー登録ページ関連の変数宣言・初期化スペース

		// 入力チェックの際に使うパターンマッチング用の文字列をまとめて格納するアレイリスト。
		ArrayList<String> InPattern = new ArrayList<String>();
		// アレイリストに入力チェックに使うパターンをまとめて追加。
		// INSERT INTOの文法。
		InPattern.add("^insert\\s+into\\s+.+\\(.+\\)\\s*values\\s*\\(.+\\)\\s*;*$");
		// UPDATEの文法。
		InPattern.add("^update\\s+.+\\s+set\\s+.+;*$");
		// DELETEの文法。
		InPattern.add("^delete\\s+from\\s+.+;*$");

		// セッションに保存するユーザーIDが格納された変数。
		String sesUserID = "";
		// メールアドレスからのユーザーID取得処理で用いるアレイリスト。
		ArrayList<Object> sesUserID_Li = null;

		// Cookieオブジェクトを格納する変数。
		Cookie cookie = null;

		//　ここまで　変数宣言・初期化スペース

		// データベース接続情報の設定
		//String url = "jdbc:mysql:///hew02?user=itasyayabisyamonten&password=syounanbakusou" + "&useUnicode=yes&characterEncoding=utf8";
		String url = "jdbc:mysql:///hew02?user=root&password=" + "&useUnicode=yes&characterEncoding=utf8";

		// データベースアクセスオブジェクト作成
		LoginDao UserData = new LoginDao(url);

		// エラーフラグ設定
		boolean boolErrFlg = false;
		// エラーコード
		String strErr = "";
		// 転送画面
		String DisPage = "";

		// データベースに登録されているユーザーID一覧を取得するアレイリスト。
		ArrayList<Object> UserIDList = new ArrayList<Object>();

		// 入力チェックを行う際に必要な入力チェックオブジェクトを作成する。
		InCheck check = new InCheck(InPattern);

		// ログイン対象のユーザーの退会フラグを格納する。　0:退会していない　1:退会している
		int wd_flg = 0;

		// 例外処理。処理途中で例外が発生した場合、error.jspへ遷移させる。
		try
		{
			// ●ログインボタンを押した時の処理●
			if(request.getParameter("S_Login") != null)
			{
				log("あ");
				// セッションの有効期限を登録時から30分間に設定。
				session.setMaxInactiveInterval(1800);

				// 入力されたユーザーIDまたは登録メールアドレスの取得
				if(request.getParameter("UserId") != null)
				{
					strUserID = request.getParameter("UserId");
				}
				// 入力されたパスワードを取得。
				if(request.getParameter("UserPass") != null)
				{
					strUserPass = request.getParameter("UserPass");
				}


				// 入力フォームの内容が正常に取得できない、またはユーザーUD・パスワードのいずれかが空白の場合はログイン失敗(SQL文の警告付き)とする。
				if( strUserID.equals("") || strUserPass.equals("") )
				{
					// 2つの入力フォームのうち1つ以上の項目が未入力
					// ログイン失敗とする。
					LoginOK = 2;
				}
				else
				{
					// 2つの入力フォームのどちらとも文字列が入力されていた場合

					// 次に、入力フォームにSQLインジェクションの原因となるSQL文が格納されていないかチェック。
					// 入力されたユーザーIDをチェックした結果がtrue、または入力したパスワードをチェックした結果がtrueの場合は、SQLの警告付きのログイン失敗とする。
					if(check.strMatching(strUserID) == true || check.strMatching(strUserPass) == true)
					{
						// SQLの警告付きのログイン失敗。
						LoginOK = 3;
					}
					// SQLインジェクションの疑いがある文字が含まれていない場合
					else
					{
						// データベースからまずはユーザーIDの一覧リストを取得する。
						UserID_Li = UserData.selectInTable("t_user","F_UserID","","");

						// ユーザーIDの一覧取得に失敗し、UserID_Liにnullが格納された場合
						if(UserID_Li == null)
						{
							// エラーフラグ設定
							boolErrFlg = true;
						}
						// ユーザーIDの一覧取得に成功した場合
						else
						{
							// 取得したリストを1件ずつ取り出し、入力されたユーザーIDと一致するか調べる。
							for(int i=0;i<UserID_Li.size();i++)
							{
								// 取り出した1件のデータと入力フォームに入力したデータが一致した場合
								if(strUserID.equals(UserID_Li.get(i)))
								{
									// ユーザーIDまたはメールアドレスの一致フラグをtrueにする。
									UserID_find = true;

									// 一致したユーザーIDを登録するセッションの内容とする。
									sesUserID = strUserID;

									// 入力されたユーザーIDからデータベース上のユーザーナンバーを取得する。
									// SELECT文の後ろに追加するオプション文を設定する。
									OptSql = "where F_UserID = ?";
									UserNum_Li = UserData.selectInTable("t_user", "F_UserNo", OptSql, strUserID);

									// ユーザーナンバーの一覧リストの戻り値がnullの場合は、エラーフラグをtrueにする。
									if(UserNum_Li == null)
									{
										// エラーフラグ設定
										boolErrFlg = true;
									}
									// ユーザーナンバーの一覧取得に成功した場合
									else
									{
										for(int j=0;j<UserNum_Li.size();j++)
										{
											// アレイリストに格納されたユーザーナンバーの内末尾のものを記憶するユーザーナンバーとする。
											strUserNum = (String) UserNum_Li.get(j);
										}
									}
								}
							}

							// この時点でユーザーIDで一致しているものが見つからない場合のみ、メールアドレスとの照合処理を実行する。
							if(UserID_find == false)
							{
								// データベースから次にメールアドレスの一覧リストを取得する。
								UserMail_Li = UserData.selectInTable("t_user", "F_Mailaddress","","");

								// メールアドレスの一覧取得に失敗し、UserID_Liにnullが格納された場合
								if(UserMail_Li == null)
								{
									// エラーフラグ設定
									boolErrFlg = true;
								}
								// メールアドレスの一覧取得に成功した場合
								else
								{
									// 取得したリストを1件ずつ取り出し、入力されたメールアドレスと一致するか調べる。
									for(int i=0;i<UserMail_Li.size();i++)
									{
										// 取り出した1件のデータと入力フォームに入力したデータが一致した場合
										if(strUserID.equals(UserMail_Li.get(i)))
										{
											// ユーザーIDまたはメールアドレスの一致フラグをtrueにする。
											UserID_find = true;

											// 入力されたメールアドレスからセッションに格納予定のユーザーIDを取得する。
											// SELECT文の後ろに追加するオプション文を設定する。
											OptSql = "where F_Mailaddress = ?";
											sesUserID_Li = UserData.selectInTable("t_user", "F_UserID", OptSql, strUserID);
											// ユーザーID一覧リストの取得に失敗し、戻り値がnullの場合はエラーフラグをtrueにする。
											if(sesUserID_Li == null)
											{
												// エラーフラグ設定
												boolErrFlg = true;
											}
											// ユーザーID一覧リストの取得に成功した場合
											else
											{
												for(int j=0;j<sesUserID_Li.size();j++)
												{
													// 一覧リストに格納されたユーザーIDの内末尾のものをセッションに格納するユーザーIDとする。
													sesUserID = (String)sesUserID_Li.get(j);
												}
											}

											// 続いて、入力されたメールアドレスからデータベース上のユーザーナンバーを取得する。
											UserNum_Li = UserData.selectInTable("t_user", "F_UserNo", OptSql, strUserID);

											// ユーザーナンバーの一覧リストの戻り値がnullの場合は、エラーフラグをtrueにする。
											if(UserNum_Li == null)
											{
												// エラーフラグ設定
												boolErrFlg = true;
											}
											// ユーザーナンバーの一覧取得に成功した場合
											else
											{
												for(int j=0;j<UserNum_Li.size();j++)
												{
													// アレイリストに格納されたユーザーナンバーの内末尾のものを記憶するユーザーナンバーとする。
													strUserNum = (String)UserNum_Li.get(j);
												}
											}
										}
									}
								}
							}

						}

						// 続いて、データベースからパスワードの一覧リストを取得する。
						// 既にユーザーIDまたは登録メールアドレスの認証が成功した場合、パスワードはユーザーIDの該当ユーザーが使用するパスワードでなくてはならない。
						if(UserID_find == true)
						{
							// SELECT文の後ろに追加するオプションSQL文を設定する。
							OptSql = "where F_UserNo = ?";
							UserPass_Li = UserData.selectInTable("t_user","F_Password",OptSql,strUserNum);
						}
						// ユーザーIDまたはメールアドレスの認証に失敗した場合はwhere句を設けず、データベースに存在するパスワードなら誰でも認証成功とする。
						else
						{
							UserPass_Li = UserData.selectInTable("t_user","F_Password","","");
						}


						// パスワードの一覧取得に失敗し、UserPass_Liにnullが格納された場合
						if(UserPass_Li == null)
						{
							// エラーフラグをtrueに設定。
							boolErrFlg = true;
						}
						// パスワードの一覧取得に成功した場合
						else
						{
							// 取得したリストを1件ずつ取り出し、入力されたパスワードと一致するか調べる。
							for(int i=0;i<UserPass_Li.size();i++)
							{
								// 取り出した1件のデータと入力フォームに入力したデータが一致した場合
								if(strUserPass.equals(UserPass_Li.get(i)))
								{
									// パスワードの一致フラグをtrueにする。
									UserPass_find = true;
								}
							}
						}

						// 入力されたユーザーID・パスワードの両方がデータベースに格納されているものと一致した場合は、ログイン成功とする。
						if(UserID_find == true && UserPass_find == true)
						{
							// 最後に、該当のユーザーが既に退会しているか判定し、退会済みの場合はログイン失敗とする。
							OptSql = "WHERE F_UserNo = " + strUserNum;
							wd_Li = UserData.selectInTable("t_user", "F_Withdrawal", OptSql, "");
							// この時点で一覧リストが正常に取得できなかった場合は、エラーフラグをtrueにする。
							if(wd_Li != null && wd_Li.size() >= 1)
							{
								// 一覧リストが正常に取得できた場合
								// 取得したリストの内先頭の要素から退会フラグの状態を取得。
								wd_flg = Integer.parseInt( (String)wd_Li.get(0) );
								// 取得したフラグの値が0の場合(退会していない場合)は、ログイン成功とする。
								if(wd_flg == 0)
								{
									// 退会していないユーザー
									// ログイン成功可否情報をログイン成功とする。
									LoginOK = 1;
									// ログインに成功。発行予定のクッキーにユーザーID情報を書き込む。
									cookie = new Cookie("UserID", sesUserID);
									// クッキーの有効期限を30分間に設定。
									cookie.setMaxAge(60*60*2);
									// レスポンスにcookieを追加する。
									response.addCookie(cookie);
								}
								else
								{
									// 該当のユーザーが既に退会している場合
									// ログイン成功可否情報をログイン失敗(既に退会したユーザー)とする。
									LoginOK = 4;
								}
							}
							else
							{
								// 一覧リストの取得に失敗した場合
								// エラーフラグをtrueにする。
								boolErrFlg = true;
							}
						}
						// どちらかでも一致しないものがあった場合、ログイン失敗とする。
						else
						{
							// ログイン成功可否情報をログイン失敗とする。
							LoginOK = 2;
						}
					}
				}

				// 転送処理(転送先画面設定)
				if(boolErrFlg)
				{
					// エラーの場合はエラー画面
					DisPage = "error.jsp";
				}
				else
				{
					if(LoginOK == 1)
					{
						// ログイン成功の場合に限り、直接トップページ画面へ遷移させる。
						DisPage = "top.jsp";
					}
					else
					{
						// ログイン失敗した場合は、結果画面にてエラーメッセージを表示させる。
						DisPage = "result.jsp";
					}
					// リクエストにログイン成功可否情報を追加する。
					request.setAttribute("LoginOK", LoginOK);
				}
			}

			// ●ユーザー登録画面から「新規登録」ボタンが押された時の処理●
			else if(request.getParameter("S_Insert") != null)
			{
				// 登録メールアドレスを入力フォームから取得する。
				if(request.getParameter("InUserMail") != null)
				{
					InUserMail = request.getParameter("InUserMail");

					// SQLインジェクション対策の入力チェックを行い、trueの場合は入力フォームのページに戻す。
					if(check.strMatching(InUserMail) == true)
					{
						InsErrMsg = InsErrMsg + "◎メールアドレスに使用できない文字があります！<br />";
					}
					// 入力値の取得結果が空文字列以外の場合は、アレイリストに入力値を格納する。
					else if( !(InUserMail.equals("")) )
					{
						// 登録メールアドレスの入力チェックをOKとする。
						InUserMailOk = true;
						// データ型に文字列型を指定。
						InsertData.add(1);
						// 続けて入力値をアレイリストに格納。
						InsertData.add(InUserMail);
					}
					// 空文字列の場合
					else
					{
						// 登録メールアドレスの入力を促すエラーメッセージを表示させる。
						InsErrMsg = InsErrMsg + "◎メールアドレスが未入力です！<br />";
					}
				}

				// 登録ユーザーIDを入力フォームから取得する。
				if(request.getParameter("InUserID") != null)
				{
					InUserID = request.getParameter("InUserID");

					// SQLインジェクション対策の入力チェックを行い、trueの場合は入力フォームのページに戻す。
					if(check.strMatching(InUserID) == true)
					{
						InsErrMsg = InsErrMsg + "◎ユーザーIDに使用できない文字があります！<br />";
					}
					// 入力値の取得結果が空文字列以外の場合は、アレイリストに入力値を格納する。
					else if( !(InUserID.equals("")) )
					{
						// 登録ユーザーIDの入力チェックをOKとする。
						InUserIdOk = true;
						// データ型に文字列型を指定。
						InsertData.add(1);
						// 続けて入力値をアレイリストに格納。
						InsertData.add(InUserID);
					}
					// 空文字列の場合
					else
					{
						// 登録ユーザーIDの入力を促すエラーメッセージを表示させる。
						InsErrMsg = InsErrMsg + "◎ユーザーIDが未入力です！<br />";
					}
				}

				// 登録ユーザー名を入力フォームから取得する。
				if(request.getParameter("InUserName") != null)
				{
					InUserName = request.getParameter("InUserName");

					// SQLインジェクション対策の入力チェックを行い、trueの場合は入力フォームのページに戻す。
					if(check.strMatching(InUserName) == true)
					{
						InsErrMsg = InsErrMsg + "◎ユーザー名に使用できない文字があります！<br />";
					}
					// 入力値の取得結果が空文字列以外の場合は、アレイリストに入力値を格納する。
					else if( !(InUserName.equals("")) )
					{
						// 登録ユーザー名の入力チェックをOKとする。
						InUserNameOk = true;
						// データ型に文字列型を指定。
						InsertData.add(1);
						// 続けて入力値をアレイリストに格納。
						InsertData.add(InUserName);
					}
					// 空文字列の場合
					else
					{
						// 登録ユーザー名の入力を促すエラーメッセージを表示させる。
						InsErrMsg = InsErrMsg + "◎ユーザー名が未入力です！<br />";
					}
				}

				// 登録パスワードを入力フォームから取得する。
				if(request.getParameter("InUserPass") != null)
				{
					InUserPass = request.getParameter("InUserPass");

					// SQLインジェクション対策の入力チェックを行い、trueの場合は入力フォームのページに戻す。
					if(check.strMatching(InUserPass) == true)
					{
						InsErrMsg = InsErrMsg + "◎パスワードに使用できない文字があります！<br />";
					}
					// 入力値の取得結果が空文字列以外の場合は、アレイリストに入力値を格納する。
					else if( !(InUserPass.equals("")) )
					{
						// 登録パスワードの入力チェックをOKとする。
						InUserPassOk = true;
						// データ型に文字列型を指定。
						InsertData.add(1);
						// 続けて入力値をアレイリストに格納。
						InsertData.add(InUserPass);
					}
					// 空文字列の場合
					else
					{
						// 登録パスワードの入力を促すエラーメッセージを表示させる。
						InsErrMsg = InsErrMsg + "◎パスワードが未入力です！<br />";
					}
				}

				// この時点で全ての入力フォームの内容が空文字列以外の場合は、入力された情報をデータベースに格納する処理を行う。
				if(InUserMailOk == true && InUserIdOk == true && InUserNameOk == true && InUserPassOk == true)
				{
					// ユーザー登録の成功可否情報を成功とする。
					InsertInfo = 1;

					// ユーザーの退会情報の初期値は0に設定する。
					// データ型に数値型を指定。
					InsertData.add(2);
					// ユーザーの退会情報データを格納する。
					InsertData.add(0);

					// アレイリストに格納された情報を基にDBへのユーザー情報登録処理を実行する。
					InsertDBOk = UserData.insertData("t_user", "F_Mailaddress,F_UserID,F_UserName,F_Password,F_Withdrawal", InsertData);
					// DB処理の途中でエラーが発生した場合、エラーフラグをオンにする。
					if(InsertDBOk != 0)
					{
						// エラーフラグをtrueに設定。
						boolErrFlg = true;
					}

					// データベースに秘密の質問の回答に関するデフォルトデータを挿入する。
					// まずはデータベースからユーザー番号を取得する。
					OptSql = "where F_UserID = ?";
					UserNum_Li = UserData.selectInTable("t_user", "F_UserNo", OptSql, InUserID);
					// ユーザーナンバーの一覧リストの戻り値がnullの場合は、エラーフラグをtrueにする。
					if(UserNum_Li == null)
					{
						// エラーフラグ設定
						boolErrFlg = true;
					}
					// ユーザーナンバーの一覧取得に成功した場合
					else
					{
						for(int i=0;i<UserNum_Li.size();i++)
						{
							// アレイリストに格納されたユーザーナンバーの内末尾のものを記憶するユーザーナンバーとする。
							strUserNum = (String)UserNum_Li.get(i);
						}
						// 取得したユーザーナンバーを使って、秘密の質問の選んだ質問や回答のデフォルト値をデータベースに格納する。

						// 秘密の質問1
						// アレイリストの中身をリセットする。
						InsertData.clear();
						InsertDBOk = 0;
						// ユーザーナンバー
						InsertData.add(2);
						InsertData.add(Integer.parseInt(strUserNum));
						// 秘密の質問区分
						InsertData.add(2);
						InsertData.add(1);
						// 秘密の質問番号
						InsertData.add(2);
						InsertData.add(1);
						// 秘密の質問の回答
						InsertData.add(1);
						InsertData.add("");

						InsertDBOk = UserData.insertData("t_answer", "F_UserNo,F_Answerclassification,F_SecretquestionID,F_Answer", InsertData);
						// DB処理の途中でエラーが発生した場合、エラーフラグをオンにする。
						if(InsertDBOk != 0)
						{
							// エラーフラグをtrueに設定。
							boolErrFlg = true;
						}

						// 秘密の質問2
						// アレイリストの中身をリセットする。
						InsertData.clear();
						InsertDBOk = 0;
						// ユーザーナンバー
						InsertData.add(2);
						InsertData.add(Integer.parseInt(strUserNum));
						// 秘密の質問区分
						InsertData.add(2);
						InsertData.add(2);
						// 秘密の質問番号
						InsertData.add(2);
						InsertData.add(2);
						// 秘密の質問の回答
						InsertData.add(1);
						InsertData.add("");

						InsertDBOk = UserData.insertData("t_answer", "F_UserNo,F_Answerclassification,F_SecretquestionID,F_Answer", InsertData);
						// DB処理の途中でエラーが発生した場合、エラーフラグをオンにする。
						if(InsertDBOk != 0)
						{
							// エラーフラグをtrueに設定。
							boolErrFlg = true;
						}
					}
				}
				// 入力フォームのうち1つでも未入力の項目がある場合は、ユーザー登録の成功可否情報を失敗とする。
				else
				{
					InsertInfo = 2;
				}

				// フォワードに関する設定。
				if(boolErrFlg)
				{
					// エラーフラグがtrueの場合は、エラー画面に遷移。
					DisPage = "error.jsp";
				}
				else if(InsertInfo == 2)
				{
					// 入力フォームの情報に不備がある場合は、入力フォームの画面に戻る。
					DisPage = "insert.jsp";
					// 入力エラーの情報をリクエストに追加。
					request.setAttribute("InsErrMsg", InsErrMsg);
				}
				else
				{
					// 正常にユーザー登録処理が完了した場合、登録処理成功画面に移る。
					DisPage = "in_result.jsp";
				}
			}

			// 個人情報追加確認ページから「変更確認」ボタンが押された場合の処理。
			else if(request.getParameter("S_CAKakuninn") != null)
			{
				// 個人情報入力項目の内の必須項目が全て入力(データベースの値として有効)されている場合にtrue、そうでない場合にfalseとなるフラグ。
				boolean ca_hissuok = true;
				// ChangeAcountクラスのインスタンスを生成。
				ChangeAcount ca = new ChangeAcount();
				// 入力データのチェック結果を格納するアレイリスト。
				ArrayList<Integer> caCheck = null;

				// リクエストから各個人情報のデータを取得し、ChangeAcountクラスのセッターに値を格納する。
				// ユーザーの氏
				if(request.getParameter("surname") != null)
				{
					ca.setSurname(request.getParameter("surname"));
				}
				// ユーザーの名
				if(request.getParameter("name") != null)
				{
					ca.setName(request.getParameter("name"));
				}
				// 郵便番号
				if(request.getParameter("postalcode") != null)
				{
					ca.setPostalcode(request.getParameter("postalcode"));
				}
				// 都道府県
				if(request.getParameter("prefectures") != null)
				{
					ca.setPrefectures(request.getParameter("prefectures"));
				}
				// 市区町村
				if(request.getParameter("city") != null)
				{
					ca.setCity(request.getParameter("city"));
				}
				// 住所詳細情報
				if(request.getParameter("streetAddress") != null)
				{
					ca.setStreetAddress(request.getParameter("streetAddress"));
				}
				// 電話番号
				if(request.getParameter("phoneNumber") != null)
				{
					ca.setPhoneNumber(request.getParameter("phoneNumber"));
				}

				// 取得した入力データを元にデータベースの値として有効かチェックをかける。
				caCheck = ca.checkAcountInfo(InPattern);

				// 結果を格納しているアレイリストの値を1個ずつ見て行き、必須項目(電話番号も含む)で1になっているデータがあれば入力エラーメッセージを追加する。
				for(int i=0;i<caCheck.size();i++)
				{
					// 必須項目の内1つでも空白または「未入力」のデータがあれば、エラーメッセージを表示し再度入力画面を表示させる。
					if(caCheck.get(i) >= 1)
					{
						// 入力チェックのフラグをfalseに設定。
						ca_hissuok = false;
						// リクエストに入力チェックのアレイリストデータを追加。
						request.setAttribute("caCheck", caCheck);
						// 空白・未入力項目が1つ見つかったので、ループを強制的に抜ける。
						break;
					}
				}

				// この時点で必須項目全入力のフラグがtrueの場合、フォワード先を変更確認画面へ。
				if(ca_hissuok == true)
				{
					DisPage = "ca_kakuninn.jsp";
				}
				else
				{
					// 入力項目の内1つでも空白・未入力データが存在した場合、入力画面へ再度戻る。
					DisPage = "c_acount.jsp";
				}

				// 現在入力されている個人情報をリクエストに全て追加。
				request.setAttribute("surname", ca.getSurname());
				request.setAttribute("name", ca.getFirstName());
				request.setAttribute("postalcode", ca.getPostalcode());
				request.setAttribute("prefectures", ca.getPrefectures());
				request.setAttribute("city", ca.getCity());
				request.setAttribute("streetAddress", ca.getStreetAddress());
				request.setAttribute("phoneNumber", ca.getPhoneNumber());

			}
			// 個人情報変更確認ページから「設定画面へ戻る」ボタンが押された場合の処理
			else if(request.getParameter("S_backtoCA") != null)
			{
				// 元ページから送られた入力情報のリクエストを受け取り、設定画面へ戻った際にデフォルト値として表示させるために送信する。
				// ユーザーの氏
				if(request.getParameter("surname") != null)
				{
					request.setAttribute("surname", request.getParameter("surname"));
				}
				// ユーザーの名
				if(request.getParameter("name") != null)
				{
					request.setAttribute("name", request.getParameter("name"));
				}
				// 郵便番号
				if(request.getParameter("postalcode") != null)
				{
					request.setAttribute("postalcode", request.getParameter("postalcode"));
				}
				// 都道府県
				if(request.getParameter("prefectures") != null)
				{
					request.setAttribute("prefectures", request.getParameter("prefectures"));
				}
				// 市区町村
				if(request.getParameter("city") != null)
				{
					request.setAttribute("city", request.getParameter("city"));
				}
				// 住所詳細情報
				if(request.getParameter("streetAddress") != null)
				{
					request.setAttribute("streetAddress", request.getParameter("streetAddress"));
				}
				// 電話番号
				if(request.getParameter("phoneNumber") != null)
				{
					request.setAttribute("phoneNumber", request.getParameter("phoneNumber"));
				}

				// 画面遷移先をc_acount.jspに設定。
				DisPage = "c_acount.jsp";
			}
		}
		catch(Exception e)
		{
			// エラーフラグをtrueとする。
			boolErrFlg = true;
			// 例外の詳細を出力する。
			e.printStackTrace();
			// 画面遷移先をerror.jspに設定。
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
