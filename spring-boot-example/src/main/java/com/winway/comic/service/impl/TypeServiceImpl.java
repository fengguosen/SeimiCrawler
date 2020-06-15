package com.winway.comic.service.impl;

import com.winway.comic.entity.Type;
import com.winway.comic.mapper.TypeMapper;
import com.winway.comic.service.TypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * <p>
 * 漫画类别表 服务实现类
 * </p>
 *
 * @author sam.feng
 * @since 2020-06-15
 */
@Service
@Transactional
public class TypeServiceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

}
