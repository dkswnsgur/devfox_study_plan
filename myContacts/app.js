const express = require("express");
const app = express();

app.get("/", (req, res) => {
});

/*router.route("/contacts")
app.get("/contacts", (req, res) => {
  res.send("contacts, page");
});

// 새 연락처 추가
app.post("/contacts", (req, res) => {
  res.send("Create Contacts");
});*/

//연락처 1개 가져오기
/*app.get("/contacts/:id", (req, res) => {
  res.send(`View contacts for id : ${req.params.id}`);
});

// post, put, delete

//연락처 수정하기
app.put("/contacts/:id", (req, res) => {
  res.send(`Update contacts for id : ${req.params.id}`);
});

//연락처 삭제하기
app.delete("/contacts/:id", (req, res) => {
  res.send(`delete contacts for id : ${req.params.id}`);
});*/

app.use("/", require("./routes/contactRoutes"));

app.listen(3000, () => {
   console.log('서버 실행 중');
});


