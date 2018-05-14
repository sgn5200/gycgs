package com.shang.cannan.car.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Description    :
 * CreateAuthor: Cannan
 * CreateTime   : 2018/5/14     10:35
 * Project          : Car
 * PackageName :  com.shang.cannan.car.util;
 */

public class ParsHtmlUtils {

	public static 	 String getErrorInfo(String html){
		Document document= Jsoup.parse(html);
		Elements er = document.body().select("div.validation-summary-errors");
		Elements error = er.select("ul li");
		return error.text();
	}
}
