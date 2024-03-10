DROP TABLE IF EXISTS tech_and_skills;

CREATE TABLE tech_and_skills (
   tech_skill_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
   tech_skill_name VARCHAR(255)
);

DROP TABLE IF EXISTS tech_and_skills_relation;

CREATE TABLE tech_and_skills_relation (
    tech_skill_relation_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    tech_skill_id uuid,
    relation_id uuid,
    relation_type int
);
