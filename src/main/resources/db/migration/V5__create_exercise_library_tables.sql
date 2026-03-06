CREATE TABLE exercise_library (
    id UUID PRIMARY KEY,
    coach_email VARCHAR(120) NOT NULL,
    name VARCHAR(100) NOT NULL,
    notes TEXT,
    video_url TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE member_exercise_assignments (
    id UUID PRIMARY KEY,
    library_exercise_id UUID NOT NULL REFERENCES exercise_library(id),
    member_id UUID NOT NULL,
    coach_email VARCHAR(120) NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (library_exercise_id, member_id, coach_email)
);
