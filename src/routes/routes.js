const router = require('express').Router();
const { login } = require('../controllers/index');
const { register } = require('../controllers/index');

router.get('/login', login.getLoginPage);
router.post('/login', login.postLoginData);
router.post('/register', register.postRegisterData);

module.exports = router;
