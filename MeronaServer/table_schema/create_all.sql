-- user --------------------------------------------------------------------------

create table users (
	user_id number primary key,
	id varchar(100),
	pw varchar(100),
	name varchar(100),
	birth varchar(100),	
	email varchar(100),	
	token varchar(1000)
);
create sequence user_seq increment by 1 start with 1;

-- insert into users values (user_seq.nextval, 'admin', '1234');

select * from users;

-- todo --------------------------------------------------------------------------

create table todos (
	todo_id number primary key,
	content varchar(1000),
	memo varchar(1000),
	duedate varchar(100),
	duetime varchar(100),
	location varchar(1000),
	share_with varchar(1000),
	done number							-- 0 false, 1 true
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



-- friend --------------------------------------------------------------------------

create table friend (
	user_id number,
	friend_id number,
	constraint fk_user_id foreign key(user_id) references users(user_id)
);

create table request (
	user_id number,
	friend_id number
);


select * from friend;
select * from request;
select * from tab;

insert into request values (1,2);
delete from REQUEST;


-- examples --------------------------------------------------------------------------

create table t2( col_name varchar2(10) );
insert into t2(col_name) values('hsaJDadkD');
ALTER TABLE t2 ADD (col_name4 varchar(100)); --virtual column

delete from users where id='admin';

Select * from T2;