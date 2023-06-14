const config = require('../configs/database');
const path = require('path');
const mysql = require('mysql');
const pool = mysql.createPool(config);

module.exports = {
    getFillDataPage(req, res) {
      const fillDataPage = path.join(__dirname, '../views/fill-data');
      res.render(fillDataPage);
    },
    postRegisterData(req, res) {
      const data = {
        no_hp: req.body.no_hp,
        nama_lengkap: req.body.nama_lengkap,
        tgl_lahir: req.body.tgl_lahir,
        jenis_kelamin: req.body.jenis_kelamin,
      };

      pool.getConnection(async function (err, connection) {
        connection.query(

        );
        connection.release();
      });


      if (!data.email && !data.no_hp) {
        return res.send({
          status: 400,
          success: false,
          message: 'Email or Nomor HP data should be filled!',
        });
      }
      if (
        !data.password ||
        !data.nama_lengkap
      ) {
        return res.send({
          status: 400,
          success: false,
          message: 'Data cannot be empty!',
        });
      }
      const userId = nanoid(16);
      pool.getConnection(async function (err, connection) {
        connection.query(
          'SELECT email FROM user WHERE email = ?',
          [data.email],
          async function (err, results) {
            if (err) throw err;
            if (results.length > 0) {
              res.send({
                status: 400,
                success: true,
                message: 'User sudah ada silahkan menuju halaman Login',
              });
              return;
            } else {
              await bcrypt.genSalt(10, (err, salt) => {
                bcrypt.hash(data.password, salt, (err, hash) => {
                  connection.query(
                    'INSERT INTO user (id_pengguna, no_hp, email, password, nama_lengkap) VALUES (?,?,?,?,?)',
                    [
                      userId,
                      data.no_hp,
                      data.email,
                      hash,
                      data.nama_lengkap,
                    ],
                    function (error, results) {
                      if (error) throw error;
                      res.send({
                        status: 200,
                        success: true,
                        message: 'User successfully registered!',
                        test: hash,
                        test2: data.password,
                      });
                    }
                  );
                });
              });
            }
          }
        );
        connection.release();
      });
    },
}