const config = require('../configs/database');
const mysql = require('mysql');
const pool = mysql.createPool(config);

pool.on('error',(err)=> {
    console.error(err);
});

module.exports ={
    getLoginPage(req, res){
        res.render('login'); //need to edit more
    },
    postLoginData(req, res){
        const data = {
            email : req.body.email,
            password : req.body.password
        };
        if (!data.email || !data.password) {
            return res.send({
                status: 400,
                success: false,
                message: 'Username and password must be provided!'
            });
        }
        pool.getConnection(function(err, connection) {
            if (err) throw err;
            connection.query(
                'SELECT * FROM users WHERE email = ?',
                [data.email],
                function (error, results) {
                    if (error) throw error;
                    if (results.length === 0 || results[0].password !== data.password) {
                        return res.status(401).json({ message: 'Invalid username or password' });
                    }
                    res.send({
                        success: true,
                        message: 'Berhasil tambah data!',
                    });
                }
            );
            connection.release();
        });
    },
}
