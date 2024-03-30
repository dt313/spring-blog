package com.blog.api.models.mapper;

import com.blog.api.models.dto.BookmarkDTO;
import com.blog.api.models.entity.Bookmark;

public class BookmarkMapper {
    public static BookmarkDTO toBookmarkDTO(Bookmark bookmark) {
        BookmarkDTO newBookmarkDTO = new BookmarkDTO();
        newBookmarkDTO.setId(bookmark.getId());
        newBookmarkDTO.setBookmarkId(bookmark.getBookmarkId());
        newBookmarkDTO.setUserId(UserMapper.toUserDTO(bookmark.getUserId()));
        newBookmarkDTO.setArtId(ArticleMapper.toArticleDTO(bookmark.getArtId()));
        newBookmarkDTO.setCreatedAt(bookmark.getCreatedAt());
        return newBookmarkDTO;
    }
}
