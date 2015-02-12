package com.pluslibrary.common;

import java.io.InputStream;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.pluslibrary.PlusConstants;
import com.pluslibrary.server.PlusXmlParser;

public class PlusParser extends PlusXmlParser {

	public ArrayList<PlusModel> doIt(InputStream in) {

		ArrayList<PlusModel> model = new ArrayList<PlusModel>();
		try {

			// XmlPullParser xml데이터를 저장
			mXpp.setInput(in, PlusConstants.SERVER_ENCODING_TYPE);

		} catch (XmlPullParserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {

			// 이벤트 저장할 변수선언
			int eventType = mXpp.getEventType();
			boolean isItemTag = false; // <item> .영역에 인지 여부 체크
			String tagName = "";
			String img1 = "";
			String idx = "";
			String title = "";
			String content = "";
			String time1 = "";
			String time2 = "";

			// xml의 데이터의 끝까지 돌면서 원하는 데이터를 얻어옴
			while (eventType != XmlPullParser.END_DOCUMENT) {

				if (eventType == XmlPullParser.START_TAG) { // 시작 태그를 만났을때.
					// 태그명을 저장
					tagName = mXpp.getName();
					if (tagName.equals("ROW"))
						isItemTag = true;

				} else if (eventType == XmlPullParser.TEXT) { // 내용
					if (isItemTag && tagName.equals("idx"))
						idx = mXpp.getText();
					if (isItemTag && tagName.equals("img1"))
						img1 = mXpp.getText();
					if (isItemTag && tagName.equals("title"))
						title = mXpp.getText();
					if (isItemTag && tagName.equals("content"))
						content = mXpp.getText();

					if (isItemTag && tagName.equals("time1"))
						time1 = mXpp.getText();
					if (isItemTag && tagName.equals("time2"))
						time2 = mXpp.getText();

				} else if (eventType == XmlPullParser.END_TAG) { // 닫는 태그를 만났을때
					// 태그명을 저장
					tagName = mXpp.getName();

					if (tagName.equals("ROW")) {
						PlusModel data = new PlusModel();
						data.idx = idx;
						data.img1 = img1;
						data.title = title;
						data.content = content;
						data.time1 = time1;
						data.time2 = time2;

						model.add(data);

						idx = "";
						img1 = "";
						title = "";
						content = "";
						time1 = "";
						time2 = "";

					}

				}

				eventType = mXpp.next(); // 다음 이벤트 타입
			}

		} catch (Exception e) {
			return null;
		}

		return model;

	}
}