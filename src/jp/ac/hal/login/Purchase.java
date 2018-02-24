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
 * Servlet implementation class Purchase
 */
@WebServlet("/Purchase")
public class Purchase extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Purchase() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		request.setCharacterEncoding("UTF-8");

		//クッキーから現在ログイン中のUserIDを取得
		Cookie[] cookies = request.getCookies();
		String cookieValue ="";

		if(cookies != null) {
			for(int i = 0; i < cookies.length; i++) {
				if(cookies[i].getName().equals("UserID")){
				cookieValue = cookies[i].getValue();
				}
			}
		}

		//使用ステッカー一覧取得
		String wd = request.getParameter("workID");
		// データベースアクセスオブジェクト作成
		UsestickerDAO usestickerDao = new UsestickerDAO();
		// データベースに登録されている使用ステッカー一覧を取得するアレイリスト。
		ArrayList<UsestickerBean> usestickerBeans = usestickerDao.usesticker(wd);

		request.setAttribute("usestickerBeans", usestickerBeans);


		//購入情報取得
		//支払い方法取得
		String pay;
		if(request.getParameter("pay").equals("WebMoney")){
			pay = request.getParameter("wmnum");
		}
		else{
			pay = request.getParameter("creditnum");
		}
		request.setAttribute("payment", pay);

		//配送先情報取得
		UserBean address = null;
		if(request.getParameter("deli").equals("address")){

			BuyDAO bd = new BuyDAO();
			address = bd.Streetaddress(cookieValue);

		}
		else{
			address = new UserBean();
			address.setSurname(request.getParameter("sumname"));
			address.setName(request.getParameter("name"));
			address.setPostalcode(request.getParameter("code"));
			address.setPrefectures(request.getParameter("prefect"));
			address.setCity(request.getParameter("pality"));
			address.setStreetaddress(request.getParameter("city"));
			address.setPhonenumber(request.getParameter("tell"));

		}

		request.setAttribute("address", address);

		// 転送処理
		RequestDispatcher disp = request.getRequestDispatcher("buyresult.jsp");
		disp.forward(request, response);
	}
}
