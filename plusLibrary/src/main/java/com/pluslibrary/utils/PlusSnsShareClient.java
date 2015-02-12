package com.pluslibrary.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;

/**
 * 트위터, 페이스북 공유
 * 
 * @author jeff
 * 
 */
public class PlusSnsShareClient {
	public static void doIt(Context context, String nameApp, String text,
			String filePath) {
		List<Intent> targetedShareIntents = new ArrayList<Intent>();
		Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setType("image/jpeg");
		List<ResolveInfo> resInfo = context.getPackageManager()
				.queryIntentActivities(share, 0);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				Intent targetedShare = new Intent(
						android.content.Intent.ACTION_SEND);
				targetedShare.setType("image/jpeg"); // put here your mime type

				Assert.assertNotNull(info);
				Assert.assertNotNull(info.activityInfo);
				Assert.assertNotNull(info.activityInfo.packageName);
				
				
				
				if (info.activityInfo.packageName.toLowerCase().contains(
						nameApp)
						|| info.activityInfo.name.toLowerCase().contains(
								nameApp)) {
					targetedShare.putExtra(Intent.EXTRA_TEXT, text);
					targetedShare.putExtra(Intent.EXTRA_STREAM, Uri
							.fromFile(new File(filePath)));
					targetedShare.setPackage(info.activityInfo.packageName);
					targetedShareIntents.add(targetedShare);
				}
			}
			
			
			if(targetedShareIntents.size() == 0 ) {
				
				PlusToaster.doIt(context, getSnsKoreanName(nameApp) + " 앱이 설치되지 않았습니다");
				return;
			}

			Intent chooserIntent = Intent.createChooser(
					targetedShareIntents.remove(0), "공유할 앱을 선택하세요");
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
					targetedShareIntents.toArray(new Parcelable[] {}));
			context.startActivity(chooserIntent);
		}
	}

	private static String getSnsKoreanName(String nameApp) {
		if(nameApp.equals("twitter")) return "트위터";
		if(nameApp.equals("facebook")) return "페이스북";
		
		return null;
	}
}
