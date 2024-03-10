DROP TABLE IF EXISTS education;

CREATE TABLE education (
   education_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
   institution_name VARCHAR(255),
   program VARCHAR(255),
   graduation_period DATE,
   education_type UUID,
   worker uuid
);

DROP TABLE IF EXISTS education_type;

CREATE TABLE education_type (
    education_type_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    education_type VARCHAR(255)
);

INSERT INTO education_type(education_type_id, education_type)
VALUES ('d40109cf-967c-45d8-a0eb-f3fbbd8f8471', 'undergraduate');

INSERT INTO education_type(education_type_id, education_type)
VALUES ('38f8028b-807e-40ad-b0aa-4569a53f0102', 'certification');

INSERT INTO education_type(education_type_id, education_type)
VALUES ('bf80431a-871c-4d6f-bf3c-a7474cfb8bab', 'graduate');