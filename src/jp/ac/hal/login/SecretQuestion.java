package jp.ac.hal.login;

import java.util.ArrayList;

public class SecretQuestion {

	// ユーザーが選んだ秘密の質問とその答えをプロパティとして持つクラス。
	// ここからプロパティ一覧スペース
	// ユーザーが選んだ秘密の質問1の種類を格納する配列メンバ変数。
	// 要素番号はそのまま秘密の質問の何番目かに相当します(0番は不使用、1番～質問番号の1、2、3と連動)
	private int[] usSQ = null;
	// ユーザーが選んだ秘密の質問の答えを格納するメンバ変数。
	private String[] usSQAnswer = null;
	// ここまでプロパティ一覧スペース

	// SecretQuestionクラスのコンストラクタ。
	// 引数には秘密の質問の質問数を入れてください(int sec_size)。
	public SecretQuestion(int sec_size)
	{
		// 秘密の質問関連メンバ変数に初期値を代入。
		// 秘密の質問の種類に質問数分の配列を生成。
		this.usSQ = new int[(sec_size + 1)];
		// 秘密の質問の答えに質問数分の配列を生成。
		this.usSQAnswer = new String[(sec_size + 1)];

		// インスタンス生成時に秘密の質問関連メンバ変数を全て初期化。質問と回答は必ず1つずつセットの前提。
		for(int i=0;i<(sec_size + 1);i++)
		{
			this.usSQ[i] = 0;
			this.usSQAnswer[i] = "";
		}
	}

	// ユーザーIDから該当ユーザーが登録した秘密の質問の内容とその答えを一斉に読み出すメソッド。
	// 引数‥‥‥
	// 1:LoginDao DBAccess‥‥‥データベース用アクセス用DAOオブジェクト。
	// 2:String UserID‥‥‥秘密の質問検索で用いるユーザーID。
	public void readSecretQuestion(LoginDao DBAccess,String UserID)
	{
		// ここから　ローカル変数の宣言・初期化スペース

		// データベースからの取得結果をまとめて格納するアレイリスト変数。
		ArrayList<Object> dbres = null;
		// ログイン中のユーザーのユーザーナンバーを格納する変数。
		int userNo = 0;
		// データベースでの処理中にエラーが発生した場合にメンバ変数を初期化させるためのエラーフラグ。エラーが発生するとtrueとなる。
		boolean errflg = false;

		// ここまで　ローカル変数の宣言・初期化スペース

		// まずはログイン中のユーザーIDから、データベースを通してユーザーナンバーを取得する。
		dbres = DBAccess.selectInTable("t_user", "F_UserNo", "where F_UserID = ?", UserID);
		// データベースの接続が成功した場合はユーザーナンバーを取得。
		if(dbres != null)
		{
			// 取得した結果リストのうち最初の要素に格納されているデータを取り出す。
			userNo = Integer.parseInt((String)dbres.get(0));

			// 続いて、取得したユーザーナンバーを使って、ユーザーが選んだ質問に対する回答を取得する。
			for(int i=1;i<this.usSQ.length;i++)
			{
				// i番目の質問。
				dbres = DBAccess.selectInTable("t_answer", "F_Answer", "where F_UserNo = " + userNo + " and F_Answerclassification = " + i + "", "");
				// データベースからの取得が失敗(null)か、返り値が空の場合は、データの取り出しをせずエラーフラグをtrueにする。
				if(dbres != null && dbres.size() >= 1)
				{
					// データベースを正しく取得できた場合は、リストのうち最初の要素に格納されているデータを取り出す。
					this.usSQAnswer[i] = (String)dbres.get(0);
				}
				else
				{
					// データベースを正しく取得できなかった場合は、エラーフラグをtrueとする。
					errflg = true;
					// ループを強制的に脱出。
					break;
				}

				// 回答を取得したら、同時にユーザーが選んだ秘密の質問IDも取得する。
				dbres = DBAccess.selectInTable("t_answer", "F_SecretquestionID", "where F_UserNo = " + userNo + " and F_Answerclassification = " + i + "", "");
				// データベースからの取得が失敗(null)か、返り値が空の場合は、データの取り出しをせずエラーフラグをtrueにする。
				if(dbres != null && dbres.size() >= 1)
				{
					// データベースを正しく取得できた場合は、リストのうち最初の要素に格納されているデータを取り出す。
					this.usSQ[i] = Integer.parseInt((String)dbres.get(0));
				}
				else
				{
					// データベースを正しく取得できなかった場合は、エラーフラグをtrueとする。
					errflg = true;
					// ループを強制的に脱出。
					break;
				}
			}

		}
		// データベースから正しくデータを取得できなかった場合は、初期値を代入。
		else
		{
			errflg = true;
		}

		// この時点でエラーフラグがtrueとなった場合は、メンバ変数の値を全て初期化する。
		if(errflg)
		{
			// メンバ変数の全初期化処理を実行。
			this.dberror();
		}
	}

	// データベースとの接続中に例外が発生し、正しくデータが取得できなかった場合に、秘密の質問関連メンバ変数を全て初期化し、画面表示に反映させるためのメソッド。
	private void dberror()
	{
		// 秘密の質問関連メンバ変数を全て初期化。質問と回答は必ず1つずつセットの前提。
		for(int i=0;i<this.usSQ.length;i++)
		{
			this.usSQ[i] = 0;
			this.usSQAnswer[i] = "";
		}
	}

	// ユーザーが選んだ秘密の質問の質問番号とユーザーの秘密の質問に対する回答をデータベースに上書きで更新するメソッド。
	// 引数:
	// LoginDao dbAccess:データベースアクセスオブジェクトを格納してください。
	// String userID:Cookieに保存されているユーザーID情報を格納してください。
	// 戻り値:
	// boolean res:データベースの更新処理に成功した場合はtrue、失敗した場合はfalseが返されます。
	public boolean writeSeqretQuestion(LoginDao dbAccess,String userID)
	{
		// ここから　変数宣言・初期化スペース
		// データベースの更新処理の処理結果を返す変数。true:成功　false:失敗
		boolean res = false;
		// データベースの処理結果を格納する変数。true:成功　false:失敗
		boolean dbres = false;

		// データベースアクセス用のオブジェクト。
		LoginDao dba = dbAccess;

		// データベースの更新処理で対象となるカラム名を格納するアレイリスト。
		ArrayList<String> dbCol = new ArrayList<String>();
		// データベースの更新処理で扱うデータをまとめて格納するアレイリスト。
		ArrayList<Object> dbData = new ArrayList<Object>();

		// データベースの取得処理で取得結果をまとめて格納するアレイリスト。
		ArrayList<Object> dbList = null;
		// ユーザーIDから取得したユーザーナンバーを格納する変数。
		int userNo = 0;

		// ここまで　変数宣言・初期化スペース

		// 例外処理。処理途中で例外が発生した場合は処理を中断し、更新処理結果をfalseとする。
		try
		{

			// データベース上で秘密の質問のユーザーが選んだ質問IDとその回答を更新する処理で、更新対象となるカラム名をアレイリストに予めセット。
			// 秘密の質問ID
			dbCol.add("F_SecretquestionID");
			// 秘密の質問の回答
			dbCol.add("F_Answer");

			// まずは、引数のユーザーIDを使って該当ユーザーのユーザーナンバーを取得。
			dbList = dba.selectInTable("t_user", "F_UserNo", "where F_UserID = ?", userID);
			// データベースからの取得結果がnull、または0件以外の場合は、データベースの取得結果リストの最初の要素をユーザーナンバーとして用いる。

			if(dbList != null && dbList.size() >= 1)
			{
				// ユーザーナンバーを取得。
				userNo = Integer.parseInt((String)dbList.get(0));

				// 続いて、取得したユーザーナンバーから該当ユーザーの秘密の質問登録情報を更新する。

				// データベースに格納する実際のデータをアレイリストに格納。
				// 1番目の秘密の質問の質問IDのそれに対する回答。
				dbData.add(2);
				dbData.add(this.usSQ[1]);
				dbData.add(1);
				dbData.add(this.usSQAnswer[1]);

				// 1つ目の秘密の質問。
				dbres = dba.updateInTable("t_answer", dbCol, dbData, "where F_UserNo = " + String.valueOf(userNo) + " and F_Answerclassification = 1");
				// 更新処理が失敗した場合は、2つ目の秘密の質問更新処理は行わない。
				if(dbres != false)
				{
					// データベース処理成功フラグを一旦falseに戻す。
					dbres = false;
					// アレイリストのDB格納データを全てクリアする。
					dbData.clear();

					// 2番目の秘密の質問IDとそれに対する回答をアレイリストに追加。
					dbData.add(2);
					dbData.add(this.usSQ[2]);
					dbData.add(1);
					dbData.add(this.usSQAnswer[2]);

					// 2番目の秘密の質問。
					dbres = dba.updateInTable("t_answer", dbCol, dbData, "where F_UserNo = " + String.valueOf(userNo) + " and F_Answerclassification = 2");
					// 2番目の秘密の質問の更新処理が成功したら、メソッドの処理成功フラグをtrueとする。
					if(dbres == true)
					{
						// メソッドの処理成功。
						res = true;
					}
				}
			}

		}
		catch(Exception e)
		{
			// メソッドの処理結果フラグをfalseとする。
			res = false;
			// 発生した例外を出力させる。
			e.printStackTrace();
		}

		return res;
	}

	/**
	 * usSQを取得します。
	 * @return usSQ
	 */
	public int[] getUsSQ() {
	    return usSQ;
	}

	/**
	 * usSQを設定します。
	 * @param usSQ usSQ
	 */
	public void setUsSQ(int[] usSQ) {
	    this.usSQ = usSQ;
	}

	/**
	 * usSQAnswerを取得します。
	 * @return usSQAnswer
	 */
	public String[] getUsSQAnswer() {
	    return usSQAnswer;
	}

	/**
	 * usSQAnswerを設定します。
	 * @param usSQAnswer usSQAnswer
	 */
	public void setUsSQAnswer(String[] usSQAnswer) {
	    this.usSQAnswer = usSQAnswer;
	}

}
