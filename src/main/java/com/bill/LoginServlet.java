package com.bill;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID=1L;

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String email=request.getParameter("email");
		String password=request.getParameter("password");

		try {
			Class.forName("org.sqlite.JDBC");
			String dbPath=getServletContext().getRealPath("/database.db");
			Connection conn=DriverManager.getConnection("jdbc:sqlite:C:\\projects\\BillGenerator\\databases.db");

			String sql="SELECT * FROM users WHERE email=? AND password=?";
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setString(1,email);
			ps.setString(2,password);

			ResultSet rs=ps.executeQuery();

			if(rs.next()){
				HttpSession session=request.getSession();
				session.setAttribute("userEmail",email);
				response.sendRedirect("getstarted.html");
			}else{
				response.getWriter().println("<h3>Login failed. Invalid email or password.</h3>");
			}

			rs.close();
			ps.close();
			conn.close();

		}catch(Exception e){
			e.printStackTrace();
			response.getWriter().println("Error: "+e.getMessage());
		}
	}
}
