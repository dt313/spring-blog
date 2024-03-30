package com.blog.api.service;

import com.blog.api.models.dto.BookmarkDTO;
import com.blog.api.models.entity.Bookmark;
import com.blog.api.models.request.BookmarkRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookmarkService {
    public List<BookmarkDTO> getAllBookmarkByUserId(Long id);
    public BookmarkDTO toggleBookmark(BookmarkRequest newBookmark);
}
