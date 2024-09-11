package kr.co.devfox.interceptor;

import javax.servlet.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.devfox.beans.ContentBean;
import kr.co.devfox.beans.UserBean;
import kr.co.devfox.service.BoardService;

public class CheckWriterInterceptor implements HandlerInterceptor{ //このクラスは、ユーザーが特定の投稿の作成者であることを確認する役割
	
	private UserBean loginUserBean; //現在ログインしているユーザーの情報を含むオブジェクトで、このオブジェクトからログインしているユーザーのIDを確認
	private BoardService boardService; //掲示物に関する情報を取得するために使用されるサービスを通じて、掲示物の作成者を確認
	
	public CheckWriterInterceptor(UserBean loginUserBean, BoardService boardService) { //CheckWriterInterceptorの生成者はloginUserBeanとboardServiceを受けて内部フィールドを初期化
		// TODO Auto-generated constructor stub
		this.loginUserBean = loginUserBean;
		this.boardService = boardService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception { //このメソッドは、要求がコントローラに到達する前に実行され、ユーザーが投稿作成者であることを確認するロジックが含まれています
		// TODO Auto-generated method stub
		
		String str1 = request.getParameter("content_idx"); //URLからcontent_idxというパラメータ値を取得する
		int content_idx = Integer.parseInt(str1); //content_idxは文字列に戻されるため、Integer.parseInt(str1)に整数型に変換する
		
		ContentBean currentContentBean = boardService.getContentInfo(content_idx); //board Serviceを通じて掲示物の情報を取得します。 この情報をカレントContentBeanに保存
		
		if(currentContentBean.getContent_writer_idx() != loginUserBean.getUser_idx()) { //currentContentBean.getContent_writer_idx()とloginUserBean.getUser_idx()を比較して、ログインしたユーザーが掲示物作成者であることを確認
			String contextPath = request.getContextPath();
			response.sendRedirect(contextPath + "/board/not_writer"); //response。sendRedirect(contextPath+"/board/not_writer")で作成者でないことを知らせるページへリダイレクト
			return false;
		}
		
		return true;
	}
}












