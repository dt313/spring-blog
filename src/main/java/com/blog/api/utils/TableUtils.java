package com.blog.api.utils;


import com.blog.api.entities.User;
import com.blog.api.repository.BookmarkRepository;
import com.blog.api.repository.ReactionRepository;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TableUtils {


    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    public boolean checkIsBookmarked (Long bookmarkTableId, User user ) {
        boolean isBookmarked = bookmarkRepository.existsByBookmarkTableIdAndBookmarkedUser(bookmarkTableId, user);
        return isBookmarked;
    }
    public boolean checkIsReacted (Long reactionTableId, User user ) {
        return reactionRepository.existsByReactionTableIdAndReactedUser(reactionTableId, user)  ;
    }
}
