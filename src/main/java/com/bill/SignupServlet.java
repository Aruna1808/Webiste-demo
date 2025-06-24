package com.bill;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/SignupServlet")
public class SignupServlet extends HttpServlet {
	private static final long serialVersionUID=1L;

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
		String email=request.getParameter("email");
		String password=request.getParameter("password");

		try {
			Class.forName("org.sqlite.JDBC");

			String dbPath=getServletContext().getRealPath("/database.db");
			Connection conn=DriverManager.getConnection("jdbc:sqlite:C:\\projects\\BillGenerator\\databases.db");

			PreparedStatement ps=conn.prepareStatement("INSERT INTO users(email,password) VALUES (?,?)");
			ps.setString(1,email);
			ps.setString(2,password);
			ps.executeUpdate();

			ps.close();
			conn.close();

			response.sendRedirect("login.html");

		}catch(Exception e){
			e.printStackTrace();
			response.getWriter().println("Error: "+e.getMessage());
		}
	}
}
