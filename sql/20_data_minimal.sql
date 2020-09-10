delete
from verification_tokens;
delete
from soulpatch_ratings;
delete
from spfiles;
delete
from soulpatches;
delete
from user_role;
delete
from app_role;
delete
from app_user;

insert into app_role (id, role_name)
values (nextval('hibernate_sequence'), 'ADMIN');

insert into app_role (id, role_name)
values (nextval('hibernate_sequence'), 'USER');
