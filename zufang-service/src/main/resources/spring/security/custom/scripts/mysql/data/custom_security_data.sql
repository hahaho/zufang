/*==============================================================*/
/* Table: T_RBAC_USERS                                         */
/*==============================================================*/
insert into T_RBAC_USERS (USER_NAME, PASSWORD, REAL_NAME,
MOBILE, EMAIL, STATUS, CREATED_BY, CREATED_TIME, UPDATED_BY, UPDATED_TIME)
VALUES ('admin','$2a$10$4AIZHNj6cuYDhUvIH/UPAe9hm/D.VOTNyRP1f5Rar1sHHcjgADFyu',
'admin', '', '', '1', 'admin', CURRENT_TIMESTAMP, 'admin', CURRENT_TIMESTAMP);
