package kr.co.softcampus.database;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.softcampus.beans.Post;

@Mapper
public interface PostMapper {

    @Select("SELECT * FROM posts")
    List<Post> findAll();

    @Insert("INSERT INTO posts (title, content) VALUES (#{title}, #{content})")
    void insertPost(@Param("title") String title, @Param("content") String content);
    
    @Update("UPDATE posts SET title = #{title}, content = #{content} WHERE id = #{id}")
    void updatePost(@Param("id") Long id, @Param("title") String title, @Param("content") String content);
    
    @Delete("DELETE FROM posts WHERE id = #{id}")
    void deletePost(Long id);
    
    
}
