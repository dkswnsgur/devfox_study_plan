package kr.co.devfox.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.devfox.beans.UserBean;

public class CheckLoginInterceptor implements HandlerInterceptor{ //このクラスはインタセプターで、ユーザがログインしたかを確認するロジックを実装するHandlerInterceptorインターフェースを実装し、preHandleメソッドをオーバーライド
	
	private UserBean loginUserBean; //現在ログインしたユーザーの情報を含んでいるオブジェクト。このloginUserBeanを通じてユーザーのログイン有無を確認
	
	public CheckLoginInterceptor(UserBean loginUserBean) { //生成者loginUserBeanをパラメータとして受け取り、このクラスで使用されるログイン情報を初期化
		// TODO Auto-generated constructor stub
		this.loginUserBean = loginUserBean;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception { //このメソッドは、リクエストがコントローラーに渡される前に実行されます。 ここでユーザーのログイン状態を確認
		// TODO Auto-generated method stub
		if(loginUserBean.isUserLogin() == false) { //loginUserBeanのisUserLoginメソッドを使用してユーザがログインしたかを確認
			String contextPath = request.getContextPath(); //request.getContextPath()は、現在のアプリケーションのルートパスを取得します。 これにより絶対経路を構成
			response.sendRedirect(contextPath + "/user/not_login"); //ユーザがログインしていない状態であれば、response。sendRedirect(contextPath+"/user/not_login")を呼び出してログインしていないユーザー専用ページにリダイレクト
			return false;
		}
		return true;
	}
}








