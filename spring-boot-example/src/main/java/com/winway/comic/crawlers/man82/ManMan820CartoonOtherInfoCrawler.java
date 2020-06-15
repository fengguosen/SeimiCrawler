package com.winway.comic.crawlers.man82;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import com.winway.comic.entity.Cartoon;
import com.winway.comic.entity.Chapter;
import com.winway.comic.service.CartoonService;
import com.winway.comic.service.ChapterService;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.HttpMethod;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;

/**
 * https://www.mm820.com/爬虫
 * @author sam.feng
 * @date 2020年6月1日
 */
@Crawler(name = "manMan820CartoonOtherInfoCrawler")
//@Transactional
public class ManMan820CartoonOtherInfoCrawler extends BaseSeimiCrawler{

	@Autowired
	private CartoonService cartoonService;
	
	
	private final static String url_prex= "https://www.mm820.com";
	
  
	@Override
	public String[] startUrls() {
		List<Cartoon> list= cartoonService.listAll();
		String url= list.get(0).getUrl();
		return new String[]{url};
	}

	@Override
	public void start(Response response) {
		List<Cartoon> list= cartoonService.listAll();
		for(int i=0;i<=list.size();i++) {
			Cartoon tmpCartoon = list.get(i);
			String url= tmpCartoon.getUrl();
			Map<String, Object> metaMap = new HashMap<String, Object>();  //已经获取的参数
			metaMap.put("cartoonId", tmpCartoon.getId());
			push(Request.build(url,"getChapter",HttpMethod.GET,null,metaMap));
		}
	}
	
	
	/**
	 * 获取章节
	 * @date 2020年5月28日
	 * @author sam.feng
	 * @param response
	 */
	public  void  getChapter(Response response) {
		Map<String, Object> metaMap = response.getMeta();
		Integer cartoonId=(Integer) metaMap.get("cartoonId");
		JXDocument doc = response.document();
		JXNode descNode= doc.selNOne("//div[@class='desc-con']");
		String desc = descNode.asElement().text().trim();
		JXNode scoreNode= doc.selNOne("//div[@class='score-num']");
		String scoreStr= scoreNode.selOne("//strong").asElement().text();
		float score;
		try {
			score = Float.parseFloat(scoreStr);
		} catch (NumberFormatException e1) {
			score = 9.5f;
		}
		cartoonService.updateById(cartoonId, desc, score);
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
