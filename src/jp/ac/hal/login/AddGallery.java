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
 * Servlet implementation class AddGallery
 */
@WebServlet("/AddGallery")
public class AddGallery extends HttpServlet {
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

		// リクエストの文字コードを設定する。
		request.setCharacterEncoding("UTF-8");

		// ここから　変数宣言・初期化スペース
		// 現在のステッカージャンルの種類数を格納する変数。
		int sg_size = 22;

		// javaの処理でのカウンタ用変数。
		int i = 0;
		// 画面遷移先jspファイル名を格納。
		String reqDisp = "";

		// データベース接続用URLを格納する変数。
		String url = "jdbc:mysql:///hew02?user=root&password=" + "&useUnicode=yes&characterEncoding=utf8";
		// データベース接続用のオブジェクト定義。
		LoginDao dao = new LoginDao(url);

		// 取得したCookie一覧を格納する配列変数。
		Cookie cookies[] = null;
		// Cookieから取得したユーザーIDを格納する変数。
		String userID = "";
		// データベースに格納用のユーザーナンバーを格納する変数。
		int userNo = 0;

		// データベースに格納予定のデータをまとめたアレイリスト変数。
		ArrayList<Object> dbdata = new ArrayList<Object>();
		// データベースからselectで取得したレコード一覧を格納するアレイリスト変数。
		ArrayList<Object> dbres = new ArrayList<Object>();
		// データベースへの作品情報新規登録の登録結果を格納する変数。
		int saku_res = 0;

		// データベースから取得した作品IDを一時的に格納する変数。
		int workID = 0;

		// ステッカーのジャンルごとのS、M、L各サイズの枚数を格納するオブジェクト。配列の要素番号はDB上のステッカーidとリンクする。なお、配列の要素番号0番はダミーデータを格納するため、未使用とする。
		SteCount[] count = new SteCount[(sg_size + 1)];
		for(i=0;i<count.length;i++)
		{
			count[i] = new SteCount();
		}

		// ここまで　変数宣言・初期化スペース

		// リクエストから展開図に貼られたジャンルごとの各ステッカーの枚数を取得する。


		// 処理途中で例外が発生した場合は処理を中断し、遷移先ページを"error.jsp"に変更する。
		try
		{
			// CookieからユーザーのユーザーIDを取得する。
			cookies = request.getCookies();
			for(i=0;i<cookies.length;i++)
			{
				if(cookies[i].getName().equals("UserID"))
				{
					// クッキーからユーザーID情報を取得する。
					userID = cookies[i].getValue();
				}
			}

			// 取得したユーザーIDを使って、該当ユーザーのユーザーナンバーを取得する。
			dbres = dao.selectInTable("t_user", "F_UserNo", "where F_UserID = ?", userID);
			// データベースからの取得結果がnull、もしくはレコード数0以外の場合は、取得した結果リストの先頭にある要素を、データベース格納用のユーザーナンバーとして設定する。
			if(dbres != null && dbres.size() >= 1)
			{
				// データベースの結果リストの先頭の要素をデータベース格納用のユーザーナンバーとする。
				userNo = Integer.parseInt( (String)dbres.get(0) );
				// 取得したユーザーナンバーを使って、データベースに作品情報を追加登録する。
				// アレイリストに追加登録する作品情報などをアレイリストに予め格納する。
				// ユーザーナンバー
				dbdata.add(2);
				dbdata.add(userNo);
				// 車型ID
				dbdata.add(2);
				dbdata.add(1);
				// ステッカー価格表ID
				dbdata.add(2);
				dbdata.add(1);
				// 作成デザインパス
				dbdata.add(1);
				dbdata.add("a");
				// サムネイル画像パス
				dbdata.add(1);
				dbdata.add("b");
				// 公開フラグ
				dbdata.add(2);
				dbdata.add(0);
				// いいね数
				dbdata.add(2);
				dbdata.add(0);
				// お気に入り数
				dbdata.add(2);
				dbdata.add(0);
				// 作成日時
				dbdata.add(3);
				dbdata.add(0);

				// アレイリストに予め格納したデータを作品情報管理用データベースに挿入する。
				saku_res = dao.insertData("t_work", "F_UserNo,F_VehicletypeID,F_StickerpricelistID,F_Workdesing,F_Thumbnailpass,F_Release,F_Nice,F_Favorite,F_Workdata", dbdata);

				// データベースに作品情報が登録できたら、続いて先ほど生成された作品IDを利用してデータベースにステッカーの貼り付け枚数を格納する。
				// まずは、ユーザーのユーザーナンバーから作品IDを取得。結果リストのうち最も値が大きい作品IDを使ってステッカーの枚数登録を行う。
				dbres = dao.selectInTable("t_work", "F_WorkID", "where F_UserNo = " + String.valueOf(userNo) + " order by F_WorkID desc", "");
				if(dbres != null && dbres.size() >= 1)
				{
					// データベースの結果リストがnullもしくはレコード数0以外の場合
					// 作品IDを取得する。
					workID = Integer.parseInt((String)dbres.get(0));

					// 展開図に貼られているステッカーのジャンルの種類数分処理を繰り返す。
					for(i=1;i<=sg_size;i++)
					{
						// データベースへのステッカー枚数情報追加可否を格納するboolean型変数。SS・S・M・L・LLが全て0枚の場合は、データベースへの追加を行わない。
						boolean ste_notfound = true;
						// リクエストにステッカーidのi番のSSサイズのステッカー枚数があれば取得し、カウントオブジェクトに枚数情報を記録する。
						if(request.getParameter("ste" + String.valueOf(i) + "SS") != null)
						{
							count[i].setSs( Integer.parseInt(request.getParameter("ste" + String.valueOf(i) + "SS")) );
							ste_notfound = false;
						}
						// リクエストにステッカーidのi番のSサイズのステッカー枚数があれば取得し、カウントオブジェクトに枚数情報を記録する。
						if(request.getParameter("ste" + String.valueOf(i) + "S") != null)
						{
							count[i].setS( Integer.parseInt(request.getParameter("ste" + String.valueOf(i) + "S")) );
							ste_notfound = false;
						}
						// リクエストにステッカーidのi番のMサイズのステッカー枚数があれば取得し、カウントオブジェクトに枚数情報を記録する。
						if(request.getParameter("ste" + String.valueOf(i) + "M") != null)
						{
							count[i].setM( Integer.parseInt(request.getParameter("ste" + String.valueOf(i) + "M")) );
							ste_notfound = false;
						}
						// リクエストにステッカーidのi番のLサイズのステッカー枚数があれば取得し、カウントオブジェクトに枚数情報を記録する。
						if(request.getParameter("ste" + String.valueOf(i) + "L") != null)
						{
							count[i].setL( Integer.parseInt(request.getParameter("ste" + String.valueOf(i) + "L")) );
							ste_notfound = false;
						}
						// リクエストにステッカーidのi番のLLサイズのステッカー枚数があれば取得し、カウントオブジェクトに枚数情報を記録する。
						if(request.getParameter("ste" + String.valueOf(i) + "LL") != null)
						{
							count[i].setLl( Integer.parseInt(request.getParameter("ste" + String.valueOf(i) + "LL")) );
							ste_notfound = false;
						}

						// この時点でステッカーのSS・S・M・L・LLいずれのサイズも貼り付け枚数が0枚の場合は、データベースへの追加処理をスキップする。
						if(ste_notfound == false)
						{
							// データベースへ格納するi番のステッカーのサイズ別枚数情報を予めアレイリストにセットして準備する。
							dbdata.clear();
							// ステッカーID(ステッカーidをセット。)
							dbdata.add(2);
							dbdata.add(i);
							// 作品ID
							dbdata.add(2);
							dbdata.add(workID);
							// ステッカーSSサイズ
							dbdata.add(2);
							dbdata.add(count[i].getSs());
							// ステッカーSサイズ
							dbdata.add(2);
							dbdata.add(count[i].getS());
							// ステッカーMサイズ
							dbdata.add(2);
							dbdata.add(count[i].getM());
							// ステッカーLサイズ
							dbdata.add(2);
							dbdata.add(count[i].getL());
							// ステッカーLLサイズ
							dbdata.add(2);
							dbdata.add(count[i].getLl());
							// ステッカー全面サイズ
							dbdata.add(2);
							dbdata.add(0);

							// ステッカーの枚数情報をデータベースへ格納する。
							saku_res = dao.insertData("t_usesticker", "F_StickerID,F_WorkID,F_NumberofsheetsSS,F_NumberofsheetsS,F_NumberofsheetsM,F_NumberofsheetsL,F_NumberofsheetsLL,F_NumberofsheetsALL", dbdata);
							log(String.valueOf(saku_res));

						}
					}
				}
			}

			// 遷移先のページをdesign.jspに設定。
			reqDisp = "design.jsp";
		}
		catch(Exception e)
		{
			// コンソールにエラーの内容を出力。
			e.printStackTrace();
			// 遷移先ページをerror.jspに変更。
			reqDisp = "error.jsp";
		}
		finally
		{
			// 遷移先ページへの転送処理。
			RequestDispatcher req = request.getRequestDispatcher(reqDisp);
			req.forward(request, response);
		}
	}
}
