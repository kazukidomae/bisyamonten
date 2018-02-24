package jp.ac.hal.Jv2d;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.ac.hal.login.LoginDao;

/**
 * Servlet implementation class Jv2d
 */
@WebServlet("/Jv2d")
public class Jv2d extends HttpServlet {
	/* ここから　グローバル変数の定義・初期化スペース */
	/* カウンタ用変数 */
	public int i = 0;

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
		move_st(request, response);
	}
	public void move_st(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// リクエストの文字コードを設定
		request.setCharacterEncoding("UTF-8");

		// ここから　変数宣言・初期化スペース
		// javaの処理でのカウンタ用変数。
		int i;

		// リクエストディスパッチオブジェクトの定義
		RequestDispatcher req = null;
		// 画面遷移先jspファイル名を格納。
		String reqdisp = "";
		// 例外が発生した時の例外の種類を格納する変数。
		// 0:例外発生なし　1:ファイルの読み込み失敗　2:ファイルの書き込み失敗　3:ファイルアウトプットストリームのクローズ失敗
		int err = 0;

		// 読み込む展開図画像ファイルのファイル名
		String fname = "images/vezel_uv_whitefinal.png";

		// ファイルの絶対パスを格納。
		String dir = getServletContext().getRealPath("/");
		// BufferedImageオブジェクト(展開図画像用)の作成。
		BufferedImage im = null;
		// BufferedImageオブジェクト(ステッカー画像用)の作成。
		BufferedImage im2 = null;

		// リクエストから取得したステッカーの各x座標リスト。
		ArrayList<Integer> stex = new ArrayList<Integer>();
		// リクエストから取得したステッカーの各y座標リスト。
		ArrayList<Integer> stey = new ArrayList<Integer>();
		// リクエストから取得した各ステッカーの幅リスト。
		ArrayList<Integer> stew = new ArrayList<Integer>();
		// リクエストから取得した各ステッカーの高さリスト。
		ArrayList<Integer> steh = new ArrayList<Integer>();
		// リクエストから取得したステッカーの画像ファイルパスリスト。
		ArrayList<String> stesrc = new ArrayList<String>();

		// ブラウザバック処理の有無情報。
		int broBack = 1;

		// データベース接続用URLを格納する変数。
		String url = "jdbc:mysql:///hew02?user=root&password=" + "&useUnicode=yes&characterEncoding=utf8";
		// データベース接続用オブジェクトの定義。
		LoginDao dao = new LoginDao(url);

		// データベースへの作品情報新規登録の登録結果を格納する変数。
		int saku_res = 0;
		// データベースに格納予定のデータをまとめたアレイリスト変数。
		ArrayList<Object> dbdata = new ArrayList<Object>();
		// データベースからselectで取得した結果リストを格納するアレイリスト。
		ArrayList<Object> dbres = null;

		// 取得したCookieの一覧リストを格納する配列変数。
		Cookie[] cookies = null;

		// Cookieから取得したユーザーIDを格納する変数。
		String userID = "";
		// データベースへの格納用のユーザーナンバー(現在ログイン中のユーザーの)を格納する数値型変数。
		int userNo = 0;
		// データベースから取得した作品IDを格納する変数。
		int workID = 0;

		// デザインページからリクエストで送られたステッカーの貼り付け枚数を取得し、格納する変数。
		int ste_count = 0;

		// リクエストから取得した、展開図に貼られたステッカーの回転角度をまとめて格納するアレイリスト。
		// 格納される値と回転角度‥‥‥　0:0度　1:90度　2:180度　3:270度
		ArrayList<Integer> sterot = new ArrayList<Integer>();
		// リクエストから取得した、展開図に貼られたステッカーのキャラクターの種類をまとめて格納するアレイリスト。
		ArrayList<Integer> stegen = new ArrayList<Integer>();
		// リクエストから取得した各ステッカーのサイズリスト。
		ArrayList<String> stesize = new ArrayList<String>();

		// 現在のステッカージャンルの種類数を格納する変数。
		int sg_size = 22;
		// 貼り付けるステッカーが存在するファイルパスを格納する配列アレイリスト。ファイル名はデータベースのステッカーidと連動している。
		ArrayList<String> stefname = new ArrayList<String>();
		for(i=1;i<=sg_size;i++)
		{
			stefname.add("C:\\Users\\doumae.kazuki\\Documents\\JV22\\Workspace\\bisyamonten\\WebContent\\images\\sticker_img\\st" + String.valueOf(i) + ".png");
		}

		// 貼り付けるステッカーのイメージデータ(BufferdImage)をまとめて格納したアレイリスト。
		ArrayList<BufferedImage> imste = new ArrayList<BufferedImage>();

		// リクエストから受信した、ユーザーが選択した痛車車体の色情報。
		String syatai_color = "";

		// ここまで　変数宣言・初期化スペース

		// リクエストから車体の色情報が取得できる場合は取得する。
		if(request.getParameter("syatai") != null)
		{
			// 読み込む展開図のファイル名をユーザーが指定した色に準じたものに変更。
			syatai_color = request.getParameter("syatai");
			fname = "tenkaizu/t" + syatai_color + ".png";
		}

		// リクエストから展開図へのステッカーの貼り付け枚数が取得可能な場合は、取得する。
		if(request.getParameter("ste_count") != null)
		{
			ste_count = Integer.parseInt(request.getParameter("ste_count"));
		}

		// 展開図に貼られたステッカーの各x座標・y座標に関するリクエストが存在したら2つとも取得する。また同時にステッカーの幅・高さを取得する。
		i = 0;
		while(true)
		{
			if(request.getParameter("stex" + i) != null)
			{
				// ステッカーのx・y座標を取得する。
				stex.add( Integer.parseInt(request.getParameter("stex" + i)) );
				stey.add( Integer.parseInt(request.getParameter("stey" + i)) );
				// 同時に、ステッカーの幅・高さ情報を取得する。
				stew.add( Integer.parseInt(request.getParameter("stew" + i)) );
				steh.add( Integer.parseInt(request.getParameter("steh" + i)) );
				// ステッカーの画像ファイルパスをリストに追加。
				stesrc.add( request.getParameter("src" + i) );
			}
			else
			{
				// 存在しなかった場合
				break;
			}
			i++;
		}

		// リクエストから各ステッカーの回転角度が取得可能な場合は取得し、アレイリストに順に格納する。
		i = 1;
		while(true)
		{
			if(request.getParameter("sterot" + String.valueOf(i)) != null )
			{
				sterot.add( Integer.parseInt( request.getParameter("sterot" + String.valueOf(i)) ) );
			}
			else
			{
				// 回転角度のリクエストが存在しなかった場合
				break;
			}
			i++;
		}

		// リクエストから各ステッカーのジャンルが取得可能な場合は取得し、アレイリストに順に格納する。
		i = 1;
		while(true)
		{
			if(request.getParameter("stegen" + String.valueOf(i)) != null )
			{
				stegen.add( Integer.parseInt( request.getParameter("stegen" + String.valueOf(i)) ) );
			}
			else
			{
				// ステッカージャンルのリクエストが存在しなかった場合
				break;
			}
			i++;
		}

		// リクエストから各ステッカーのサイズが取得可能な場合は取得し、アレイリストに順に格納する。
		i = 1;
		while(true)
		{
			if(request.getParameter("stesize" + String.valueOf(i)) != null )
			{
				stesize.add( request.getParameter("stesize" + String.valueOf(i)) );
			}
			else
			{
				// ステッカージャンルのリクエストが存在しなかった場合
				break;
			}
			i++;
		}

		// 展開図画像の読み込み処理
		File f = new File(dir + fname);
		// ファイルが存在している場合読み込み処理を開始。
		if(f.exists())
		{
			// ファイルの読み込みに失敗した場合は例外処理を行う。
			try
			{
				im = ImageIO.read(f);
			}
			catch(Exception e)
			{
				// ファイルの読み込みに失敗した場合
				// エラーの種類を「ファイルの読み込み失敗」に設定。
				err = 1;
				// 仮の画像を作成する。
				im = new BufferedImage(1024,1024,BufferedImage.TYPE_INT_RGB);
				e.getStackTrace();
			}
		}
		else
		{
			// ファイルの存在が確認できない場合
			// 仮の画像を作成する。
			im = new BufferedImage(1024,1024,BufferedImage.TYPE_INT_RGB);
		}

		// ステッカー画像の読み込み処理
		// アリサ・マリオ・FFのステッカーを一斉に読み込む。
		// ファイル名をステッカー画像のものに変更。
		// ステッカーのジャンル数分処理を繰り返す。
		for(i=0;i<stefname.size();i++)
		{
			fname = stefname.get(i);
			File f2 = new File(fname);
			// ファイルが存在している場合は、ステッカー画像ファイルを読み込み。
			if(f2.exists())
			{
				// ファイルの読み込みに失敗した場合は例外処理を行う。
				try
				{
					im2 = ImageIO.read(f2);
				}
				catch(Exception e)
				{
					// ファイルの読み込みに失敗した場合
					// エラーの種類を「ファイルの読み込み失敗」に設定。
					err = 1;
					// 仮の画像を作成する。
					im2 = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
					e.getStackTrace();
				}
			}
			else
			{
				// ファイルの存在自体確認できない場合
				// 仮の画像を作成する。
				im2 = new BufferedImage(200,200,BufferedImage.TYPE_INT_RGB);
			}
			// ステッカーのBufferdImageアレイリストに、読み込んだ画像データを追加。
			imste.add(im2);
		}

		// 展開図画像への描画処理を行う。
		im = newTex(im,imste,stex,stey,sterot,stew,steh,stegen);

		// イメージの保存処理
		FileOutputStream out = null;
		try
		{
			//リフレッシュあり
			out = new FileOutputStream("C:\\Users\\doumae.kazuki\\Documents\\JV22\\Workspace\\bisyamonten\\WebContent\\obj\\male02\\UV03.jpg");

			//本番環境
			//out = new FileOutputStream("C:\\xampp\\tomcat\\webapps\\bisyamonten\\obj\\male02\\UV03.jpg");

			//権限あり
			//out = new FileOutputStream("C:\\Users\\doumae.kazuki\\Documents\\JV22\\workspace\\.metadata\\.plugins\\org.eclipse.wst.server.core\\tmp0\\wtpwebapps\\bisyamonten\\obj\\male02");
			ImageIO.write(im,"png",out);
			out.flush();

		}
		catch(IOException e)
		{
			// 例外の種類を「ファイルの書き込み失敗」に設定。
			err = 2;
			e.getStackTrace();
			System.out.println(e.getMessage());
		}
		finally
		{
			try
			{
				out.close();
			}
			catch(IOException e1)
			{
				// 例外の種類を「ファイルアウトプットストリームのクローズ失敗」に設定。
				err = 3;
				e1.getStackTrace();
			}
		}

		// データベースからt_workのF_WorkIDの末尾のidを取得し、リクエストにその番号+1の番号を追加する。
		// Cookieに現在格納されているユーザーIDからユーザーナンバーを取得し、該当のユーザーナンバーのユーザーに作品情報を追加する。
		try
		{
			// データベースから作品IDの末尾の数字を取得する。
			dbres = dao.selectInTable("t_work", "F_WorkID", "", "");
			if(dbres != null && dbres.size() >= 1)
			{
				workID = Integer.parseInt( (String)dbres.get(dbres.size() - 1) );
				workID++;
			}

			// 発生した例外の種類をリクエストに追加する。
			request.setAttribute("err",err);
			// リクエストに各ステッカーのx座標情報を追加する。
			request.setAttribute("stex", stex);
			// リクエストに各ステッカーのy座標情報を追加する。
			request.setAttribute("stey", stey);
			// リクエストに各ステッカーのサイズ情報(購入処理で用いるサイズ別カウント用)を追加する。
			request.setAttribute("stesize", stesize);
			// リクエストに各ステッカーのジャンル情報を追加する。
			request.setAttribute("stegen", stegen);
			// リクエストに各ステッカーの回転角度情報を追加する。
			request.setAttribute("sterot", sterot);
			// リクエストに次に挿入予定の作品IDを追加する。
			request.setAttribute("workID", workID);

			// ブラウザバッグ処理の情報をリクエストに追加する。
			// request.setAttribute("broBack", broBack);
			// 画面遷移先jspファイル名を設定。
			reqdisp = "design.jsp";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			// 遷移先jspページを"error.jsp"に設定。
			reqdisp = "error.jsp";
		}

		// リクエストに車体の色情報を追加する。
		request.setAttribute("syatai_color", syatai_color);

		// リクエストディスパッチオブジェクトの作成
		req = request.getRequestDispatcher(reqdisp);
		// 転送
		req.forward(request,response);

	}
	// 別のクラスで描画処理を行う。
	// 引数のsterotは、各ステッカーの回転角度を管理する。
	// 引数のstewはステッカーの幅を、引数のstehはステッカーの高さをそれぞれ表す。
	// 引数のstegは使用ステッカーのジャンルを表す。
	public BufferedImage newTex(BufferedImage im,ArrayList<BufferedImage> im2,ArrayList<Integer> stex,ArrayList<Integer> stey,ArrayList<Integer> sterot,ArrayList<Integer> stew,ArrayList<Integer> steh,ArrayList<Integer> steg)
	{
		// Javaのステッカーの展開図への貼り付けで使用するステッカーの画像オブジェクトを一時的に格納する変数。
		BufferedImage taisyou_ste = null;

		Graphics2D g = im.createGraphics();

		// ステッカー画像回転用の描画領域オブジェクトを作成。
		BufferedImage rotspace = null;

		// 展開図画像上にステッカー画像を貼り付け。
		// 展開図上に配置されているステッカーの数分ステッカーの描画を行う。
		for(i=0;i<stex.size();i++)
		{
			System.out.print(i);

			// 使用ステッカーのジャンルに関する情報がアレイリストのi番目の要素に存在した場合、該当するステッカー画像を読み込む。
			if(steg.get(i) != null)
			{
				taisyou_ste = im2.get( steg.get(i) - 1 );
			}
			// 使用ステッカーのジャンルに関する情報がアレイリストのi番目の要素に存在しなかった場合、マリオのステッカーを使用して描画する。
			else
			{
				taisyou_ste = im2.get(1);
			}

			// ステッカーの回転角度アレイリストに回転角度に関する情報が存在すれば、実際に画像を回転させる。
			if(sterot.get(i) != null)
			{
				// ステッカー画像のサイズに合わせて回転描画用オブジェクトのインスタンスを生成する。
				// 回転描画用オブジェクトは元のステッカーの幅・高さに応じて自身の幅・高さを変更する。
				rotspace = new BufferedImage(stew.get(i),steh.get(i),BufferedImage.TYPE_INT_ARGB);
				Graphics2D g2 = rotspace.createGraphics();

				// ステッカーの回転前の準備処理。
				AffineTransform af = new AffineTransform();
				double[] flatmatrix = new double[6];
				af.getMatrix(flatmatrix);

				af.setToRotation((90 * sterot.get(i)) * Math.PI/180,(stew.get(i) / 2),(steh.get(i) / 2) );
				af.getMatrix(flatmatrix);

				g2.setTransform(af);

				g2.drawImage(taisyou_ste,0,0,stew.get(i),steh.get(i),null);
				// ステッカーを展開図上に描画。
				g.drawImage(rotspace,stex.get(i),stey.get(i),null);
			}
			// 現在処理中のステッカーの回転角度の取得に失敗した場合は、ステッカーを回転させずに展開図に描画する。
			else
			{
				g.drawImage(taisyou_ste,stex.get(i),stey.get(i),stew.get(i),steh.get(i),null);
			}
		}
		// 展開図上へのステッカー描画処理を終了する。
		g.dispose();
		return im;
	}

}
