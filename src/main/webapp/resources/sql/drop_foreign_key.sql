ALTER TABLE billSession
DROP FOREIGN KEY FK_BILLSESSION_SERVICESESSION_ID;

ALTER TABLE fee
DROP FOREIGN KEY FK_FEE_SERVICESESSION_ID;