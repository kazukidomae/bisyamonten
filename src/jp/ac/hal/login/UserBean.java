package jp.ac.hal.login;

public class UserBean {
	private int userNo;
	private String userID;
	private String userName;
	private String password;
	private String surname;
	private String name;
	private String postalcode;
	private String prefectures;
	private String city;
	private String streetaddress;
	private String phonenumber;
	private String mailaddress;
	private String credit;
	private String TwitterID;
	private int Withdrawa;

	//ゲッター・セッター
	public int getUserNo() {
		return userNo;
	}
	public void setUserNo(int userNo) {
		this.userNo = userNo;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPostalcode() {
		return postalcode;
	}
	public void setPostalcode(String postalcode) {
		this.postalcode = postalcode;
	}
	public String getPrefectures() {
		return prefectures;
	}
	public void setPrefectures(String prefectures) {
		this.prefectures = prefectures;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getStreetaddress() {
		return streetaddress;
	}
	public void setStreetaddress(String streetaddress) {
		this.streetaddress = streetaddress;
	}
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getMailaddress() {
		return mailaddress;
	}
	public void setMailaddress(String mailaddress) {
		this.mailaddress = mailaddress;
	}
	public String getCredit() {
		return credit;
	}
	public void setCredit(String credit) {
		this.credit = credit;
	}
	public String getTwitterID() {
		return TwitterID;
	}
	public void setTwitterID(String twitterID) {
		TwitterID = twitterID;
	}
	public int getWithdrawa() {
		return Withdrawa;
	}
	public void setWithdrawa(int withdrawa) {
		Withdrawa = withdrawa;
	}
}
