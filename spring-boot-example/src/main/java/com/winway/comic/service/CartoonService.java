package com.winway.comic.service;

import com.winway.comic.entity.Cartoon;
import com.winway.comic.mapper.CartoonMapper;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 漫画表 服务类
 * </p>
 *
 * @author sam.feng
 * @since 2020-05-28
 */
public interface CartoonService extends IService<Cartoon> {

	boolean isExit(String title, String url);

	List<Cartoon> listAll();

	void updateById(Integer id, String introduction, float score);

	Cartoon getCartoon(String title, String url);

	void updateNewDate(Integer id);

	void updateNewDateAndType(Integer id, String type);

	List<Cartoon> pageAll(int pageNo, int pageSize);


}
