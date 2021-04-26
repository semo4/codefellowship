package com.example.CodeFellowship;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PostRepository extends CrudRepository<PostModel, Integer> {
    @Query(value = "SELECT * FROM post_model where application_user_id = ?1", nativeQuery = true)
    List<PostModel> findPostByUserId(int id);
}
