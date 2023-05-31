const config = require('../configs/database');
const mysql = require('mysql');
const pool = mysql.createPool(config);

pool.on('error', (err) => {
  console.error(err);
});

module.exports = {
  getMetadata(req, res) {
    pool.getConnection(function (err, connection) {
      if (err) throw err;
      connection.query('SELECT * FROM barang;', function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Barang data retrieved successfully',
            data: results,
          });
        }
      );
      connection.release();
    });
  },
  postMetadata(req, res) {
    const data = {
      id_barang: req.body.id_barang,
      judul_barang: req.body.email,
      tgl_masuk: req.body.password,
      stock: req.body.stock,
      terjual: req.body.terjual,
      rating: req.body.rating,
      jml_komentar: req.body.jml_komentar,
      harga: req.body.harga,
      nama_tipe: req.body.nama_tipe,
      isi_tipe: req.body.isi_tipe,
      kondisi: req.body.kondisi,
      berat_kg: req.body.berat_kg,
      deskripsi: req.body.deskripsi,
      id_toko: req.body.id_toko,
    };
    pool.getConnection(function (err, connection) {
      if (err) throw err;
      connection.query('SELECT id_barang FROM barang;',
      [data.id_barang]
      ,function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Barang data retrieved successfully',
            data: results,
          });
        }
      );
      connection.release();
    });
  },
};
