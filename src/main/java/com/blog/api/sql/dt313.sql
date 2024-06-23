select * from user_sequences;
SELECT owner, table_name FROM all_tables where owner='DT313';

select * from comments;
select * from users;
select * from articles;
select * from reactions;
select * from bookmark;
select * from images;
delete from articles where id = 'eb8270fd-efd1-47a6-b1fa-1a14d9d8d673';

drop table comments cascade constraint;
drop table reactions cascade constraint;




