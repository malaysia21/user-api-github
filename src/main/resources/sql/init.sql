CREATE SCHEMA IF NOT EXISTS users;

CREATE TABLE IF NOT EXISTS users.login_req_counter (
  login varchar(250) NOT NULL PRIMARY KEY,
  req_counter bigint NOT NULL
);

CREATE OR REPLACE FUNCTION users.update_req_counter(inlogin character varying, OUT outReqCounter bigint)
 RETURNS bigint
 LANGUAGE plpgsql
AS $function$
DECLARE 
currentReqCounter bigint;

BEGIN
	
	SELECT req_counter INTO currentReqCounter FROM users.login_req_counter WHERE login = inlogin; 
	
	IF(currentReqCounter IS NULL) THEN
		outReqCounter = 1;
		INSERT INTO users.login_req_counter(login, req_counter)
		VALUES (inlogin, outReqCounter);
	ELSE 
		outReqCounter = 1 + currentReqCounter;
		UPDATE users.login_req_counter 
		SET req_counter = outReqCounter
		where login = inlogin;
	END IF;

END;

$function$
;

