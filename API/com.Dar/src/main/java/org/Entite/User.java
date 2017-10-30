package org.Entite;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.Dao.UserDaoImpl;
import org.json.JSONException;
import org.json.JSONObject;

@Entity
@Table(name="user",uniqueConstraints = {@UniqueConstraint(columnNames = {"nom","prenom"})})
public class User {
	
	
	@Id
	@Column(name="idUser")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idUser;
	
	@Column(name="mail", unique=true )
	private String mail;
	
	@Column(name="pseudo", unique = true)
	private String pseudo;
	
	@Column(unique = true)
	private String token;
	
	@Column
	private String nom;
	
	@Column
	private String prenom;
	
	@Column
	private Date dateDeNaissance;
	
	
	@Column(name ="password")
	private String password;

	
	@OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
	private Set<Commentaire> liker = new HashSet<Commentaire>();


	
	
	public User(String mail, String nom, String prenom, Date dateDeNaissance, String password) {
		super();
		this.mail = mail;
		this.nom = nom;
		this.prenom = prenom;
		this.dateDeNaissance = dateDeNaissance;
		this.password = password;
	}
	
	public User() {
	}

	public int getIdUser() {
		return idUser;
	}


	public void setIdUser(int idUser) {
		this.idUser = idUser;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	public String getNom() {
		return nom;
	}


	public void setNom(String nom) {
		this.nom = nom;
	}


	public String getPrenom() {
		return prenom;
	}


	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}


	public Date getDateDeNaissance() {
		return dateDeNaissance;
	}


	public void setDateDeNaissance(Date dateDeNaissance) {
		this.dateDeNaissance = dateDeNaissance;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String toJson() throws JSONException {
		return new JSONObject()
				.put("id", idUser)
				.put("nom", nom)
				.put("prenom", prenom)
				.put("date_naissance",this.dateDeNaissance.toString())
				.put("email",this.mail)
				.toString();
	}

	
	public  static void addUser(JSONObject body,PrintWriter out) throws JSONException {
		
		User user = new User();
		user.setMail(body.getString("mail"));
		user.setNom(body.getString("nom"));
		user.setPrenom(body.getString("prenom"));
		user.setPassword(body.getString("password"));
		user.setDateDeNaissance(new Date());
		
		UserDaoImpl userDao = new UserDaoImpl();
		userDao.add(user);
		out.println(user.toJson());

	}
	
	public static User getUser(PrintWriter out,int id)  {
		UserDaoImpl userDao = new UserDaoImpl();
		User user = userDao.getUser(id);
		try {
		out.println(user.toJson());
		
		} catch (Exception e) {
			e.printStackTrace();
			out.println("{\"error\" : \""+e.getMessage()+"\"}");
		}
		System.out.println(user);
		return user;
	}
}