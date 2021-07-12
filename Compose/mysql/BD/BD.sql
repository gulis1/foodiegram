drop trigger if exists ValoracioMediaD;
drop trigger if exists ValoracioMediaI;
drop trigger if exists ValoracioMediaU;
drop event if exists borrarCosasExpiradas;
drop trigger if exists updateOnUnsubscribe;
drop trigger if exists desbaneo;
drop trigger if exists changeVIP;
drop trigger if exists deleteNumPubli;
drop trigger if exists insertNumPubli;
drop trigger if exists insertNumVal;
drop trigger if exists insertNumValPubliUser;
drop trigger if exists borrarusuariotoken;




drop table if exists follow;
drop table if exists comment;
drop table if exists message;
drop table if exists sponsor;
drop table if exists restaurant;
drop table if exists rating;
drop table if exists post;
drop table if exists verifytoken;
drop table if exists numvalpubli;
drop table if exists ban;
drop table if exists user;

CREATE TABlE user (
    userId INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL,
    passwd VARCHAR(256) NOT NULL,
    email VARCHAR(256) NOT NULL,
    image VARCHAR(256),
  	enabled boolean NOT NULL,
  	role ENUM('ROLE_ADMIN', 'ROLE_MOD', 'ROLE_USER', 'ROLE_COL'),

    PRIMARY KEY (userId),
    UNIQUE (email),
  	UNIQUE (name)
);


CREATE TABLE post (
    postId INT NOT NULL AUTO_INCREMENT,
    user INT NOT NULL,
  	title VARCHAR(20) NOT NULL,
    text VARCHAR(1000),
    image VARCHAR(256),
    day DATE,
  	country VARCHAR(256),
  	city VARCHAR(256),
  	avg Float,
  	numRatings Int,
    PRIMARY KEY (postId),
    FOREIGN KEY (user) REFERENCES user(userId) ON DELETE CASCADE
);

CREATE TABLE comment (
    commentId INT NOT NULL AUTO_INCREMENT,
    post INT NOT NULL,
    user INT NOT NULL,
    text VARCHAR(256) NOT NULL,
    PRIMARY KEY (commentId),
    FOREIGN KEY (post) REFERENCES post(postId) ON DELETE CASCADE,
    FOREIGN KEY (user) REFERENCES user(userId) ON DELETE CASCADE
);

CREATE TABLE rating (
    post INT NOT NULL,
    user INT NOT NULL,
    score INT NOT NULL,
    PRIMARY KEY (post, user),
    FOREIGN KEY (post) REFERENCES post(postId) ON DELETE CASCADE,
    FOREIGN KEY (user) REFERENCES user(userId)
);


CREATE TABLE message (
    messageId INT NOT NULL AUTO_INCREMENT,
    sender INT NOT NULL,
    receiver INT NOT NULL,
    Text VARCHAR(256),
    PRIMARY KEY (messageId),
    FOREIGN KEY (sender) REFERENCES user(userId) ON DELETE CASCADE,
    FOREIGN KEY (receiver) REFERENCES user(userId) ON DELETE CASCADE
);

CREATE TABLE follow (
    follower INT NOT NULL,
    followed INT NOT NULL,
    PRIMARY KEY (follower, followed),
    FOREIGN KEY (follower) REFERENCES user(userId) ON DELETE CASCADE,
    FOREIGN KEY (followed) REFERENCES user(userId) ON DELETE CASCADE
);

CREATE TABLE restaurant (
    restaurantId INT NOT NULL,
    type VARCHAR(256) NOT NULL,
    country VARCHAR(256) NOT NULL,
    city VARCHAR(256) NOT NULL,
	street VARCHAR(256) NOT NULL,
  	owner int NOT NULL,
    vip BOOLEAN NOT NULL,
    PRIMARY KEY (restaurantId),
    FOREIGN KEY (owner) REFERENCES user(userId) ON DELETE CASCADE
);

CREATE TABLE verifytoken (
    verifytokenId INT NOT NULL AUTO_INCREMENT,
    email VARCHAR(256) NOT NULL,
  	token INT NOT NULL,
  	expiredate datetime,
    PRIMARY KEY (verifytokenId),
    FOREIGN KEY (email) REFERENCES user(email) ON DELETE CASCADE,
  	UNIQUE(token),
  	UNIQUE(email)

);


create table numvalpubli (
    user INT NOT NULL,
    numval INT NOT NULL,
    numpubli INT NOT NULL,
    PRIMARY KEY (user),
    FOREIGN KEY (user) REFERENCES user(userId) ON DELETE CASCADE
);


CREATE TABLE ban(
    banId INT NOT NULL,
    expiredate datetime,
    PRIMARY KEY (banId),
    FOREIGN KEY (banId) REFERENCES user(userId) ON DELETE CASCADE

);

CREATE TABLE sponsor (
    sponsorId INT NOT NULL,
    endtime DATE NOT NULL,
    money FLOAT DEFAULT 0.0,
  	restaurant INT NOT NULL,

    PRIMARY KEY (sponsorId),
    FOREIGN KEY (restaurant) REFERENCES restaurant(restaurantId) ON DELETE CASCADE
);

DELIMITER //

CREATE TRIGGER ValoracioMediaI
After Insert on rating For Each ROW

Begin

    declare mediaO double;
    declare numO double;
    declare new_media double;
    declare new_num double;
    declare suma double;

    SELECT avg into mediaO from post where (new.post = post.postId);
    SELECT numRatings into numO from post where (new.post = post.postId);

    set suma := (mediaO*numO) + new.score;
    set new_num := numO + 1;
    set new_media := suma / new_num;

    update  post
    set avg = new_media where new.post=post.postId;

    update  post
    set numRatings = new_num where new.post = post.postId;

end;

CREATE TRIGGER ValoracioMediaD
After delete on rating For Each ROW

Begin

    declare mediaO double;
    declare numO double;
    declare new_media double;
    declare new_num double;
    declare suma double;

    SELECT avg into mediaO From post where (old.post = post.postId);
    SELECT numRatings into numO From post where (old.post = post.postId);

    set suma := (mediaO*numO)-old.score;
    set new_num := numO-1;
    set new_media := suma/new_num;

    update post
    set avg = new_media where old.post = post.postId;

    update post
    set numRatings = new_num where old.post = post.postId;

end;

CREATE TRIGGER ValoracioMediaU
After update on rating For Each ROW

Begin

    declare mediaO double;
    declare numO double;
    declare new_media double;
    declare suma double;

    SELECT avg into mediaO From post where (old.post = post.postId);
    SELECT numRatings into numO From post where (old.post = post.postId);

    set suma :=(mediaO*numO)+(new.score-old.score);
    set new_media :=suma/numO;

    update  post
    set avg = new_media where old.post = post.postId;

end;


CREATE EVENT borrarCosasExpiradas
  ON SCHEDULE EVERY 1 MINUTE
  DO
  BEGIN
    delete from verifytoken where NOW() > expiredate;
    delete from ban where NOW() > expiredate;
    delete from sponsor where NOW() > endtime;
END;


CREATE TRIGGER borrarusuariotoken
After delete on verifytoken For Each ROW

Begin

    delete from user where OLD.email = email AND enabled = false;

end;


create trigger updateOnUnsubscribe
before delete on user
for each row
begin
	delete from follow where (follower = OLD.userId) OR (followed = OLD.userId);
    delete from restaurant where restaurantId = OLD.userId;
    delete from comment where user = OLD.userId;
    delete from message where (sender = OLD.userId) OR (receiver = OLD.userId);
    delete from post where user = OLD.userId;
    delete from rating where user = OLD.userId;
end;


create trigger insertNumValPubliUser
after insert on user for each row
begin
   insert into numvalpubli (user, numval, numpubli) values (new.userId, 0, 0);
end;


create trigger insertNumVal
after insert on rating for each row
begin
    declare idu int;
    select post.user into idu from post where post.postId = new.post;
    update numvalpubli
    set numval = numval+1 where idu = user;
end;


create trigger insertNumPubli
after insert on post for each row
begin
    update numvalpubli
    set numpubli = numpubli+1 where new.user = user;
end;


create trigger deleteNumPubli
after delete on post for each row
begin
    declare numeroVal int;
    update numvalpubli
    set numpubli = numpubli-1 where old.user = user;
    select count(*) into numeroVal from rating where post = old.postId;
    update numvalpubli
    set numval = numval-numVal where old.user = user;
end;


create trigger desbaneo
    after delete on ban for each row

    begin
    update user
    set enabled=true where OLD.banId=user.userId;
end;

create trigger changeVIP
after delete on sponsor for each row
begin
    update restaurant
    set vip = false where restaurant.restaurantId = old.restaurant;
end;
