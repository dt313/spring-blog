package com.blog.api.service;

import com.blog.api.dto.request.BookmarkRequest;
import com.blog.api.dto.response.BookmarkResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookmarkService {
    List<BookmarkResponse> getAllArticleBookmarkedByUser(Long userId);
    boolean toggle(BookmarkRequest request);
    List<BookmarkResponse> getAll();
}
