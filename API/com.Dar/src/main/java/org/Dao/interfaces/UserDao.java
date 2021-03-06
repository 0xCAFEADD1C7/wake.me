package org.Dao.interfaces;

import org.Dao.GenericDao;
import org.Entite.User;

public interface UserDao extends GenericDao<User> {

	public User getByMail(String mail);
	public User getByToken(String tok);
}
