const router = require('express').Router();
const {
  login,
  register,
  profile,
  home,
  metadata,
  xendit,
} = require('../controllers/index');
const { getBalance } = require('../controllers/xendit');

router.get('/login', login.getLoginPage);
router.post('/login', login.postLoginData);
router.post('/register', register.postRegisterData);
router.get('/profile/:userId', profile.getUserData);

router.get('/home/:page?', home.getBarangData);
router.get('/home/search/:judulbarang', home.getBarangByName);
router.get('/home/bundle', home.getBundle);
router.get('/home/bundle/search/:idBundle', home.getBundleById);

router.get('/toko/:page?', home.getTokoData);
router.get('/toko/search/:namatoko', home.getTokoByName);
// post toko
// post bundle

router.get('/metadata', metadata.getMetadata);
router.get('/balance', xendit.getBalance);
router.get('/ewalet', xendit.postEwallet);

module.exports = router;
