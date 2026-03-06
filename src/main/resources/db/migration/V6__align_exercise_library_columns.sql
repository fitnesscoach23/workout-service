ALTER TABLE exercise_library
RENAME COLUMN name TO exercise_name;

ALTER TABLE exercise_library
ADD COLUMN sr_no INT;

ALTER TABLE exercise_library
ADD COLUMN muscle_group VARCHAR(100);

UPDATE exercise_library
SET sr_no = 1
WHERE sr_no IS NULL;

UPDATE exercise_library
SET muscle_group = 'General'
WHERE muscle_group IS NULL;

ALTER TABLE exercise_library
ALTER COLUMN sr_no SET NOT NULL;

ALTER TABLE exercise_library
ALTER COLUMN muscle_group SET NOT NULL;

ALTER TABLE exercise_library
ALTER COLUMN exercise_name SET NOT NULL;

ALTER TABLE exercise_library
DROP COLUMN notes;
