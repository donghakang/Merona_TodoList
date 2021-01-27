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

select * from todos;


-- address --------------------------------------------------------------------------

create table address (
	addr_id number primary key,
	address_name varchar2(1000),
	place_name varchar2(1000),
	road_address_name varchar2(1000),
	category_name varchar2(1000),
	lat number,
	lng number,
	notify number
);
select * from address;
drop table address;

create sequence addr_seq increment by 1 start with 1;
drop sequence addr_seq;



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

insert into friend values (4, 1);
delete from REQUEST;

-- notification ----------------------------------------------------------

create table noti (
	noti_id number primary key,
	user_id number,
	friend_id number,
	type number,
	pushDate varchar2(50)
);

drop table noti;
create sequence noti_seq increment by 1 start with 1;

select * from noti;

delete from noti where noti_id=2

-- examples --------------------------------------------------------------------------

create table t2( col_name varchar2(10) );
insert into t2(col_name) values('hsaJDadkD');
ALTER TABLE t2 ADD (col number); --virtual column

alter table address add (notify number);
select * from address;

update address set notify=0

delete from users where id='admin';

Select * from T3;

create table t3( a number, b number);

insert into t3 values (2, 21);
insert into t3 values (1, 2);
insert into t3 values (22, 2);
insert into t3 values (1, 21);


select users.* from 
(select b as "uid" from t3 where a=2
union
select a as "uid" from t3 where b=2) 
left join users on "uid"=user_id


select * from todos;
select * from users;

select * from users
where lower(email) like '%naver%';

select * from todos
where writer_id=2;

drop table t3;

select * from todos, address;
select * from todos 
left join address on todos.addr_id=address.addr_id
where writer_id=1;


update (select * from todos left join address on todos.addr_id=address.addr_id)
set notify=1


SELECT * FROM TODOS 
LEFT JOIN ADDRESS ON TODOS.ADDR_ID=ADDRESS.ADDR_ID
WHERE WRITER_ID=1

update address set notify=0;

update address set notify=0
where addr_id=(select addr_id from todos where todo_id=61)

select addr_id
where 

select * from todos 
where writer_id=2;
