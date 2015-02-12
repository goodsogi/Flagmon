package com.pluslibrary.server;

import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.pluslibrary.PlusConstants;

public class PlusXmlParserBase {
	protected XmlPullParser mXpp;

	public PlusXmlParserBase(InputStream in) {
		// XmlPullParser를 사용하기 위해서
		XmlPullParserFactory factory = null;

		try {
			factory = XmlPullParserFactory.newInstance();

			// 네임스페이스 사용여부
			factory.setNamespaceAware(true);
			// xml문서를 이벤트를 이용해서 데이터를 추출해주는 객체

			mXpp = factory.newPullParser();
			mXpp.setInput(in, PlusConstants.SERVER_ENCODING_TYPE);

		} catch (XmlPullParserException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
	}

}
