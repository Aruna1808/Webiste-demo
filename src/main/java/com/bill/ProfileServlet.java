package com.bill;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ProfileServlet")
public class ProfileServlet extends HttpServlet {
	private static final long serialVersionUID=1L;

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException {
		HttpSession session=request.getSession(false);
		String email=(session!=null)?(String)session.getAttribute("userEmail"):null;

		if(email==null){
			response.sendRedirect("login.html");
			return;
		}

		String invoiceFolder=getServletContext().getRealPath("/Invoices/"+email);
		File folder=new File(invoiceFolder);
		List<String> invoices=new ArrayList<>();

		if(folder.exists()){
			for(File file:folder.listFiles()){
				if(file.isFile()&&file.getName().endsWith(".html")){
					invoices.add("Invoices/"+email+"/"+file.getName());
				}
			}
		}

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");

		StringBuilder json=new StringBuilder();
		json.append("{\"email\":\"").append(email).append("\",\"invoices\":[");
		for(int i=0;i<invoices.size();i++){
			json.append("\"").append(invoices.get(i)).append("\"");
			if(i<invoices.size()-1)json.append(",");
		}
		json.append("]}");

		response.getWriter().write(json.toString());
	}
}
