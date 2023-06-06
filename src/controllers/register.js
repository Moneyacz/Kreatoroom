const config = require('../configs/database');
const mysql = require('mysql');
const pool = mysql.createPool(config);
const path = require('path');
const { nanoid } = require('nanoid');

pool.on('error', (err) => {
  console.error(err);
});

module.exports = {
  getRegisterPage(req, res) {
    const registerPagePath = path.join(__dirname, '../views/register-page');
    res.render(registerPagePath);
  },
  postRegisterData(req, res) {
    const data = {
      email: req.body.email,
      no_hp: req.body.no_hp,
      password: req.body.password,
      nama_lengkap: req.body.nama_lengkap,
      tanggal_lahir: req.body.tanggal_lahir,
      jenis_kelamin: req.body.jenis_kelamin,
    };
    if (!data.email && !data.no_hp) {
      return res.send({
        status: 400,
        success: false,
        message: 'Email atau Nomor HP harus diisi!',
      });
    }
    if (
      !data.password ||
      !data.nama_lengkap ||
      !data.tanggal_lahir ||
      !data.jenis_kelamin
    ) {
      return res.send({
        status: 400,
        success: false,
        message: 'Data tidak boleh kosong!',
      });
    }

    const userId = nanoid(16);
    //enkripsi password blabla bla

    pool.getConnection(function (err, connection) {
      if (err) throw err;
      connection.query(
        'INSERT INTO user (id_pengguna, no_hp, email, password, nama_lengkap, tgl_lahir, jenis_kelamin) VALUES (?,?,?,?,?,?,?)',
        [
          userId,
          data.no_hp,
          data.email,
          data.password,
          data.nama_lengkap,
          data.tanggal_lahir,
          data.jenis_kelamin,
        ],
        function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Berhasil register!',
          });
        }
      );
      connection.release();
    });
  },
};
