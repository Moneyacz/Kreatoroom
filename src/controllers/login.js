const config = require('../configs/database');
const path = require('path');
const mysql = require('mysql');
const { throws } = require('assert');
const pool = mysql.createPool(config);
const { nanoid } = require('nanoid');
const bcrypt = require('bcrypt');

pool.on('error', (err) => {
  console.error(err);
});
module.exports = {
  getLoginPage(req, res) {
    const loginPagePath = path.join(__dirname, '../views/login-page');
    res.render(loginPagePath);
  },
  postLoginData(req, res) {
    const id_pengguna = nanoid(16);
    const data = {
      email: req.body.email,
      token: req.body.token || '',
      google_id: req.body.google_id || '',
      password: req.body.password || '',
      name: req.body.name || '',
    };

    if (data.google_id.length!=0) { //if the user is not google user
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
                'INSERT INTO user (id_pengguna, token, google_id, email, nama_lengkap) VALUES (?,?,?,?,?)',
                [id_pengguna ,data.token, data.google_id, data.email, data.name]
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
    } else {
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
      
            if (results.length === 0) {
              res.send({
                status: 400,
                success: false,
                message: 'Invalid email',
              });
              return;
            }
            const dbPass = results[0].password;
            const isPasswordValid = await bcrypt.compare(data.password, dbPass);
            if (!isPasswordValid) {
              res.send({
                status: 400,
                success: false,
                message: 'Invalid password',
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
        connection.release();
      });
    }
  },
};
