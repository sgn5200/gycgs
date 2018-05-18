package com.shang.cannan.car.util;

import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/14     10:35
 * Project          : Car
 * PackageName :  com.shang.cannan.car.util;
 */

public class ParsHtmlUtils {

	public static  String getErrorInfo(String html){
		Document document= Jsoup.parse(html);
		Elements er = document.body().select("div.validation-summary-errors");
		Elements error = er.select("ul li");
		return error.text();
	}

	public static  ArrayList<String> getInfo(String html){
		ArrayList<String> list= new ArrayList<>();
		Document document= Jsoup.parse(html);
		Elements lable = document.body().select("form div");

		if(lable==null || lable.size()==0){
			return null;
		}

		int i=0;
		for(Element element : lable){
			if("fieldcontain".equals(element.attr("data-role"))){
				String t1=element.text();
				if(TextUtils.isEmpty(t1) || t1.contains("办理所需资料清单")){
					continue;
				}
				list.add(t1);
			}
		}
		return list;
	}

}
