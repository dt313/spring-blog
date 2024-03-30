package com.blog.api.database;


import com.blog.api.models.entity.*;
import com.blog.api.repository.ArticleRepository;
import com.blog.api.repository.LikeRepository;
import com.blog.api.repository.NotificationRepository;
import com.blog.api.repository.UserRepository;
import com.blog.api.types.NotificationType;
import com.blog.api.types.TableType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository , ArticleRepository articleRepository , NotificationRepository notificationRepository, LikeRepository likeRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
//                User user1 = new User("danhtuan3103", "1234", "Tuan", "Danh", "danh@gmail.com");
//                User user2 = new User("tranhau123", "1234", "Tran", "Hau", "hau@gmail.com");
//
//
//                Article article1 = new Article(user1, "Blog 1", "Hello Anh Em",  12,15, null, null);
//                Article article2 = new Article(user1, "Blog 2", "Hello World", 125,155, null, null);
//                Article article3 = new Article(user2, "Blog 3", "Hello Chi Em", 152,115, null, null);
//
//                Metadata m1 = new Metadata(article1.getId(),"Meta title", "Meta Description", "thumbnail");
//                article1.setMetadata(m1);
//
//
//                Notification notification1 = new Notification(TableType.QUESTION, NotificationType.BOOKMARKED, user1, article1, user2);
//                Notification notification2 = new Notification(TableType.QUESTION,NotificationType.BOOKMARKED, user2, article2, user1);
//
//                Like like1 = new Like(article1,TableType.ARTICLE, user1);
//                Like like2 = new Like(article2,TableType.QUESTION, user1);
//
//
//
//
//                logger.info("insert data : " + userRepository.save(user1));
//                logger.info("insert data : " + userRepository.save(user2));
//
//                logger.info("insert data : " + articleRepository.save(article1));
//                logger.info("insert data : " + articleRepository.save(article2));
//                logger.info("insert data : " + articleRepository.save(article3));
////
//                logger.info("insert data : " + notificationRepository.save(notification1));
//                logger.info("insert data : " + notificationRepository.save(notification2));
//
//                logger.info("insert data : " + likeRepository.save(like1));
//                logger.info("insert data : " + likeRepository.save(like2));
            }


        };
    }
}
