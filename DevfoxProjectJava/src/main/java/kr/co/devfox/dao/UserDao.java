package kr.co.devfox.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.devfox.beans.UserBean;
import kr.co.devfox.mapper.UserMapper;

@Repository
public class UserDao { // ユーザーに関連付けられたデータベース タスクを処理する役割
	
	@Autowired
	private UserMapper userMapper; //UserMapperオブジェクトを自動的に注入してもらう
	
	public String checkUserIdExist(String user_id) { //与えられたユーザIDが既に存在することを確認するメソッド
		return userMapper.checkUserIdExist(user_id); //userMapperのcheckUserIdExistメソッドを呼び出し、データベースに該当IDがあるかどうかを照会
	}
	
	public void addUserInfo(UserBean joinUserBean) { //新しいユーザーの情報をデータベースに追加するメソッド
		userMapper.addUserInfo(joinUserBean); //joinUserBeanに含まれているユーザの情報をuserMapperのaddUserInfoメソッドを呼び出してデータベースに保存
	}
	
	public UserBean getLoginUserInfo(UserBean tempLoginUserBean) { //ログイン時にユーザーの情報を確認するメソッド
		return userMapper.getLoginUserInfo(tempLoginUserBean); //配信されたtempLoginUserBeanを通じてログイン情報をデータベースで照会し、当該ユーザーの情報が含まれたUserBeanオブジェクトを返却
	}
	
	public UserBean getModifyUserInfo(int user_idx) { //ユーザーの情報を修正するために既存の情報を取得するメソッド
		return userMapper.getModifyUserInfo(user_idx); //ユーザー固有番号(user_idx)を使用してデータベースから修正するユーザー情報を取得
	}
	
	public void modifyUserInfo(UserBean modifyUserBean) { //ユーザーの情報を修正するメソッド
		userMapper.modifyUserInfo(modifyUserBean); //modifyUserBeanに含まれる変更された情報をデータベースにアップデート
	}
}








