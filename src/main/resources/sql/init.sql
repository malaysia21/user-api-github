CREATE SCHEMA IF NOT EXISTS users;

CREATE TABLE IF NOT EXISTS users.login_request_count (
  login varchar(255) NOT NULL PRIMARY KEY,
  request_count bigint NOT NULL
);

CREATE OR REPLACE FUNCTION users.update_request_count(inlogin character varying, OUT outReqCount bigint)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE
currentReqCount bigint;

BEGIN

	SELECT request_count INTO currentReqCount FROM users.login_request_count WHERE login = inlogin;

	IF(currentReqCount IS NULL) THEN
		outReqCount = 1;
		INSERT INTO users.login_request_count(login, request_count)
		VALUES (inlogin, outReqCount);
	ELSE
		outReqCount = 1 + currentReqCount;
		UPDATE users.login_request_count
		SET request_count = outReqCount
		where login = inlogin;
	END IF;

END;

$function$
;

