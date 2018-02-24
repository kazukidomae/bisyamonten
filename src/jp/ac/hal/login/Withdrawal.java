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

/**
 * Servlet implementation class Withdrawal
 */
@WebServlet("/Withdrawal")
public class Withdrawal extends HttpServlet {
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
		// Cookieオブジェクトを格納する変数。
		Cookie cookie = null;
		// ブラウザから取得した格納されているCookie一覧リストを取得し、格納する配列変数。
		Cookie[] cookies = null;
		// 現在Cookieに入っているユーザーIDを格納する変数。
		String cookUserID = "";

		// 入力フォームに入力されたパスワードを一時的に格納する変数。
		String userPass = "";

		// データベース接続用のurlを格納する変数。
		String url = "jdbc:mysql:///hew02?user=root&password=" + "&useUnicode=yes&characterEncoding=utf8";
		// データベースのアクセスオブジェクト。
		LoginDao dao = new LoginDao(url);

		// 入力されたパスワードがDBのパスワードと一致した場合にtrueとなるフラグ。
		boolean passOk = false;

		// データベースの更新処理結果を格納するフラグ。更新処理が失敗した場合はfalseとなる。
		boolean dbres = false;
		// データベースの更新処理で取得対象となるカラム名をまとめたアレイリスト変数。
		ArrayList<String> up_col = new ArrayList<String>();
		// データベースの更新処理で実際にデータベースへ入れるデータをまとめたアレイリスト変数。
		ArrayList<Object> up_data = new ArrayList<Object>();
		// データベースから取得したデータを格納するためのアレイリスト。なお、データベースからの取得が失敗した場合はnullとなる。
		ArrayList<Object> dbselect = new ArrayList<Object>();
		// 退会処理のエラーフラグ。退会処理の途中でエラーが発生した場合はtrueとなる。
		boolean wd_error = false;
		// 退会処理に失敗した場合の原因の種類を格納する変数。
		// 0:原因不明　1:パスワードが登録パスワードと異なる　2:パスワードが入力されていない　3:不正な文字列が使用されている
		int wd_error_reason = 0;

		// 入力チェックの際に使うパターンマッチング用の文字列をまとめて格納するアレイリスト。
		ArrayList<String> InPattern = new ArrayList<String>();
		// アレイリストに入力チェックに使うパターンをまとめて追加。
		// INSERT INTOの文法。
		InPattern.add("^insert\\s+into\\s+.+\\(.+\\)\\s*values\\s*\\(.+\\)\\s*;*$");
		// UPDATEの文法。
		InPattern.add("^update\\s+.+\\s+set\\s+.+;*$");
		// DELETEの文法。
		InPattern.add("^delete\\s+from\\s+.+;*$");
		// 入力チェックを行う際に必要な入力チェックオブジェクトを作成する。
		InCheck check = new InCheck(InPattern);

		// 転送先のjspファイル名を格納する変数。
		String dispage = "";

		// ここまで　変数宣言・初期化スペース

		// mypage.jspの「退会するボタンが押された場合実行される処理。
		if(request.getParameter("quit") != null)
		{
			// 現在Cookieに保存されているユーザーIDデータを取得。
			if(request.getCookies() != null)
			{
				// 保存されているCookieを取得する。
				cookies = request.getCookies();
				for(int i=0;i<cookies.length;i++)
				{
					if(cookies[i].getName().equals("UserID"))
					{
						// クッキーにユーザーID情報が格納されていた場合は、クッキーからユーザーID情報を取得する。
						cookUserID = cookies[i].getValue();
					}
				}
			}

			// リクエストからテキストボックスに入力されたパスワードを取得。取得できなかった場合は退会失敗とする。
			if(request.getParameter("now_pass") != null)
			{
				userPass = request.getParameter("now_pass");
				// テキストボックスから取得したパスワードが空白、もしくは使用禁止の単語を使用していた場合、退会失敗とする。
				if(userPass.equals("") || check.strMatching(userPass) == true)
				{
					// エラーフラグをtrueにする。
					wd_error = true;
					// パスワードが空白の場合
					if(userPass.equals(""))
					{
						// エラーの原因を2とする。
						wd_error_reason = 2;
					}
					// 不正な文字列が使用された場合
					else
					{
						// エラーの原因を3とする。
						wd_error_reason = 3;
					}
				}
				else
				{
					// データベース上に格納されているパスワードと入力されたパスワードが一致しているか調べる。
					dbselect = dao.selectInTable("t_user", "F_Password", "WHERE F_UserID = ?", cookUserID);
					// データベースへの接続に失敗した場合は、退会失敗とする。
					if(dbselect != null)
					{
						// データベースからのパスワードデータの取得に成功
						// 入力したパスワードとデータベースからのパスワードデータが一致しているか調べる。
						for(int i=0;i<dbselect.size();i++)
						{
							log( String.valueOf(dbselect.get(i)) );
							if(userPass.equals( String.valueOf( dbselect.get(i) ) ))
							{
								// パスワードの合致フラグをtrueとする。
								passOk = true;
								// ループを強制脱出。
								break;
							}
						}
						// この時点で入力パスワードとDB上のパスワードが合致していた場合は、該当ユーザーの退会フラグを1に更新する。
						if(passOk == true)
						{
							// 入力パスワードとDB上のパスワードが合致した場合
							// データベースの更新対象カラム名リストに退会フラグのカラムを追加。
							up_col.add("F_Withdrawal");
							// データベースの更新で使用するデータ(退会フラグの1)をアレイリストに追加。
							// 数値型を指定。
							up_data.add(2);
							// 退会を表す1。
							up_data.add(1);

							dbres = dao.updateInTable("t_user", up_col, up_data, "WHERE F_UserID = \"" + cookUserID + "\"");
							log(String.valueOf(dbres) );
							// データベースの更新処理に失敗した場合は、退会失敗とする。
							if(dbres == false)
							{
								// エラーフラグをtrueとする。
								wd_error = true;
							}
						}
						else
						{
							// 入力パスワードがDB上のパスワードと一致しなかった場合
							// エラーフラグをtrueとする。
							wd_error = true;
							// エラーの原因を1とする。
							wd_error_reason = 1;
						}
					}
					else
					{
						//エラーフラグをtrueとする。
						wd_error = true;
					}
				}
			}
			else
			{
				// エラーフラグをtrueとする。
				wd_error = true;
			}

			// この時点でエラーフラグがtrueの場合は、mypage.jspに戻り、退会処理に失敗した旨のエラーメッセージを表示させる。
			if(wd_error == true)
			{
				// 退会処理に失敗
				// リクエストにエラーフラグの内容を追加。
				request.setAttribute("wd_error", wd_error);
				// リクエストにエラーの発生原因を追加。
				request.setAttribute("wd_error_reason", wd_error_reason);
				// 遷移先ページをmypage.jspに設定。
				dispage = "mypage.jsp";
			}
			else
			{
				// 退会処理に成功
				// 現在Cookieに格納されているユーザーID情報を削除する。
				if(request.getCookies() != null)
				{
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
				}
				// 遷移先ページをwdcomp.jspに設定。
				dispage = "wdcomp.jsp";
			}
		}

		// フォワード処理
		RequestDispatcher disp = request.getRequestDispatcher(dispage);
		disp.forward(request, response);
	}
}
