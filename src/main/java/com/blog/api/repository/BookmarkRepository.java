package com.blog.api.repository;

import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Bookmark;
import com.blog.api.models.entity.Topic;
import com.blog.api.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Bookmark findBookmarkByUserIdAndArtId(User userId, Article articleId);
    List<Bookmark> findBookmarkByUserId(User userId);
    Bookmark findBookmarkByArtId( Article articleId);
}
