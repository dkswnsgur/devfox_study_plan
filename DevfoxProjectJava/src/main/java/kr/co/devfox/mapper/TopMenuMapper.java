package kr.co.devfox.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import kr.co.devfox.beans.BoardInfoBean;

public interface TopMenuMapper { //MyBatisインタフェースで、データベースから上段メニュー情報を照会するSQLクエリを提供
	//このクエリーは、board_info_tableというテーブルでboard_info_idxとboard_info_nameの2つのカラムを照会 order by board_info_idxは、その結果をbOard_infaidxを基準に昇順に整列
	@Select("select board_info_idx, board_info_name " +
			"from board_info_table " + 
			"order by board_info_idx")
	List<BoardInfoBean> getTopMenuList();
}
