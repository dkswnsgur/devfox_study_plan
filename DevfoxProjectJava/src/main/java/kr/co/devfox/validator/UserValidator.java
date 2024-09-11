package kr.co.devfox.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import kr.co.devfox.beans.UserBean;

public class UserValidator implements Validator{ //ユーザー登録や修正時に使用するクラス

	@Override
	public boolean supports(Class<?> clazz) { //Validatorがサポートするクラスタイプを確認
		// TODO Auto-generated method stub
		return UserBean.class.isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) { //targetオブジェクトの有効性を検査し、エラーがある場合はerrorsオブジェクトにエラー情報を追加
		// TODO Auto-generated method stub
		UserBean userBean = (UserBean)target; //targetをUserBeanにキャスティングしてユーザー情報を取得
		
		String beanName = errors.getObjectName(); //errors.get Object Name()を呼び出して、現在検証中のオブジェクトの名前を取得する。 この名前は一般的に「joinUserBean」または「modifyUserBean」である可能性があります
		
		if(beanName.equals("joinUserBean") || beanName.equals("modifyUserBean")) { //beanNameが"joinUserBean"または"modifyUserBean"の場合、二つのパスワード(user_pwとuser_pw2)が一致しているか確認
			if(userBean.getUser_pw().equals(userBean.getUser_pw2()) == false) { //パスワードが一致しない場合は、errors.rejectValue("user_pw"、"NotEquals")とerrors.rejectValue("user_pw2"、"NotEquals")を呼び出してエラーメッセージを設定
				errors.rejectValue("user_pw", "NotEquals");
				errors.rejectValue("user_pw2", "NotEquals");
			}
		}
			
		if(beanName.equals("joinUserBean")) { //beanNameが"joinUserBean"の場合、ユーザのIDが存在するか確認
			
			if(userBean.isUserIdExist() == false) { //IDが存在しない場合、errors.rejectValue("user_id"、"DontCheckUserIdExist")を呼び出してエラーメッセージを設定
				errors.rejectValue("user_id", "DontCheckUserIdExist");
			}
		}
	}
	
}
