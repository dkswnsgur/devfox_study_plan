package kr.co.softcampus.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.softcampus.beans.Post;
import kr.co.softcampus.database.PostMapper;

@Repository
public class PostDao {
    private final PostMapper postMapper;

    @Autowired
    public PostDao(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

    public List<Post> findAllPosts() {
        return postMapper.findAll();
    }

    public void savePost(String title, String content) {
        postMapper.insertPost(title, content);
    }
    
    public void modifyPost(Long id, String title, String content) {
        postMapper.updatePost(id, title, content);
    }
    
    public void deletePost(Long id) {
        postMapper.deletePost(id);
    }

		
	

		
	}

