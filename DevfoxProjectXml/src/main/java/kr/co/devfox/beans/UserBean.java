package kr.co.devfox.beans;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserBean {           //ユーザー情報を保存および管理するためのJavaクラス
	private int user_idx;         //ユーザー固有のインデックスを保存する整数フィールド
	
	@Size(min=2, max=4)            //名前の長さが2字から4字の間であることを意味する
	@Pattern(regexp = "[가-힣]*")   //ハングルのみ入力可能である意味
	private String user_name;      //ユーザー名を保存する文字列型フィールド
	
	@Size(min=4, max=20)              //名前の長さが2字から4字の間であることを意味する
	@Pattern(regexp = "[a-zA-Z0-9]*") //アルファベットの大文字と小文字のみ入力可能である意味
	private String user_id;           //ユーザーIDを保存する文字列型フィールド
	
	@Size(min=4, max=20)                //名前の長さが2字から4字の間であることを意味する
	@Pattern(regexp = "[a-zA-Z0-9]*")   //アルファベットの大文字と小文字のみ入力可能である意味
	private String user_pw;             //ユーザーのパスワードを保存する文字列型フィールド
	
	@Size(min=4, max=20)                //名前の長さが2字から4字の間であることを意味する
	@Pattern(regexp = "[a-zA-Z0-9]*")   //アルファベットの大文字と小文字のみ入力可能である意味
	private String user_pw2;           //ユーザーのパスワード確認用フィールド
	
	private boolean userIdExist;     //IDが既に存在するかどうかを示すブールフィールド
	private boolean userLogin;       //ユーザーがログインしているかどうかを示すブールフィールド
	
	public UserBean() {               //userIdExistとuserLoginをfalseに初期化
		this.userIdExist = false;
		this.userLogin = false;
	}
	
	public int getUser_idx() {
		return user_idx;
	}
	public void setUser_idx(int user_idx) {
		this.user_idx = user_idx;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_pw() {
		return user_pw;
	}
	public void setUser_pw(String user_pw) {
		this.user_pw = user_pw;
	}
	public String getUser_pw2() {
		return user_pw2;
	}
	public void setUser_pw2(String user_pw2) {
		this.user_pw2 = user_pw2;
	}

	public boolean isUserIdExist() {
		return userIdExist;
	}

	public void setUserIdExist(boolean userIdExist) {
		this.userIdExist = userIdExist;
	}

	public boolean isUserLogin() {
		return userLogin;
	}

	public void setUserLogin(boolean userLogin) {
		this.userLogin = userLogin;
	}
	
	
}
