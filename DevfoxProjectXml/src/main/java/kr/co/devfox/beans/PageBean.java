package kr.co.devfox.beans;

public class PageBean {
	
	// 最小ページ番号
	private int min;
	// 最大ページ番号
	private int max;
	// 前ボタンのページ番号
	private int prevPage;
	// 次のボタンのページ番号
	private int nextPage;
	// 全ページ数
	private int pageCnt;
	// 現在のページ番号
	private int currentPage;
	
	
	// content Cnt : 全文数、current Page : 現在の文番号、content Page Cnt : ページ当たり文の数、pagination Cnt : ページボタンの数
	public PageBean(int contentCnt, int currentPage, int contentPageCnt, int paginationCnt) {
		
		// 現在のページ番号
		this.currentPage = currentPage;
		
		// 全ページ数
		pageCnt = contentCnt / contentPageCnt;
		if(contentCnt % contentPageCnt > 0) {
			pageCnt++;
		}
		
		min = ((currentPage - 1) / contentPageCnt) * contentPageCnt + 1;
		max = min + paginationCnt - 1;
		
		if(max > pageCnt) {
			max = pageCnt;
		}
		
		prevPage = min - 1;
		nextPage = max + 1;
		if(nextPage > pageCnt) {
			nextPage = pageCnt;
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
