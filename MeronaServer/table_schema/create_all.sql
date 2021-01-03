-- user

create table users (
	id varchar(50),
	password varchar(50)
);


insert into users values ('admin', '1234');




-- todo

create table todos (
	content varchar(200),
	isDone number(1)
);

insert into todos values ('메로나 사오기', 0);
insert into todos values ('바밤바 사오기', 1);
insert into todos values ('더위사냥 사오기', 1);
insert into todos values ('설레임 사오기', 1);
insert into todos values ('엑셀런트 사오기', 0);
insert into todos values ('찰떡 붕어빵 사오기', 1);

