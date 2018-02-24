package jp.ac.hal.login;

//DAOクラスへSQLクラスのインポート
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsestickerDAO {
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
	public UsestickerDAO()
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
	//usestickerメソッド
	public ArrayList<UsestickerBean>usesticker(String wd)
	{
		int intRet = 0;
		// 使用ステッカー情報を格納するUsestickerBeanArrayList
		ArrayList<UsestickerBean> usestickerBeans = new ArrayList<UsestickerBean>();

		// コネクションを作成
		intRet = this.getConnection();

		// コネクション処理正常終了時
		if(intRet == 0)
		{
			//使用ステッカー一覧情報取得
			//SQLの作成(SQL文を作成する時点でselect対象のカラム名とテーブル名を埋め込み。)
			String sql = "select * from T_Usesticker join T_Work on T_Usesticker.F_WorkID = T_Work.F_WorkID join T_Stickerpricelist on T_Work.F_StickerpricelistID = T_Stickerpricelist.F_StickerpricelistID join T_Sticker on T_Usesticker.F_StickerID = T_Sticker.F_StickerID where T_Usesticker.F_WorkID = ?";

			try
			{
				// ステートメントの作成
				this.st = this.conn.prepareStatement(sql);
				st.setInt( 1,Integer.parseInt(wd) );

				// SQLの実行
				this.rs = this.st.executeQuery();

				//galleryBeansに格納
				while(rs.next())
				{
					UsestickerBean ub = new UsestickerBean();


					//データベースから使用ステッカー情報を取得
					//T_Usestickerテーブル情報
					//使用ステッカーID
					ub.setUsestickerid(rs.getInt("F_UsestickerID"));
					//ステッカーID
					ub.setStickerid(rs.getInt("F_StickerID"));
					//作品ID
					ub.setWorkid(rs.getInt("F_WorkID"));
					//SS枚数
					ub.setNumberofsheetsSS(rs.getInt("F_NumberofsheetsSS"));
					//S枚数
					ub.setNumberofsheetsS(rs.getInt("F_NumberofsheetsS"));
					//M枚数
					ub.setNumberofsheetsM(rs.getInt("F_NumberofsheetsM"));
					//L枚数
					ub.setNumberofsheetsL(rs.getInt("F_NumberofsheetsL"));
					//LL枚数
					ub.setNumberofsheetsLL(rs.getInt("F_NumberofsheetsLL"));
					//全面
					ub.setNumberofsheetsALL(rs.getInt("F_NumberofsheetsALL"));

					//T_Stickerpricelistテーブル情報
					//ステッカー価格表ID
					ub.setStickerpricelistID(rs.getInt("F_StickerpricelistID"));
					//ステッカー種類名
					ub.setStickertypename(rs.getString("F_Stickertypename"));
					//SS
					ub.setPriceSS(rs.getInt("F_PriceSS"));
					//S
					ub.setPriceS(rs.getInt("F_PriceS"));
					//M
					ub.setPriceM(rs.getInt("F_PriceM"));
					//L
					ub.setPriceL(rs.getInt("F_PriceL"));
					//LL
					ub.setPriceLL(rs.getInt("F_PriceLL"));
					//全面
					ub.setPriceALL(rs.getInt("F_PriceALL"));

					//T_Stickerテーブル情報
					//ステッカーID
					ub.setStickerID(rs.getInt("F_StickerID"));
					//ステッカー名
					ub.setStickerName(rs.getString("F_StickerName"));

					usestickerBeans.add(ub);
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

		return usestickerBeans;
	}
}
