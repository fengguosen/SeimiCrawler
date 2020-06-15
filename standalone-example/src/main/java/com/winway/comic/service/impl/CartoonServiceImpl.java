package com.winway.comic.service.impl;

import com.winway.comic.entity.Cartoon;
import com.winway.comic.mapper.CartoonMapper;
import com.winway.comic.service.CartoonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <p>
 * 漫画表 服务实现类
 * </p>
 *
 * @author sam.feng
 * @since 2020-05-28
 */
@Service
@Transactional
public class CartoonServiceImpl extends ServiceImpl<CartoonMapper, Cartoon> implements CartoonService {

}
