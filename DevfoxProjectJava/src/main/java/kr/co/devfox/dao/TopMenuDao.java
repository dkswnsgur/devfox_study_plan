package kr.co.devfox.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.devfox.beans.BoardInfoBean;
import kr.co.devfox.mapper.TopMenuMapper;

@Repository
public class TopMenuDao { //上段メニューに関連するデータを処理する役割

	@Autowired
	private TopMenuMapper topMenuMapper; //TopMenuMapperオブジェクトを自動的に注入される
	
	public List<BoardInfoBean> getTopMenuList(){ //上段メニューに表示する掲示板情報を取得するメソッド
		List<BoardInfoBean> topMenuList = topMenuMapper.getTopMenuList(); //topMenuMapperのgetTopMenuList()メソッドを呼び出し、データベースで上段メニューとして表示する掲示板リストを取得する
		return topMenuList; //データベースから照会された掲示板リストを返却
	}
}
