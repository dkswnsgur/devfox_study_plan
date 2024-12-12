package kr.co.devfox.service;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.devfox.beans.CommentBean;
import kr.co.devfox.beans.ContentBean;
import kr.co.devfox.beans.PageBean;
import kr.co.devfox.beans.UserBean;
import kr.co.devfox.dao.BoardDao;
import kr.co.devfox.dao.CommentDao;

@Service
@PropertySource("/WEB-INF/properties/option.properties") //option.propertiesファイルから設定値を読み込む
public class BoardService { //このBoard Serviceクラスは、掲示板関連機能を具現したサービスクラス。 主に掲示物の追加、修正、削除、照会とファイルアップロード処理などの機能を担当
	
	@Value("${path.upload}") //@Value("${path.upload}"):アップロードされたファイルを保存するパスを注入
	private String path_upload; 
	
	@Value("${page.listcnt}") //@Value("${page.listcnt}"): 1ページに表示する項目の個数を注入
	private int page_listcnt;
	
	@Value("${page.paginationcnt}") //@Value("${page。paginationcnt}"):ページネーションのページ数を注入
	private int page_paginationcnt;
	
	@Autowired
	private BoardDao boardDao; //BoardDaoオブジェクトを自動注入
	
	@Autowired
	private CommentDao commentDao;
	
	@Resource(name = "loginUserBean")
	private UserBean loginUserBean;
	
	private String saveUploadFile(MultipartFile upload_file) { //アップロードされたファイルをサーバーに保存し、保存されたファイルの名前を返す
		
		String file_name = System.currentTimeMillis() + "_" + upload_file.getOriginalFilename();
		
		try {
			upload_file.transferTo(new File(path_upload + "/" + file_name));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return file_name;
	}
	
	public void addContentInfo(ContentBean writeContentBean) { //新しい投稿を追加
		
		MultipartFile upload_file = writeContentBean.getUpload_file(); 
		
		
		if(upload_file.getSize() > 0) {
			String file_name = saveUploadFile(upload_file); //アップロードされたファイルがあればsave Upload Fileメソッドを使用してファイルを保存し、ファイル名をContent Beanに設定
			writeContentBean.setContent_file(file_name);
		}
		
		writeContentBean.setContent_writer_idx(loginUserBean.getUser_idx()); //掲示物の作成者インデックスをloginUserBeanから取得して設定
		
		boardDao.addContentInfo(writeContentBean); //boardDaoを通じて掲示物情報をデータベースに追加
	}
	
       public void addComment(CommentBean writeCommentBean) { 
    	   
        commentDao.addComment(writeCommentBean);
	}
       
       
       public void updateComment(int commentId, String commentText) {
    	   commentDao.updateComment(commentId, commentText);
    	}
       
      
	public String getBoardInfoName(int board_info_idx) { //与えられた掲示板インデックスに該当する掲示板の名前を返還
		return boardDao.getBoardInfoName(board_info_idx);
	}
	
	public List<ContentBean> getContentList(int board_info_idx, int page){ //与えられた掲示板インデックスとページ番号に従って掲示物リストを返却
		
		int start = (page - 1) * page_listcnt; //ページ番号に基づいてデータベースで照会する投稿の開始位置を計算
		RowBounds rowBounds = new RowBounds(start, page_listcnt); //RowBoundsオブジェクトを使用してページネーションを処理
		
		return boardDao.getContentList(board_info_idx, rowBounds); //boardDaoのsearchContentListメソッドを呼び出し、検索結果を取得する
	}
	
    public List<CommentBean> getCommentList(int contentIdx){ 
    	
    	
	
		return commentDao.getCommentList(contentIdx);
	}
	
	
	public List<ContentBean> searchContentList(int board_info_idx, String searchKeyword){ //searchContentListメソッドはboardDaoで検索されたコンテンツリストを返却
		return boardDao.searchContentList(board_info_idx, searchKeyword);
		
	}
	
	public List<ContentBean> searchContent(int board_info_idx, String searchContent){ //searchContentListメソッドはboardDaoで検索されたコンテンツリストを返却
		return boardDao.searchContent(board_info_idx, searchContent);
		
	}
	
	
	public ContentBean getContentInfo(int content_idx) { //与えられた掲示物インデックスに該当する掲示物の詳細情報を返還
		return boardDao.getContentInfo(content_idx);
	}
	
	public void modifyContentInfo(ContentBean modifyContentBean) { //既存の掲示物を修正
		
		MultipartFile upload_file = modifyContentBean.getUpload_file(); //アップロードされたファイルがあればsave Upload Fileメソッドを使用してファイルを保存し、ファイル名をContent Beanに設定
		
		if(upload_file.getSize() > 0) {
			String file_name = saveUploadFile(upload_file);
			modifyContentBean.setContent_file(file_name);
		}
		
		boardDao.modifyContentInfo(modifyContentBean);
	}
	
	public void deleteContentInfo(int content_idx) { //与えられた投稿インデックスに該当する投稿を削除する
		boardDao.deleteContentInfo(content_idx);
	}
	
	public void deleteCommentInfo(int comment_id) { 
		commentDao.deleteCommentInfo(comment_id);
	}
	
	public PageBean getContentCnt(int content_board_idx, int currentPage) { //掲示板の総掲示物数を計算し、これを基にPageBeanオブジェクトを生成して返却する
		
		int content_cnt = boardDao.getContentCnt(content_board_idx); //board Daoを通じて掲示物数を照会
		
		PageBean pageBean = new PageBean(content_cnt, currentPage, page_listcnt, page_paginationcnt); //PageBeanオブジェクトを作成してページネーション関連情報を計算
		
		return pageBean;
	}
}








