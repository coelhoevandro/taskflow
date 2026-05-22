-- Hibernate 6 + PostgreSQL não fazem cast automático de VARCHAR para tipos ENUM nativos.
-- Converter para VARCHAR elimina o problema sem perda funcional — a validação fica no Java.

ALTER TABLE tasks
    ALTER COLUMN status          TYPE VARCHAR(20),
    ALTER COLUMN priority        TYPE VARCHAR(20),
    ALTER COLUMN assignment_status TYPE VARCHAR(20);

DROP TYPE IF EXISTS task_status CASCADE;
DROP TYPE IF EXISTS task_priority CASCADE;
DROP TYPE IF EXISTS assignment_status CASCADE;
