const config = require('../configs/database');
const mysql = require('mysql');
// const { param } = require('../routes/routes');
const pool = mysql.createPool(config);

pool.on('error', (err) => {
    console.error(err);
});

module.exports = {
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
                'SELECT * FROM barang WHERE judul_barang LIKE ? ',
                [barangName],
                function (error, results) {
                    if (error) throw error;
                    res.send({
                        status: 200,
                        success: true,
                        message: 'Barang data retrieved successfully',
                        data: results,
                        test: `${barangName}`,
                    });
                }
            );
            connection.release();
        });
    },
    // toko
    // bundel all
    // bindel spesifik
    // daily promo
};
