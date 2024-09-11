package kr.co.devfox.dao;

import javax.annotation.Resource;


import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import kr.co.devfox.beans.UserBean;

@Repository
public class UserDao { // ユーザーに関連付けられたデータベース タスクを処理する役割
	
	@Autowired
	private SqlSessionTemplate sqlSessionTemplate; //SqlSessionTemplateオブジェクトを自動的に注入してもらう
	
	public String checkUserIdExist(String user_id) { //与えられたユーザIDが既に存在することを確認するメソッド
		return sqlSessionTemplate.selectOne("user.checkUserIdExist", user_id); //sqlSessionTemplateのcheckUserIdExistメソッドを呼び出し、データベースに該当IDがあるかどうかを照会
	}
	
	public void addUserInfo(UserBean joinUserBean) { //新しいユーザーの情報をデータベースに追加するメソッド
		sqlSessionTemplate.insert("user.addUserInfo", joinUserBean); //joinUserBeanに含まれているユーザの情報をsqlSessionTemplateのaddUserInfoメソッドを呼び出してデータベースに保存
	}
	
	public UserBean getLoginUserInfo(UserBean tempLoginUserBean) { //ログイン時にユーザーの情報を確認するメソッド
		return sqlSessionTemplate.selectOne("user.getLoginUserInfo", tempLoginUserBean); //配信されたtempLoginUserBeanを通じてログイン情報をデータベースで照会し、当該ユーザーの情報が含まれたUserBeanオブジェクトを返却
	}
	
	public UserBean getModifyUserInfo(int user_idx) { //ユーザーの情報を修正するために既存の情報を取得するメソッド
		return sqlSessionTemplate.selectOne("user.getModifyUserInfo", user_idx); //ユーザー固有番号(user_idx)を使用してデータベースから修正するユーザー情報を取得
	}
	
	public void modifyUserInfo(UserBean modifyUserBean) { //ユーザーの情報を修正するメソッド
		sqlSessionTemplate.update("user.modifyUserInfo", modifyUserBean); //sqlSessionTemplateに含まれる変更された情報をデータベースにアップデート
	}
}






