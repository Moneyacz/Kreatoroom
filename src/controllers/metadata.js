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
};
