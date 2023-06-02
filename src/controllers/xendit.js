const config = require('../configs/database');
const mysql = require('mysql');
const pool = mysql.createPool(config);

pool.on('error', (err) => {
  console.error(err);
});

const xendit = require('xendit-node');
const xenditInstance = new xendit({ secretKey: process.env.XENDIT_SECRETKEY });
const { Balance, EWallet } = xenditInstance;

module.exports = {
  async getBalance(req, res) {
    const balanceSpecificOptions = {};
    const b = new Balance(balanceSpecificOptions);

    try {
      const resp = await b.getBalance();
      res.send(resp);
    } catch (error) {
      console.error(error);
      res.status(500).send('An error occurred');
    }
  },
  async postEwallet(req, res) {
    const ewalletSpecificOptions = {};
    const ew = new EWallet(ewalletSpecificOptions);

    try {
      const resp = await ew.createEWalletCharge({
        referenceID: 'test-reference-id',
        currency: 'IDR',
        amount: 1000,
        checkoutMethod: 'ONE_TIME_PAYMENT',
        channelCode: 'ID_DANA',
        channelProperties: {
          successRedirectURL: 'https://dashboard.xendit.co/register/1',
        },
        metadata: {
          branch_code: 'tree_branch',
        },
      });
      res.send(resp);
    } catch (error) {
      console.error(error);
      res.status(500).send('An error occurred');
    }
  },
};
