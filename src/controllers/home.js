const config = require('../configs/database');
const mysql = require('mysql');
const path = require('path');
const pool = mysql.createPool(config);

pool.on('error', (err) => {
  console.error(err);
});

module.exports = {
  getHomePage(req, res) {
    const homePagePath = path.join(__dirname, '../views/home-page');
    res.render(homePagePath);
  },
  getBarangData(req, res) {
    pool.getConnection(function (err, connection) {
      let count = 0;
      let page = parseInt(req.params.page);
      if (page > count) {
        count = page;
      }
      if (err) throw err;
      connection.query(
        'SELECT * FROM barang ORDER BY judul_barang LIMIT ?, 20;',
        [count],
        function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Barang data retrieved successfully',
            data: results,
            test: `page: ${page}`,
          });
        }
      );
      connection.release();
    });
  },
  // barang spesifik
  getBarangByName(req, res) {
    const barangName = '%' + req.params.judulbarang + '%';
    pool.getConnection(function (err, connection) {
      if (err) throw err;
      connection.query(
        'SELECT * FROM barang WHERE judul_barang LIKE ? ORDER BY judul_barang ',
        [barangName],
        function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Barang specific name retrieved successfully',
            data: results,
            test: `${barangName}`,
          });
        }
      );
      connection.release();
    });
  },
  // bundel all
  getBundle(req, res) {
    pool.getConnection(function (err, connection) {
      if (err) throw err;
      connection.query('SELECT * FROM bundle ', function (error, results) {
        if (error) throw error;
        res.send({
          status: 200,
          success: true,
          message: 'Bundle data retrieved successfully',
          data: results,
        });
      });
      connection.release();
    });
  },
  // bundel spesifik
  getBundleById(req, res) {
    const bundleId = req.params.idBundle + '%';
    pool.getConnection(function (err, connection) {
      if (err) throw err;
      connection.query(
        'SELECT * FROM bundle WHERE id_bundle Like ? ',
        [bundleId],
        function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Bundle data specific retrieved successfully',
            data: results,
          });
        }
      );
      connection.release();
    });
  },
  // toko
  getTokoData(req, res) {
    pool.getConnection(function (err, connection) {
      let count = 0;
      let page = parseInt(req.params.page);
      if (page > count) {
        count = page;
      }
      if (err) throw err;
      connection.query(
        'SELECT * FROM toko ORDER BY nama_toko LIMIT ?, 20;',
        [count],
        function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Toko data retrieved successfully',
            data: results,
            test: `page: ${page}`,
          });
        }
      );
      connection.release();
    });
  },
  // toko spesifik
  getTokoByName(req, res) {
    const tokoName = '%' + req.params.namatoko + '%';
    pool.getConnection(function (err, connection) {
      if (err) throw err;
      connection.query(
        'SELECT * FROM toko WHERE nama_toko LIKE ? ORDER BY nama_toko ',
        [tokoName],
        function (error, results) {
          if (error) throw error;
          res.send({
            status: 200,
            success: true,
            message: 'Toko specific name retrieved successfully',
            data: results,
            test: `${tokoName}`,
          });
        }
      );
      connection.release();
    });
  },
  // daily promo ?

};
