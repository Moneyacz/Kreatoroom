const router = require('express').Router();
const { postLoginData, getLoginPage } = require('../controllers/login');

router.get('/login', getLoginPage);
router.post('/login', postLoginData);

module.exports = router;
