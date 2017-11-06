package org.Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Dao.UserDaoImpl;
import org.Entite.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class LoginServlet
 */

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) {
		   fixHeaders(response);
		   response.setStatus(200);
		   
		System.out.println(request.getHeader("origin"));
    }
    private void fixHeaders(HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, PUT, POST, OPTIONS, DELETE");
        response.addHeader("Access-Control-Allow-Headers", "Content-Type, autorization");
        response.addHeader("Access-Control-Max-Age", "86400");
       
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		fixHeaders(response);		
		System.out.println("test");
		PrintWriter out = response.getWriter();
		
		try {
			JSONObject body = getBody(request);
			
			
			
			UserDaoImpl userDao = new UserDaoImpl();
			User user = userDao.getByMail(body.getString("mail"));
			System.out.println(toSHA256(body.getString("password").getBytes()));
			if(user != null) {
				if(user.getPassword().equals(toSHA256(body.getString("password").getBytes()))) {
					System.out.println("true connected");
					Random random = new Random();
					String token =toSHA256(new String(user.getMail()+random.nextDouble()).getBytes());
					Date dt = new Date();
					Calendar c = Calendar.getInstance(); 
					c.setTime(dt); 
					c.add(Calendar.DATE, 1);
					dt = c.getTime();
					user.setExperiationDate(dt);
					
					userDao.updateUser(token, user);
					
					out.println(new JSONObject().put("token", token).put("Date d'expiration ",new Timestamp(user.getExperiationDate().getTime()).getTime()));
				}
				
				else {
					out.println("{\"error\" : \""+"Le mot de passe ou le username est incorrect"+"\"}");
				}
				
			}else {
				out.println("{\"error\" : \""+"Le mot de passe ou le username est incorrect"+"\"}");
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	 public JSONObject getBody(HttpServletRequest request) throws IOException, JSONException {
	 		BufferedReader in = request.getReader();
			StringBuilder bodyBuilder = new StringBuilder();
			String line;
			while ((line = in.readLine()) != null) {
				bodyBuilder.append(line);
			}
			String body = bodyBuilder.toString();
			return new JSONObject(body);
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

}
