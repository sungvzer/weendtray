CREATE TABLE wallet (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    balance REAL NOT NULL DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(id)
)