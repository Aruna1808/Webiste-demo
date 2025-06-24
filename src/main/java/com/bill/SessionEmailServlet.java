package com.bill;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/SessionEmailServlet")
public class SessionEmailServlet extends HttpServlet {
	private static final long serialVersionUID=1L;

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session=request.getSession(false);
		String email=(session!=null)?(String)session.getAttribute("userEmail"):null;

		if(email!=null){
			response.getWriter().write(email);
		}else{
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}
}
