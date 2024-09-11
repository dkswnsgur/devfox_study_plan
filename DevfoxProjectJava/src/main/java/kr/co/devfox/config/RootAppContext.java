package kr.co.devfox.config;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.SessionScope;

import kr.co.devfox.beans.UserBean;


@Configuration //XMLベースの設定の代わりにJavaクラスを使用して設定を構成するときに使用
public class RootAppContext { //スプリングの設定クラス スプリングビーンを定義
	
	@Bean("loginUserBean") //スプリングコンテナにloginUserBeanという名前の空き（オブジェクト）を生成して登録
	@SessionScope //当該ウィーンのスコープが「セッション」範囲であることを示す
	public UserBean loginUserBean() { //ログインしたユーザーの情報を盛り込むための空きを生成するメソッド
		return new UserBean();
	}
}
