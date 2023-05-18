CREATE TABLE IF NOT EXISTS "schedule_entry" (
  "username" text,
  "show_id" integer,

  PRIMARY KEY ("username", "show_id")
);
