package kr.co.devfox.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.devfox.beans.BoardInfoBean;
import kr.co.devfox.beans.ContentBean;
import kr.co.devfox.service.MainService;
import kr.co.devfox.service.TopMenuService;

@Controller // Spring MVCのコントローラーであることを明示
public class MainController { //main URLに入ってくるGETリクエストを処理するSpring MVCコントローラ
	
	@Autowired
	private MainService mainService; //MainServiceオブジェクトを自動的に注入
	
	@Autowired
	private TopMenuService topMenuService; //topMenuServiceオブジェクトを自動的に注入
	
	@GetMapping("/main") //@GetMapping("/main") アノテーションを通じて/main URLに入ってくるGETリクエストを処理
	public String main(Model model) { 
		
		ArrayList<List<ContentBean>> list = new ArrayList<List<ContentBean>>(); //ArrayList<List<ContentBean>listを生成して4つのリストを保存
		
		for(int i = 1 ; i <= 4 ; i++) { 
			List<ContentBean> list1 = mainService.getMainList(i); //リスト4つをmainService.getMainList(i)を呼び出して読み込み1から4までの値を持ち、計4つのリストを生成してlistに追加
			list.add(list1); 
		} 
		
		model.addAttribute("list", list); //model.add Attribute("list", list)を通じてこのデータをモデルに追加し、JSPページで使用できるようにする
		
		List<BoardInfoBean> board_list = topMenuService.getTopMenuList(); //top Menu Service.get Top Menu List()を呼び出してBoard Info Bean リストを取得する
		model.addAttribute("board_list", board_list); //model.add Attribute("board_list"、board_list)を通じてこのデータをモデルに追加し、JSPページで使用できるようにする
		
		return "main"; //"main"文字列を返し、このメソッドがWEB-INF/views/main.jspファイルをレンダリングするようSpring MVCに指示する
	}
}
