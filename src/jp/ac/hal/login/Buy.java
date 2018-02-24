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
 * Servlet implementation class UseStickerDAO
 */
@WebServlet("/Buy")
public class Buy extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Buy() {
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

		String wd = request.getParameter("workID");

		// データベースアクセスオブジェクト作成
		UsestickerDAO usestickerDao = new UsestickerDAO();

		// データベースに登録されている使用ステッカー一覧を取得するアレイリスト。
		ArrayList<UsestickerBean> usestickerBeans = usestickerDao.usesticker(wd);

		request.setAttribute("usestickerBeans", usestickerBeans);

		// 転送処理
		RequestDispatcher disp = request.getRequestDispatcher("buy.jsp");
		disp.forward(request, response);
	}

}
