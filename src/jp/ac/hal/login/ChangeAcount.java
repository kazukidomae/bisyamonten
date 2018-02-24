package jp.ac.hal.login;

import java.util.ArrayList;

// 個人情報変更関連の処理をまとめたクラス。
public class ChangeAcount {

	// ここから　プロパティ一覧スペース
	// 現在のDB上のユーザーの氏(未入力時は「未入力」)。
	private String surname;
	private String surname_col = "F_Surname";
	// 現在のDB上のユーザーの名(未入力時は「未入力」)。
	private String firstname;
	private String firstname_col = "F_Name";
	// 現在のDB上の郵便番号(未入力時は「未入力」)。
	private String postalcode;
	private String postalcode_col = "F_Postalcode";
	// 現在のDB上の住所の都道府県(未入力時は「未入力」)。
	private String prefectures;
	private String prefectures_col = "F_Prefectures";
	// 現在のDB上の住所の市区町村(未入力時は「未入力」)。
	private String city;
	private String city_col = "F_City";
	// 現在のDB上の住所詳細情報(未入力時は「未入力」)。
	private String streetAddress;
	private String streetAddress_col = "F_Streetaddress";
	// 現在のDB上の電話番号(未入力時は「未入力」)。
	private String phoneNumber;
	private String phoneNumber_col = "F_Phonenumber";
	// 現在のDB上のユーザーID。
	private String userid;
	private String userid_col = "F_UserID";
	// 現在のDB上のユーザーネーム。
	private String username;
	private String username_col = "F_UserName";
	// 現在のDB上のツイッターID。
	private String twiId;
	private String twiId_col = "F_TwitterID";
	// 現在のDB上のメールアドレス。
	private String mail;
	private String mail_col = "F_Mailaddress";

	// 個人情報の入力項目名の一覧配列(7種類)。
	private String acountInfoName[] = {"ユーザーの氏","ユーザーの名","郵便番号","住所の都道府県","住所の市区町村","住所の町域以下","電話番号"};
	// ここまで　プロパティ一覧スペース

	// 個人情報変更関連のクラスのコンストラクタ。
	public ChangeAcount()
	{
		// 個人情報関連のメンバ変数に初期値を代入。
		// ユーザーの氏。
		this.surname = "";
		// ユーザーの名。
		this.firstname = "";
		// 郵便番号
		this.postalcode = "";
		// 都道府県
		this.prefectures = "";
		// 市区町村
		this.city = "";
		// 住所詳細情報
		this.streetAddress = "";
		// 電話番号
		this.phoneNumber = "";
		// 現在のユーザーID。
		this.userid = "";
		// 現在のユーザーネーム。
		this.username = "";
		// 現在のツイッターID。
		this.twiId = "";
		// 現在のDB上のメールアドレス。
		this.mail = "";
	}



	// DBから登録されている現在の個人情報を読み出すメソッド。
	// 引数:
	// LoginDao DBAccess:データベースアクセスオブジェクトを格納してください。
	// String UserID:Cookieに保存されているユーザーID情報を格納してください。
	// int readType:データベースから個人情報を読み出す時の読み出しの種類を数値で指定してください。
	// 1:個人情報変更ページ用　2:ユーザー情報変更ページ用
	public void readAcountInfo(LoginDao DBAccess,String UserID,int readType)
	{
		// ここから　ローカル変数定義・初期化スペース
		// 現在データベースに格納されている個人情報一覧を格納するリスト。
		ArrayList<Object> UserInfo = null;
		// ここまで　ローカル変数定義・初期化スペース

		// データベースアクセス用オブジェクトの作成。
		LoginDao DBA = DBAccess;

		// データベースから個人情報を読み込み、各メンバ変数に格納する。
		// 個人情報変更ページの場合
		if(readType == 1)
		{
			// ユーザーの氏。
			UserInfo = DBA.selectInTable("t_user", "F_Surname", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、ユーザーの氏に空文字を代入する。
			if(UserInfo == null)
			{
				// ユーザーの氏を空白にする。
				this.surname = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素をユーザーの氏データとして取り出す。
				// ユーザーの氏データがまだ何も入力されていない(DBでnullが格納されている)場合は、ユーザーの氏に「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.surname = "未入力";
				}
				else
				{
					// 取得したユーザーの氏データにnull以外の値が格納されている場合、取得したユーザーの氏をメンバ変数に代入する。
					this.surname = (String)UserInfo.get(0);
				}
			}

			// ユーザーの名。
			UserInfo = DBA.selectInTable("t_user", "F_Name", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、ユーザーの名に空文字を代入する。
			if(UserInfo == null)
			{
				// ユーザーの名を空白にする。
				this.firstname = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素をユーザーの名データとして取り出す。
				// ユーザーの名データがまだ何も入力されていない(DBでnullが格納されている)場合は、ユーザーの名に「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.firstname = "未入力";
				}
				else
				{
					// 取得したユーザーの名データにnull以外の値が格納されている場合、取得したユーザーの名をメンバ変数に代入する。
					this.firstname = (String)UserInfo.get(0);
				}
			}

			// 郵便番号。
			UserInfo = DBA.selectInTable("t_user", "F_Postalcode", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、郵便番号に空文字を代入する。
			if(UserInfo == null)
			{
				// 郵便番号を空白にする。
				this.postalcode = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素を郵便番号データとして取り出す。
				// 郵便番号データがまだ何も入力されていない(DBでnullが格納されている)場合は、郵便番号に「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.postalcode = "未入力";
				}
				else
				{
					// 取得した郵便番号データにnull以外の値が格納されている場合、取得した郵便番号をメンバ変数に代入する。
					this.postalcode = (String)UserInfo.get(0);
				}
			}

			// 都道府県。
			UserInfo = DBA.selectInTable("t_user", "F_Prefectures", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、都道府県に空文字を代入する。
			if(UserInfo == null)
			{
				// 都道府県を空白にする。
				this.prefectures = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素を都道府県データとして取り出す。
				// 都道府県データがまだ何も入力されていない(DBでnullが格納されている)場合は、都道府県に「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.prefectures = "未入力";
				}
				else
				{
					// 取得した都道府県データにnull以外の値が格納されている場合、取得した都道府県をメンバ変数に代入する。
					this.prefectures = (String)UserInfo.get(0);
				}
			}

			// 市区町村。
			UserInfo = DBA.selectInTable("t_user", "F_City", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、ユーザーの氏に空文字を代入する。
			if(UserInfo == null)
			{
				// 市区町村を空白にする。
				this.city = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素を市区町村データとして取り出す。
				// 市区町村データがまだ何も入力されていない(DBでnullが格納されている)場合は、市区町村に「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.city = "未入力";
				}
				else
				{
					// 取得した市区町村データにnull以外の値が格納されている場合、取得した市区町村をメンバ変数に代入する。
					this.city = (String)UserInfo.get(0);
				}
			}

			// 住所詳細情報。
			UserInfo = DBA.selectInTable("t_user", "F_Streetaddress", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、ユーザーの氏に空文字を代入する。
			if(UserInfo == null)
			{
				// 住所詳細情報を空白にする。
				this.streetAddress = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素を住所詳細情報データとして取り出す。
				// 住所詳細情報データがまだ何も入力されていない(DBでnullが格納されている)場合は、住所詳細情報に「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.streetAddress = "未入力";
				}
				else
				{
					// 取得した住所詳細情報データにnull以外の値が格納されている場合、取得した住所詳細情報をメンバ変数に代入する。
					this.streetAddress = (String)UserInfo.get(0);
				}
			}

			// 電話番号。
			UserInfo = DBA.selectInTable("t_user", "F_Phonenumber", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、ユーザーの氏に空文字を代入する。
			if(UserInfo == null)
			{
				// 電話番号を空白にする。
				this.phoneNumber = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素電話番号データとして取り出す。
				// 電話番号データがまだ何も入力されていない(DBでnullが格納されている)場合は、電話番号に「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.phoneNumber = "未入力";
				}
				else
				{
					// 取得した電話番号データにnull以外の値が格納されている場合、取得した電話番号をメンバ変数に代入する。
					this.phoneNumber = (String)UserInfo.get(0);
				}
			}

		}
		// ユーザー情報変更ページの場合
		else if(readType == 2)
		{
			// ユーザーID。
			UserInfo = DBA.selectInTable("t_user", "F_UserID", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、ユーザーIDに空文字を代入する。
			if(UserInfo == null)
			{
				// ユーザーIDを空白にする。
				this.userid = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素をユーザーIDデータとして取り出す。
				// ユーザーIDデータがまだ何も入力されていない(DBでnullが格納されている)場合は、ユーザーIDに「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.userid = "未入力";
				}
				else
				{
					// 取得したユーザーIDデータにnull以外の値が格納されている場合、取得したユーザーIDをメンバ変数に代入する。
					this.userid = (String)UserInfo.get(0);
				}
			}

			// ユーザーネーム。
			UserInfo = DBA.selectInTable("t_user", "F_UserName", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、ユーザーネームに空文字を代入する。
			if(UserInfo == null)
			{
				// ユーザーネームを空白にする。
				this.username = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素をユーザーネームデータとして取り出す。
				// ユーザーネームデータがまだ何も入力されていない(DBでnullが格納されている)場合は、ユーザーネームに「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.username = "未入力";
				}
				else
				{
					// 取得したユーザーネームデータにnull以外の値が格納されている場合、取得したユーザーネームをメンバ変数に代入する。
					this.username = (String)UserInfo.get(0);
				}
			}

			// ツイッターID。
			UserInfo = DBA.selectInTable("t_user", "F_TwitterID", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、ツイッターIDに空文字を代入する。
			if(UserInfo == null)
			{
				// ツイッターIDを空白にする。
				this.twiId = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素をツイッターIDデータとして取り出す。
				// ツイッターIDデータがまだ何も入力されていない(DBでnullが格納されている)場合は、ツイッターIDに「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.twiId = "未入力";
				}
				else
				{
					// 取得したツイッターIDデータにnull以外の値が格納されている場合、取得したツイッターIDをメンバ変数に代入する。
					this.twiId = (String)UserInfo.get(0);
				}
			}

			// メールアドレス。
			UserInfo = DBA.selectInTable("t_user", "F_Mailaddress", "where F_UserID = ?", UserID);
			// データベースと接続中にエラーが発生した場合は、メールアドレスに空文字を代入する。
			if(UserInfo == null)
			{
				// メールアドレスを空白にする。
				this.mail = "";
			}
			else
			{
				// データベースとの接続に成功した場合、アレイリストに格納された個人情報リストの内最初の要素をメールアドレスデータとして取り出す。
				// メールアドレスデータがまだ何も入力されていない(DBでnullが格納されている)場合は、メールアドレスに「未入力」を格納する。
				if(UserInfo.get(0) == null)
				{
					this.mail = "未入力";
				}
				else
				{
					// 取得したメールアドレスデータにnull以外の値が格納されている場合、取得したメールアドレスをメンバ変数に代入する。
					this.mail = (String)UserInfo.get(0);
				}
			}
		}

	}

	// 現在メンバ変数に格納されているユーザー情報をデータベースに格納するメソッド。
	// 引数:
	// LoginDao DBAccess:データベースアクセスオブジェクトを格納してください。
	// String UserID:Cookieに保存されているユーザーID情報を格納してください。
	// ArrayList<String> acountInfo:更新対象となるユーザー情報のカラム名をアレイリストにまとめて格納してください。
	// 戻り値:boolean res　　‥‥‥　　データベースの更新に成功した場合はtrue、それ以外の場合はfalseが格納される。
	public boolean writeAcountInfo(LoginDao DBAccess,String UserID,ArrayList<String> acountInfo)
	{
		// メソッドの処理結果を格納するフラグ。false:失敗　true:成功
		boolean res = false;
		// データベースの更新処理の処理結果を格納するフラグ。false:失敗　true:成功
		boolean dbres = false;

		// 更新対象のデータベースのカラム名を格納する文字列型アレイリスト。
		ArrayList<String> dbCol = new ArrayList<String>();
		// データベースの更新処理で送るデータをまとめて格納したアレイリスト。
		ArrayList<Object> updateData = new ArrayList<Object>();
		// データベースアクセス用オブジェクトを作成。
		LoginDao DBA = DBAccess;
		// フィールドを動的に取得するためにオブジェクトを一時的に格納するための変数。
		java.lang.reflect.Field f = null;

		// 例外処理。例外が発生した場合、resをfalseとする。
		try
		{
			// 引数にセットされた更新対象カラム名情報から、データベース用のカラム名を読み出してアレイリストとしてまとめる。
			for(int i=0;i<acountInfo.size();i++)
			{
				f = this.getClass().getDeclaredField(acountInfo.get(i) + "_col");
				f.setAccessible(true);
				dbCol.add( (String)f.get(this) );
			}

			// データベースの更新処理で使うデータのアレイリストを作成。
			for(int i=0;i<acountInfo.size();i++)
			{
				// データアレイリストの偶数要素番号にデータの型(文字列型)を格納。
				updateData.add(1);
				// 指定したカラムに格納されている値をデータアレイリストの値としてセット。
				f = this.getClass().getDeclaredField(acountInfo.get(i));
				f.setAccessible(true);
				updateData.add( f.get(this) );
			}

			// データベースの更新処理を実行。
			dbres = DBA.updateInTable("t_user", dbCol, updateData, "where F_UserID = '" + UserID + "'");
			if(dbres == true)
			{
				// データベースの更新処理に成功した場合は、メソッドの処理結果フラグをtrueとする。
				res = true;
			}

		}
		catch(Exception e)
		{
			// メソッドの処理結果フラグをfalse(失敗)とする。
			res = false;
			// 例外の内容を出力させる。
			e.printStackTrace();
		}

		// 引数に入力されたカラム名から、データベース用のカラム名を読み出し、アレイリストへまとめて格納する。
		return res;
	}

	/**
	 * surnameを取得します。
	 * @return surname
	 */
	public String getSurname() {
	    return surname;
	}
	/**
	 * surnameを設定します。
	 * @param surname surname
	 */
	public void setSurname(String surname) {
	    this.surname = surname;
	}


	/**
	 * nameを取得します。
	 * @return name
	 */
	public String getFirstName() {
	    return firstname;
	}
	/**
	 * nameを設定します。
	 * @param name name
	 */
	public void setName(String name) {
	    this.firstname = name;
	}


	/**
	 * postalcodeを取得します。
	 * @return postalcode
	 */
	public String getPostalcode() {
	    return postalcode;
	}

	/**
	 * postalcodeを設定します。
	 * @param postalcode postalcode
	 */
	public void setPostalcode(String postalcode) {
	    this.postalcode = postalcode;
	}


	/**
	 * prefecturesを取得します。
	 * @return prefectures
	 */
	public String getPrefectures() {
	    return prefectures;
	}
	/**
	 * prefecturesを設定します。
	 * @param prefectures prefectures
	 */
	public void setPrefectures(String prefectures) {
	    this.prefectures = prefectures;
	}


	/**
	 * cityを取得します。
	 * @return city
	 */
	public String getCity() {
	    return city;
	}
	/**
	 * cityを設定します。
	 * @param city city
	 */
	public void setCity(String city) {
	    this.city = city;
	}


	/**
	 * streetAddressを取得します。
	 * @return streetAddress
	 */
	public String getStreetAddress() {
	    return streetAddress;
	}
	/**
	 * streetAddressを設定します。
	 * @param streetAddress streetAddress
	 */
	public void setStreetAddress(String streetAddress) {
	    this.streetAddress = streetAddress;
	}


	/**
	 * phoneNumberを取得します。
	 * @return phoneNumber
	 */
	public String getPhoneNumber() {
	    return phoneNumber;
	}
	/**
	 * phoneNumberを設定します。
	 * @param phoneNumber phoneNumber
	 */
	public void setPhoneNumber(String phoneNumber) {
	    this.phoneNumber = phoneNumber;
	}

	// ユーザーの個人情報が格納されているメンバ変数の内容を全てチェックして、空白及び「未入力」となっている場合は1、それ以外の場合は0をアレイリストに格納し、結果のアレイリストを返すメソッド。
	// 戻り値のアレイリストの値は、要素番号の若い順から、ユーザーの氏、ユーザーの名、郵便番号、住所の都道府県、住所の市区町村、住所詳細情報、電話番号を表す(合計7種類)。
	// 引数には、空白・「未入力」以外で跳ね返す特定の文字列パターンを格納したアレイリストをセットしてください。
	public ArrayList<Integer> checkAcountInfo(ArrayList<String> checkPattern)
	{
		// ユーザーの個人情報の入力状況のチェック結果を格納するアレイリスト変数。
		ArrayList<Integer> checkResult = new ArrayList<Integer>();

		// メンバ変数に格納されているユーザーの個人情報をチェックし、アレイリストに結果を格納する。
		// ユーザーの氏
		checkResult.add(this.checkValid(this.surname,checkPattern));
		// ユーザーの名
		checkResult.add(this.checkValid(this.firstname,checkPattern));
		// 郵便番号
		checkResult.add(this.checkValid(this.postalcode,checkPattern));
		// 住所の都道府県
		checkResult.add(this.checkValid(this.prefectures,checkPattern));
		// 住所の市区町村
		checkResult.add(this.checkValid(this.city,checkPattern));
		// 住所詳細情報
		checkResult.add(this.checkValid(this.streetAddress,checkPattern));
		// 電話番号
		checkResult.add(this.checkValid(this.phoneNumber,checkPattern));

		return checkResult;
	}

	// 引数に入れられた個人情報のデータ変数に格納されている値が空白、もしくは「未入力」の場合は1、それ以外の場合は0を返すプライベートメソッド。
	// 引数‥‥‥
	// 1:checkData:チェックする対象の文字列をセットしてください。
	// 2:checkPattern:空白・「未入力」の他に引っかかった場合に跳ね返すためのパターンをアレイリストに格納してセットしてください。
	private int checkValid(String checkData,ArrayList<String> checkPattern)
	{
		// 正規表現により特定の文字列パターンと判定するためのオブジェクト。
		InCheck check = new InCheck(checkPattern);

		// 戻り値を格納する変数。初期値は0(入力文字列がDBに入れるデータとして有効)。
		int ret = 0;
		if(checkData.equals("") || checkData.equals("未入力"))
		{
			// 該当データの格納文字列が空白、もしくは「未入力」の場合、返り値を1(未入力のエラー)にする。
			ret = 1;
		}
		// 入力値が空白・「未入力」でない場合、さらに特定の文字列パターンにマッチングしないか見て、マッチングしていれば不正な値とみなし、返り値を2とする。
		else if(check.strMatching(checkData) == true)
		{
			// 返り値を2(不正な値のエラー)にする。
			ret = 2;
		}

		return ret;
	}



	/**
	 * useridを取得します。
	 * @return userid
	 */
	public String getUserid() {
	    return userid;
	}



	/**
	 * useridを設定します。
	 * @param userid userid
	 */
	public void setUserid(String userid) {
	    this.userid = userid;
	}



	/**
	 * usernameを取得します。
	 * @return username
	 */
	public String getUsername() {
	    return username;
	}



	/**
	 * usernameを設定します。
	 * @param username username
	 */
	public void setUsername(String username) {
	    this.username = username;
	}



	/**
	 * twiIdを取得します。
	 * @return twiId
	 */
	public String getTwiId() {
	    return twiId;
	}



	/**
	 * twiIdを設定します。
	 * @param twiId twiId
	 */
	public void setTwiId(String twiId) {
	    this.twiId = twiId;
	}



	/**
	 * mailを取得します。
	 * @return mail
	 */
	public String getMail() {
	    return mail;
	}



	/**
	 * mailを設定します。
	 * @param mail mail
	 */
	public void setMail(String mail) {
	    this.mail = mail;
	}
}

