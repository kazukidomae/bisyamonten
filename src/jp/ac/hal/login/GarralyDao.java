package jp.ac.hal.login;

//DAOクラスへSQLクラスのインポート
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class GarralyDao {

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
	public GarralyDao(String strUrl)
	{
		// 初期値を設定
		this.url = strUrl;
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

	// insertDataメソッド(データベースにデータを新規挿入するメソッド。)
	// 戻り値:0(正常),0以外(異常)
	// 引数:
	// String table_name:　　データを入れる先のテーブル名を格納する。
	// String col_name:　　データを入れる対象のカラム名をカンマで区切って格納する。
	// ArrayList<Object> data_list:　　テーブルに入れるデータを左側のカラムから順にアレイリストに格納する。そのアレイリストを引数に入れてください。
	// なお、アレイリストには次の順でデータを格納してください。
	// 偶数の要素番号‥‥‥　　データベースに入れるデータ型を整数で指定します。1:文字列(String)型　2:数値(Int)型
	// 奇数の要素番号‥‥　データベースに実際に入れたいデータをそのまま格納します。1つ前の要素番号のデータ型に沿ったデータを格納してください。
	public int insertData(String table_name,String col_name,ArrayList<Object> data_list)
	{
		int intRet = 0;
		// コネクションの作成
		intRet = this.getConnection();

		// コネクション処理が正常に終了した場合
		if(intRet == 0)
		{
			//作品ID取得処理

			// 実行SQLを作成
			String sql = "INSERT INTO " + table_name + "(" + col_name + ") VALUES(";

			// アレイリストに格納されたデータの数分、実行SQLのVALUESのカッコ内に?を追加。
			for(int i=0;i<data_list.size();i=i+2)
			{
				sql = sql + "?";
				if( i != (data_list.size() - 2) )
				{
					sql = sql + ",";
				}
			}

			sql = sql + ");";

			try
			{
				// 作成したSQL文を基にステートメント作成
				this.st = this.conn.prepareStatement(sql);

				// ステートメントにアレイリストの要素数分、指定された型に添ってデータを格納する。
				for(int i=0;i<data_list.size();i=i+2)
				{
					// 指定されたデータ型が文字列型の場合
					if((Integer)data_list.get(i) == 1)
					{
						// 文字列型のデータをSQL文に埋め込み。
						this.st.setString( (i / 2 + 1), (String)data_list.get(i + 1) );
					}
					// 指定されたデータ型が数値型の場合
					else if((Integer)data_list.get(i) == 2)
					{
						// 数値型のデータをSQL文に埋め込み。
						this.st.setInt( (i / 2 + 1), (Integer)data_list.get(i + 1) );
					}
				}

				// SQLの実行
				this.st.executeUpdate();
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				intRet = 4;
			}
			finally
			{
				// クローズ処理
				this.closeDB();
			}

		} //コネクション

		return intRet;

	} //addUserDataメソッド終わり

	// delUserDataメソッド
	// 戻り値:0(正常),0以外(異常)
	// 引数:String strUser(削除ユーザID)
	public int delUserData(String strUser)
	{
		// 戻り値初期値
		int intRet = 0;

		// コネクション作成
		intRet = this.getConnection();

		// コネクション処理正常終了時
		if(intRet == 0)
		{
			// 削除処理
			// 削除SQL作成
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

	// selectInTableメソッド
	// 戻り値:ArrayList<Object>　(登録ユーザー一覧)
	// 引数:String table_name(セレクト対象となるテーブル名),
	// String col_name(テーブルからセレクトさせたいカラム名),
	// String sql_option(SELECTの通常SQL文の後ろに追加するオプションのSQL文。ここにはwhere文などを格納します。オプションのSQL文を追加しない場合は空文字列を入れてください。)
	// String SetStr(sql_optionにwhere句を指定した場合に、sql_optionの"?"の中に入れる値をここに格納する。)
	public ArrayList<Object> selectInTable(String table_name,String col_name,String sql_option,String SetStr)
	{
		int intRet = 0;
		// ユーザ情報を格納するUserData ArrayList
		ArrayList<Object> DB_Data = new ArrayList<Object>();

		// コネクションを作成
		intRet = this.getConnection();

		// コネクション処理正常終了時
		if(intRet == 0)
		{
			// ユーザ情報一覧取得
			// SQLの作成(SQL文を作成する時点でselect対象のカラム名とテーブル名を埋め込み。)
			String sql = "select " + col_name + " from " + table_name;

			if( !(sql_option.equals("")) )
			{
				// オプションのSQL文に何か文字列が入力されている場合
				sql = sql + " " + sql_option;
			}

			sql = sql + ";";

			try
			{
				// ステートメントの作成
				this.st = this.conn.prepareStatement(sql);

				// SetStrが空文字列以外の場合は、ステートメント内の"?"に値を追加する。
				if( !(SetStr.equals("")) )
				{
					// ステートメントにSetStrの内容を埋め込む。
					this.st.setString(1, SetStr);
				}

				// SQLの実行
				this.rs = this.st.executeQuery();

				// リザルトセットから値を取り出しUserDataに格納
				while(rs.next())
				{
					// select文により指定したカラムから取り出したデータをArrayList変数に格納。
					DB_Data.add(rs.getString(col_name));
				}
			}
			catch(SQLException e)
			{
				e.printStackTrace();
				DB_Data = null;
			}
			finally
			{
				// クローズ処理
				intRet = this.closeDB();
				if(intRet != 0)
				{
					// 異常時は、UserDataをnullに設定する。
					DB_Data = null;
				}
			}

		}
		else
		{
			// 異常時は、UserDataをnullに設定する。
			DB_Data = null;
		}


		return DB_Data;
	}

	// updateInTableメソッド。
	// 戻り値:true(正常),false(異常)
	// 引数:
	// ◎String table_name:データの格納先となるテーブル名を入れてください。
	// ◎ArrayList<String> col_name:データの格納対象となるカラム名をテーブルの左側のカラムから順に、全てアレイリストに格納してセットしてください。
	// ◎ArrayList<Object> data_list:　　更新対象となるデータを左側のカラムから順にアレイリストに格納する。そのアレイリストを引数に入れてください。
	// なお、アレイリストには次の順でデータを格納してください。
	// 偶数の要素番号‥‥‥　　データベースに入れるデータ型を整数で指定します。1:文字列(String)型　2:数値(Integer)型
	// 奇数の要素番号‥‥　データベースに実際に入れたいデータをそのまま格納します。1つ前の要素番号のデータ型に沿ったデータを格納してください。
	// ◎String where:UPDATEで書き換え対象となるデータの検索条件を指定してください(whereも含む)。文末のセミコロンは入れなくて大丈夫です。省略して全てのデータを書き換える場合は空白。
	public boolean updateInTable(String table_name,ArrayList<String> col_name,ArrayList<Object> data_list,String where)
	{
		// ここから　変数宣言・初期化スペース
		// メソッドの処理結果を格納する変数。true:更新成功　false:更新失敗
		boolean res = false;
		// DBへのコネクション処理の結果・DBのクローズ処理の結果を格納する変数。
		int intRet = 0;
		// ここまで　変数宣言・初期化スペース

		// まずは、データベースへの接続処理を行う。
		intRet = this.getConnection();

		// データベースへのコネクション処理が正常に終了した場合
		if(intRet == 0)
		{
			// 例外処理。処理途中で例外が発生した場合は、メソッドの処理結果のフラグをfalseとし、エラー内容の出力も行う。
			try
			{
				// 実行SQLを生成する。
				String sql = "UPDATE " + table_name + " SET ";

				// アレイリストに格納されたカラムの数に応じて、更新後データのアレイリストに格納された値に更新するSQL文を生成する。
				for(int i=0;i<col_name.size();i++)
				{
					sql = sql + col_name.get(i) + " = ?";
					if(i < ( col_name.size() - 1 ) )
					{
						sql = sql + ",";
					}
				}
				sql = sql + " ";

				// データ更新文の後ろに条件指定のwhere句を追加。
				sql = sql + where + ";";

				// 作成したSQL文を基にステートメントを作成する。
				this.st = this.conn.prepareStatement(sql);

				// SQL文のうち、?の部分にデータ型に応じてステートメントを追加。
				for(int i=0;i<data_list.size();i=i+2)
				{
					// 指定されたデータ型が文字列型の場合
					if((Integer)data_list.get(i) == 1)
					{
						// 文字列型のデータをSQL文に埋め込み。
						this.st.setString( (i / 2 + 1), (String)data_list.get(i + 1) );
					}
					// 指定されたデータ型が数値型の場合
					else if((Integer)data_list.get(i) == 2)
					{
						// 数値型のデータをSQL文に埋め込み。
						this.st.setInt( (i / 2 + 1), (Integer)data_list.get(i + 1) );
					}
				}

				// 生成したSQL文を実行する。
				this.st.executeUpdate();

				// メソッドの処理結果フラグをtrueに設定する。
				res = true;
			}
			catch(Exception e)
			{
				// メソッドの処理結果をfalseとする。
				res = false;
				// エラー内容を出力させる。
				e.printStackTrace();
			}
			finally
			{
				// データベースのクローズ処理を行う。
				intRet = this.closeDB();
				// この時点で正常にデータベースをクローズできなかった場合は、メソッドの処理結果フラグをfalseとする。
				if(intRet != 0)
				{
					// メソッドの処理結果をfalseとする。
					res = false;
				}
			}
		}
		// データベースへのコネクション処理で例外が発生した(intRetが0以外)場合は、処理結果のフラグをfalseとする。

		return res;
	}
}
