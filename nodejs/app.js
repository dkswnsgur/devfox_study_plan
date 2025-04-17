const express = require('express');
const mysql = require('mysql2/promise');
const ejs = require('ejs');
const app = express();
const port = 3000;
var bodyParser = require('body-parser');
var session = require("express-session")

const pool = mysql.createPool({
  host: 'localhost',
  user: 'root',
  password: '1234',
  database: 'contact',
  waitForConnections: true,
  connectionLimit: 10
});



async function testDBConnection() {
  try {
    const connection = await pool.getConnection();
    console.log('MySQL 연결 성공!');
    connection.release();
  } catch (err) {
    console.error('MySQL 연결 실패:', err);
    process.exit(1);
  }
}

testDBConnection();


app.set('view engine', 'ejs');
app.set('views', './views'); // 'views' 디렉토리 설정

app.use(bodyParser.urlencoded({ extended: false }));
app.use(express.static('public'));
app.use(session({
  secret: 'wnsgur2135',
  resave: false,
  saveUninitialized: false,
  cookie: {
    maxAge: 1000 * 60 * 60
  }
}));
app.use((req, res, next) => {
  res.locals.user = req.session.user;
  next();
});
//라우팅
app.get('/', (req, res) =>  {
  if (req.session.user) {
  res.render('index', { name: req.session.user.name }); //한문장에 send rend는 한개바께 안된다
  } else {
        res.send("<script>alert('로그인 후 이용해주세요.'); location.href='/login';</script>");
  }
});

app.get('/profile', (req, res) =>  {
  if (req.session.user) {
    res.render('profile', { name: req.session.user.name }); //한문장에 send rend는 한개바께 안된다
    } else {
          res.send("<script>alert('로그인 후 이용해주세요.'); location.href='/login';</script>");
    }
});

app.get('/map', (req, res) =>  {
  if (req.session.user) {
      res.render('map', { name: req.session.user.name }); //한문장에 send rend는 한개바께 안된다
      } else {
            res.send("<script>alert('로그인 후 이용해주세요.'); location.href='/login';</script>");
      }
});

app.get('/contact', (req, res) =>  {
  if (req.session.user) {
        res.render('contact', { name: req.session.user.name }); //한문장에 send rend는 한개바께 안된다
        } else {
              res.send("<script>alert('로그인 후 이용해주세요.'); location.href='/login';</script>");
        }
});



app.post('/contactProc', async (req, res) => {
  const name = req.body.name;
  const phone = req.body.phone;
  const email = req.body.email;
  const memo = req.body.memo;
  /*const { name, phone, email, memo } = req.body;*/

  const sql = 'INSERT INTO contact (name, phone, email, memo, regdate) VALUES (?, ?, ?, ?, NOW())';

  try {
    const connection = await pool.getConnection();
    await connection.query(sql, [name, phone, email, memo]);
    connection.release();

    console.log('자료 1개를 삽입하였습니다.');
    res.send("<script>alert('문의사항이 등록되었습니다.'); window.location.href='/contact';</script>");
  } catch (err) {
    console.error('DB 오류:', err);
    res.status(500).send("서버 오류가 발생했습니다.");
  }
});

app.get('/contactDelete', async (req, res) =>  {
  var idx = req.query.idx
  var sql = "delete from contact where idx=?"
  try {
  const connection = await pool.getConnection();
  await connection.query(sql, [idx]);
  connection.release();
  res.send("<script>alert('삭제 되었습니다.'); window.location.href='/contactList';</script>");
  } catch (err) {
      console.error(err);
      res.status(500).send("삭제 중 오류 발생");
  }
});

app.get('/contactList', async (req, res) =>  {
if (req.session.user) {
         var sql = "SELECT idx, name, phone, email, memo, DATE_FORMAT(regdate, '%Y-%m-%d %H:%i:%s') as regdate FROM contact ORDER BY idx DESC"
            const connection = await pool.getConnection();
            const [results] = await connection.query(sql); // results만 꺼냄
            connection.release();
            res.render('contactList',{lists:results})
        } else {
              res.send("<script>alert('로그인 후 이용해주세요.'); location.href='/login';</script>");
        }
});

app.post('/loginProc', async (req, res) => {
  const user_id = req.body.user_id;
  const pw = req.body.pw;

  const sql = 'select * from users where user_id = ? and pw = ?';

  try {
    const connection = await pool.getConnection();
    const [rows] = await connection.query(sql, [user_id, pw]);
    connection.release();

    if (rows.length > 0) {
    req.session.user = {
        user_id: rows[0].user_id,
        name: rows[0].name
      };
          console.log('로그인에 성공하였습니다.');
           res.send("<script>alert('로그인 되었습니다.'); window.location.href='/';</script>");
        } else {
          console.log('아이디 또는 비밀번호가 틀렸습니다.');
          res.status(401).send("아이디 또는 비밀번호가 틀렸습니다.");
        }

  } catch (err) {
    console.error('로그인 실패', err);
    res.status(500).send("서버 오류가 발생했습니다.");
  }
});

app.get('/logout', (req, res) =>  {
  req.session.user = null;
  res.send("<script>alert('로그아웃 되었습니다.'); location.href='/';</script>");
});

app.get('/login', (req, res) =>  {
  res.render('login');
});


app.listen(port, () => {
  console.log(`서버가 실행되었습니다 접속 주소 ${port}`);
});