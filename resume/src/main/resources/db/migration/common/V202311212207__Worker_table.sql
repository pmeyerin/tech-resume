CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DROP TABLE IF EXISTS worker;

CREATE TABLE IF NOT EXISTS worker (
    worker_id uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    worker_name VARCHAR(255),
    worker_phone VARCHAR(255),
    worker_email VARCHAR(255)
);