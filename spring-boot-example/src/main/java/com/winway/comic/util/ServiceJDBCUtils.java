package com.winway.comic.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.winway.comic.entity.Cartoon;
import com.winway.comic.entity.Chapter;

public class ServiceJDBCUtils {

	public static boolean isExitCartoon(String title,String url) throws SQLException {
		Connection connection = JDBCUtils.getConnection();
		String sql = "select count(*) from cartoon where title = ? and url = ?";
		// 2. 获取PreparedStatement
	    PreparedStatement preparedStatement = connection.prepareStatement(sql);
	    preparedStatement.setString(1, title);
	    preparedStatement.setString(2, url);
	 // 4. 执行查询sql
	    ResultSet resultSet = preparedStatement.executeQuery();
	    
	    while(resultSet.next())
	    {
	    //打印的就是总记录数。把检索结果看成只有一跳记录一个字段的表
	       int count = resultSet.getInt(1);
	       
	       return count>0;
	    }
	    JDBCUtils.close(resultSet, preparedStatement, connection);
	    return false;
	}
	
	public static Integer insertCartoon(Cartoon cartoonEntity)  {
//		Connection connection = JDBCUtils.getConnection();
//		String sql = "insert into cartoon where title = ? and url = ?";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//建立连接
			conn = JDBCUtils.getConnection();
			//创建语句
			String sql = "insert into cartoon(title,cover,url) values ('"+cartoonEntity.getTitle()+"', '"+cartoonEntity.getCover()+"', '"+cartoonEntity.getUrl()+"') ";
			//通过传入第二个参数,就会产生主键返回给我们
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			
			//返回的结果集中包含主键,注意：主键还可以是UUID,
			//复合主键等,所以这里不是直接返回一个整型
			rs = ps.getGeneratedKeys();
			int id = 0;
			if(rs.next()) {
				id = rs.getInt(1);
			}
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JDBCUtils.close(rs, ps, conn);
		}
		return 0;
	}
	
	public static Integer insertChapter(Chapter chapter){
//		Connection connection = JDBCUtils.getConnection();
//		String sql = "insert into cartoon where title = ? and url = ?";
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			//建立连接
			conn = JDBCUtils.getConnection();
			//创建语句
			String sql = "insert into chapter(cartoon_id,num,name,content) values ("+chapter.getCartoonId()+", "+chapter.getNum()+", '"+chapter.getName()+"','"+chapter.getContent()+"') ";
			//通过传入第二个参数,就会产生主键返回给我们
			ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.executeUpdate();
			
			//返回的结果集中包含主键,注意：主键还可以是UUID,
			//复合主键等,所以这里不是直接返回一个整型
			rs = ps.getGeneratedKeys();
			int id = 0;
			if(rs.next()) {
				id = rs.getInt(1);
			}
			return id;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			JDBCUtils.close(rs, ps, conn);
		}
		return 0;
	}
	
	
    public static void main(String[] args) throws SQLException {
//    	boolean flag=isExitCartoon("极乐幻想夜","");
//    	System.out.println(flag);
//    	Cartoon cartoonEntity = new Cartoon();
//    	cartoonEntity.setCover("test");
//    	cartoonEntity.setTitle("title");
//    	cartoonEntity.setCover("cover");
//    	Integer id= insertCartoon(cartoonEntity);
//    	System.out.println(id);
    	Chapter chapter = new Chapter();
    	chapter.setCartoonId(1111111);
    	chapter.setContent("cc");
    	chapter.setName("nnnn");
    	chapter.setNum(2);
    	
    	insertChapter(chapter);
    }

	
	}