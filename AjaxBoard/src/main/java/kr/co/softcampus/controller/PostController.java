package kr.co.softcampus.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.co.softcampus.beans.Post;
import kr.co.softcampus.service.PostService;

@Controller
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    @ResponseBody
    public List<Post> index() {
        return postService.getAllPosts();
    }

    @PostMapping
    @ResponseBody
    public String createPost(@RequestParam String title, @RequestParam String content) {
        postService.createPost(title, content);
        return "success";
    }
    
    // 게시물 수정
    @PutMapping("/{id}")
    @ResponseBody
    public String modifyPost(@PathVariable Long id, @RequestBody Post post) {
        postService.modifyPost(id, post.getTitle(), post.getContent());
        return "success";
    }
    
    @DeleteMapping("/{id}")
    @ResponseBody
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return "success";
    }
}
