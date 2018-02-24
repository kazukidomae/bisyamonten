package jp.ac.hal.login;

public class GalleryBean {
	//作品ＩＤ
	private int workid;
	//ユーザーNo
	private int userno;
	//車型ID
	private int vehicletypeid;
	//ステッカー価格表ID
	private int stickerpricelistid;
	//作成デザインパス
	private String workdesing;
	//サムネイル画像パス
	private String thumbnailpass;
	//公開フラグ
	private int release;
	//いいね数
	private int nice;
	//お気に入り数
	private int favorite;
	//作成日時
	private String workdata;

	//T_Userテーブルからの情報
	//ユーザーID
	private String userid;
	//ユーザーネーム
	private String username;

	//ゲッター・セッター
	public int getWorkid() {
		return workid;
	}
	public void setWorkid(int workid) {
		this.workid = workid;
	}
	public int getUserno() {
		return userno;
	}
	public void setUserno(int userno) {
		this.userno = userno;
	}
	public int getVehicletypeid() {
		return vehicletypeid;
	}
	public void setVehicletypeid(int vehicletypeid) {
		this.vehicletypeid = vehicletypeid;
	}
	public int getStickerpricelistid() {
		return stickerpricelistid;
	}
	public void setStickerpricelistid(int stickerpricelistid) {
		this.stickerpricelistid = stickerpricelistid;
	}
	public String getWorkdesing() {
		return workdesing;
	}
	public void setWorkdesing(String workdesing) {
		this.workdesing = workdesing;
	}
	public String getThumbnailpass() {
		return thumbnailpass;
	}
	public void setThumbnailpass(String thumbnailpass) {
		this.thumbnailpass = thumbnailpass;
	}
	public int getRelease() {
		return release;
	}
	public void setRelease(int release) {
		this.release = release;
	}
	public int getNice() {
		return nice;
	}
	public void setNice(int nice) {
		this.nice = nice;
	}
	public int getFavorite() {
		return favorite;
	}
	public void setFavorite(int favorite) {
		this.favorite = favorite;
	}
	public String getWorkdata() {
		return workdata;
	}
	public void setWorkdata(String workdata) {
		this.workdata = workdata;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}

	//T_Userテーブルからの情報
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
}
