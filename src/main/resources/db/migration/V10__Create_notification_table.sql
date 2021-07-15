create table notification
(
	id bigint auto_increment,
	notifier bigint not null,
	receiver bigint not null,
	status int not null,
	gmt_create bigint null,
	content varchar(1024) null,
	type_name varchar(100) null,
	constraint table_name_pk
		primary key (id)
);