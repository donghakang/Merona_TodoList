-- user

create table users (
	user_id number,
	id varchar(50),
	password varchar(50)
);
create sequence user_seq increment by 1 start with 1;

insert into users values (user_seq.nextval, 'admin', '1234');

select * from users;


-- todo

create table todos (
	todo_id number,
	content varchar(200),
	done number(1)
);

create sequence todo_seq increment by 1 start with 1;

insert into todos values (todo_seq.nextval, '메로나 사오기', 0);
insert into todos values (todo_seq.nextval, '바밤바 사오기', 1);
insert into todos values (todo_seq.nextval, '더위사냥 사오기', 1);
insert into todos values (todo_seq.nextval, '설레임 사오기', 1);
insert into todos values (todo_seq.nextval, '엑셀런트 사오기', 0);
insert into todos values (todo_seq.nextval, '찰떡 붕어빵 사오기', 1);
insert into todos values (todo_seq.nextval, '비비빅 사오기', 1);

select * from todos;
