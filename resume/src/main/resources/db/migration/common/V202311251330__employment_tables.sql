DROP TABLE IF EXISTS employment;

CREATE TABLE employment (
   employment_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
   employment_name VARCHAR(255),
   employment_description VARCHAR(2048),
   employment_start DATE,
   employment_end DATE,
   worker uuid
);