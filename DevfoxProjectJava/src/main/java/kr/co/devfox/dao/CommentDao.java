package kr.co.devfox.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import kr.co.devfox.beans.CommentBean;
import kr.co.devfox.mapper.CommentMapper;

@Repository
public class CommentDao {
	
	@Autowired
	private CommentMapper commentMapper; //CommentMapper オブジェクトを自動的に注入してもらう
	
	public void addComment(CommentBean writeCommentBean) { 
		commentMapper.addComment(writeCommentBean); 
	}
	
	public List<CommentBean> getCommentList(int contentIdx){ 
		return commentMapper.getCommentList(contentIdx); 
	}
	
	public void deleteCommentInfo(int comment_id) { 
		commentMapper.deleteComment(comment_id); 
	}
}
