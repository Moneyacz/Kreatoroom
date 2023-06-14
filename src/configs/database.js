require('dotenv').config();

module.exports = {
    multipleStatements  : true,
    host                : process.env.DB_HOST,
    prompt              : process.env.DB_PORT,
    user                : process.env.DB_USER,
    password            : process.env.DB_PASSWORD,
    database            : process.env.DB_NAME
};