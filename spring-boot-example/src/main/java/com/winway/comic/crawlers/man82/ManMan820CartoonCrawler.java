package com.winway.comic.crawlers.man82;

import java.util.Date;
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
import com.winway.comic.util.T;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.HttpMethod;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import lombok.extern.slf4j.Slf4j;

/**
 * https://www.mm820.com/爬虫
 * @author sam.feng
 * @date 2020年6月1日
 */
@Crawler(name = "manMan820CartoonCrawler")
@Slf4j
//@Transactional
public class ManMan820CartoonCrawler extends BaseSeimiCrawler{

	@Autowired
	private CartoonService cartoonService;
	
	@Autowired
	private ChapterService chapterService;
	
	private final static String url_prex= "https://www.mm820.com";
	
  @Scheduled(cron = "0 15 0 ? * 1,3,5")  //周一,周三，周五00:15开始爬
  public void callByCron(){
      logger.info("ManMan820定时爬虫开始");
      // 可定时发送一个Request
       push(Request.build(url_prex,"start").setSkipDuplicateFilter(true));
  }
  
	@Override
	public String[] startUrls() {
		log.info("ManMan820首页爬虫开始");
		return new String[]{"https://www.mm820.com/"};
	}

	@Override
	public void start(Response response) {
		JXDocument doc = response.document();
		crawAllCartoon(doc);
//		logger.info("ManMan820爬虫结束");
	}
	
	public void crawAllCartoon(JXDocument doc) {
        try {
        	List<JXNode> list= doc.selN("//div[@class='row mt24']");
        	for (int i = 1; i < list.size(); i++) {
//        	for (int i = 1; i < 3; i++) {
        		JXNode node= list.get(i);
        		JXNode typeNode = node.selOne("//p[@class='title-desc']");
        		String typeTitle = typeNode.asElement().text();  //漫画分类
        		List<JXNode> cartoonList = node.sel("//li[@class='item']");
        		for(JXNode cartoon:cartoonList) {
        			
        			
        			if(cartoon==null) return;
        			JXNode aNode= cartoon.selOne("//a[@class='thumbnail']");
        			if(aNode==null) return;
        			String title = aNode.asElement().attr("title");			//标题
        			String url = url_prex + aNode.asElement().attr("href");  //访问地址
        			JXNode imgNode= aNode.selOne("//img");					
        			String cover = imgNode.asElement().attr("data-src");		//封面
        			
        			
        			Cartoon entity= cartoonService.getCartoon(title, url);
        			Map<String, Object> metaMap = new HashMap<String, Object>();  //已经获取的参数
        			Integer cartoonId = 0;
        			if(entity!=null) {
        				
//        				Chapter chapter=chapterService.getByMaxCartoonId(entity.getId());
//        				String name= chapter.getName();
        				cartoonId = entity.getId();
        				
        			}else { //漫画从未爬取，全部抓取
        				
            			Cartoon cartoonEntity = new Cartoon();
            			cartoonEntity.setCover(cover);
            			cartoonEntity.setTitle(title);
            			cartoonEntity.setType(typeTitle);
            			cartoonEntity.setUrl(url);
            			cartoonEntity.setCreateTime(new Date());
            			cartoonService.save(cartoonEntity);
            			cartoonId = cartoonEntity.getId();
        			}
        			metaMap.put("cartoonId", cartoonId);
        			synchronized (ManMan820CartoonCrawler.class) {
        				push(Request.build(url,"getChapter",HttpMethod.GET,null,metaMap));
					}
        		}
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 获取章节
	 * @date 2020年5月28日
	 * @author sam.feng
	 * @param response
	 */
	public  void  getChapter(Response response) {
		boolean isUpdate =false;
		Map<String, Object> metaMap = response.getMeta();
		JXDocument doc = response.document();
		Integer cartoonId=(Integer) metaMap.get("cartoonId");
		JXNode descNode= doc.selNOne("//div[@class='desc-con']");
		String desc = descNode.asElement().text().trim();
		float score = T.getScore();
		cartoonService.updateById(cartoonId, desc, score);
		List<JXNode> list= doc.selN("//ul[@class='chapter-list clearfix']//li[@class='item']");
		for(JXNode chapterNode: list) {
			synchronized (ManMan820CartoonCrawler.class) {
				JXNode aNode= chapterNode.selOne("//a");
				String chaperName = aNode.asElement().text();			//章节名称
				String chaperUrl = url_prex + aNode.asElement().attr("href");   //章节地址
				Chapter existEntity= chapterService.getByCartoonIdAndName(cartoonId, chaperName);
				if(existEntity==null) {
					//还没爬虫，
					push(Request.build(chaperUrl,"getContent",HttpMethod.GET,null,metaMap));
					isUpdate = true;
				}
				
			}
			
		}
		if(isUpdate) {
			cartoonService.updateNewDate(cartoonId);
		}
		
	}
	
	/**
	 * 获取内容
	 * @date 2020年5月28日
	 * @author sam.feng
	 * @param response
	 */
	public void getContent(Response response) {
		synchronized (ManMan820CartoonCrawler.class) {
			Integer cartoonId = 0;
			StringBuffer sBuffer;
			String chaperName;
			Integer count;
			try {
				Map<String, Object> metaMap = response.getMeta();
				cartoonId = (Integer) metaMap.get("cartoonId");
				sBuffer = new StringBuffer();
				JXDocument doc = response.document();
				JXNode chaperNameNode= doc.selNOne("//div[@class='header']//h1[@class='title']");
				JXNode numNode= doc.selNOne("//div[@class='header']//span[@class='curPage']");
				chaperName = chaperNameNode.asElement().text();
				try {
					count = Integer.parseInt(numNode.asElement().text());
				} catch (NumberFormatException e) {
					count = 00;
				}
				List<JXNode> list= doc.selN("//div[@class='comicpage']");
				for(JXNode contentNode: list) {
					JXNode imgNode= contentNode.selOne("//img");
					String img = imgNode.asElement().attr("src");
					sBuffer.append(img).append(","); 
					
				}
				Chapter chapterEntity = new Chapter();
				chapterEntity.setCartoonId(cartoonId);
				chapterEntity.setName(chaperName);
				chapterEntity.setNum(count);
				chapterEntity.setContent(sBuffer.toString());
				chapterEntity.setCreateTime(new Date());
				chapterService.save(chapterEntity);
			} catch (Exception e1) {
				log.error("卡通id:{}",cartoonId);
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
