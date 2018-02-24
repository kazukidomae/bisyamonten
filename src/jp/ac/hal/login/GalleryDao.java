package jp.ac.hal.login;

//DAOクラスへSQLクラスのインポート
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GalleryDao {

	// 属性(プロパティ)
	// 変数とデータベースオブジェクトの定義
	// 接続情報
	String url;
	// コネクション
	Connection conn;
	// ステートメント
	PreparedStatement st;
	// リザルトセット
	ResultSet rs;

	// コンストラクタ
	// 属性値の初期化
	// 接続情報をコンストラクタで渡す
	public GalleryDao()
	{
		// 初期値を設定
		this.url = "jdbc:mysql:///hew02?user=root&password=" + "&useUnicode=yes&characterEncoding=utf8";
		// コネクションオブジェクト初期化
		this.conn = null;
		// ステートメントの初期化
		this.st = null;
		// リザルトセットオブジェクトの初期化
		this.rs = null;
	}


	// getConnectionメソッド
	// コネクション(DB接続を行う)
	// 戻り値:0(正常)　1(異常)
	// 引数:なし
	private int getConnection()
	{
		int intRet = 0;
		try
		{
			// ドライバ読み込み
			Class.forName("com.mysql.jdbc.Driver");
			// コネクション作成
			this.conn = DriverManager.getConnection(this.url);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			intRet = 2;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			intRet = 1;
		}
		finally
		{
			return intRet;
		}
	}

	// closeDBメソッド
	// 戻り値:0(正常),3(異常・クローズ処理失敗)
	// 引数:なし
	private int closeDB()
	{
		int intRet = 0;
		try
		{
			// リザルトセットのクローズ
			if(this.rs != null)
			{
				this.rs.close();
			}
			// ステートメントのクローズ
			if(this.st != null)
			{
				this.st.close();
			}
			// コネクションのクローズ
			if(this.conn != null)
			{
				this.conn.close();
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			intRet = 3;
		}
		finally
		{
			return intRet;
		}
	}

	// delGalleryDataメソッド
	// 戻り値:0(正常),0以外(異常)
	// 引数:String strUser(削除ユーザID)
	public int delGalleryData(String strUser)
	{
		// 戻り値初期値
		int intRet = 0;

		// コネクション作成
		intRet = this.getConnection();

		// コネクション処理正常終了時
		if(intRet == 0)
		{
			//削除処理
			//削除SQL作成
			String sql = "delete from tbl_user where id=?;";
			try
			{
				// ステートメントの作成
				this.st = this.conn.prepareStatement(sql);

				// ステートメントに削除ユーザ埋め込み
				this.st.setString(1, strUser);

				// SQLの実行
				this.st.executeUpdate();
			}
			catch(SQLException e)
			{
				intRet = 5;
				// Eclipseのコンソールに例外内容を出力
				e.getStackTrace();
			}
			finally
			{
				// クローズ処理
				intRet = this.closeDB();
			}

		}
		// 戻り値設定
		return intRet;
	}

	//publicgalleryメソッド
	public ArrayList<GalleryBean>publicgallery()
	{
		int intRet = 0;
		// 登録作品情報を格納するGalleryData ArrayList
		ArrayList<GalleryBean> galleryBeans = new ArrayList<GalleryBean>();

		// コネクションを作成
		intRet = this.getConnection();

		// コネクション処理正常終了時
		if(intRet == 0)
		{
			//登録作品情報一覧取得
			//SQLの作成(SQL文を作成する時点でselect対象のカラム名とテーブル名を埋め込み。)
			String sql = "select * from T_Work join T_User on T_Work.F_UserNo = T_User.F_UserNo where F_Release = 1";

			try
			{
				// ステートメントの作成
				this.st = this.conn.prepareStatement(sql);

				// SQLの実行
				this.rs = this.st.executeQuery();

				//galleryBeansに格納
				while(rs.next())
				{
					GalleryBean gb = new GalleryBean();


					//データベースから作品情報を取得
					//作品ID
					gb.setWorkid(rs.getInt("F_WorkID"));
					//ユーザーNo
					gb.setUserno(rs.getInt("F_UserNo"));
					//車型ID
					gb.setVehicletypeid(rs.getInt("F_VehicletypeID"));
					//ステッカー価格表ID
					gb.setStickerpricelistid(rs.getInt("F_StickerpricelistID"));
					//作成デザインパス
					gb.setWorkdesing(rs.getString("F_Workdesing"));
					//サムネイル画像パス
					gb.setThumbnailpass(rs.getString("F_Thumbnailpass"));
					//公開フラグ
					gb.setRelease(rs.getInt("F_Release"));
					//いいね数
					gb.setNice(rs.getInt("F_Nice"));
					//お気に入り数
					gb.setFavorite(rs.getInt("F_Favorite"));
					//作成日時
					gb.setWorkdata(rs.getString("F_Workdata"));

					//T_Userテーブルからの情報
					//ユーザーID
					gb.setUserid(rs.getString("F_UserID"));
					//ユーザーネーム
					gb.setUsername(rs.getString("F_UserName"));

					galleryBeans.add(gb);
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			finally
			{
				// クローズ処理
				intRet = this.closeDB();
			}
		}

		return galleryBeans;
	}


	//mygalleryメソッド
	public ArrayList<GalleryBean> mygallery(String UserID)
	{
		int intRet = 0;
		// 登録作品情報を格納するGalleryData ArrayList
		ArrayList<GalleryBean> galleryBeans = new ArrayList<GalleryBean>();

		// コネクションを作成
		intRet = this.getConnection();

		// コネクション処理正常終了時
		if(intRet == 0)
		{
			//登録作品情報一覧取得
			//SQLの作成(SQL文を作成する時点でselect対象のカラム名とテーブル名を埋め込み。)
			String sql = "select * from T_Work join T_User on T_Work.F_UserNo = T_User.F_UserNo where F_UserID = ?";

			try
			{
				// ステートメントの作成
				this.st = this.conn.prepareStatement(sql);
				st.setString(1, UserID);

				// SQLの実行
				this.rs = this.st.executeQuery();

				//galleryBeansに格納
				while(rs.next())
				{
					GalleryBean gb = new GalleryBean();


					//データベースから作品情報を取得
					//作品ID
					gb.setWorkid(rs.getInt("F_WorkID"));
					//ユーザーNo
					gb.setUserno(rs.getInt("F_UserNo"));
					//車型ID
					gb.setVehicletypeid(rs.getInt("F_VehicletypeID"));
					//ステッカー価格表ID
					gb.setStickerpricelistid(rs.getInt("F_StickerpricelistID"));
					//作成デザインパス
					gb.setWorkdesing(rs.getString("F_Workdesing"));
					//サムネイル画像パス
					gb.setThumbnailpass(rs.getString("F_Thumbnailpass"));
					//公開フラグ
					gb.setRelease(rs.getInt("F_Release"));
					//いいね数
					gb.setNice(rs.getInt("F_Nice"));
					//お気に入り数
					gb.setFavorite(rs.getInt("F_Favorite"));
					//作成日時
					gb.setWorkdata(rs.getString("F_Workdata"));

					//T_Userテーブルからの情報
					//ユーザーID
					gb.setUserid(rs.getString("F_UserID"));
					//ユーザーネーム
					gb.setUsername(rs.getString("F_UserName"));

					galleryBeans.add(gb);
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
			finally
			{
				// クローズ処理
				intRet = this.closeDB();
			}
		}

			return galleryBeans;
	}

	// updateNiceメソッド
	public void updateNice(int workid)
	{
		// DBへのコネクション処理の結果・DBのクローズ処理の結果を格納する変数。
		int intRet = 0;


		System.out.println(workid);

		// まずは、データベースへの接続処理を行う。
		intRet = this.getConnection();
		//実行SQLを生成する。
		String sql = "UPDATE T_Work SET F_Nice=F_Nice+1 where F_WorkID = "+workid;

		// 例外処理。処理途中で例外が発生した場合は、メソッドの処理結果のフラグをfalseとし、エラー内容の出力も行う。
		try
		{
			//作成したSQL文を基にステートメントを作成する。
			this.st = this.conn.prepareStatement(sql);

			//生成したSQL文を実行する。
			this.st.executeUpdate();

		}
		catch(Exception e)
		{
			//エラー内容を出力させる。
			e.printStackTrace();
		}
		finally
		{
			// データベースのクローズ処理を行う。
			intRet = this.closeDB();
		}
	}

	//updateFavoriteメソッド
	public void updateFavorite()
	{
		// DBへのコネクション処理の結果・DBのクローズ処理の結果を格納する変数。
		int intRet = 0;

		// まずは、データベースへの接続処理を行う。
		intRet = this.getConnection();

		//実行SQLを生成する。
		String sql = "UPDATE T_Work SET F_Favorite=F_Favorite+1";

		// 例外処理。処理途中で例外が発生した場合は、メソッドの処理結果のフラグをfalseとし、エラー内容の出力も行う。
		try
		{
			//作成したSQL文を基にステートメントを作成する。
			this.st = this.conn.prepareStatement(sql);

			//生成したSQL文を実行する。
			this.st.executeUpdate();

		}
		catch(Exception e)
		{
			//エラー内容を出力させる。
			e.printStackTrace();
		}
		finally
		{
			// データベースのクローズ処理を行う。
			intRet = this.closeDB();
		}
	}

	//ギャラリー公開処理
	public void Relese(String wd){

		// DBへのコネクション処理の結果・DBのクローズ処理の結果を格納する変数。
		int intRet = 0;

		// まずは、データベースへの接続処理を行う。
		intRet = this.getConnection();

		//実行SQL
		String sql ="UPDATE T_Work SET F_Release = 1 where F_WorkID=?";
		try{
			//作成したSQL文を基にステートメントを作成する。
			this.st = this.conn.prepareStatement(sql);
			st.setInt(1, Integer.parseInt(wd));
			//生成したSQL文を実行する。
			this.st.executeUpdate();
		}
		catch(Exception e)
		{
			//エラー内容を出力させる。
			e.printStackTrace();
		}
		finally
		{
			// データベースのクローズ処理を行う。
			intRet = this.closeDB();
		}
	}


}