package com.bill;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet("/GetInvoicePathServlet")
public class GetInvoicePathServlet extends HttpServlet {
	private static final long serialVersionUID=1L;

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session=request.getSession(false);
		String path=(session!=null)?(String)session.getAttribute("invoicePath"):null;

		if(path!=null){
			response.getWriter().write(path);
		}else{
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
}
