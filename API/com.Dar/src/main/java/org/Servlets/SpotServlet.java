package org.Servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.Dao.UserDaoImpl;
import org.Entite.Spot;
import org.Entite.User;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class SpotServlet
 */
@WebServlet("/spot/*")

public class SpotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SpotServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
    	String[] requete = request.getPathInfo().split("/");
    	
    	System.out.println(request.getPathInfo());
    	
    	try {
			JSONObject body = getBody(request);
			
			String token = body.getString("token");
			UserDaoImpl userDao = new UserDaoImpl();
			
			
			
		User user =	userDao.getUserByToken(token);
		if(user == null || !user.isValidToken())
			out.println("{\"error\" : \""+"Token is not valid"+"\"}");
		else {
		
		if(requete.length<1)
			User.getAllUsers(out);
		
		else if(!requete[1].isEmpty())
     		User.getUser(Integer.parseInt(requete[1]));
		 			
		}
		
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    
        		
    	
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		JSONObject body;
		try {
			body = getBody(request);
			Spot.addSpot(body, out);

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

}
