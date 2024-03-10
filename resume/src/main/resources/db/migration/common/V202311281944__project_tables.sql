DROP TABLE IF EXISTS project;

CREATE TABLE project (
   project_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
   project_name VARCHAR(255),
   project_description VARCHAR(2048),
   project_start DATE,
   project_end DATE,
   project_relation uuid,
   project_relation_type int
);