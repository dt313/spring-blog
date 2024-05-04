package com.blog.api.repository;

import com.blog.api.entities.Bookmark;
import com.blog.api.entities.User;
import com.blog.api.types.TableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark,String> {
    List<Bookmark> findAllByBookmarkTableId(String id);
    List<Bookmark> findAllByBookmarkedUserAndBookmarkTableType(User userId, TableType type);
    Bookmark findAllByBookmarkTableIdAndBookmarkedUser(String id, User userId);
}
