package com.blog.api.mapper;

import com.blog.api.dto.request.BookmarkRequest;
import com.blog.api.dto.response.BookmarkResponse;
import com.blog.api.entities.Bookmark;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface BookmarkMapper {
    @Mapping(target = "bookmarkedUser", ignore = true)
    Bookmark toBookmark(BookmarkRequest request);
    @Mapping(target = "bookmarkedUser", ignore = true)
    BookmarkResponse toBookmarkResponse(Bookmark bookmark);

}
