package com.blog.api.service.impl;

import com.blog.api.dto.request.BookmarkRequest;
import com.blog.api.dto.response.ArticleResponse;
import com.blog.api.dto.response.BasicUserResponse;
import com.blog.api.dto.response.BookmarkResponse;
import com.blog.api.entities.Article;
import com.blog.api.entities.Bookmark;
import com.blog.api.entities.Reaction;
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
import com.blog.api.types.ReactionTableType;
import com.blog.api.types.TableType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.awt.print.Book;
import java.lang.reflect.Array;
import java.util.*;

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
    public List<Object> getAllBookmarkedArticleByType(TableType type,String userId) {
        User bookmarkedUser = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkedUserAndBookmarkTableType(bookmarkedUser, type);
        if(bookmarks.isEmpty()) {
            return  new ArrayList<>();
        }else {
            List<ArticleResponse> articles = bookmarks.stream().map((bookmark) -> {
                Article article = articleRepository.findById(bookmark.getBookmarkTableId()).orElse(null);
                ArticleResponse articleResponse = new ArticleResponse();
                if(Objects.nonNull(article)) {
                    articleResponse = articleMapper.toArticleResponse(article);
                    articleResponse.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));
                }
                return articleResponse;
            }).toList();

            return Collections.singletonList(articles);
        }


    }

    @Override
    public boolean checkIsBookmarked(TableType type, String bookmarkTableId, String userId) {
        boolean isExistsBookMarkTable =
                checkBookmarkTable(type, bookmarkTableId);

        if(!isExistsBookMarkTable) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        User bookmarkedUser = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findAllByBookmarkTableIdAndBookmarkedUser(
                bookmarkTableId,bookmarkedUser);

        if(Objects.isNull(bookmark)) return false;
        return true;
    }

    @Override
    public void toggle(BookmarkRequest request) {
        System.out.println(request);
        boolean isExistsBookMarkTable =
                checkBookmarkTable(request.getBookmarkTableType(), request.getBookmarkTableId());

        if(!isExistsBookMarkTable) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        User bookmarkedUser = userRepository.findById(request.getBookmarkedUser()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findAllByBookmarkTableIdAndBookmarkedUser(
                request.getBookmarkTableId(), bookmarkedUser);

        if(Objects.isNull(bookmark)) {
            // create bookmark
            System.out.println("CREATE BOOKMARK");
            bookmark = bookmarkMapper.toBookmark(request);
            bookmark.setBookmarkedUser(bookmarkedUser);
            bookmarkRepository.save(bookmark);

            return;

        }else {
            // delete bookmark
            bookmarkRepository.deleteById(bookmark.getId());
            return;
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

    @Override
    public Integer countOfBookmarkByBookmarkTableId(String id) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkTableId(id);
        return bookmarks.size();
    }

    private boolean checkBookmarkTable(TableType type, String id) {
        switch (type.toString()){
            case "ARTICLE":
                articleRepository.findById(id).orElseThrow(
                        () -> new AppException(ErrorCode.ARTICLE_NOT_FOUND)
                );
                return true;
            case "QUESTION":
                return false;
            default:
                return false;
        }
    }
}
