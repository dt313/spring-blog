SELECT user AS active_user FROM dual;

select * from USERS;    
select * from BOOKMARKS;
select * from ARTICLES;
select * from NOTIFICATIONS;
select * from "METADATA";
select * from TOPIC;
select * from ART_TOPIC;
select * from LIKES;
select * from LIKED_USER;
select * from COMMENTS_REPLIES;
select * from COMMENTS;
select * from INVALID_TOKEN;


delete from comments where id=5;
 
SELECT table_name
FROM all_tables
WHERE owner = 'SYSTEM';









drop table article_of_user cascade constraints ;
drop table articles cascade constraints ;
drop table comments cascade constraints ;
drop table comments_replies cascade constraints;
drop table  invalid_token cascade constraints ;
drop table  liked_user cascade constraints ;
drop table  likes cascade constraints ;
drop table  notifications cascade constraints ;
drop table  permission_of_role cascade constraints ;
drop table  role_of_user cascade constraints;
drop table  permissions cascade constraints ;
drop table  roles cascade constraints ;
drop table  topic_of_article cascade constraints ;
drop table  topics cascade constraints ;
drop table  users cascade constraints ;
drop table  users_articles cascade constraints ;
drop table  articles_comments cascade constraints ;
drop table  reactions cascade constraints ;
drop table  articles_reactions cascade constraints ;
