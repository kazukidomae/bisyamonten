package jp.ac.hal.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BuyDAO {
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
	public BuyDAO(){
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

	//Streetaddressオブジェクト
	//登録済みの住所を取得
	public UserBean Streetaddress(String UserID){

		int intRet = 0;
		// コネクションを作成
		intRet = this.getConnection();

		//購入情報を格納する変数
		UserBean address = null;

		//SQL実行
		String sql = "select F_Surname,F_Name,F_Postalcode,F_Prefectures,F_City,F_Streetaddress,F_Phonenumber,F_Credit from T_User where T_User.F_UserID=?";
		try{
			// ステートメントの作成
			this.st = this.conn.prepareStatement(sql);
			st.setString(1, UserID);
			// SQLの実行
			this.rs = this.st.executeQuery();
			while(rs.next()){
				UserBean ub = new UserBean();

				//データベースから購入に必要なユーザー情報を取得
				ub.setSurname(rs.getString("F_Surname"));
				ub.setName(rs.getString("F_Name"));
				ub.setPostalcode(rs.getString("F_Postalcode"));
				ub.setPrefectures(rs.getString("F_Prefectures"));
				ub.setCity(rs.getString("F_City"));
				ub.setStreetaddress(rs.getString("F_Streetaddress"));
				ub.setPhonenumber(rs.getString("F_Phonenumber"));
				ub.setCredit(rs.getString("F_Credit"));

				address = ub;
			}
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			// クローズ処理
			intRet = this.closeDB();
		}
		return address;
	}

	//purchaseメソッド
	//購入情報を登録
	/* public void purchase(){
		int intRet = 0;
		// コネクションを作成

		//sql実行
		String sql = "INTERT INTO";
		intRet = this.getConnection();
		try
		{
			// ステートメントの作成
			this.st = this.conn.prepareStatement(sql);
			// SQLの実行
			this.rs = this.st.executeUpdate();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			// クローズ処理
			intRet = this.closeDB();
		}
	} */
}


