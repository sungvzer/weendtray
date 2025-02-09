CREATE TABLE IF NOT EXISTS logs (id INTEGER PRIMARY KEY AUTOINCREMENT, message TEXT, level TEXT, timestamp TIMESTAMP, execution_id TEXT DEFAULT NULL);
CREATE TABLE IF NOT EXISTS `user` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `username` TEXT NOT NULL, `password` TEXT NOT NULL, `name` TEXT NOT NULL, `surname` TEXT NOT NULL, `role` TEXT NOT NULL, `phonenumber` TEXT DEFAULT NULL, "plan" TEXT DEFAULT 'REGULAR', active INTEGER DEFAULT (1));

CREATE UNIQUE INDEX `user_username` ON `user` (`username`);
CREATE UNIQUE INDEX `user_phonenumber` ON `user` (`phonenumber`);