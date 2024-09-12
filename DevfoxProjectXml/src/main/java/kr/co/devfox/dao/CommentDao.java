package kr.co.devfox.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import kr.co.devfox.beans.CommentBean;

@Repository
public class CommentDao {
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate; //MyBatisのSqlSessionTemplateを注入してもらい、データベース作業を処理
	
	public void addComment(CommentBean writeCommentBean) { 
		sqlSessionTemplate.insert("comment.addComment",writeCommentBean); 
	}
	
	public List<CommentBean> getCommentList(int contentIdx){ 
		return sqlSessionTemplate.selectList("comment.getCommentList",contentIdx); 
	}
	
	public void deleteCommentInfo(int comment_id) { 
		sqlSessionTemplate.delete("comment.deleteComment",comment_id); 
	}
}
