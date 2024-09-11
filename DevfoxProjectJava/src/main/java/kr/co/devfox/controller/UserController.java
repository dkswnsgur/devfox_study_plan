package kr.co.devfox.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.devfox.beans.UserBean;
import kr.co.devfox.service.UserService;
import kr.co.devfox.validator.UserValidator;

@Controller //このクラスがSpring MVCのコントローラーであることを示す
@RequestMapping("/user") // すべてのメソッドの基本URLパスを/userに設定
public class UserController { // ユーザー関連機能を処理するSpring MVC コントローラー。 主にユーザーログイン、会員登録、情報修正、ログアウトなどの作業を処理

	@Autowired //User Serviceオブジェクトを自動的に注入してもらう
	private UserService userService;
	
	@Resource(name = "loginUserBean") //セッションスコープのUserBeanオブジェクトを注入されます。 ログイン状態を維持するために使用
	private UserBean loginUserBean;
	
	@GetMapping("/login") //経路でGET要請が入ってくると呼び出し
	public String login(@ModelAttribute("tempLoginUserBean") UserBean tempLoginUserBean, //ログインフォームにバインディングする空のオブジェクトを生成
						@RequestParam(value = "fail", defaultValue = "false") boolean fail, //URLパラメータfailを読み込む
						Model model) {
		
		model.addAttribute("fail", fail); //失敗の有無をモデルに追加
	
		return "user/login"; //ログインページを表示
	}
	
	@PostMapping("/login_pro") //user/login_pro 経路で POST リクエストが入ってくると呼び出し
	public String login_pro(@Valid @ModelAttribute("tempLoginUserBean") UserBean tempLoginUserBean, BindingResult result) { //ログイン情報を検証
		//Binding Result result:検証結果を盛り込む
		if(result.hasErrors()) { //エラーがある場合はログインページに戻る
			return "user/login"; 
		}
		
		userService.getLoginUserInfo(tempLoginUserBean); //ログイン処理を実行
		
		if(loginUserBean.isUserLogin() == true) { //ログインの成否によって適切なページにリダイレクト
			return "user/login_success";
		} else {
			return "user/login_fail";
		}
	}
	
	@GetMapping("/join") ///user/join経路でGET要請が入ってくると呼び出し
	public String join(@ModelAttribute("joinUserBean") UserBean joinUserBean) { //会員登録フォームにバインディングする空のオブジェクトを作成
		return "user/join"; //会員登録ページを表示
	}
	
	@PostMapping("/join_pro") ///user/join_pro経路でPOST要請が入ってくると呼び出し
	public String join_pro(@Valid @ModelAttribute("joinUserBean") UserBean joinUserBean, BindingResult result) { //会員登録情報を検証
		if(result.hasErrors()) { //検証エラーがあれば会員登録ページに戻る
			return "user/join"; 
		}
		
		userService.addUserInfo(joinUserBean); //会員登録処理を遂行
		
		return "user/join_success"; //会員登録成功ページにリダイレクト
	}
	
	@GetMapping("/modify") ///user/modify経路でGET要請が入ってくると呼び出し
	public String modify(@ModelAttribute("modifyUserBean") UserBean modifyUserBean) { //修正するユーザー情報を含む空のオブジェクトを作成
		
		userService.getModifyUserInfo(modifyUserBean); //userService.getModifyUserInfo(modifyUserBean):ユーザの情報を取得して空のオブジェクトに設定
		
		return "user/modify"; //情報修正ページを表示
	}
	
	@PostMapping("/modify_pro") ///user/modify_pro経路でPOST要請が入ってくると呼び出し
	public String modify_pro(@Valid @ModelAttribute("modifyUserBean") UserBean modifyUserBean, BindingResult result) { //ユーザー情報を検証
		
		if(result.hasErrors()) { //検証エラーがあれば情報修正ページに戻る
			return "user/modify";
		}
		
		userService.modifyUserInfo(modifyUserBean); //ユーザー情報を修正する
		
		return "user/modify_success"; //修正成功ページへリダイレクト
	}
	
	@GetMapping("/logout") ///user/logout経路でGET要請が入ってくると呼び出し
	public String logout() {
		
		loginUserBean.setUserLogin(false); //ユーザーログインを終了
		
		return "user/logout"; //ログアウトページにリダイレクト
	}
	
	@GetMapping("/not_login") ///user/not_login経路でGET要請が入ると呼び出し
	public String not_login() { //ログインしていないユーザーに表示するページを返す
		return "user/not_login"; 
	}
	
	@InitBinder //データ バインディングと検証を初期化するメソッド
	public void initBinder(WebDataBinder binder) { 
		UserValidator validator1 = new UserValidator(); //UserValidatorを使用してUserBeanオブジェクトに対する検証を追加
		binder.addValidators(validator1);
	}
}








