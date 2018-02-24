package jp.ac.hal.login;

public class UsestickerBean {

	//T_Usestickerテーブル情報
	//使用ステッカーID
	private int usestickerid;
	//ステッカーID
	private int stickerid;
	//作品ID
	private int workid;
	//SSサイズ枚数
	private int numberofsheetsSS;
	//Sサイズ枚数
	private int numberofsheetsS;
	//Mサイズ枚数
	private int numberofsheetsM;
	//Lサイズ枚数
	private int numberofsheetsL;
	//LLサイズ枚数
	private int numberofsheetsLL;
	//全面サイズ枚数
	private int numberofsheetsALL;

	//T_Stickerpricelistテーブル情報
	//ステッカー価格表ID
	private int StickerpricelistID;
	//ステッカー種類名
	private String Stickertypename;
	//SS価格
	private int PriceSS;
	//S価格
	private int PriceS;
	//M価格
	private int PriceM;
	//L価格
	private int PriceL;
	//LL価格
	private int PriceLL;
	//全面価格
	private int PriceALL;


	//T_Stickerテーブル情報
	//ステッカーID
	private int StickerID;
	//ステッカー名
	private String StickerName;


	//ゲッター・セッター
	//T_Usestickerテーブル情報
	public int getUsestickerid() {
		return usestickerid;
	}
	public void setUsestickerid(int usestickerid) {
		this.usestickerid = usestickerid;
	}
	public int getStickerid() {
		return stickerid;
	}
	public void setStickerid(int stickerid) {
		this.stickerid = stickerid;
	}
	public int getWorkid() {
		return workid;
	}
	public void setWorkid(int workid) {
		this.workid = workid;
	}
	public int getNumberofsheetsSS() {
		return numberofsheetsSS;
	}
	public void setNumberofsheetsSS(int numberofsheetsSS) {
		this.numberofsheetsSS = numberofsheetsSS;
	}
	public int getNumberofsheetsS() {
		return numberofsheetsS;
	}
	public void setNumberofsheetsS(int numberofsheetsS) {
		this.numberofsheetsS = numberofsheetsS;
	}
	public int getNumberofsheetsM() {
		return numberofsheetsM;
	}
	public void setNumberofsheetsM(int numberofsheetsM) {
		this.numberofsheetsM = numberofsheetsM;
	}
	public int getNumberofsheetsL() {
		return numberofsheetsL;
	}
	public void setNumberofsheetsL(int numberofsheetsL) {
		this.numberofsheetsL = numberofsheetsL;
	}
	public int getNumberofsheetsLL() {
		return numberofsheetsLL;
	}
	public void setNumberofsheetsLL(int numberofsheetsLL) {
		this.numberofsheetsLL = numberofsheetsLL;
	}
	public int getNumberofsheetsALL() {
		return numberofsheetsALL;
	}
	public void setNumberofsheetsALL(int numberofsheetsALL) {
		this.numberofsheetsALL = numberofsheetsALL;
	}

	//T_Stickerpricelistテーブル情報
	public int getStickerpricelistID() {
		return StickerpricelistID;
	}
	public void setStickerpricelistID(int stickerpricelistID) {
		StickerpricelistID = stickerpricelistID;
	}
	public String getStickertypename() {
		return Stickertypename;
	}
	public void setStickertypename(String stickertypename) {
		Stickertypename = stickertypename;
	}
	public int getPriceSS() {
		return PriceSS;
	}
	public void setPriceSS(int priceSS) {
		PriceSS = priceSS;
	}
	public int getPriceS() {
		return PriceS;
	}
	public void setPriceS(int priceS) {
		PriceS = priceS;
	}
	public int getPriceM() {
		return PriceM;
	}
	public void setPriceM(int priceM) {
		PriceM = priceM;
	}
	public int getPriceL() {
		return PriceL;
	}
	public void setPriceL(int priceL) {
		PriceL = priceL;
	}
	public int getPriceLL() {
		return PriceLL;
	}
	public void setPriceLL(int priceLL) {
		PriceLL = priceLL;
	}
	public int getPriceALL() {
		return PriceALL;
	}
	public void setPriceALL(int priceALL) {
		PriceALL = priceALL;
	}

	//T_Stickerテーブル情報
	public int getStickerID() {
		return StickerID;
	}
	public void setStickerID(int stickerID) {
		StickerID = stickerID;
	}
	public String getStickerName() {
		return StickerName;
	}
	public void setStickerName(String stickerName) {
		StickerName = stickerName;
	}
}
