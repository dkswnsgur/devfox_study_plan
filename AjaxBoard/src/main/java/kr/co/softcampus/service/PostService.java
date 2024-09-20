package kr.co.softcampus.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.softcampus.beans.Post;
import kr.co.softcampus.dao.PostDao;

@Service
public class PostService {
    private final PostDao postDao;

    @Autowired
    public PostService(PostDao postDao) {
        this.postDao = postDao;
    }

    public List<Post> getAllPosts() {
        return postDao.findAllPosts();
    }

    public void createPost(String title, String content) {
        postDao.savePost(title, content);
    }
    
    public void modifyPost(Long id, String title, String content) {
        postDao.modifyPost(id, title, content);
    }
    
    public void deletePost(Long id) {
        postDao.deletePost(id);
    }

	
}
