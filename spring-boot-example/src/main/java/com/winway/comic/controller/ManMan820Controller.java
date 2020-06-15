package com.winway.comic.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.winway.comic.crawlers.man82.ManMan820KeyWordCrawler;
import com.winway.comic.entity.Cartoon;
import com.winway.comic.service.CartoonService;

import cn.wanghaomiao.seimi.core.Seimi;


@RequestMapping("/manman820")
@RestController
public class ManMan820Controller {

//	@Autowired
//	private ManMan820KeyWordCrawler man820KeyWordCrawler;
	
	@Autowired
	private CartoonService cartoonService;
	
	

	
//	@GetMapping("/crawlByKeyword/{keyWord}")
//	public void crawlByKeyword(@PathVariable String keyWord) {
//		ManMan820KeyWordCrawler man820KeyWordCrawler = new ManMan820KeyWordCrawler();
//		man820KeyWordCrawler.setKeyWord(keyWord);
//		 Seimi s = new Seimi();
//	     s.goRun("manMan820KeyWordCrawler");
////	     man820KeyWordCrawler.start(response);
//	}
	
	@GetMapping("/updateScore")
	public void crawlByKeyword() {
		int count = cartoonService.count();
		
		int pageNo = 1;
		int pageSize = 1000;
		int start = 1;
		int pageSum=(count-1)/pageSize+1;
		for(int i=1;i<pageSum;i++) {
			start = (i-1)*pageSize;
			int pageMax = i * pageSize;
			List<Cartoon> all= cartoonService.pageAll(start, start+1000);
			for(Cartoon cartoon :all) {
				cartoon.setScore(getScore());
			}
			cartoonService.updateBatchById(all);
			
		}
		
		
		
	}
	
	
	public static float getScore() {
		float min = 9f;
		float max = 9.8f;
		DecimalFormat decimalFormat=new DecimalFormat(".0");
		float floatBounded = min + new Random().nextFloat() * (max - min);
		float result= Float.parseFloat(decimalFormat.format(floatBounded)) ;
		 return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getScore());
	}
	
}
