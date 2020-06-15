package cn.wanghaomiao.crawlers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.http.HttpMethod;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;

@Crawler(name = "cartoonCrawler")
public class CartoonCrawler extends BaseSeimiCrawler{

	private final static String url_prex= "https://www.mm820.com";
	@Override
	public String[] startUrls() {
		return new String[]{"https://www.mm820.com/"};
	}

	@Override
	public void start(Response response) {
		JXDocument doc = response.document();
        try {
//        	List<JXNode> list= baseNode.sel("//div[@class='row mt24']");
        	List<JXNode> list= doc.selN("//div[@class='row mt24']");
        	for (int i = 1; i < list.size(); i++) {
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
        			Map<String, Object> metaMap = new HashMap<String, Object>();  //已经获取的参数
        			metaMap.put("typeTitle", typeTitle);
        			metaMap.put("title", title);
        			metaMap.put("url", url);
        			metaMap.put("cover", cover);
        			push(Request.build(url,"getChapter",HttpMethod.GET,null,metaMap));
        			return;
//        			System.out.println(cover);
        			
        		}
        		
			}
        	
//            logger.info("{}", urls.size());
//            for (Object s:urls){
//                push(Request.build(s.toString(),Basic::getTitle));
//            }
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
	public void getChapter(Response response) {
		Map<String, Object> metaMap = response.getMeta();
		JXDocument doc = response.document();
		List<JXNode> list= doc.selN("//ul[@class='chapter-list clearfix']//li[@class='item']");
		for(JXNode chapterNode: list) {
			JXNode aNode= chapterNode.selOne("//a");
			String chaperName = aNode.asElement().text();			//章节名称
			String chaperUrl = url_prex + aNode.asElement().attr("href");   //章节地址
			metaMap.put("chaperName", chaperName);
			metaMap.put("chaperUrl", chaperUrl);
			push(Request.build(chaperUrl,"getContent",HttpMethod.GET,null,metaMap));
			return;
		}
	}
	
	/**
	 * 获取内容
	 * @date 2020年5月28日
	 * @author sam.feng
	 * @param response
	 */
	public void getContent(Response response) {
		Map<String, Object> metaMap = response.getMeta();
		JXDocument doc = response.document();
		List<JXNode> list= doc.selN("//div[@class='comicpage']");
		for(JXNode contentNode: list) {
			JXNode imgNode= contentNode.selOne("//img");
			String img = imgNode.asElement().attr("src");
			System.out.println(img);
		}
	}

}
