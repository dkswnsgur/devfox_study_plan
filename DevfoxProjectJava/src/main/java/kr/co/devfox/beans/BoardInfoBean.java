package kr.co.devfox.beans;

public class BoardInfoBean {        //掲示板の情報を保存するJavaクラス
	private int board_info_idx;     //掲示板情報の固有インデックス(番号)を保存する整数型フィールド
	private String board_info_name; //掲示板の名前を保存する文字列型フィールド
	
	public int getBoard_info_idx() {
		return board_info_idx;
	}
	public void setBoard_info_idx(int board_info_idx) {
		this.board_info_idx = board_info_idx;
	}
	public String getBoard_info_name() {
		return board_info_name;
	}
	public void setBoard_info_name(String board_info_name) {
		this.board_info_name = board_info_name;
	}
	
	
}
