package org.Entite;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.json.JSONObject;
import org.utils.JSONable;

@Entity
@Table(name="user")
public class User implements JSONable {


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
	private Date tokenExperiationDate;

	@Column
	private String lastName;

	@Column
	private String firstName;

	@Column(name ="password")
	private String password;


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

	public String getPseudo() {
		return pseudo;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getTokenExperiationDate() {
		return tokenExperiationDate;
	}

	public void setTokenExperiationDate(Date tokenExperiationDate) {
		this.tokenExperiationDate = tokenExperiationDate;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = toSHA256(password.getBytes());
	}

	public static String toSHA256(byte[] convertme) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		}
		catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		} 
		byte[] mdbytes =  md.digest(convertme);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mdbytes.length; i++) {
			sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();

	}

	public String toJson() throws Exception {
		return new JSONObject()
				.put("id", idUser)
				.put("lastName", lastName)
				.put("firstName", firstName)
				.put("email", mail)
				.toString();
	}

	
	public void fromJson(JSONObject body, Map<String,Object> infos) throws Exception {
		mail = body.getString("email");
		firstName = body.getString("firstName");
		lastName = body.getString("lastName");
		pseudo = body.getString("pseudo");
		password = body.getString("password");
		//TODO setPassword();
	}

	public boolean isValidToken() {
		return tokenExperiationDate.before(new Date());
	}
}
