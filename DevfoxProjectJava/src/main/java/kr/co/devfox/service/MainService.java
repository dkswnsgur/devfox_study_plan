package kr.co.devfox.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.devfox.beans.ContentBean;
import kr.co.devfox.dao.BoardDao;

@Service
public class MainService { //クラスは掲示板の主要リストを照会するサービス、このサービスは掲示板のコンテンツを読み込む機能を担当

	@Autowired
	private BoardDao boardDao; //BoardDaoオブジェクトを自動注入
	
	public List<ContentBean> getMainList(int board_info_idx){ //board_info_idxをパラメータとして受け取り、該当掲示板のコンテンツリストを照会
		RowBounds rowBounds = new RowBounds(0, 5); //RowBoundsオブジェクトを使用してページネーションを適用。ここでRowBounds(0、5)は開始行インデックス0から5つのコンテンツを照会するように設定
		return boardDao.getContentList(board_info_idx, rowBounds); //boardDao.getContentList(board_info_idx、rowBounds)を呼び出し、実際のデータベースからコンテンツリストを取得する
	}
}











