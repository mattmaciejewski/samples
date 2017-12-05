CREATE TABLE FUEL_CYCLE_DISCUSSION_BOARD  (
    CommentIdNum BIGINT NOT NULL,
    ParentIdNum VARCHAR(20),
    CommentLevel SMALLINT,
    LastModifiedDtm    DATETIME              not null default current_timestamp,
    LastModifiedByNam    VARCHAR(25)            not null,
    RowCreateDtm       DATETIME              not null default current_timestamp,
    RowCreatedByNam   VARCHAR(255)           not null,
    UsernameTxt       VARCHAR(40),
    MemberEmailAddrNam		VARCHAR(250),
    UserTypeTxt		VARCHAR(30),
    MessageTxt		VARCHAR(5000),
    Votes			SMALLINT,
    AttachmentsCount SMALLINT,
    AttachmentLinks VARCHAR(500),
    PostDtm DATETIME,
    ProcessStatusInd SMALLINT default 0,
    StepTxt VARCHAR(250)
);