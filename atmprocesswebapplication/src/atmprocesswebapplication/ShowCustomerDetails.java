//$Id$
package atmprocesswebapplication;

import java.io.IOException;
import java.io.PrintWriter;
import atmprocesswebapplication.ShowCustomerData;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import atmprocesswebapplication.ConnectToDatabase;

public class ShowCustomerDetails extends HttpServlet {

	PrintWriter out;
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		String query = "select * from CustomerDetails;";
		try
		{
			resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
			
			Connection con = ConnectToDatabase.getConnectionObj();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			out = resp.getWriter();
			
			ShowCustomerData scd = new ShowCustomerData();
			List<String> json = new ArrayList<String>();
			Gson obj = new Gson();
			String customer = null;
			while(rs.next())
			{
				scd.acc_no = rs.getInt("acc_no");
				scd.acc_holder = rs.getString("acc_holder");
				scd.pin_no = rs.getInt("pin_no");
				scd.acc_balance = rs.getInt("acc_balance");
				
				 customer = obj.toJson(scd);
				 json.add(customer);	
			}
			
			con.close();
			st.close();
			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			
			System.out.println(json);
		    out.print(json);
		    out.flush(); 			
		}
		catch(Exception e)
		{
			resp.sendError(500, "An Error Occured in Machine");
			System.out.println(e);
		}
	}
}

/*out.println("<html><head>" 
+"<style>" 
+"table, th, td {"
+"  border: 1px solid black;" 
+"  border-collapse: collapse;}"
+"th, td {"
+"  padding: 5px;}" 
+"th { text-align: left;}" 
+"</style>"
+"</head><body><h1>Customer Details Table<table style=\"width:100%\">");

out.println("<tr>"
+ "<th>Account Number</th>"
+ "<th>Account Holder</th>"
+ "<th>Pin Number</th>"
+ "<th>Account Balance</th>"
+ "</tr>");*/
/*out.println("<tr>"
+ "<td>"+acc_no+"</td>"
+ "<td>"+acc_holder+"</td>"
+ "<td>"+pin_no+"</td>"
+ "<td>"+acc_balance+"</td>"
+ "</tr>");*/
//out.println("</body></html>");
