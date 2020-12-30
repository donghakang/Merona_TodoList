create table users (
	id varchar(50),
	password varchar(50)
);


insert into users values ('admin', '1234');


SELECT * FROM USERS 
WHERE id='admin' and password='1234';