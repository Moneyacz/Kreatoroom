const config = require('../configs/database');
const path = require('path');
const mysql = require('mysql');
const pool = mysql.createPool(config);

module.exports = {
    getFillDataPage(req, res) {
      const fillDataPage = path.join(__dirname, '../views/fill-data');
      res.render(fillDataPage);
    },
}