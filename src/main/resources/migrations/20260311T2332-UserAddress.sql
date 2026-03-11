CREATE TABLE IF NOT EXISTS user_address (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    state VARCHAR(255) NOT NULL,
    postal_code VARCHAR(20) NOT NULL,
    country VARCHAR(255) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
-- For each user, add a default address
INSERT INTO user_address (
        user_id,
        address,
        city,
        state,
        postal_code,
        country
    )
SELECT id,
    'INDIRIZZO MANCANTE',
    'INDIRIZZO MANCANTE',
    'INDIRIZZO MANCANTE',
    'INDIRIZZO MANCANTE',
    'INDIRIZZO MANCANTE'
FROM `user`
WHERE id NOT IN (
        SELECT user_id
        FROM user_address
    )
    AND `user`.role = 'USER';
