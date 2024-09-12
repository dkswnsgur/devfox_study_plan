package kr.co.devfox.mapper;

import java.util.List;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import kr.co.devfox.beans.ContentBean;

public interface BoardMapper { //MyBatisを使用してデータベースにアクセスする機能を提供。 主に掲示板のコンテンツと関連したCRUD(Create, Read, Update, Delete)作業を担当
	//新しいコンテンツを追加する時、content_seqシーケンスを使用して自動的にcontent_idx値を生成するdualはOracleで使われる仮想のテーブル
	@SelectKey(statement = "select content_seq.nextval from dual", keyProperty = "content_idx", before = true, resultType = int.class)
	//コンテンツデータをデータベースに挿入するSQL構文**#{}**で包まれた部分はMyBatisがContentBeanオブジェクトの属性値とマッピングしてデータを挿入content_fileの場合、ファイルがない可能性があるため、jdbcType=VARCHARで指定してnull値を許容
	@Insert("insert into content_table(content_idx, content_subject, content_text, " +
			"content_file, content_writer_idx, content_board_idx, content_date) " +
			"values (#{content_idx}, #{content_subject}, #{content_text}, #{content_file, jdbcType=VARCHAR}, " +
			"#{content_writer_idx}, #{content_board_idx}, sysdate)")
	void addContentInfo(ContentBean writeContentBean);
	// 特定掲示板の名前を照会するクエリboard_info_idxを利用してboard_info_nameを返却
	@Select("select board_info_name " + 
			"from board_info_table " + 
			"where board_info_idx = #{board_info_idx}")
	String getBoardInfoName(int board_info_idx);
	// 特定掲示板の掲示物リストを照会するクエリーRowBoundsは、ページング処理のためのMyBatisの機能掲示物の番号、タイトル、作成者の名前、作成日を返還
	@Select("select a1.content_idx, a1.content_subject, a2.user_name as content_writer_name, " + 
			"       to_char(a1.content_date, 'YYYY-MM-DD') as content_date " + 
			"from content_table a1, user_table a2 " + 
			"where a1.content_writer_idx = a2.user_idx " + 
			"      and a1.content_board_idx = #{board_info_idx} " + 
			"order by a1.content_idx desc")
	List<ContentBean> getContentList(int board_info_idx, RowBounds rowBounds);
	// 特定掲示文の詳細情報を照会するクエリー作成者の名前、作成日付、タイトル、内容、ファイル情報、作成者IDを返還
	@Select("select a2.user_name as content_writer_name, " + 
			"       to_char(a1.content_date, 'YYYY-MM-DD') as content_date, " + 
			"       a1.content_subject, a1.content_text, a1.content_file, a1.content_writer_idx " + 
			"from content_table a1, user_table a2 " + 
			"where a1.content_writer_idx = a2.user_idx " + 
			"      and content_idx = #{content_idx}")
	ContentBean getContentInfo(int content_idx);
    //既存の掲示文の内容を修正するクエリーのタイトル(content_subject)、内容(content_text)、ファイル(content_file)をアップデート
	@Update("update content_table " +
			"set content_subject = #{content_subject}, content_text = #{content_text}, " +
			"content_file = #{content_file, jdbcType=VARCHAR} " +
			"where content_idx = #{content_idx}")
	void modifyContentInfo(ContentBean modifyContentBean);
	//特定の投稿を削除するSQL構文。 掲示文のcontent_idxで掲示文を削除
	@Delete("delete from content_table where content_idx = #{content_idx}")
	void deleteContentInfo(int content_idx);
	//特定掲示板の全体掲示文数を照会するクエリーページングや掲示板の状態確認に使用
	@Select("select count(*) from content_table where content_board_idx = #{content_board_idx}")
	int getContentCnt(int content_board_idx);
	@Select("SELECT * FROM content_table " +
	        "WHERE content_board_idx = #{board_info_idx} " +
	        "AND content_subject LIKE '%' || #{searchKeyword} || '%' " +
	        "ORDER BY content_idx DESC")
	//検索キーワードを含むコンテンツをフィルタリングし、ソートされた結果を提供
	List<ContentBean> searchContentList(@Param("board_info_idx") int board_info_idx, 
	                                     @Param("searchKeyword") String searchKeyword);
} 


















