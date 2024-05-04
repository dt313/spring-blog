package com.blog.api.service;

import com.blog.api.dto.request.BookmarkRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.BookmarkResponse;
import com.blog.api.types.TableType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookmarkService {
    public List<Object> getAllBookmarkedArticleByType(TableType typeTable,String userId);
    public boolean checkIsBookmarked(TableType type, String bookmarkTableId, String userId);
    public void toggle(BookmarkRequest request);
    public List<BookmarkResponse> getAll();
    public Integer countOfBookmarkByBookmarkTableId(String id);
}
