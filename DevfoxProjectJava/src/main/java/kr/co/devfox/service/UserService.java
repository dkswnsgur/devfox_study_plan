package kr.co.devfox.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.devfox.beans.UserBean;
import kr.co.devfox.dao.UserDao;

@Service
public class UserService { //User Service クラスは、ユーザー関連のビジネスロジックを処理するサービスです。 ユーザー情報の確認、追加、修正などを担当
	
	@Autowired
	private UserDao userDao; //UserDaoを自動的に注入してもらって使用
	
	@Resource(name = "loginUserBean") //UserBeanの注入を受け、ログインユーザー情報を管理
	private UserBean loginUserBean; //UserBeanを自動的に注入され使用
	
	public boolean checkuserIdExist(String user_id) { //与えられたユーザーIDが既に存在するか確認
		
		String user_name = userDao.checkUserIdExist(user_id); //userDao.checkUserIdExist(user_id)を呼び出してユーザー名を照会
		
		if(user_name == null) { //ユーザー名がnullの場合、IDが存在しないものとみなし、trueを返還、そうでなければfalseを返還
			return true;
		} else {
			return false;
		}
	}
	
	public void addUserInfo(UserBean joinUserBean) { //새로운 사용자 정보를 데이터베이스에 추가
		userDao.addUserInfo(joinUserBean); //userDao.addUserInfo(joinUserBean)를 호출하여 사용자 정보를 추가
	}
	
	public void getLoginUserInfo(UserBean tempLoginUserBean) { //로그인 시 사용자 정보를 조회하여 로그인 상태를 설정
		
		UserBean tempLoginUserBean2 = userDao.getLoginUserInfo(tempLoginUserBean); //userDao.getLoginUserInfo(tempLoginUserBean)를 호출하여 사용자 정보를 조회
		
		if(tempLoginUserBean2 != null) { 
			loginUserBean.setUser_idx(tempLoginUserBean2.getUser_idx());
			loginUserBean.setUser_name(tempLoginUserBean2.getUser_name());
			loginUserBean.setUserLogin(true);
		}
	}
	
	public void getModifyUserInfo(UserBean modifyUserBean) { //로그인된 사용자의 정보를 조회하여 수정할 정보를 설정
		UserBean tempModifyUserBean = userDao.getModifyUserInfo(loginUserBean.getUser_idx()); //userDao.getModifyUserInfo(loginUserBean.getUser_idx())를 호출하여 현재 로그인된 사용자의 정보를 조회
		//조회된 정보를 modifyUserBean에 설정
		modifyUserBean.setUser_id(tempModifyUserBean.getUser_id());
		modifyUserBean.setUser_name(tempModifyUserBean.getUser_name());
		modifyUserBean.setUser_idx(loginUserBean.getUser_idx());
	}
	
	public void modifyUserInfo(UserBean modifyUserBean) { //사용자 정보를 수정
		
		modifyUserBean.setUser_idx(loginUserBean.getUser_idx()); //modifyUserBean에 현재 로그인된 사용자의 ID를 설정
		
		userDao.modifyUserInfo(modifyUserBean); //userDao.modifyUserInfo(modifyUserBean)를 호출하여 정보를 수정
	}
}











