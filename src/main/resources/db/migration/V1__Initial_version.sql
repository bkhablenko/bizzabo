CREATE TABLE IF NOT EXISTS "schedule_entry" (
  "username" text,
  "show_id" integer,

  PRIMARY KEY ("username", "show_id")
);

CREATE TABLE IF NOT EXISTS "watched_episode" (
  "username" text,
  "episode_id" integer,

  PRIMARY KEY ("username", "episode_id")
);
