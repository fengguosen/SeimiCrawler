package com.winway.comic.service.impl;

import com.winway.comic.entity.Cartoon;
import com.winway.comic.entity.Chapter;
import com.winway.comic.mapper.ChapterMapper;
import com.winway.comic.service.ChapterService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.awt.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <p>
 * 漫画章节表 服务实现类
 * </p>
 *
 * @author sam.feng
 * @since 2020-05-28
 */
@Service
@Transactional
public class ChapterServiceImpl extends ServiceImpl<ChapterMapper, Chapter> implements ChapterService {

	@Autowired
	private ChapterMapper chapterMapper;
	
//	public int getMaxChapter(Integer cartoonId) {
//		
//	}
	
	@Override
	public Chapter getByCartoonIdAndName(Integer cartoonId,String name) {
		QueryWrapper<Chapter> queryWrapper = new QueryWrapper<Chapter>();
		queryWrapper.eq("cartoon_id", cartoonId);
		queryWrapper.eq("name", name);
		queryWrapper.orderByDesc("num","create_time");
		queryWrapper.last("limit 1");
		return chapterMapper.selectOne(queryWrapper);
	}
}
