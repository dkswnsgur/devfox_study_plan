package kr.co.devfox.interceptor;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.devfox.beans.BoardInfoBean;
import kr.co.devfox.beans.UserBean;
import kr.co.devfox.service.TopMenuService;

public class TopMenuInterceptor implements HandlerInterceptor{ //このクラスはHandlerInterceptorインタフェースを実装。 このインターフェイスを使用して、要求がコントローラーに到達する前に特定のタスクを実行

	@Autowired
	private TopMenuService topMenuService; //上段メニュー情報を取得するサービス。 これにより掲示板リストを読み込む
	
	@Resource(name = "loginUserBean")
	@Lazy
	private UserBean loginUserBean; //現在ログインしているユーザーの情報を含むオブジェクト。ログイン状態に関する情報を設定するために使用する
	
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception { //このメソッドはリクエストがコントローラーに到達する前に実行され、上段メニュー情報とログインユーザー情報をリクエストに追加する役割をする
		// TODO Auto-generated method stub
		List<BoardInfoBean> topMenuList = topMenuService.getTopMenuList(); //top Menu Serviceを通じて上段メニューに表示される掲示板のリストを取得する。 このリストはBoard Info Beanオブジェクトのリストで構成され
		request.setAttribute("topMenuList", topMenuList); //上段メニューリスト(top Menu List)をrequestオブジェクトに設定し、ビュー(View)でこの情報を使用できるようにする
		request.setAttribute("loginUserBean", loginUserBean); //ログインしたユーザー情報（loginUserBean）をrequestオブジェクトに設定します。 これにより、ビューでログイン状態を確認したり、ログインしたユーザーに関する情報を出力することができる
		
		return true;
	}
}
