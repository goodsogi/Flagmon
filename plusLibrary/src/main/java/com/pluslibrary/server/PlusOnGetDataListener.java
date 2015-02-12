package com.pluslibrary.server;



/**
 * 서버에서 데이터 가져오는 작업 리스너
 * 
 * @author jeff
 * 
 */
public interface PlusOnGetDataListener {
	// 성공
	public void onSuccess(Integer from, Object datas);
}
