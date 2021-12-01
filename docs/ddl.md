# DDL

```sqlite
CREATE TABLE IF NOT EXISTS `user`
(
    `user_id`
        INTEGER
        PRIMARY
            KEY
        AUTOINCREMENT
        NOT
            NULL,
    `created`
        INTEGER
        NOT
            NULL,
    `name`
        TEXT
        NOT
            NULL,
    `email`
        TEXT
        NOT
            NULL,
    `birthday`
        INTEGER
        NOT
            NULL
);

CREATE INDEX IF NOT EXISTS `index_user_name` ON `user` (`name`);

CREATE UNIQUE INDEX IF NOT EXISTS `index_user_email` ON `user` (`email`);

CREATE INDEX IF NOT EXISTS `index_user_created` ON `user` (`created`);

CREATE TABLE IF NOT EXISTS `vaccine`
(
    `vaccine_id`
        INTEGER
        PRIMARY
            KEY
        AUTOINCREMENT
        NOT
            NULL,
    `created`
        INTEGER
        NOT
            NULL,
    `name`
        TEXT
        NOT
            NULL,
    `description`
        TEXT
        NOT
            NULL,
    `user_id`
        INTEGER,
    `frequency`
        INTEGER
        NOT
            NULL,
    `total_number_of_doses`
        INTEGER
        NOT
            NULL,
    `age_range_lower_limit`
        INTEGER
        NOT
            NULL,
    `age_range_upper_limit`
        INTEGER
        NOT
            NULL,
    FOREIGN
        KEY
        (
         `user_id`
            ) REFERENCES `user`
        (
         `user_id`
            ) ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX IF NOT EXISTS `index_vaccine_name` ON `vaccine` (`name`);

CREATE INDEX IF NOT EXISTS `index_vaccine_created` ON `vaccine` (`created`);

CREATE INDEX IF NOT EXISTS `index_vaccine_user_id` ON `vaccine` (`user_id`);

CREATE TABLE IF NOT EXISTS `dose`
(
    `dose_id`
        INTEGER
        PRIMARY
            KEY
        AUTOINCREMENT
        NOT
            NULL,
    `created`
        INTEGER
        NOT
            NULL,
    `vaccine_id`
        INTEGER
        NOT
            NULL,
    `doctor_id`
        INTEGER,
    `name`
        TEXT
        NOT
            NULL,
    `date_administered`
        INTEGER,
    `image`
        TEXT,
    FOREIGN
        KEY
        (
         `vaccine_id`
            ) REFERENCES `vaccine`
        (
         `vaccine_id`
            ) ON UPDATE NO ACTION
        ON DELETE CASCADE,
    FOREIGN KEY
        (
         `doctor_id`
            ) REFERENCES `doctor`
        (
         `doctor_id`
            )
        ON UPDATE NO ACTION
        ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS `index_dose_created` ON `dose` (`created`);

CREATE INDEX IF NOT EXISTS `index_dose_vaccine_id` ON `dose` (`vaccine_id`);

CREATE INDEX IF NOT EXISTS `index_dose_doctor_id` ON `dose` (`doctor_id`);

CREATE TABLE IF NOT EXISTS `doctor`
(
    `doctor_id`
        INTEGER
        PRIMARY
            KEY
        AUTOINCREMENT
        NOT
            NULL,
    `created`
        INTEGER
        NOT
            NULL,
    `name`
        TEXT
        NOT
            NULL
);

CREATE INDEX IF NOT EXISTS `index_doctor_name` ON `doctor` (`name`);

CREATE INDEX IF NOT EXISTS `index_doctor_created` ON `doctor` (`created`);

CREATE VIEW `vaccine_summary` AS
SELECT v.*, d.*
FROM vaccine AS v
         INNER JOIN (SELECT *
                     FROM dose
                     GROUP BY vaccine_id) AS d
                    ON v.vaccine_id = d.vaccine_id;

CREATE VIEW `dose_summary` AS
SELECT v.*, d.*
FROM vaccine AS v
         INNER JOIN (SELECT *
                     FROM dose
                     GROUP BY vaccine_id) AS d
                    ON v.vaccine_id = d.vaccine_id;


```