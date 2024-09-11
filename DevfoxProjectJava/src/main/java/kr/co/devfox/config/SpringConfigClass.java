package kr.co.devfox.config;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class SpringConfigClass extends AbstractAnnotationConfigDispatcherServletInitializer{ //ウェブアプリケーションの初期設定と構成を処理するAbstractAnnotation Config Dispatcher Servlet Initializerを継承し、Spring MVCで必要な設定を提供
	
	@Override
	protected String[] getServletMappings() { //Dispatcher Servletが処理するURLパターンを定義するメソッド
		// TODO Auto-generated method stub
		return new String[] {"/"}; //ルートURL("/")で始まるすべてのリクエストをDispatcherServletが処理するように指定
	}
	
	
	@Override
	protected Class<?>[] getServletConfigClasses() { //Dispatcher Servletにロードする設定クラスを指定するメソッド
		// TODO Auto-generated method stub
		return new Class[] {ServletAppContext.class}; //ウェブ関連の設定を定義したクラス
	}
	
	
	@Override
	protected Class<?>[] getRootConfigClasses() { //アプリケーションの全体的な設定を担当するクラス
		// TODO Auto-generated method stub
		return new Class[] {RootAppContext.class}; //アプリケーションのビジネスロジック、サービス、データベース設定などを定義したクラス
	}
	
	
	@Override
	protected Filter[] getServletFilters() { //Dispatcher Servletで使用するフィルターを指定
		// TODO Auto-generated method stub
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter(); //characterEncodingFilterを使用して、すべての要求と応答の文字エンコーディングを「UTF-8」に設定
		encodingFilter.setEncoding("UTF-8");
		return new Filter[] {encodingFilter};
	}
	
	@Override
	protected void customizeRegistration(Dynamic registration) { //ファイルのアップロード設定を担当するメソッド
		// TODO Auto-generated method stub
		super.customizeRegistration(registration);
		
		MultipartConfigElement config1 = new MultipartConfigElement(null, 52428800, 524288000, 0); //MultipartConfigElementオブジェクトを使用してファイルアップロードのための設定を指定、最大ファイルサイズ50MB(52428800バイト)とリクエストあたり最大500MB(524288000バイト)を設定
		registration.setMultipartConfig(config1);
	}
}

















