CREATE TABLE Basket (
    Name        STRING,
    CreateDate  STRING,
    CreateTime  STRING,
    UserName    STRING,
    ReferenceNo STRING,
    Deleted     STRING,
    ParentName  STRING,
    PRIMARY KEY (UserName,ParentName,Name)
);

CREATE TABLE Project (
    Name        STRING,
    CreateDate  STRING,
    CreateTime  STRING,
    UserName    STRING,
    ReferenceNo STRING,
    Deleted     STRING,
    PRIMARY KEY (UserName,Name)
);

CREATE TABLE Task (
    Name        STRING,
    Description STRING,
    CreateDate  STRING,
    CreateTime  STRING,
    UserName    STRING,
    ReferenceNo STRING,
    Deleted     STRING,
    ParentName  STRING,
    PRIMARY KEY (UserName,ParentName,Name)
);

/*INSERT INTO PROJECT SELECT * FROM PROJECTOLD;
INSERT INTO BASKET SELECT * FROM BASKETOLD;
INSERT INTO TASK SELECT * FROM TASKOLD;*/