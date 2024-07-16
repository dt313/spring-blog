package com.blog.api.service.impl;

import com.blog.api.dto.request.BookmarkRequest;
import com.blog.api.dto.response.*;
import com.blog.api.entities.*;
import com.blog.api.exception.AppException;
import com.blog.api.exception.ErrorCode;
import com.blog.api.mapper.ArticleMapper;
import com.blog.api.mapper.BookmarkMapper;
import com.blog.api.mapper.QuestionMapper;
import com.blog.api.mapper.UserMapper;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.BookmarkRepository;
import com.blog.api.repository.QuestionRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.service.BookmarkService;
import com.blog.api.types.TableType;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookmarkServiceImp implements BookmarkService {
    BookmarkRepository bookmarkRepository;
    ArticleRepository articleRepository;
    UserRepository userRepository;
    BookmarkMapper bookmarkMapper;
    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    ArticleMapper articleMapper;
    UserMapper userMapper;

    @Override
    public List<BookmarkResponse> getAllArticleBookmarkedByUser(String userId) {
        User bookmarkedUser = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<BookmarkResponse> bookmarks = bookmarkRepository.findAllByBookmarkedUser(bookmarkedUser).stream().map((bookmark) -> {
            return bookmarkMapper.toBookmarkResponse(bookmark);
        }).toList();

        if(bookmarks.isEmpty()) {
            return  new ArrayList<>();
        }else {

            List<BookmarkResponse> newBookmarks = bookmarks.stream().map((bookmark) -> {
                if(bookmark.getBookmarkTableType().equals(TableType.ARTICLE)) {
                    Article article = articleRepository.findById(bookmark.getBookmarkTableId()).orElse(null);
                    ArticleResponse articleResponse = new ArticleResponse();
                    if(Objects.nonNull(article)) {
                        articleResponse = articleMapper.toArticleResponse(article);
                        articleResponse.setAuthor(userMapper.toBasicUserResponse(article.getAuthor()));
                    }
                    bookmark.setContent(articleResponse);
                    return bookmark;

                }else {
                    Question question = questionRepository.findById(bookmark.getBookmarkTableId()).orElse(null);
                    QuestionResponse questionResponse = new QuestionResponse();
                    if(Objects.nonNull(question)) {
                        questionResponse = questionMapper.toQuestionResponse(question);
                        questionResponse.setAuthor(userMapper.toBasicUserResponse(question.getAuthor()));
                    }
                    bookmark.setContent(questionResponse);
                    return bookmark;
                }

            }).toList();

            System.out.println(newBookmarks);

            return newBookmarks;
        }
    }

    @Override
    public boolean checkIsBookmarked(TableType type, String bookmarkTableId, String userId) {
        Object isExistsBookMarkTable = checkBookmarkTable(type, bookmarkTableId);

        System.out.println("IS EXITST : "+ isExistsBookMarkTable);
        if(Objects.isNull(isExistsBookMarkTable)) {
            throw new AppException(ErrorCode.ARTICLE_NOT_FOUND);
        }

        User bookmarkedUser = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND));

        Bookmark bookmark = bookmarkRepository.findByBookmarkTableIdAndBookmarkedUser(
                bookmarkTableId,bookmarkedUser);

        if(Objects.isNull(bookmark)) return false;
        return true;
    }

    @Override
    public boolean toggle(BookmarkRequest request) {
        System.out.println(request);
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
            System.out.println("CREATE BOOKMARK");
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



    @Override
    public Integer countOfBookmarkByBookmarkTableId(String id) {
        List<Bookmark> bookmarks = bookmarkRepository.findAllByBookmarkTableId(id);
        return bookmarks.size();
    }

    private Object checkBookmarkTable(TableType type, String id) {
        switch (type.toString()){
            case "ARTICLE":
                Article art = articleRepository.findById(id).orElseThrow(
                        () -> new AppException(ErrorCode.ARTICLE_NOT_FOUND)
                );
                return art;
            case "QUESTION":
                Question question = questionRepository.findById(id).orElseThrow(
                        () -> new AppException(ErrorCode.ARTICLE_NOT_FOUND)
                );
                return question;
            default:
                return null;
        }
    }

    private boolean isSameUser(String userId, Object entity) {
        if(entity instanceof  Article) {
            Article art = (Article) entity;
            if(userId.equals(art.getAuthor().getId())) return true;
        }

        if(entity instanceof  Question) {
            Question question = (Question) entity;
            if(userId.equals(question.getAuthor().getId())) return true;
        }
        return false;
    }

}
