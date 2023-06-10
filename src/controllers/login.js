const config = require('../configs/database');
const path = require('path');
const mysql = require('mysql');
const { throws } = require('assert');
const pool = mysql.createPool(config);
const bcrypt = require('bcrypt');

pool.on('error', (err) => {
  console.error(err);
});

module.exports = {
  getLoginPage(req, res) {
    const loginPagePath = path.join(__dirname, '../views/login-page');
    res.render(loginPagePath);
  },
  postLoginGoogle(req, res) {
    const data = {
      email: req.body.email,
      userid: req.body.userid,
      name: req.body.name,
    };
    pool.getConnection(function (err, connection) {
      if (err) throw err;
      connection.query(
        'SELECT * FROM user WHERE email = ?',
        [data.email],
        (err, rows) => {
          if (err) {
            console.log(err);
          } else if (rows.length > 0) {
            res.send({
              message: 'User already exist',
            });
          } else {
            connection.query(
              'INSERT INTO user (id_pengguna, email, nama_lengkap) VALUES (?,?,?)',
              [data.userid, data.email, data.name]
            );
            res.send({
              message: 'User data added successfully',
              data: data,
            });
          }
        }
      );
      connection.release();
    });
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

    pool.getConnection(async function (err, connection) {
      connection.query(
        'SELECT password FROM user WHERE email = ?',
        [data.email],
        async function (error, results) {
          if (error) throw error;
          const dbPass = await results[0].password;
          const isPasswordValid = await bcrypt.compare(data.password, dbPass);
          if (isPasswordValid == false) {
            res.send({
              status: 400,
              success: false,
              message: 'Invalid inserted password',
              test: dbPass,
              ress: results[0].password,
              test2: typeof isPasswordValid,
              test3: data.password,
            });
          } else {
            res.send({
              status: 200,
              success: true,
              message: 'Login success',
            });
          }
        }
      );
      // connection.query(
      //   'SELECT * FROM user WHERE email = ?',
      //   [data.email],
      //   function (error, results) {
      //     if (error) throw error;
      //     // if (results.length === 0 || results[0].password !== data.password) {
      //     if (results.length === 0) {
      //       return res
      //         .status(401)
      //         .json({ message: 'Invalid username or password', results });
      //     }
      //     res.send({
      //       status: 200,
      //       success: true,
      //       message: 'Berhasil login!',
      //     });
      //   }
      // );
      connection.release();
    });
  },
};
