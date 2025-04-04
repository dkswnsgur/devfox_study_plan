const express = require('express');
const mysql = require('mysql2/promise');
const ejs = require('ejs');
const app = express();
const port = 3000;

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

var bodyParser = require('body-parser');

app.set('view engine', 'ejs');
app.set('views', './views'); // 'views' 디렉토리 설정

app.use(bodyParser.urlencoded({ extended: false }));

//라우팅
app.get('/', (req, res) =>  {
  res.render('index'); // ./views/index.ejs
});

app.get('/profile', (req, res) =>  {
  res.render('profile');
});

app.get('/map', (req, res) =>  {
  res.render('map');
});

app.get('/contact', (req, res) =>  {
  res.render('contact');
});

app.post('/contactProc', async (req, res) => {
  const { name, phone, email, memo } = req.body;

  try {
    const connection = await pool.getConnection();
    const sql = `INSERT INTO contact (name, phone, email, memo, regdate) VALUES (?, ?, ?, ?, NOW())`;
    await connection.query(sql, [name, phone, email, memo]);
    connection.release(); // 연결 반환

    console.log('문의 사항이 등록되었습니다!');
    res.send("<script>alert('문의 사항이 등록되었습니다'); location.href='/';</script>");
  } catch (err) {
    console.error('DB 오류:', err);
    res.status(500).send('문의사항 등록 중 오류 발생');
  }
});


app.listen(port, () => {
  console.log(`서버가 실행되었습니다 접속 주소 ${port}`);
});