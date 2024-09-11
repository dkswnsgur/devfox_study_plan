package kr.co.devfox.beans;

public class PageBean {    //ページネーション情報を計算して管理するためのJavaクラス
	
	
	private int min;    //ページネーションで表示される最小ページ番号
	
	private int max;    //ページネーションで表示される最大ページ番号
	
	private int prevPage;  //"以前"ボタンをクリックしたときに移動するページ番号
	
	private int nextPage;  //"次へ"ボタンをクリックしたときに移動するページ番号

	private int pageCnt;    //全ページ数
	
	private int currentPage; //現在見ているページ番号
	
	
	
	public PageBean(int contentCnt, int currentPage, int contentPageCnt, int paginationCnt) { 
		//ページネーションに必要な値を計算するロジック入力値としては、総コンテンツ数(content Cnt)、現在のページ番号(current Page)、1ページに表示するコンテンツ数(content Page Cnt)、そしてページネーションボタンの個数(pagination Cnt)が伝達
		
		
		this.currentPage = currentPage;
		
		
		pageCnt = contentCnt / contentPageCnt;   //全ページ数=総コンテンツ数/1ページに表示するコンテンツ数
		if(contentCnt % contentPageCnt > 0) {  //総コンテンツ数% 1ページに表示するコンテンツ数 > 0
			pageCnt++; //全ページ数 ++
		}
		
		min = ((currentPage - 1) / contentPageCnt) * contentPageCnt + 1; //最小ページ番号 = ((現在のページ番号-1) / 1ページに表示するコンテンツ数) * 1ページに表示するコンテンツ数 + 1
		max = min + paginationCnt - 1;  //最大ページ番号 = 最小ページ番号 + ページネーションボタンの個数 - 1
		
		if(max > pageCnt) {  //if（最大ページ番号 > 全ページ数）
			max = pageCnt;   //最大ページ番号=全ページ数
		}
		
		prevPage = min - 1;  // "以前"ボタンをクリックしたときに移動するページ番号 = 最小ページ番号 - 1
		nextPage = max + 1;  // "次へ"ボタンをクリックしたときに移動するページ番号 = 最大ページ番号 + 1
		if(nextPage > pageCnt) {  // if（"次へ"ボタンをクリックしたときに移動するページ番号 > 全ページ数）
			nextPage = pageCnt;  // "次へ"ボタンをクリックしたときに移動するページ番号=全ページ数
		}
	}
	
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
	public int getPrevPage() {
		return prevPage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public int getPageCnt() {
		return pageCnt;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	
	
}
