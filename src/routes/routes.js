const router = require('express').Router();
const { login, register, profile, home } = require('../controllers/index');



router.get('/login', login.getLoginPage);
router.post('/login', login.postLoginData);
router.post('/register', register.postRegisterData);
router.get('/profile/:userId', profile.getUserData);
router.get('/', home.getBarangData);


module.exports = router;
