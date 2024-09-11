package kr.co.devfox.controller;

import java.util.List;


import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.devfox.beans.ContentBean;
import kr.co.devfox.beans.PageBean;
import kr.co.devfox.beans.UserBean;
import kr.co.devfox.service.BoardService;

@Controller //クラスがスプリングMVCのコントローラであることを示すクライアント要求を処理し、モデルデータを返却
@RequestMapping("/board") //クラスにマッピングされたURLが/boardで始まる要求を処理することを意味する
public class BoardController { //掲示板crudを使用するboardクラス
	
	@Autowired //掲示板に関連するビジネスロジックを処理するサービスクラススプリングの依存性注入(DI)機能で自動的に接続
	private BoardService boardService;
	
	@Resource(name = "loginUserBean") //セッションに保存されたloginUserBeanオブジェクトを参照
	@Lazy
	private UserBean loginUserBean;
	
	@GetMapping("/main") ///board/mainに入ってくるGET要請を処理
	public String main(@RequestParam("board_info_idx") int board_info_idx, //board_info_idxはどんな掲示板なのかを識別するインデックス
					   @RequestParam(value = "page", defaultValue = "1") int page, //現在見ているページ番号で、既定値は1
					   Model model) {
		
		model.addAttribute("board_info_idx", board_info_idx); //モデルにデータを盛り込み、ビューに配信
		
		String boardInfoName = boardService.getBoardInfoName(board_info_idx);
		model.addAttribute("boardInfoName", boardInfoName); //該当掲示板の名前を取得してビューに送信
		
		List<ContentBean> contentList = boardService.getContentList(board_info_idx, page);
		model.addAttribute("contentList", contentList); // 現在のページに該当する投稿リストを取得する
		
		PageBean pageBean = boardService.getContentCnt(board_info_idx, page);
		model.addAttribute("pageBean", pageBean); //全投稿数をもとにページネーション情報を取得し、ビューに配信
		
		model.addAttribute("page", page);
		
		return "board/main"; //"board/main"を返し、JSPで掲示板リストとページネーションを表示できるようにする
	}
	
	@GetMapping("/read") ///board/readに入ってくるGET要請を処理
	public String read(@RequestParam("board_info_idx") int board_info_idx, //board_info_idxはどんな掲示板なのかを識別するインデックス
					   @RequestParam("content_idx") int content_idx, //読もうとする投稿のID
					   @RequestParam("page") int page, //現在見ているページ番号で、既定値は1
					   Model model) {
		
		model.addAttribute("board_info_idx", board_info_idx); ////board_info_idxはどんな掲示板なのかを識別するインデックスを探してビューに伝達
		model.addAttribute("content_idx", content_idx); //読みたい投稿のインデックスをビューに配信
		
		ContentBean readContentBean = boardService.getContentInfo(content_idx);
		model.addAttribute("readContentBean", readContentBean); //該当IDの掲示文情報を取得してビューに伝達
		
		model.addAttribute("loginUserBean", loginUserBean); //ログインしたユーザー情報をモデルに入れて伝達する。 この情報は、当該ユーザが掲示文の作成者であるか否かを判断
		model.addAttribute("page", page);
		
		return "board/read"; //「board/read」ビューを返し、ユーザーが選択した掲示文を読める画面を表示する
	}
	
	@GetMapping("/write") ///board/writeに入ってくるGET要請を処理して書き込みページを表示する
	public String write(@ModelAttribute("writeContentBean") ContentBean writeContentBean, //オブジェクトは投稿の作成に使用される。 これはJSPフォームでユーザ入力を入れる空のオブジェクトに設定
						@RequestParam("board_info_idx") int board_info_idx) { //作成する掲示板がどの掲示板に属するかを識別するために掲示板IDを設定
		
		writeContentBean.setContent_board_idx(board_info_idx);
		
		return "board/write"; //「board/write」ビューを返し、ユーザーが掲示文の作成画面を表示する
	}
	
	@PostMapping("/write_pro") //board/write_proで入ってくるPOST要請を処理して掲示文を保存
	public String write_pro(@Valid @ModelAttribute("writeContentBean") ContentBean writeContentBean, BindingResult result) { //writeContentBeanに対する有効性検査を行います。 検証に失敗すると、再び作成ページに移動し、BindingResultオブジェクトで有効性検査結果を受け取り、エラーがある場合は、再び作成ページにリダイレクト
		if(result.hasErrors()) {
			return "board/write";
		}
		
		boardService.addContentInfo(writeContentBean); //サービスで投稿を実際に保存するロジックを処理
		
		return "board/write_success"; //「board/write_success」ビューを返し、ユーザーが掲示文の作成に成功した時に画面を表示する
	}
	
	@GetMapping("/modify") ///board/modifyで入ってくるGET要請を処理して文の修正画面を表示し
	public String modify(@RequestParam("board_info_idx") int board_info_idx,
						 @RequestParam("content_idx") int content_idx,
						 @ModelAttribute("modifyContentBean") ContentBean modifyContentBean,
						 @RequestParam("page") int page,
						 Model model) {
		
		model.addAttribute("board_info_idx", board_info_idx);
		model.addAttribute("content_idx", content_idx);
		model.addAttribute("page", page);
		
		ContentBean tempContentBean = boardService.getContentInfo(content_idx);
		
		modifyContentBean.setContent_writer_name(tempContentBean.getContent_writer_name());
		modifyContentBean.setContent_date(tempContentBean.getContent_date());
		modifyContentBean.setContent_subject(tempContentBean.getContent_subject());
		modifyContentBean.setContent_text(tempContentBean.getContent_text());
		modifyContentBean.setContent_file(tempContentBean.getContent_file());
		modifyContentBean.setContent_writer_idx(tempContentBean.getContent_writer_idx());
		modifyContentBean.setContent_board_idx(board_info_idx);
		modifyContentBean.setContent_idx(content_idx);
	
		return "board/modify"; //「board/modify」ビューを返し、ユーザーが掲示文の修正画面を表示する
	} 
	
	@PostMapping("/modify_pro") //board/modify_proで入ってくるPOSTリクエストを処理
	public String modify_pro(@Valid @ModelAttribute("modifyContentBean") ContentBean modifyContentBean, //作成された修正内容を検証し、問題がなければ修正された内容を保存	 
							 BindingResult result,
							 @RequestParam("page") int page,
							 Model model) {
		
		model.addAttribute("page", page);
		
		if(result.hasErrors()) {
			return "board/modify";
		}
		
		boardService.modifyContentInfo(modifyContentBean); //サービスで掲示文を修正するロジックを処理
		
		return "board/modify_success"; //"board/modify_success"ビューを返し、ユーザーが掲示文修正成功時に成功画面を表示する
	}
	
	@GetMapping("/delete") //board/deleteに入ってくるGET要請を処理して掲示
	public String delete(@RequestParam("board_info_idx") int board_info_idx,
						 @RequestParam("content_idx") int content_idx,
						 Model model) {
		
		boardService.deleteContentInfo(content_idx); //サービスから掲示文を削除するロジックを処理
		
		model.addAttribute("board_info_idx", board_info_idx);
		
		return "board/delete"; //"board/delete"ビューを返し、ユーザーが掲示文の削除に成功すると、成功画面が表示される
	}
	
	@GetMapping("/not_writer")
	public String not_writer() { //ユーザーが作成者でないときに適切な権限がないことを知らせるページを表示するときに使用するメソッド
		return "board/not_writer";
	}
}








