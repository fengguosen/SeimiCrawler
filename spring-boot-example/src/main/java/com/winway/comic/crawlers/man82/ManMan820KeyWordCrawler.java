package com.winway.comic.crawlers.man82;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import com.winway.comic.entity.Cartoon;
import com.winway.comic.entity.Chapter;
import com.winway.comic.util.ServiceJDBCUtils;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.HttpMethod;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import lombok.Data;

/**
 * https://www.mm820.com/爬虫
 * @author sam.feng
 * @date 2020年6月1日
 */
@Crawler(name = "manMan820KeyWordCrawler")
@Data
//@Transactional
public class ManMan820KeyWordCrawler extends BaseSeimiCrawler{

	
	private final static String url_prex= "https://www.mm820.com";
	private final static String url= "https://www.mm820.com/book/1051/";
	@Override
	public String[] startUrls() {
		return new String[]{url};
	}

	@Override
	public void start(Response response) {
		JXDocument doc = response.document();
		crawSingleCartoon(doc);
	}
	
	public void crawSingleCartoon(JXDocument doc) {
		try {
			JXNode comicCoverNode= doc.selNOne("//div[@class='comic-cover']");
			JXNode comicCoverImgNode=comicCoverNode.selOne("//img");
			String coverStyle = comicCoverImgNode.asElement().attr("data-src");
			
			
    		JXNode comicInfoNode= doc.selNOne("//div[@class='comic-intro']");
    		JXNode titleWarperNode =comicInfoNode.selOne("//div[@class='title-warper']");
    		JXNode titleNode =titleWarperNode.selOne("//h1[@class='title']");
    		String title = titleNode.asElement().text(); //标题
    		JXNode attrWarperNode =comicInfoNode.selOne("//div[@class='attr']");
    		JXNode typeNode = attrWarperNode.selOne("//a");
    		String type = typeNode.asElement().text();
    		
    		Cartoon cartoonEntity = new Cartoon();
			cartoonEntity.setCover(coverStyle);
			cartoonEntity.setTitle(title);
			cartoonEntity.setType(type);
			cartoonEntity.setUrl(url);

			
			Integer cartoonId = ServiceJDBCUtils.insertCartoon(cartoonEntity);
    		
    		JXNode comicListBoxNode= doc.selNOne("//ul[@class='chapter-list clearfix']");
    		List<JXNode> chapterList = comicListBoxNode.sel("//li[@class='item']");
    		for(JXNode chapter:chapterList) {
    			
    			String charpterUrl = url_prex+chapter.selOne("//a").asElement().attr("href");
    			Map<String, Object> metaMap = new HashMap<String, Object>();  //已经获取的参数
    			metaMap.put("cartoonId", cartoonId);
    			synchronized (ManMan820KeyWordCrawler.class) {
    				push(Request.build(charpterUrl,"getContent",HttpMethod.GET,null,metaMap));
    			}
    			
    		}
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	

	
//	/**
//	 * 获取章节
//	 * @date 2020年5月28日
//	 * @author sam.feng
//	 * @param response
//	 */
//	public  void  getChapter(Response response) {
//		Map<String, Object> metaMap = response.getMeta();
//		JXDocument doc = response.document();
//		List<JXNode> list= doc.selN("//ul[@class='chapter-list clearfix']//li[@class='item']");
//		for(JXNode chapterNode: list) {
//			synchronized (ManMan820KeyWordCrawler.class) {
//				JXNode aNode= chapterNode.selOne("//a");
//				String chaperName = aNode.asElement().text();			//章节名称
//				String chaperUrl = url_prex + aNode.asElement().attr("href");   //章节地址
////				metaMap.put("chaperName", chaperName);
////				metaMap.put("count", count);
//				push(Request.build(chaperUrl,"getContent",HttpMethod.GET,null,metaMap));
//			}
//			
//		}
//	}
	
	/**
	 * 获取内容
	 * @date 2020年5月28日
	 * @author sam.feng
	 * @param response
	 */
	public void getContent(Response response) {
		synchronized (ManMan820KeyWordCrawler.class) {
			Map<String, Object> metaMap = response.getMeta();
			Integer cartoonId= (Integer) metaMap.get("cartoonId");
			StringBuffer sBuffer = new StringBuffer();
			JXDocument doc = response.document();
			JXNode chaperNameNode= doc.selNOne("//div[@class='header']//h1[@class='title']");
			JXNode numNode= doc.selNOne("//div[@class='header']//span[@class='curPage']");
			String chaperName = chaperNameNode.asElement().text();
			Integer count;
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
//			chapterService.save(chapterEntity);
			ServiceJDBCUtils.insertChapter(chapterEntity);
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
