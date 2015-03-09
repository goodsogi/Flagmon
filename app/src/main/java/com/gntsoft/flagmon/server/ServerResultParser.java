package com.gntsoft.flagmon.server;

import com.pluslibrary.server.PlusInputStreamStringConverter;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * 서버 응답 파서
 *
 * @author jeff
 */
public class ServerResultParser {

    public ServerResultModel doIt(String serverResultData) {
        ServerResultModel model = new ServerResultModel();
        try {

            JSONObject jsonObject = new JSONObject(serverResultData);

            model.setMsg(jsonObject.optString("t"));
            model.setResult(jsonObject.optString("result"));


        } catch (Exception e) {
            return null;
        }

        return model;
    }

}