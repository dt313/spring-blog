select * from user_sequences;
SELECT owner, table_name FROM all_tables where owner='DT313';

select * from comments;
select * from users;
select * from articles;
select * from reactions;
select * from bookmark;
select * from images;
select * from topics;
delete from articles where id = 'eb8270fd-efd1-47a6-b1fa-1a14d9d8d673';

drop table comments cascade constraint;
drop table reactions cascade constraint;

SELECT COUNT(*) FROM comments r WHERE COMMENTABLE_ID = 89 and comment_type = 'COMMENT';






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
drop table  topics_of_article cascade constraints ;
drop table  topics cascade constraints ;
drop table  users cascade constraints ;
drop table  users_articles cascade constraints ;
drop table  articles_comments cascade constraints ;
drop table  reactions cascade constraints ;
drop table  articles_reactions cascade constraints ;
drop table  bookmarks cascade constraints ;
drop table  images cascade constraints ;