package kr.co.devfox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import kr.co.devfox.beans.CommentBean;

public interface CommentMapper {
	
	 @Insert("INSERT INTO comment_table (content_idx, comment_text, comment_writer_name, comment_date) " +
	            "VALUES (#{content_idx}, #{comment_text, jdbcType=VARCHAR}, #{comment_writer_name, jdbcType=VARCHAR}, sysdate)")
	    void addComment(CommentBean writecommentBean);

	    @Select("SELECT * FROM comment_table WHERE content_idx = #{content_Idx}")
	    List<CommentBean> getCommentList(int content_Idx);

	    @Delete("DELETE FROM comment_table WHERE comment_id = #{comment_id}")
	    void deleteComment(int comment_id);

}