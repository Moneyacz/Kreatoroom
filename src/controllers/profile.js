const config = require('../configs/database');
const mysql = require('mysql');
const pool = mysql.createPool(config);

pool.on('error', (err) => {
  console.error(err);
});

module.exports = {
  getUserData(req, res) {
    const userId = req.params.userId;

    pool.getConnection(function (err, connection) {
      if (err) throw err;
      connection.query(
        "SELECT * FROM user WHERE id_pengguna = ?",
        [userId],
        function (error, results) {
          if (error) throw error;
          if (results.length === 0) {
            return res.status(404).json({ message: 'User not found' });
          }
          const userProfile = results[0]; // Assuming only one user is returned
          res.send({
            status: 200,
            success: true,
            message: 'User profile retrieved successfully',
            data: userProfile,
          });
        }
      );
      connection.release();
    });
  },
};
