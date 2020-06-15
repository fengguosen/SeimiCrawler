package com.winway.comic.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.winway.comic.entity.Cartoon;
import com.winway.comic.mapper.CartoonMapper;
import com.winway.comic.service.CartoonService;
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

	@Autowired 
	private CartoonMapper cartoonMapper;
	
	@Override
	public boolean isExit(String title,String url) {
		QueryWrapper<Cartoon> queryWrapper = new QueryWrapper<Cartoon>();
		queryWrapper.eq("title", title);
		queryWrapper.eq("url", url);
		int count = cartoonMapper.selectCount(queryWrapper);
		return count>0;
	}
	
	@Override
	public Cartoon getCartoon(String title,String url) {
		QueryWrapper<Cartoon> queryWrapper = new QueryWrapper<Cartoon>();
		queryWrapper.eq("title", title);
//		queryWrapper.eq("url", url);
		queryWrapper.last("limit 1");
		return cartoonMapper.selectOne(queryWrapper);
	}
	
	@Override
	public List<Cartoon> listAll(){
		QueryWrapper<Cartoon> queryWrapper = new QueryWrapper<Cartoon>();
//		queryWrapper.last("limit 0,1000");
		return cartoonMapper.selectList(queryWrapper);
	}
	
	@Override
	public List<Cartoon> pageAll(int pageNo,int pageSize){
		QueryWrapper<Cartoon> queryWrapper = new QueryWrapper<Cartoon>();
		queryWrapper.last("limit "+pageNo+","+pageSize);
		return cartoonMapper.selectList(queryWrapper);
	}
	
	@Override
	public void updateById(Integer id,String introduction,float score) {
		Cartoon entity = getById(id);
		entity.setIntroduction(introduction);
		entity.setScore(score);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		cartoonMapper.updateById(entity);
	}
	
	@Override
	public void updateNewDate(Integer id) {
		Cartoon entity = getById(id);
		entity.setUpdateTime(new Date());
		cartoonMapper.updateById(entity);
	}
	
	@Override
	public void updateNewDateAndType(Integer id,String type) {
		Cartoon entity = getById(id);
		entity.setUpdateTime(new Date());
		entity.setType(type);
		cartoonMapper.updateById(entity);
	}
}
