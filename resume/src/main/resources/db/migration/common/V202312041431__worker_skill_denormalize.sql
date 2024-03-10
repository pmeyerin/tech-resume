DROP TABLE IF EXISTS worker_skill_estimate;

CREATE TABLE worker_skill_estimate (
   worker_skill_relation_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
   worker uuid,
   tech_skill_id uuid,
   skill_years_estimate int
);