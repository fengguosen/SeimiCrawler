package com.winway.comic.service.impl;

import com.winway.comic.entity.CartoonType;
import com.winway.comic.mapper.CartoonTypeMapper;
import com.winway.comic.service.CartoonTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <p>
 * 漫画类别对应表，漫画类别多对多关系 服务实现类
 * </p>
 *
 * @author sam.feng
 * @since 2020-06-15
 */
@Service
@Transactional
public class CartoonTypeServiceImpl extends ServiceImpl<CartoonTypeMapper, CartoonType> implements CartoonTypeService {

}
