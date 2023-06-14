const config = require('../configs/database');
const mysql = require('mysql');
const pool = mysql.createPool(config);
const path = require('path');
const { nanoid } = require('nanoid');
const { connect } = require('http2');

pool.on('error', (err) => {
  console.error(err);
});

module.exports = {
  getTokoPage(req, res) {
    const registerPagePath = path.join(__dirname, '../views/toko-page');
    // res.render(registerPagePath);
    pool.getConnection(function (err, connection) {
      connection.query(
        'SELECT * FROM toko ORDER BY nama_toko',
        function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Toko data retrieved successfully',
            data: results,
          });
        }
      );
      connection.release();
    });
  },
  postTokoData(req, res) {
    const data = {
      nama_toko: req.body.nama_toko,
      alamat: req.body.alamat,
      jam_buka: req.body.jam_buka,
      no_hp: req.body.no_hp,
    };
    if (!data.nama_toko || !data.alamat || !data.jam_buka || !data.no_hp) {
      return res.send({
        status: 400,
        success: false,
        message: 'Data cannot be empty!',
      });
    }
    const tokoId = nanoid(16);
    pool.getConnection(function (err, connection) {
      connection.query(
        'INSERT INTO toko (id_toko, nama_toko, alamat_toko, jam_buka, rating_toko, no_hp_toko) VALUES (?,?,?,?,?,?);',
        [tokoId, data.nama_toko, data.alamat, data.jam_buka, 5.0, data.no_hp],
        function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Toko successfully registered!',
            test: results,
          });
        }
      );
      connection.release();
    });
  },
  getBarangToko(req, res) {
    const data = {
      id_toko: req.body.id_toko,
    };
    pool.getConnection(function (error, connection) {
      connection.query(
        'SELECT id_toko FROM toko WHERE id_toko = ?',
        [data.id_toko],
        function (err, results) {
          if (err) throw err;
          if (results.length == 0) {
            res.send({
              status: 404,
              success: false,
              message: 'Toko not found',
              data: data.id_toko,
            });
            return;
          } else {
            connection.query(
              'SELECT * FROM barang WHERE id_toko = ?',
              [data.id_toko],
              function (err, results) {
                if (err) throw err;
                res.send({
                  status: 200,
                  success: true,
                  message: 'Data Barang From Toko',
                  data: results,
                });
              }
            );
          }
        }
      );
      connection.release();
    });
  },
  postBarangToko(req, res) {
    const data = {
      judul_barang: req.body.judul_barang,
      tgl_masuk: req.body.tgl_masuk,
      stock: req.body.stock,
      harga: req.body.harga,
      nama_tipe: req.body.nama_tipe,
      isi_tipe: req.body.isi_tipe,
      berat_kg: req.body.berat_kg,
      deskripsi: req.body.deskripsi,
      id_toko: req.body.id_toko,
    };

    if (
      !data.judul_barang ||
      !data.tgl_masuk ||
      !data.stock ||
      !data.harga ||
      !data.nama_tipe ||
      !data.isi_tipe ||
      !data.berat_kg ||
      !data.deskripsi ||
      !data.id_toko
    ) {
      return res.send({
        status: 400,
        success: false,
        message: 'Data cannot be empty!',
      });
    }

    const barangId = nanoid(16);
    pool.getConnection(function (err, connection) {
      connection.query(
        'SELECT id_toko FROM toko WHERE id_toko = ?',
        [data.id_toko],
        function (err, results) {
          if (err) throw err;
          if (results.length == 0) {
            res.send({
              status: 404,
              success: false,
              message: 'Toko not found',
              data: data.id_toko,
            });
            return;
          } else {
            connection.query(
              'INSERT INTO barang(id_barang, judul_barang, tgl_masuk, stock, terjual, rating, jml_komentar, harga, nama_tipe, isi_tipe, kondisi, berat_kg, deskripsi, id_toko) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)',
              [
                barangId,
                data.judul_barang,
                data.tgl_masuk,
                data.stock,
                0, //terjual
                0, //rating
                0, //jml komen
                data.harga,
                data.nama_tipe,
                data.isi_tipe,
                1, //kondisi
                data.berat_kg,
                data.deskripsi,
                data.id_toko,
              ],
              function (err, results) {
                if (err) throw err;
                res.send({
                  status: 200,
                  success: true,
                  message: 'Barang success to insert',
                  data: results,
                });
              }
            );
          }
        }
      );
      connection.release();
    });
  },
};
