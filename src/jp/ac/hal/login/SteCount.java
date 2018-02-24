package jp.ac.hal.login;

public class SteCount {

	// ここから　フィールドの宣言・初期化スペース
	// ステッカーのSSサイズのカウント数。
	private int ss;
	// ステッカーのSサイズのカウント数。
	private int s;
	// ステッカーのMサイズのカウント数。
	private int m;
	// ステッカーのLサイズのカウント数。
	private int l;
	// ステッカーのLLサイズのカウント数。
	private int ll;
	// ここまで　フィールドの宣言・初期化スペース

	// SteCountクラスのコンストラクタ。
	public SteCount()
	{
		// ステッカーのそれぞれのカウント数を0枚に初期化。
		this.ss = 0;
		this.s = 0;
		this.m = 0;
		this.l = 0;
		this.ll = 0;
	}

	/**
	 * ssを取得します。
	 * @return ss
	 */
	public int getSs() {
	    return ss;
	}

	/**
	 * ssを設定します。
	 * @param ss ss
	 */
	public void setSs(int ss) {
	    this.ss = ss;
	}

	/**
	 * sを取得します。
	 * @return s
	 */
	public int getS() {
	    return s;
	}

	/**
	 * sを設定します。
	 * @param s s
	 */
	public void setS(int s) {
	    this.s = s;
	}

	/**
	 * mを取得します。
	 * @return m
	 */
	public int getM() {
	    return m;
	}

	/**
	 * mを設定します。
	 * @param m m
	 */
	public void setM(int m) {
	    this.m = m;
	}

	/**
	 * lを取得します。
	 * @return l
	 */
	public int getL() {
	    return l;
	}

	/**
	 * lを設定します。
	 * @param l l
	 */
	public void setL(int l) {
	    this.l = l;
	}

	/**
	 * llを取得します。
	 * @return ll
	 */
	public int getLl() {
	    return ll;
	}

	/**
	 * llを設定します。
	 * @param ll ll
	 */
	public void setLl(int ll) {
	    this.ll = ll;
	}

}
