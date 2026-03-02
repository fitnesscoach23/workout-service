CREATE TABLE workout_assignments (
    id UUID PRIMARY KEY,
    plan_id UUID NOT NULL,
    member_id UUID NOT NULL,
    coach_email VARCHAR(120) NOT NULL,
    active BOOLEAN NOT NULL,
    assigned_at TIMESTAMP NOT NULL
);
