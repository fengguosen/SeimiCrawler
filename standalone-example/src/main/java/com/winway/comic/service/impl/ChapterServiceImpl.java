package com.winway.comic.service.impl;

import com.winway.comic.entity.Chapter;
import com.winway.comic.mapper.ChapterMapper;
import com.winway.comic.service.ChapterService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

}
