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
	todo_id 	number primary key,
	content 	varchar(1000),
	memo    	varchar(1000),
	duedate     varchar(100),
	duetime     varchar(100),
	share_with  varchar(1000),
	importance  varchar(10),
	writer_id 	number,
	addr_id		number,
	done 		number							
);-- 0 false, 1 true

select * from todos;
drop table todos;

create sequence todo_seq increment by 1 start with 1;
drop sequence todo_seq;

insert into todos values (todo_seq.nextval, '메로나 사오기', 0);
insert into todos values (todo_seq.nextval, '바밤바 사오기', '잘', '12-12-12', '11:11', '["admin"]', '2', '2', addr_seq.currval, 0);
insert into todos values (todo_seq.nextval, '더위사냥 사오기', 1);
insert into todos values (todo_seq.nextval, '설레임 사오기', 1);
insert into todos values (todo_seq.nextval, '엑셀런트 사오기', 0);
insert into todos values (todo_seq.nextval, '찰떡 붕어빵 사오기', 1);
insert into todos values (todo_seq.nextval, '비비빅 사오기', 1);

select * from todos;


-- address --------------------------------------------------------------------------

create table address (
	addr_id number primary key,
	address_name varchar2(1000),
	place_name varchar2(1000),
	road_address_name varchar2(1000),
	category_name varchar2(1000),
	lat number,
	lng number
);
select * from address;
drop table address;

create sequence addr_seq increment by 1 start with 1;
drop sequence addr_seq;
select addr_seq.NEXTVAL from dual;
select addr_seq.CURRVAL from dual;
select todo_seq.NEXTVAL from dual;


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
ALTER TABLE t2 ADD (col number); --virtual column

delete from users where id='admin';

Select * from T3;

create table t3( a number, b number);
insert into t3 values (1, (select max(addr_id) from address));
