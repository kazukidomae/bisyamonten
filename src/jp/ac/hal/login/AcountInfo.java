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
 * Servlet implementation class AcountInfo
 */
@WebServlet("/AcountInfo")
public class AcountInfo extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: xxx").append(request.getContextPath());
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

		// SELECT文の後ろオプションとして追加するSQL文の内容を格納する。
		String OptSql = "";

		// 入力チェックの際に使うパターンマッチング用の文字列をまとめて格納するアレイリスト。
		ArrayList<String> InPattern = new ArrayList<String>();
		// アレイリストに入力チェックに使うパターンをまとめて追加。
		// INSERT INTOの文法。
		InPattern.add("^insert\\s+into\\s+.+\\(.+\\)\\s*values\\s*\\(.+\\)\\s*;*$");
		// UPDATEの文法。
		InPattern.add("^update\\s+.+\\s+set\\s+.+;*$");
		// DELETEの文法。
		InPattern.add("^delete\\s+from\\s+.+;*$");

		// クッキーから取り出したユーザーIDを格納する変数。
		String cookUserId = "";

		// 現在ブラウザに格納されているクッキーの一覧リストをまとめて格納する配列変数。
		Cookie[] cookies = request.getCookies();
		for(int i=0;i<cookies.length;i++)
		{
			if(cookies[i].getName().equals("UserID"))
			{
				// 該当のクッキーからユーザーIDデータを取り出す。
				cookUserId = cookies[i].getValue();
			}
		}

		//　ここまで　変数宣言・初期化スペース

		// データベース接続情報の設定
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

		// ChangeAcountクラスのインスタンスを生成。
		ChangeAcount ca = new ChangeAcount();
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

		// 例外処理。万が一処理途中で例外が発生した場合は、error.jspへ遷移させる。
		try
		{
			// 個人情報追加確認ページから「変更確認」ボタンが押された場合の処理。
			if(request.getParameter("S_CAKakuninn") != null)
			{
				// 個人情報入力項目の内の必須項目が全て入力(データベースの値として有効)されている場合にtrue、そうでない場合にfalseとなるフラグ。
				boolean ca_hissuok = true;
				// 入力データのチェック結果を格納するアレイリスト。
				ArrayList<Integer> caCheck = null;

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
					// 入力項目の内1つでも空白・未入力データが存在した場合、マイページ画面へ再度戻る。
					DisPage = "mypage.jsp";
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
				request.setAttribute("surname", ca.getSurname());
				// ユーザーの名
				request.setAttribute("name", ca.getFirstName());
				// 郵便番号
				request.setAttribute("postalcode", ca.getPostalcode());
				// 都道府県
				request.setAttribute("prefectures", ca.getPrefectures());
				// 市区町村
				request.setAttribute("city", ca.getCity());
				// 住所詳細情報
				request.setAttribute("streetAddress", ca.getStreetAddress());
				// 電話番号
				request.setAttribute("phoneNumber", ca.getPhoneNumber());

				// 画面遷移先をmypage.jspに設定。
				DisPage = "mypage.jsp";
			}

			// 個人情報変更確認ページから「変更」ボタンが押された場合の処理。
			else if(request.getParameter("S_CAtoDB") != null)
			{
				// ここから　変数宣言・初期化スペース
				// 変更対象のカラム名を全て追加したアレイリストを用意する。
				ArrayList<String> colLi = new ArrayList<String>();
				// 個人情報更新関連のカラム名を追加。
				colLi.add("surname");
				colLi.add("firstname");
				colLi.add("postalcode");
				colLi.add("prefectures");
				colLi.add("city");
				colLi.add("streetAddress");
				colLi.add("phoneNumber");

				// データベースの更新処理が成功した場合true、失敗した場合falseとなるフラグ。
				boolean update_suc = false;
				// フォワード先で表示させるページのタイトルを格納する。
				String comp_title = "";
				// フォワード先で表示させるページの本文を格納する。
				String comp_mes = "";
				// ここまで　変数宣言・初期化スペース

				// 個人情報のデータベース更新処理を実行する。
				update_suc = ca.writeAcountInfo(UserData, cookUserId, colLi);

				// データベースの更新処理に成功した場合
				if(update_suc == true)
				{
					// フォワード先の大見出し
					comp_title = "個人情報の変更完了";
					// フォワード先の本文
					comp_mes = "個人情報の変更が正常に完了しました。";
				}
				// データベースの更新処理に失敗した場合
				else
				{
					// フォワード先の大見出し
					comp_title = "個人情報の変更失敗";
					// フォワード先の本文
					comp_mes = "申し訳ありません。個人情報の変更処理が正常に行われませんでした。";
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
			boolErrFlg = true;
			// エラー内容を出力させる。
			e.printStackTrace();
			// 画面遷移先をerror.jspに設定。
			DisPage = "errpr.jsp";
		}
		finally
		{

			// 転送処理
			RequestDispatcher disp = request.getRequestDispatcher(DisPage);
			disp.forward(request, response);
		}

	}
}
