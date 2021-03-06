package com.nokia.xpress.now.common.util.news;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.dom4j.Element;

import com.nokia.xpress.now.common.util.RequestUtil;
import com.nokia.xpress.now.entity.news.News;
import com.nokia.xpress.now.entity.news.Rss;

public class IfengRSSParser implements IRSSParser {
	private static DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.ENGLISH);

	@Override
	public List<News> parseToEntity(Rss rss) throws Exception {
		List<News> result = new ArrayList<News>();
		if (rss != null && rss.getLink() != null && !rss.getLink().trim().equals("")) {
			Element root = RequestUtil.requestAsXML(new URL(rss.getLink()));
			Element channel = root.element("channel");
			@SuppressWarnings("unchecked")
			List<Element> items = channel.elements("item");
			if (items != null)
				for (Element item : items) {
					if (item != null) {
						News news = new News();
						news.setRss(rss);
						news.setTitle(item.elementText("title"));
						news.setLink(item.elementText("link"));
						String imageStr = item.elementText("image");
						if (imageStr != null && !imageStr.trim().equals(""))
							news.setImage(imageStr);
						news.setSummary(item.elementText("summary"));
						news.setDescription(item.elementText("description"));
						news.setPubDate(dateFormat.parse(item.elementText("pubDate")));
						news.setEnable(true);
						news.setCreateDate(new Date());
						news.setModifyDate(new Date());
						result.add(news);
					}
				}
		}
		return result;
	}
}
