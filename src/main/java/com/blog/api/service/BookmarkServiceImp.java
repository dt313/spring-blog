package com.blog.api.service;

import com.blog.api.exception.NotFoundException;
import com.blog.api.models.dto.BookmarkDTO;
import com.blog.api.models.entity.Article;
import com.blog.api.models.entity.Bookmark;
import com.blog.api.models.entity.User;
import com.blog.api.models.mapper.BookmarkMapper;
import com.blog.api.models.request.BookmarkRequest;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.BookmarkRepository;
import com.blog.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BookmarkServiceImp implements BookmarkService{

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Override
    public List<BookmarkDTO> getAllBookmarkByUserId(Long id) {
        User isExistUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found id = " + id));
        List<Bookmark> bookmarks =  bookmarkRepository.findBookmarkByUserId(isExistUser);
        List<BookmarkDTO> result = new ArrayList<>();
        for(Bookmark bm : bookmarks){
            result.add(BookmarkMapper.toBookmarkDTO(bm));
        }
        return result;
    }

    @Override
    public BookmarkDTO toggleBookmark(BookmarkRequest newBookmark) {
        User isExistUser = userRepository.findById(newBookmark.getUserId()).orElseThrow(() -> new NotFoundException("User not found id = " + newBookmark.getUserId()));
        Article isExistArt = articleRepository.findById(newBookmark.getArtId()).orElseThrow(() -> new NotFoundException("Article not found id = " + newBookmark.getArtId()));
        // error
        if(isExistUser.equals(isExistArt.getAuthor())) throw new NotFoundException("Can not bookmark your article");
        Bookmark isExistBookmark = bookmarkRepository.findBookmarkByUserIdAndArtId(isExistUser, isExistArt);

        if(isExistBookmark == null) {
            Bookmark bookmark = new Bookmark(isExistArt, isExistUser, true);
            return BookmarkMapper.toBookmarkDTO(bookmarkRepository.save(bookmark));
        }else {
            bookmarkRepository.deleteById(isExistBookmark.getId());
        }
        return null;
    }
}
