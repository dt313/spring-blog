package com.blog.api.utils;


import com.blog.api.entities.User;
import com.blog.api.repository.BookmarkRepository;
import com.blog.api.repository.ReactionRepository;
import lombok.*;
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

    public boolean checkIsBookmarked (String bookmarkTableId, User user ) {
        boolean isBookmarked = bookmarkRepository.existsByBookmarkTableIdAndBookmarkedUser(bookmarkTableId, user);
        System.out.println("BOOK MARK " + bookmarkTableId + user + isBookmarked);
        return isBookmarked;
    }
    public boolean checkIsReacted (String reactionTableId, User user ) {
        boolean isReacted  = reactionRepository.existsByReactionTableIdAndReactedUser(reactionTableId, user);
        System.out.println("REACT " + reactionTableId + user + isReacted);

        return isReacted;
    }
}
