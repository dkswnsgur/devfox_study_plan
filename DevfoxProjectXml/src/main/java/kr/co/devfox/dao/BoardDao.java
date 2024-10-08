package kr.co.devfox.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.devfox.beans.ContentBean;

@Repository
public class BoardDao { //DAO(Data Access Object)クラスで、データベースとの相互作用を担当する
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate; //MyBatisのSqlSessionTemplateを注入してもらい、データベース作業を処理
	
	public void addContentInfo(ContentBean writeContentBean) { //投稿をデータベースに追加するメソッド
		sqlSessionTemplate.insert("board.addContentInfo", writeContentBean); //マッパーのaddContentInfoメソッドを呼び出してwriteContentBeanデータを保存
	}
	
	public String getBoardInfoName(int board_info_idx) { //掲示板名を取得するメソッド
		return sqlSessionTemplate.selectOne("board.getBoardInfoName", board_info_idx); //掲示板の固有インデックスboard_info_idxを利用してマッパーのgetBoard InfoNameメソッドでデータベースから掲示板名を取得する
	}
	
	public List<ContentBean> getContentList(int board_info_idx, RowBounds rowBounds){ //特定掲示板の掲示物リストを取得するメソッド
		return sqlSessionTemplate.selectList("board.getContentList", board_info_idx, rowBounds); //board_info_idxで掲示板を識別し、RowBoundsオブジェクトでページング処理をして掲示文リストを取得する
	}
	
	public List<ContentBean> searchContentList(int board_info_idx, String searchKeyword) {
	    // パラメータをMapにまとめて伝達
	    Map<String, Object> params = new HashMap<>();
	    params.put("board_info_idx", board_info_idx);
	    params.put("searchKeyword", searchKeyword);
	    
	    // クエリーIDとMapを配信
	    return sqlSessionTemplate.selectList("board.searchContentList", params);
	}
	
	public ContentBean getContentInfo(int content_idx) { //特定の投稿の詳細を取得するメソッド
		return sqlSessionTemplate.selectOne("board.getContentInfo", content_idx); //掲示文の固有インデックスcontent_idxでマッパーのgetContentInfoメソッドを呼び出し、掲示文の詳細情報を取得する
	}
	
	public void modifyContentInfo(ContentBean modifyContentBean) { //投稿を修正するメソッド
		sqlSessionTemplate.selectOne("board.modifyContentInfo", modifyContentBean); //修正された投稿情報を含むmodifyContentBeanを利用してマッパーのmodifyContentInfoメソッドを呼び出してデータベースに修正内容を反映
	}
	
	public void deleteContentInfo(int content_idx) { //特定の投稿を削除するメソッド
		sqlSessionTemplate.delete("board.deleteContentInfo", content_idx); //掲示文の固有インデックスcontent_idxを利用してマッパーのdeleteContentInfoメソッドを呼び出し、該当掲示文を削除する
	}
	
	public int getContentCnt(int content_board_idx) { //特定掲示板の書き込み数を取得するメソッド
		return sqlSessionTemplate.selectOne("board.getContentCnt", content_board_idx); //掲示板インデックスcontent_board_idxで当該掲示板に存在する掲示板の合計数を返還
	}

	public List<ContentBean> selectList(String string, Map<String, Object> params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}










