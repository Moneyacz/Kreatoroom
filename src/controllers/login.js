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

    if (data.google_id.length !== 0) { // if the user is signed in with Google
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
                message: 'User already exists',
              });
            } else {
              // Perform Google authentication and set session token here
              // You can use the existing code for Google authentication from your original question
              let token = req.body.token;
    
              async function verify() {
                const ticket = await client.verifyIdToken({
                  idToken: token,
                  audience: CLIENT_ID,
                });
                const payload = ticket.getPayload();
                const userid = payload['sub'];
              }
              verify()
              .then(() => {
                connection.query(
                  'INSERT INTO user (id_pengguna, token, google_id, email, nama_lengkap) VALUES (?,?,?,?,?)',
                  [id_pengguna, data.token, data.google_id, data.email, data.name],
                  (err) => {
                    if (err) {
                      console.log(err);
                      res.send({
                        status: 400,
                        success: false,
                        message: 'Error occurred while adding user data',
                      });
                    } else {
                      // Set session token cookie
                      res.cookie('session-token', data.token);
                      
                      res.send({
                        status: 200,
                        success: true,
                        message: 'User data added successfully',
                        data: data,
                      });
                    }
                  }
                );
              })
              .catch(console.error);
            }
          }
        );
        connection.release();
      });
    } else { //if the user is not signed in with google
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
