CREATE TABLE workout_plans (
    id UUID PRIMARY KEY,
    member_id UUID NOT NULL,
    coach_email VARCHAR(120) NOT NULL,
    title VARCHAR(100) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE workout_days (
    id UUID PRIMARY KEY,
    plan_id UUID NOT NULL REFERENCES workout_plans(id),
    day_name VARCHAR(100) NOT NULL
);

CREATE TABLE exercises (
    id UUID PRIMARY KEY,
    day_id UUID NOT NULL REFERENCES workout_days(id),
    name VARCHAR(100) NOT NULL,
    notes TEXT
);

CREATE TABLE exercise_sets (
    id UUID PRIMARY KEY,
    exercise_id UUID NOT NULL REFERENCES exercises(id),
    set_number INT NOT NULL,
    reps INT,
    weight DECIMAL(10,2),
    rpe DECIMAL(3,1),
    tempo VARCHAR(20),
    rest VARCHAR(20)
);
