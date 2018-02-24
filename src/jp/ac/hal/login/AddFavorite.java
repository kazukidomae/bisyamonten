package jp.ac.hal.login;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AddFavorite
 */
@WebServlet("/AddFavorite")
public class AddFavorite extends HttpServlet {
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
		// TODO Auto-generated method stub
		// doGet(request, response);

		// リクエストの文字コード設定。
		request.setCharacterEncoding("UTF-8");

		// ここから　変数宣言・初期化スペース

		// データベース接続用のURL。
		String url = "jdbc:mysql:///hew02?user=root&password=" + "&useUnicode=yes&characterEncoding=utf8";
		// データベースアクセス用オブジェクト。
		LoginDao dao = new LoginDao(url);
		// データベースへのお気に入り情報の挿入結果を格納するフラグ。　false:DB処理失敗　true:DB処理成功
		boolean db_res = true;
		// データベースに挿入処理を行った時の戻り値を格納する整数型変数。
		int ins_res = 0;
		// データベースに挿入するデータをまとめて格納したアレイリスト。
		ArrayList<Object> insdata = new ArrayList<Object>();
		// データベースからのセレクトによる取得結果を格納するアレイリスト。
		ArrayList<Object> sele_res = new ArrayList<Object>();

		// リクエストから取得した作品ID情報を格納する数値型変数。
		int workID = 0;
		// ユーザーIDを格納する変数。
		String userID = "";
		// データベースから取得したユーザーナンバーを格納する変数。
		int userNo = 0;
		// 遷移先のページ名を格納する変数。
		String dispage = "";

		// ここまで　変数宣言・初期化スペース

		// 処理途中で例外が発生した場合、error.jspに遷移させる。
		try
		{
			//クッキーから現在ログイン中のUserIDを取得
			/* Cookie[] cookies = request.getCookies();
			String cookUserID = "";

			if(cookies != null) {
				for(int i = 0; i < cookies.length; i++) {
					if(cookies[i].getName().equals("UserID")){
						cookUserID = cookies[i].getValue();
						userID = cookUserID;
					}
				}
			}

			// リクエストから作品IDを取得する。
			if(request.getParameter("FrWorkID") != null)
			{
				log(request.getParameter("FrWorkID"));
				workID = Integer.parseInt( request.getParameter("FrWorkID") );

				// ユーザーIDを基にデータベースからユーザーナンバーを取得する。
				sele_res = dao.selectInTable("t_user", "F_UserNo", "WHERE F_UserID = ?", userID);
				if(sele_res != null && sele_res.size() >= 1)
				{
					// 取得した結果リストのうち先頭の要素番号に格納されているユーザーナンバーを使用する。
					userNo = Integer.parseInt( (String)sele_res.get(0) );
				}

				// データベースに格納するデータをアレイリストに予め格納する。
				// ユーザーナンバー
				insdata.add(2);
				insdata.add(userNo);
				// 作品ID
				insdata.add(2);
				insdata.add(workID);
				// お気に入り登録日時(現在時刻を代入)
				insdata.add(3);
				insdata.add(0);

				// データベース挿入処理
				ins_res = dao.insertData("t_favorite", "F_UserNo,F_WorkID,F_Registrationday", insdata);
				// データベースへの挿入処理が正しく行われた場合は、お気に入り登録成功。
				if(ins_res == 0)
				{
					db_res = true;
				}
			}
			// リクエストにDB処理の結果情報追加。 */
			request.setAttribute("db_res", db_res);
			dispage = "gallery.jsp";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			dispage = "error.jsp";
		}
		finally
		{
			// 転送処理
			RequestDispatcher rd = request.getRequestDispatcher(dispage);
			rd.forward(request, response);
		}

	}

}
