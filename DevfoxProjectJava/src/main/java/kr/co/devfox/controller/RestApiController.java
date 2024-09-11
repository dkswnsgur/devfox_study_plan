package kr.co.devfox.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import kr.co.devfox.service.UserService;

@RestController //このクラスがRESTfulウェブサービスを提供するコントローラであることを明示
public class RestApiController { //コントローラーはユーザーIDの有無を確認する機能を提供
	
	@Autowired //User Serviceオブジェクトを自動的に注入、このサービスはビジネスロジックを処理し、ユーザーIDの有無を確認するメソッドを提供
	private UserService userService;
	
	@GetMapping("/user/checkUserIdExist/{user_id}") ///user/checkUserIdExist/{user_id}経路で入ってくるGET要請を処理します。 {user_id}はURL経路の変数で、メソッドの@PathVariableアノテーションにマッピング
	public String checkUserIdExist(@PathVariable String user_id) { //@PathVariable アノテーションを使用してURL経路の変数user_idをメソッド媒介変数として伝達
		
		boolean chk = userService.checkuserIdExist(user_id); //user Service.checkuserIdExist(user_id)を呼び出し、与えられたuser_idが存在するか確認。 このメソッドはboolean値を返します
		
		return chk + ""; //chk + ""を使ってboolean値を文字列に変換して返す
	}
}












