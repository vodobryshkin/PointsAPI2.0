create table users (
                       id uuid default random_uuid() primary key,
                       username varchar(50) unique not null,
                       email varchar(50) unique not null,
                       password varchar(256) not null,
                       enabled boolean not null,
                       verified boolean not null
);

create table authorities (
                             id uuid default random_uuid() primary key,
                             username varchar(50) references users(username),
                             authority varchar(50)
);

create trigger add_role_user_authority
    after insert on users
    for each row
call "com.vdska.pointsapi2.h2.AddRoleUserTrigger";

