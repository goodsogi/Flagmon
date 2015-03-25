package com.gntsoft.flagmon.server;

import org.json.JSONObject;

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

            if (jsonObject.has("t")) model.setMsg(jsonObject.optString("t"));
            if (jsonObject.has("msg")) model.setMsg(jsonObject.optString("msg"));
            model.setResult(jsonObject.optString("result"));


        } catch (Exception e) {
            return null;
        }

        return model;
    }

}