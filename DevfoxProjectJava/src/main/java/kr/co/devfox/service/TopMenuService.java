package kr.co.devfox.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.devfox.beans.BoardInfoBean;
import kr.co.devfox.dao.TopMenuDao;

@Service
public class TopMenuService { //TopMenuServiceクラスは、上段メニューに表示する掲示板情報を管理するサービス
	
	@Autowired
	private TopMenuDao topMenuDao; //TopMenuDaoを自動的に注入され使用。TopMenuDaoはデータベースとの相互作用を担当するDAOクラス
	
	public List<BoardInfoBean> getTopMenuList(){ //topMenuDao.getTopMenuList()を呼び出し、データベースから上段メニューに表示する掲示板情報を取得する
		List<BoardInfoBean> topMenuList = topMenuDao.getTopMenuList();
		return topMenuList; //返還された掲示板情報リストをtopMenuList変数に保存し、これを返還
	}
	
}
