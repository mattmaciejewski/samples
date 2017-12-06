DROP TABLE LIVECHAT IF EXISTS;

CREATE TABLE FUEL_CYCLE_CHAT  (
    PostIdNum BIGINT NOT NULL IDENTITY(1, 1),
    LastModifiedDtm    DATETIME              not null default current_timestamp,
    LastModifiedByNam    VARCHAR(25)            not null,
    RowCreateDtm       DATETIME              not null default current_timestamp,
    RowCreatedByNam   VARCHAR(255)           not null,
    UsernameTxt       VARCHAR(40),
    MemberEmailAddrNam		VARCHAR(250),
    UserTypeTxt		VARCHAR(30),
    MessageTxt		VARCHAR(5000),
    LikeCountNum	SMALLINT,
    StepTxt			VARCHAR(100),
    PostDtm DATETIME,
    ProcessStatusInd SMALLINT default 0
);


CREATE TABLE LIVECHAT  (
    POSTIDNUM BIGINT NOT NULL IDENTITY(1, 1),
    LASTMODIFIEDDTM    DATETIME              not null default current_timestamp,
    LASTMODIFIEDBYNAM    VARCHAR(25)            not null,
    ROWCREATEDTM       DATETIME              not null default current_timestamp,
    ROWCREATEDBYNAM   VARCHAR(255)           not null,
    USERNAMETXT       VARCHAR(40),
    MEMBEREMAILADDRNAM		VARCHAR(250),
    USERTYPETXT		VARCHAR(30),
    MESSAGETXT		VARCHAR(5000),
    LIKECOUNTNUM	SMALLINT,
    STEPTXT			VARCHAR(100),
    POSTDTM DATETIME,
    PROCESSSTATUSIND SMALLINT default 0
);