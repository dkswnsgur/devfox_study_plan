package kr.co.devfox.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import kr.co.devfox.beans.UserBean;

public interface UserMapper { //UserMapperというMyBatisインターフェースで、user_tableというデータベーステーブルでユーザー情報と関連した作業を処理するメソッドを提供
	//user_idでデータベースから当該ユーザのuser_nameを照会。 ユーザーが存在することを確認するために使用
	@Select("select user_name " +
			"from user_table " +
			"where user_id = #{user_id}")
	String checkUserIdExist(String user_id);
	//ユーザーの会員登録情報をuser_tableに挿入。 user_seq.nextvalはユーザーインデックス(user_idx)のシーケンス値を自動的に生成
	@Insert("insert into user_table (user_idx, user_name, user_id, user_pw) " +
			"values (user_seq.nextval, #{user_name}, #{user_id}, #{user_pw})")
	void addUserInfo(UserBean joinUserBean);
	//ユーザーが入力したuser_idとuser_pwを通じてログイン情報を確認した後、一致するユーザーのuser_idxとuser_nameを返却
	@Select("select user_idx, user_name " + 
			"from user_table " + 
			"where user_id=#{user_id} and user_pw=#{user_pw}")
	UserBean getLoginUserInfo(UserBean tempLoginUserBean);
	//このメソッドは、特定のユーザインデックス(user_idx)に該当するユーザのuser_idとuser_name情報を取得するユーザ情報修正ページで使用する
	@Select("select user_id, user_name " +
			"from user_table " +
			"where user_idx = #{user_idx}")
	UserBean getModifyUserInfo(int user_idx);
	//ユーザーのパスワードを修正する機能をする。 与えられたユーザーインデックス(user_idx)に該当するユーザーのパスワード(user_pw)をアップデートする
	@Update("update user_table " +
			"set user_pw = #{user_pw} " +
			"where user_idx = #{user_idx}")
	void modifyUserInfo(UserBean modifyUserBean);
}










