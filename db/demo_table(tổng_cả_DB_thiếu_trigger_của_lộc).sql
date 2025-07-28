SELECT * FROM reloop_v2.users;
UPDATE reloop_v2.users
SET email = 'nguyenlehuyhungka@gmail.com'
WHERE user_id = 'CUS0014';
SELECT email FROM reloop_v2.users WHERE user_id = 'CUS0014'
