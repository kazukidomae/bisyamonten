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
 * Servlet implementation class galley
 */
@WebServlet("/gallery")
public class Gallery extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Gallery() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub


		// データベースアクセスオブジェクト作成
		GalleryDao galleryDao = new GalleryDao();

		// データベースに登録されている作品ID一覧を取得するアレイリスト。
		ArrayList<GalleryBean> galleryBeans = galleryDao.publicgallery();

		//いいね数アップ
		//galleryDao.updateNice();

		request.setAttribute("galleryBeans", galleryBeans);



		// 転送処理
		RequestDispatcher disp = request.getRequestDispatcher("gallery.jsp");
		disp.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
