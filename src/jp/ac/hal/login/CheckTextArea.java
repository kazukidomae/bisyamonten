package jp.ac.hal.login;

import java.util.ArrayList;

// テキストボックスに入力された文字列に不正な値が入力されていないか、未入力・空白ではないかを一斉にチェックするクラス。
public class CheckTextArea {

	// ここから　フィールドの記述スペース
	// 入力チェックをしてほしい文字列をまとめて格納した文字列型のアレイリスト。
	private ArrayList<String> checkStr = null;
	// ここまで　フィールドの記述スペース

	// コンストラクタ。
	// 引数には判定をしてほしい文字列がまとめて格納されたアレイリストをセットしてください。
	public CheckTextArea(ArrayList<String> str)
	{
		this.checkStr = str;
	}

	// 現在メンバ変数に格納されている文字列型のアレイリストを一斉に入力チェックを行い、チェック結果を数値型のアレイリストで返すメソッド。
	// 引数には入力チェックで扱うパターン文字列(SQL文など)のアレイリストをセットしてください。
	public ArrayList<Integer> checkTextList(ArrayList<String> checkPattern)
	{
		// 入力チェックの結果を入力文字列のアレイリストの格納順に数値で格納するアレイリスト。
		// 0:異常なし　1:未入力のエラー　2:空白のエラー
		ArrayList<Integer> checkret = new ArrayList<Integer>();
		// 正規表現により特定の文字列パターンと判定するためのオブジェクト。
		InCheck check = new InCheck(checkPattern);

		// 入力文字列の要素数分処理を繰り返す。
		for(int i=0;i<this.checkStr.size();i++)
		{
			// 入力文字列の現在処理中の要素の空白・未入力チェック。
			if( (this.checkStr.get(i)).equals("") || (this.checkStr.get(i)).equals("未入力") )
			{
				// 該当データの格納文字列が空白、もしくは「未入力」の場合、結果アレイリストに1(未入力)を追加。
				checkret.add(1);
			}
			// 入力値が空白・「未入力」でない場合、さらに特定の文字列パターンにマッチングしないか見て、マッチングしていれば不正な値とみなし、返り値を2とする。
			else if( check.strMatching(this.checkStr.get(i)) == true)
			{
				// 結果アレイリストに2(不正な値のエラー)を追加。
				checkret.add(2);
			}
			// 上記以外の場合は、結果アレイリストに0(異常なし)を追加。
			else
			{
				checkret.add(0);
			}
		}

		return checkret;
	}

}
