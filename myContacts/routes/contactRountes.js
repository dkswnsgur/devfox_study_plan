const express = require("express");
const router = express.Router();

// 연락처 가져오기
router
.route("/contacts")
.get((req, res) => {
   res.send("Contacts Page")
})
.post((req, res) => {
   res.send("Contacts Page")
})

router
.route("/contacts/:id")
.get((req, res) => {
   res.send(`View contacts for id : ${req.params.id}`)
})
.put((req, res) => {
   res.send(`Update contacts for id : ${req.params.id}`)
})
.delete((req, res) => {
   res.send(`delete contacts for id : ${req.params.id}`)
})

module.exports = router;