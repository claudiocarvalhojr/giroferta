package connection;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public final class Connection {
	
	private SessionFactory sessionFactory = null;
	private Session session = null; 
	private Transaction transaction = null;
	
	public Connection(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	public Session getSession() {
		return session;
	}
	
	private void openSession() {
		session = sessionFactory.openSession();
	}
	
	private void closeSession() {
		if (session.isOpen())
			session.close();
	}
	
	public void beginTransaction() {
		openSession();
		transaction = session.beginTransaction();
	}
	
	public void closeTransaction() {
		closeSession();
	}
	
	public void commit() {
		transaction.commit();
	}
	
	public void rollback() {
		transaction.rollback();
	}
	
	public void save(Object entity) {
		session.persist(entity);
	}

	public void saveOrUpdate(Object entity) {
		session.saveOrUpdate(entity);
	}

	public void update(Object entity) {
		session.merge(entity);
	}

	public void delete(Object entity) {
		session.delete(entity);
	}
	
	public int count(String namedQuery, Map<String, Object> param) {
		Query query = (Query) session.getNamedQuery(namedQuery);
//		queryParameters(query, param);
		for (Map.Entry<String, Object> entry : param.entrySet())
			query.setParameter(entry.getKey(), entry.getValue());
		return ((Number) query.uniqueResult()).intValue();
	}
	
	public int count(String namedQuery) {
		Query query = (Query) session.getNamedQuery(namedQuery);
		return ((Number) query.uniqueResult()).intValue();
	}
	
	public Object find(Class<?> entityClass, int entityID) {
		return session.get(entityClass, entityID);
	}
	
	public Object find(String namedQuery, Map<String, Object> param) {
		Query query = (Query) session.getNamedQuery(namedQuery);
//		queryParameters(query, param);
		for (Map.Entry<String, Object> entry : param.entrySet())
			query.setParameter(entry.getKey(), entry.getValue());
		return query.uniqueResult();
	}
	
	public List<?> list(String namedQuery) {
		Query query = (Query) session.getNamedQuery(namedQuery);
		return query.list();
	}
	
	public List<?> list(String namedQuery, Map<String, Object> param) {
		Query query = (Query) session.getNamedQuery(namedQuery);
//		queryParameters(query, param);
		for (Map.Entry<String, Object> entry : param.entrySet())
			query.setParameter(entry.getKey(), entry.getValue());
		return query.list();
	}
	
	public List<?> list(String namedQuery, Map<String, Object> param, int current, int maximum) {
		Query query = (Query) session.getNamedQuery(namedQuery).setFirstResult(current).setMaxResults(maximum);
//		queryParameters(query, param);
		for (Map.Entry<String, Object> entry : param.entrySet())
			query.setParameter(entry.getKey(), entry.getValue());
		return query.list();
	}
	
	public List<?> list(String namedQuery, Map<String, Object> param, int maximum) {
		Query query = (Query) session.getNamedQuery(namedQuery).setMaxResults(maximum);
//		queryParameters(query, param);
		for (Map.Entry<String, Object> entry : param.entrySet())
			query.setParameter(entry.getKey(), entry.getValue());
		return query.list();
	}
	
	public List<?> list(String namedQuery, int current, int maximum) {
		Query query = (Query) session.getNamedQuery(namedQuery).setFirstResult(current).setMaxResults(maximum);
		return query.list();
	}
	
	public List<?> list(String namedQuery, int maximum) {
		Query query = (Query) session.getNamedQuery(namedQuery).setMaxResults(maximum);
		return query.list();
	}
	
	@SuppressWarnings("unchecked")
	public List<Object[]> listByCoords(String lat, String lng, int radius) {
		String sql = " SELECT store.id, store.latitude, store.longitude FROM stores AS store"
					+ " HAVING (6371 * "
					+ " ACOS( "
					+ " COS(RADIANS(" + lat + ")) * "
					+ " COS(RADIANS(store.latitude)) * "
					+ " COS(RADIANS(" + lng + ") - RADIANS(store.longitude)) + "
					+ " SIN(RADIANS(" + lat + ")) * "
					+ " SIN(RADIANS(store.latitude)) "
					+ " )) <= " + radius + "; ";
		SQLQuery query = session.createSQLQuery(sql);
		return query.list();
	}
	
//	private void queryParameters(Query query, Map<String, Object> param) {
//		for (Map.Entry<String, Object> entry : param.entrySet())
//			query.setParameter(entry.getKey(), entry.getValue());
////		param.clear();
////		param = null;
//	}
	
}
