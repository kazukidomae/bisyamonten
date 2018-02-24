package jp.ac.hal.login;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class PurchaseComp
 */
@WebServlet("/PurchaseComp")
public class PurchaseComp extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());

		// リクエストの文字コードをUTF-8に設定。
		request.setCharacterEncoding("UTF-8");
		// 遷移先のマイページで表示させる注文完了のメッセージ。
		String comp_mes = "ご注文手続きが完了しました。";

		// リクエストに注文を完了した旨のメッセージを追加。
		request.setAttribute("comp_mes",comp_mes);
		RequestDispatcher rd = request.getRequestDispatcher("mypage.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
