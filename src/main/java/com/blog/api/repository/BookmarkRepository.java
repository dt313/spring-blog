package com.blog.api.repository;

import com.blog.api.entities.Bookmark;
import com.blog.api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {
    List<Bookmark> findAllByBookmarkTableId(Long id);
    List<Bookmark> findAllByBookmarkedUser(User userId);
    Bookmark findByBookmarkTableIdAndBookmarkedUser(Long id, User userId);
    boolean existsByBookmarkTableIdAndBookmarkedUser(Long id, User userId);
}
