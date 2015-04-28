package com.gntsoft.flagmon.server;

import android.util.Log;

import com.pluslibrary.PlusConstants;
import com.pluslibrary.server.PlusXmlParser;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * 목적지 파서
 * 
 * @author jeff
 * 
 */
public class PlaceParser extends PlusXmlParser {

	public ArrayList<PlaceModel> doIt(InputStream in) {

		ArrayList<PlaceModel> datas = new ArrayList<PlaceModel>();

		try {

			// XmlPullParser xml데이터를 저장
			mXpp.setInput(in, PlusConstants.SERVER_ENCODING_TYPE);

			// 이벤트 저장할 변수선언
			int eventType = mXpp.getEventType();

			boolean isItemTag = false; // <item> .영역에 인지 여부 체크
			String tagName = "";
			String name = "";
			String address = "";
			String lat = "";
			String lng = "";

			// xml의 데이터의 끝까지 돌면서 원하는 데이터를 얻어옴
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) { // 시작 태그를 만났을때.
					// 태그명을 저장
					tagName = mXpp.getName();
					if (tagName.equals("result"))
						isItemTag = true;

				} else if (eventType == XmlPullParser.TEXT) { // 내용
					if (isItemTag && tagName.equals("name") && name.equals(""))
						name = mXpp.getText();
					if (isItemTag && tagName.equals("formatted_address")
							&& address.equals(""))
						address = mXpp.getText();
					if (isItemTag && tagName.equals("lat") && lat.equals(""))
						lat = mXpp.getText();
					if (isItemTag && tagName.equals("lng") && lng.equals(""))
						lng = mXpp.getText();

				} else if (eventType == XmlPullParser.END_TAG) { // 닫는 태그를 만났을때
					// 태그명을 저장
					tagName = mXpp.getName();

					if (tagName.equals("result")) {

						PlaceModel data = new PlaceModel();

						data.setName(name);
						data.setAddress(address);
						data.setLat(lat);
						data.setLng(lng);
						datas.add(data);

						isItemTag = false; // 초기화
						tagName = "";
						name = "";
						address = "";
						lat = "";
						lng = "";

					}

				}

				eventType = mXpp.next(); // 다음 이벤트 타입
			}

		} catch (Exception e) {
			Log.d(PlusConstants.LOG_TAG, e.getMessage());
			datas = null;

		}

		return datas;
	}
}