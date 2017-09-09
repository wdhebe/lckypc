package com.mvc.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.base.enums.EditState;
import com.base.enums.IsDelete;
import com.mvc.dao.AdDao;
import com.mvc.entity.Ad;
import com.mvc.repository.AdRepository;

/**
 * 
 * @ClassName: AdDaoImpl
 * @Description: ad
 * @author ycj
 * @date 2017年9月6日 上午10:07:43 
 * 
 *
 */
@Repository("adDaoImpl")
public class AdDaoImpl implements AdDao{

	@Autowired
	@Qualifier("entityManagerFactory")
	EntityManagerFactory emf;
	@Autowired
	AdRepository adRepository;
	
	//查询全部ad信息
	@Override
	public Integer countTotal(String searchKey) {
		// TODO 自动生成的方法存根
		EntityManager em = emf.createEntityManager();
		String countSql = " select count(ad_id) from Ad tr where is_delete=0 ";
		if (null != searchKey) {
			countSql += "   and (ad_title like '%" + searchKey + "%')";
		}
		Query query = em.createNativeQuery(countSql);
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}
	
	//根据页数筛选ad信息
	@Override
	public List<Ad> findAdByPage(String searchKey, int offset, int end) {
		// TODO 自动生成的方法存根
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from Ad where is_delete=0";
	// 判断查找关键字是否为空
		if (null != searchKey) {
			selectSql += " and ( ad_title like '%" + searchKey + "%')";
		}
		selectSql += " order by ad_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Ad.class);
		query.setParameter("offset", offset);
		query.setParameter("end", end);
		List<Ad> list = query.getResultList();
		em.close();
		return list;
	}
	
	//根据id变更state
	@Override
	public boolean editState(Integer ad_id) {
		// TODO 自动生成的方法存根
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			String selectSql = "update ad set ad_state =:ad_state where ad_id =:ad_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("ad_id", ad_id);
			query.setParameter("ad_state",EditState.YES.value);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		}finally{
			em.close();
		}
		return true;
	}

	//根据state获得页数
	@Override
	public Integer countStateTotal(String adState) {
		EntityManager em = emf.createEntityManager();
		String countSql = "select count(ad_id) from Ad where is_delete=0 and ad_state = " +adState;
		Query query = em.createNativeQuery(countSql);
		List<Object> totalRow = query.getResultList();
		em.close();
		return Integer.parseInt(totalRow.get(0).toString());
	}

	//根据state、page筛选ad信息
	@Override
	public List<Ad> findAdByStatePage(String adState, int offset, int limit) {
		EntityManager em = emf.createEntityManager();
		String selectSql = "select * from Ad where is_delete=0 and ad_state = " + adState;
		selectSql += " order by ad_id desc limit :offset, :end";
		Query query = em.createNativeQuery(selectSql, Ad.class);
		query.setParameter("offset", offset);
		query.setParameter("end", limit);
		List<Ad> list = query.getResultList();
		em.close();
		return list;
	}

	//根据id删除ad信息
	@Override
	public boolean updateState(Integer ad_id) {
		// TODO 自动生成的方法存根
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		try {
			String selectSql = "update ad set is_delete =:is_delete where ad_id =:ad_id ";
			Query query = em.createNativeQuery(selectSql);
			query.setParameter("ad_id", ad_id);
			query.setParameter("is_delete", IsDelete.YES.value);
			query.executeUpdate();
			em.flush();
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return true;
	}
}
