package kr.co.devfox.config;

import javax.annotation.Resource;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import kr.co.devfox.beans.UserBean;
import kr.co.devfox.interceptor.CheckLoginInterceptor;
import kr.co.devfox.interceptor.CheckWriterInterceptor;
import kr.co.devfox.interceptor.TopMenuInterceptor;
import kr.co.devfox.mapper.BoardMapper;
import kr.co.devfox.mapper.TopMenuMapper;
import kr.co.devfox.mapper.UserMapper;
import kr.co.devfox.service.BoardService;
import kr.co.devfox.service.TopMenuService;


@Configuration //アプリケーションで必要な空きを定義

@EnableWebMvc //ウェブアプリケーションのMVCパターンを設定するために使用

@ComponentScan("kr.co.devfox.controller") //指定されたパッケージをスキャンしてSpringで管理するオブジェクト(空)を自動的に登録
@ComponentScan("kr.co.devfox.dao") //指定されたパッケージをスキャンしてSpringで管理するオブジェクト(空)を自動的に登録
@ComponentScan("kr.co.devfox.service") //指定されたパッケージをスキャンしてSpringで管理するオブジェクト(空)を自動的に登録

@PropertySource("/WEB-INF/properties/db.properties") //指定されたファイルからプロパティ値を読み込んで使用できるようにするデータベース接続情報の使用
public class ServletAppContext implements WebMvcConfigurer{  //Spring MVCのウェブアプリケーション設定を担当するクラス
	
	@Value("${db.classname}") //プロパティ ファイルに定義された値をフィールドに注入データベース接続情報を取得する
	private String db_classname;
	
	@Value("${db.url}") //プロパティ ファイルに定義された値をフィールドに注入データベース接続情報を取得する
	private String db_url;
	
	@Value("${db.username}") //プロパティ ファイルに定義された値をフィールドに注入データベース接続情報を取得する
	private String db_username;
	
	@Value("${db.password}") //プロパティ ファイルに定義された値をフィールドに注入データベース接続情報を取得する
	private String db_password;
	
	@Autowired //Springから依存性を自動的に注入
	private TopMenuService topMenuService; //上段メニューを管理するサービスオブジェクト
	
	@Resource(name = "loginUserBean") 
	private UserBean loginUserBean; //セッション スコープに保存されているログイン ユーザー情報
	
	@Autowired
	private BoardService boardService; //掲示板関連サービスを提供するオブジェクト
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) { //コントローラで返すJSPファイルのパスと拡張子を設定します
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.configureViewResolvers(registry);
		registry.jsp("/WEB-INF/views/", ".jsp");
	}
	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) { //静的ファイル（画像、CSS、JSなど）のパスを設定します
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);
		registry.addResourceHandler("/**").addResourceLocations("/resources/");
	}
	
	
	@Bean
	public BasicDataSource dataSource() { //データベース接続情報を管理するBasicDataSourceウィーンを作成
		BasicDataSource source = new BasicDataSource();
		source.setDriverClassName(db_classname);
		source.setUrl(db_url);
		source.setUsername(db_username);
		source.setPassword(db_password);
		
		return source;
	}
	
	
	@Bean
	public SqlSessionFactory factory(BasicDataSource source) throws Exception{ //SQLクエリ文とデータベース接続を管理するSqlSessionFactoryオブジェクトを作成
		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(source);
		SqlSessionFactory factory = factoryBean.getObject();
		return factory;
	}
	
	
	@Bean
	public MapperFactoryBean<BoardMapper> getBoardMapper(SqlSessionFactory factory) throws Exception{ //それぞれBoardMapper、TopMenuMapper、UserMapperを生成し、SQLセッションと接続してクエリー実行をサポートするマッパーオブジェクトを返却
		MapperFactoryBean<BoardMapper> factoryBean = new MapperFactoryBean<BoardMapper>(BoardMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	@Bean
	public MapperFactoryBean<TopMenuMapper> getTopMenuMapper(SqlSessionFactory factory) throws Exception{ //それぞれBoardMapper、TopMenuMapper、UserMapperを生成し、SQLセッションと接続してクエリー実行をサポートするマッパーオブジェクトを返却
		MapperFactoryBean<TopMenuMapper> factoryBean = new MapperFactoryBean<TopMenuMapper>(TopMenuMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	@Bean
	public MapperFactoryBean<UserMapper> getUserMapper(SqlSessionFactory factory) throws Exception{ //それぞれBoardMapper、TopMenuMapper、UserMapperを生成し、SQLセッションと接続してクエリー実行をサポートするマッパーオブジェクトを返却
		MapperFactoryBean<UserMapper> factoryBean = new MapperFactoryBean<UserMapper>(UserMapper.class);
		factoryBean.setSqlSessionFactory(factory);
		return factoryBean;
	}
	
	@Override 
	public void addInterceptors(InterceptorRegistry registry) { //アプリケーションのリクエスト処理に対するインターセプターを登録
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addInterceptors(registry);
		
		TopMenuInterceptor topMenuInterceptor = new TopMenuInterceptor(topMenuService, loginUserBean); //すべてのパスに対して上段メニューを処理するインターセプター
		
		InterceptorRegistration reg1 = registry.addInterceptor(topMenuInterceptor);
		reg1.addPathPatterns("/**");
		
		CheckLoginInterceptor checkLoginInterceptor = new CheckLoginInterceptor(loginUserBean); //ユーザーがログインしていないと特定のパスにアクセスできないように設定
		InterceptorRegistration reg2 = registry.addInterceptor(checkLoginInterceptor);
		reg2.addPathPatterns("/user/modify", "/user/logout", "/board/*");
		reg2.excludePathPatterns("/board/main");
		
		CheckWriterInterceptor checkWriterInterceptor = new CheckWriterInterceptor(loginUserBean, boardService); //ユーザーが作成者であることを確認し、投稿の修正と削除を制御するインターセプター
		InterceptorRegistration reg3 = registry.addInterceptor(checkWriterInterceptor);
		reg3.addPathPatterns("/board/modify", "/board/delete");
	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer PropertySourcesPlaceholderConfigurer() { //プロパティ ファイルから読み込んだ値を使用できるようにするビンを作成
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public ReloadableResourceBundleMessageSource messageSource() { //エラー メッセージを管理するメッセージ ソース ビーンを作成
		ReloadableResourceBundleMessageSource res = new ReloadableResourceBundleMessageSource();
		res.setBasenames("/WEB-INF/properties/error_message");
		return res;
	}
	
	@Bean
	public StandardServletMultipartResolver multipartResolver() { //ファイルアップロードを処理するStandardServletMultipartResolverビンを作成
		return new StandardServletMultipartResolver();
	}
}










