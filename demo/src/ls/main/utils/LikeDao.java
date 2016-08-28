package ls.main.utils;

import android.content.Context;

import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import ls.main.bean.Like;


public class LikeDao {

	
	private Context context;
	private DbUtils db;
	
	public LikeDao(Context context){
		this.context = context;
		db = DbUtils.create(context);
	}
	
	public boolean add(Like like){
		Like bean = new Like();
		bean.setImg_url(like.getImg_url());
		bean.setTime(like.getTime());
		bean.setTitle(like.getTitle());
		bean.setUrl(like.getUrl());
		try {
			db.save(bean);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	public List<Like> getAllLikes(){
		try {
			return db.findAll(Like.class);
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public boolean delete(Like like){
		try {
			db.deleteById(Like.class, like.getId());
			return true;
		} catch (DbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
