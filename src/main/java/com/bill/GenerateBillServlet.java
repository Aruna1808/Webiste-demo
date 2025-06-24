package com.bill;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xhtmlrenderer.pdf.ITextRenderer;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/GenerateBillServlet")
public class GenerateBillServlet extends HttpServlet {
	private static final long serialVersionUID=1L;

	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {

		String email=request.getParameter("email");
		String businessName=request.getParameter("businessName");
		String businessEmail=request.getParameter("businessEmail");
		String businessAddress=request.getParameter("businessAddress");
		String businessCountry=request.getParameter("businessCountry");
		String businessPhone=request.getParameter("businessPhone");

		String customerName=request.getParameter("customerName");
		String customerEmail=request.getParameter("customerEmail");
		String customerAddress=request.getParameter("customerAddress");
		String customerPhone=request.getParameter("customerPhone");

		String[] itemNames=request.getParameterValues("itemName[]");
		String[] quantities=request.getParameterValues("quantity[]");
		String[] rates=request.getParameterValues("rate[]");
		String[] taxes=request.getParameterValues("tax[]");

		double subtotal=0.0;
		double total=0.0;
		StringBuilder itemsTable=new StringBuilder();

		for(int i=0;i<itemNames.length;i++){
			String name=itemNames[i];
			int qty=Integer.parseInt(quantities[i]);
			double rate=Double.parseDouble(rates[i]);
			double tax=Double.parseDouble(taxes[i]);

			double amount=qty*rate;
			double taxAmount=amount*(tax/100);
			subtotal+=amount;
			total+=amount+taxAmount;

			itemsTable.append("<tr>")
				.append("<td>").append(name).append("</td>")
				.append("<td>").append(qty).append("</td>")
				.append("<td>").append(rate).append("</td>")
				.append("<td>").append(String.format("%.2f",amount)).append("</td>")
				.append("</tr>");
		}

		String timestamp=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String fileNameHtml="invoice-"+new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date())+".html";
		String fileNamePdf=fileNameHtml.replace(".html",".pdf");

		String appPath=getServletContext().getRealPath("/Invoices");
		String userFolder=appPath+File.separator+email;
		File folder=new File(userFolder);
		if(!folder.exists()){
			folder.mkdirs();
		}

		String htmlPath=userFolder+File.separator+fileNameHtml;
		String htmlContent="<!DOCTYPE html><html xmlns='http://www.w3.org/1999/xhtml'><head><meta charset='UTF-8'/><title>Invoice</title></head><body style='font-family:Arial;'>"
			+"<h1 style='color:#4849BD;'>Invoice</h1>"
			+"<h3>From:</h3>"
			+businessName+"<br/>"
			+businessEmail+"<br/>"
			+businessAddress+"<br/>"
			+businessCountry+"<br/>"
			+businessPhone+"<br/><br/>"
			+"<h3>To:</h3>"
			+customerName+"<br/>"
			+customerEmail+"<br/>"
			+customerAddress+"<br/>"
			+customerPhone+"<br/><br/>"
			+"<table border='1' cellpadding='10' cellspacing='0' style='width:100%;border-collapse:collapse;'>"
			+"<thead style='background:#f0f0f0;'><tr><th>Item</th><th>Quantity</th><th>Rate</th><th>Amount</th></tr></thead>"
			+"<tbody>"+itemsTable+"</tbody></table>"
			+"<p><strong>Subtotal:</strong> Rs. "+String.format("%.2f",subtotal)+"</p>"
			+"<p><strong>Total:</strong> Rs. "+String.format("%.2f",total)+"</p>"
			+"<p><strong>Generated on:</strong> "+timestamp+"</p>"
			+"<p style='margin-top:40px;color:#4849BD;font-style:italic;'>Thank you for doing business with us! 🙏</p>"
			+"</body></html>";

		Files.write(Paths.get(htmlPath),htmlContent.getBytes());

		String pdfPath=userFolder+File.separator+fileNamePdf;
		try(OutputStream os=new FileOutputStream(pdfPath)){
			ITextRenderer renderer=new ITextRenderer();
			renderer.setDocument(new File(htmlPath).toURI().toString());
			renderer.layout();
			renderer.createPDF(os);
		}catch(Exception e){
			e.printStackTrace();
			response.getWriter().println("Error during PDF generation: "+e.getMessage());
			return;
		}

		HttpSession session=request.getSession();
		String invoicePath="Invoices/"+email+"/"+fileNamePdf;
		session.setAttribute("invoicePath",invoicePath);
		response.sendRedirect("download.html");
	}
}
