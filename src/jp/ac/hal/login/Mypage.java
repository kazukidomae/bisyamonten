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
 * Servlet implementation class MyGallery
 */
@WebServlet("/mypage")
public class Mypage extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Mypage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		// ここから　変数宣言・初期化スペース

		// 遷移先ページを格納する変数。
		String DisPage = "";

		//クッキーから現在ログイン中のUserIDを取得
		Cookie[] cookies = request.getCookies();
		Integer count = null;
		String cookieValue ="";

		// ユーザー情報を格納するChangeAcountクラス。
		ChangeAcount CA = new ChangeAcount();

		// データベース接続情報の設定
		String url = "jdbc:mysql:///hew02?user=root&password=" + "&useUnicode=yes&characterEncoding=utf8";
		// データベースアクセスオブジェクト作成
		LoginDao UserData = new LoginDao(url);
		// セッションを格納するためのオブジェクトを定義。
		HttpSession session = request.getSession();


		// ここまで　変数宣言・初期化スペース

		// 処理途中で例外が発生した場合は、error.jspのページへ遷移させる。
		try
		{
			if(cookies != null) {
				for(int i = 0; i < cookies.length; i++) {
					if(cookies[i].getName().equals("UserID")){
					cookieValue = cookies[i].getValue();
					}
				}
			}

			// データベースアクセスオブジェクト作成
			GalleryDao galleryDao = new GalleryDao();

			// データベースに登録されている作品ID一覧を取得するアレイリスト。
			ArrayList<GalleryBean> galleryBeans = galleryDao.mygallery(cookieValue);

			request.setAttribute("galleryBeans", galleryBeans);

			// DBからの個人情報の取得処理。
			// DBから個人情報データを取得。
			CA.readAcountInfo(UserData, cookieValue, 1);
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
			CA.readAcountInfo(UserData, cookieValue, 2);
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
			sec.readSecretQuestion(UserData, cookieValue);
			// DBからの読み出し結果をリクエストに追加
			request.setAttribute("usSQ", sec.getUsSQ());

			// DBからの秘密の質問ユーザー回答データをリクエストに追加。
			request.setAttribute("usSQAnswer", sec.getUsSQAnswer());

			// セッションの有効期限を無期限に設定。
			session.setMaxInactiveInterval(-1);

			// 遷移先ページをmypage.jspに設定する。
			DisPage = "mypage.jsp";
		}
		catch(Exception e)
		{
			// エラーの原因をスタックトレースとして出力する。
			e.printStackTrace();
			// 遷移先ページをerror.jspに設定。
			DisPage = "error.jsp";
		}
		finally
		{
			// 転送処理
			RequestDispatcher disp = request.getRequestDispatcher(DisPage);
			disp.forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
