package jp.ac.hal.login;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class InCheck {

	// SQLインジェクションの防止用の入力チェックのクラス。

	// 入力チェックに使うチェック用パターンをまとめて格納するアレイリスト。
	ArrayList<String> CPattern = null;
	// 生成したパターンオブジェクトを格納するための変数。
	Pattern p = null;

	// クラスのコンストラクタ。引数には入力チェックを行うための正規表現のパターン一覧となるアレイリストを入れてください。
	public InCheck(ArrayList<String> Pattern)
	{
		this.CPattern = Pattern;
	}

	// 引数に入力された文字列を正規表現によりパターンマッチング(インスタンス生成時に引数に入れたパターンを基に)し、マッチングの可否を返すメソッド。
	// 引数:String CStr　(ここに入力された文字列がパターンマッチングの対象となる。)
	// 戻り値:boolean　(正規表現によるパターンマッチングでマッチングした場合はtrue、しなかった場合はfalseが返される。)

	public boolean strMatching(String CStr)
	{
		// パターンマッチングの成功可否を格納する変数。マッチングした場合はtrue、マッチングしなかった場合はfalse。
		boolean MatchResult = false;

		// オブジェクトで持っているパターンマッチングのパターンを基にパターンマッチングを行う。
		for(int i=0;i<this.CPattern.size();i++)
		{
			p = Pattern.compile(this.CPattern.get(i),Pattern.CASE_INSENSITIVE);
			if(p.matcher(CStr).find())
			{
				// 現在処理中の要素番号のパターンとマッチングした場合(アレイリストの中の1つでもマッチングした場合)
				// マッチングの成功可否をtrueとする。
				MatchResult = true;
				// for文の実行を強制終了。
				break;
			}
		}

		return MatchResult;
	}

}
