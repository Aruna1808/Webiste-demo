package com.bill;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/DownloadServlet")
public class DownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {
		String relPath=request.getParameter("file");
		if(relPath==null){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		String fullPath=getServletContext().getRealPath("/"+relPath);
		File file=new File(fullPath);

		if(!file.exists()){
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		response.setContentType("text/html");
		response.setHeader("Content-Disposition","attachment; filename=\""+file.getName()+"\"");
		try(FileInputStream in=new FileInputStream(file);ServletOutputStream out=response.getOutputStream()){
			byte[] buffer=new byte[4096];
			int bytesRead;
			while((bytesRead=in.read(buffer))!=-1){
				out.write(buffer,0,bytesRead);
			}
		}
	}
}
