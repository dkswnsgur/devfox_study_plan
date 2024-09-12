package kr.co.devfox.beans;

public class CommentBean {
	
	private int comment_id;
	private int content_idx; 
	private String comment_text;
	private String comment_writer_name;
	private String comment_date;
	
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public int getContent_idx() {
		return content_idx;
	}
	public void setContent_idx(int content_idx) {
		this.content_idx = content_idx;
	}
	public String getComment_text() {
		return comment_text;
	}
	public void setComment_text(String comment_text) {
		this.comment_text = comment_text;
	}
	public String getComment_writer_name() {
		return comment_writer_name;
	}
	public void setComment_writer_name(String comment_writer_name) {
		this.comment_writer_name = comment_writer_name;
	}
	public String getComment_date() {
		return comment_date;
	}
	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}
	


}
