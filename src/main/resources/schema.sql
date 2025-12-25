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

create table hits (
                      id uuid primary key,
                      user_id uuid not null references users(id),
                      x numeric(38, 2) not null,
                      y numeric(38, 2) not null,
                      r numeric(38, 2) not null,
                      status bool not null,
                      date_of_creation timestamp not null
);

insert into users values (uuid '000aed89-3083-4b86-a3cb-6e0a11dd4348',
                          'dobryak',
                          'vovadobryshkin@gmail.com',
                          '$2a$10$eJKuoB7EcTqBn827KJmRSuTx1Ap5lzEssRWECLEsoma5bU6k7qe/C',
                          true,
                          true);

insert into authorities values ( uuid 'a33acb27-f5bf-407b-87f9-cbd450e903cb',
                                'dobryak',
                                'ROLE_ADMIN');

insert into authorities values ( uuid '000aed89-3083-4b86-a3cb-6e0a11dd4348',
                                 'dobryak',
                                 'ROLE_USER');

create trigger add_role_user_authority
    after insert on users
    for each row
call "com.vdska.pointsapi2.h2.AddRoleUserTrigger";

create trigger update_role_on_verified
    after update on users
    for each row
call "com.vdska.pointsapi2.h2.UpdateRoleUserTrigger";
