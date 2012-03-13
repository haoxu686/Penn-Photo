DROP TABLE IF EXISTS user;
CREATE TABLE user ( uid integer NOT NULL AUTO_INCREMENT,
                    firstname varchar(255) NOT NULL,
                    lastname varchar(255) NOT NULL,
                    email varchar(255) default NULL,
                    dob date default NULL,
                    gender varchar(5) default NULL,
                    address varchar(255) default NULL,
                    type varchar(5) NOT NULL,
                    password varchar(255),
                    PRIMARY KEY (uid));

DROP TABLE IF EXISTS professor;
CREATE TABLE professor ( pid varchar(255) NOT NULL,
                         uid integer NOT NULL,
                         researchArea varchar(255) default NULL,
                         office varchar(255) default NULL,
                         PRIMARY KEY (pid),
                         FOREIGN KEY (uid) REFERENCES user (uid));

DROP TABLE IF EXISTS student;
CREATE TABLE student ( sid varchar(255) NOT NULL,
                       uid integer NOT NULL,
                       major varchar(255) NOT NULL,
                       gpa float default NULL,
                       advisor varchar(255) NOT NULL,
                       yearsAdvised integer NOT NULL,
                       PRIMARY KEY (sid),
                       FOREIGN KEY (uid) REFERENCES user (uid),
                       FOREIGN KEY (advisor) REFERENCES professor (pid));

DROP TABLE IF EXISTS institution;
CREATE TABLE institution ( insName varchar(255) NOT NULL,
                           uid integer NOT NULL,
                           PRIMARY KEY (insName, uid),
                           FOREIGN KEY (uid) REFERENCES user (uid));

DROP TABLE IF EXISTS interest;
CREATE TABLE interest ( uid integer NOT NULL,
                        interest varchar(255) NOT NULL,
                        PRIMARY KEY (uid, interest),
                        FOREIGN KEY (uid) REFERENCES user (uid));

DROP TABLE IF EXISTS circle;
CREATE TABLE circle ( cid integer NOT NULL AUTO_INCREMENT,
                      uid integer NOT NULL,
                      name varchar(255) NOT NULL,
                      PRIMARY KEY (cid),
                      FOREIGN KEY (uid) REFERENCES user (uid));

DROP TABLE IF EXISTS friend;
CREATE TABLE friend ( cid integer NOT NULL,
                      uid integer NOT NULL,
                      PRIMARY KEY (cid, uid),
                      FOREIGN KEY (cid) REFERENCES circle,
                      FOREIGN KEY (uid) REFERENCES user (uid));

DROP TABLE IF EXISTS photo;
CREATE TABLE photo ( photoid integer NOT NULL AUTO_INCREMENT,
                     uid integer NOT NULL,
                     url varchar(1023) NOT NULL,
                     uploadTime timestamp DEFAULT NOW(),
                     PRIMARY KEY (photoid),
                     FOREIGN KEY (uid) REFERENCES user (uid));

DROP TABLE IF EXISTS rating;
CREATE TABLE rating ( photoid integer NOT NULL,
                      uid integer NOT NULL,
                      score float NOT NULL,
                      PRIMARY KEY (photoid, uid),
                      FOREIGN KEY (photoid) REFERENCES photo (photoid),
                      FOREIGN KEY (uid) REFERENCES user (uid));

DROP TABLE IF EXISTS tag;
CREATE TABLE tag ( photoid integer NOT NULL,
                   tag varchar(255) NOT NULL,
                   PRIMARY KEY (photoid, tag),
                   FOREIGN KEY (photoid) REFERENCES photo (photoid));

DROP TABLE IF EXISTS visibility;
CREATE TABLE visibility ( photoid integer NOT NULL,
                          uid integer NOT NULL,
                          PRIMARY KEY (photoid, uid),
                          FOREIGN KEY (photoid) REFERENCES photo (photoid),
                          FOREIGN KEY (uid) REFERENCES user (uid));
