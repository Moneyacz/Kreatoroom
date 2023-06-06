const config = require('../configs/database');
const path = require('path');
const mysql = require('mysql');
const pool = mysql.createPool(config);

pool.on('error', (err) => {
  console.error(err);
});

module.exports = {
  getLoginPage(req, res) {
    const loginPagePath = path.join(__dirname, '../views/login-page');
    res.render(loginPagePath);
  },
  postLoginData(req, res) {
    const data = {
      email: req.body.email,
      userid: req.body.userid,
      name: req.body.name,
    };
    // if (!data.email || !data.password) {
    //   return res.send({
    //     status: 400,
    //     success: false,
    //     message: 'Email dan password harus diisi!',
    //   });
    // }
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
              message: 'User alredy exists',
            });
          } else {
            connection.query(
              'INSERT INTO user (id_pengguna, email, nama_lengkap) VALUES (?,?,?)',
              [data.userid, data.email, data.name]
            );
            res.send({
              message: 'User berhasil ditambahkan',
              data: data,
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
