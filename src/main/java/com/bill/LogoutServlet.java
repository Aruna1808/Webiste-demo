package com.bill;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID=1L;

	protected void doGet(HttpServletRequest req,HttpServletResponse res) throws IOException {
		HttpSession session=req.getSession(false);
		if(session!=null)session.invalidate();
		res.sendRedirect("login.html");
	}
}
