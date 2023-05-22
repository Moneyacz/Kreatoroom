const router = require('express').Router();
const { postRegisterData } = require('../controllers/register');

// router.get('/login', getLoginPage);
router.post('/registers', postRegisterData);

module.exports = router;
