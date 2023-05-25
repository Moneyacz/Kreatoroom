const config = require('../configs/database');
const path = require('path');
const mysql = require('mysql');
const pool = mysql.createPool(config);

pool.on('error', (err) => {
  console.error(err);
});

module.exports = {
  //this is a login page routing example, needs further research
  getLoginPage(req, res) {
    const loginPagePath = path.join(__dirname, '../views/login-page.html');
    res.sendFile(loginPagePath);
  },
  postLoginData(req, res) {
    const data = {
      email: req.body.email,
      password: req.body.password,
    };
    if (!data.email || !data.password) {
      return res.send({
        status: 400,
        success: false,
        message: 'Email dan password harus diisi!',
      });
    }
    pool.getConnection(function (err, connection) {
    if (err) throw err;
      connection.query(
        'SELECT * FROM user WHERE email = ?',
        [data.email],
        function (error, results) {
          if (error) throw error;
          if (results.length === 0 || results[0].password !== data.password) {
            return res
              .status(401)
              .json({ message: 'Invalid username or password' });
          }
          res.send({
            status: 200,
            success: true,
            message: 'Berhasil login!',
          });
        }
      );
      connection.release();
    });
  },
};
