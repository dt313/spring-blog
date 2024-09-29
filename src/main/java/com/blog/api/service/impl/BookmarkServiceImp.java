package com.blog.api.service.impl;

import com.blog.api.dto.request.BookmarkRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.BasicUserResponse;
import com.blog.api.dto.response.BookmarkResponse;
import com.blog.api.entities.Article;
import com.blog.api.entities.Bookmark;
import com.blog.api.entities.User;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.ArticleMapper;
import com.blog.api.mapper.BookmarkMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.BookmarkRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.BookmarkService;
import com.blog.api.types.TableType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkServiceImp implements BookmarkService {
    BookmarkRepository bookmarkRepository;
    ArticleRepository articleRepository;
    UserRepository userRepository;
    BookmarkMapper bookmarkMapper;
    ArticleMapper articleMapper;
    UserMapper userMapper;

    @Override
    public List<BookmarkResponse> getAllArticleBookmarkedByUser(Long userId) {
        User bookmarkedUser = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<BookmarkResponse> bookmarks = bookmarkRepository.findAllByBookmarkedUser(bookmarkedUser).stream().map(bookmarkMapper::toBookmarkResponse).toList();

        if(bookmarks.isEmpty()) {
            return new ArrayList<>();
        }else {

            List<BookmarkResponse> newBookmarks = bookmarks.stream().map((bookmark) -> {
                    Article article = articleRepository.findById(bookmark.getBookmarkTableId()).orElse(null);
                    ArticleResponse articleResponse = new ArticleResponse();
                    if(Objects.nonNull(article)) {
                        articleResponse = articleMapper.toArticleResponse(article);
                        articleResponse.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));
                    }
                    bookmark.setContent(articleResponse);
                    return bookmark;

            }).toList();

            return newBookmarks;
        }
    }


    @Override
    public boolean toggle(BookmarkRequest request) {
        Object isExistsBookMarkTable =
                checkBookmarkTable(request.getBookmarkTableType(), request.getBookmarkTableId());

        if(Objects.isNull(isExistsBookMarkTable)) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        if(isSameUser(request.getBookmarkedUser(), isExistsBookMarkTable))
            throw new AppException(ErrorCode.USER_CONFLICT);


        User bookmarkedUser = userRepository.findById(request.getBookmarkedUser()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));
        Bookmark bookmark = bookmarkRepository.findByBookmarkTableIdAndBookmarkedUser(
                request.getBookmarkTableId(), bookmarkedUser);

        if(Objects.isNull(bookmark)) {
            // create bookmark
            bookmark = bookmarkMapper.toBookmark(request);
            bookmark.setBookmarkedUser(bookmarkedUser);
            bookmarkRepository.save(bookmark);
            return true;

        }else {
            // delete bookmark
            bookmarkRepository.deleteById(bookmark.getId());
            return false;
        }

    }

    @Override
    public List<BookmarkResponse> getAll() {
        List<Bookmark> bookmarks = bookmarkRepository.findAll();

        List<BookmarkResponse> response = bookmarks.stream().map((bookmark) -> {
            BookmarkResponse bookmarkResponse = bookmarkMapper.toBookmarkResponse(bookmark);
            BasicUserResponse bookmarkedUser = userMapper.toBasicUserResponse(bookmark.getBookmarkedUser());
            bookmarkResponse.setBookmarkedUser(bookmarkedUser);
            return bookmarkResponse;
        }).toList();

        return response;
    }


    private Object checkBookmarkTable(TableType type, Long id) {
        switch (type.toString()){
            case "ARTICLE":
                Article art = articleRepository.findById(id).orElseThrow(
                        () -> new AppException(ErrorCode.ARTICLE_NOT_FOUND)
                );
                return art;
            default:
                return null;
        }
    }

    private boolean isSameUser(Long userId, Object entity) {
        if(entity instanceof Article art) {
            return userId.equals(art.getAuthor().getId());
        }
        return false;
    }

}
