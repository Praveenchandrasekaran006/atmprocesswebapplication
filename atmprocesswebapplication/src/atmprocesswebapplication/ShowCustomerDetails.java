//$Id$
package atmprocesswebapplication;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import atmprocesswebapplication.ConnectToDatabase;

public class ShowCustomerDetails extends HttpServlet {

	public void service(HttpServletRequest req, HttpServletResponse resp)
	{
		String query = "select * from CustomerDetails;";
		try
		{
			Connection con = ConnectToDatabase.getConnectionObj();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			PrintWriter out = resp.getWriter();
			
			out.println("<html><head>" 
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
					+ "</tr>");
			
			while(rs.next())
			{
				int acc_no = rs.getInt("acc_no");
				String acc_holder = rs.getString("acc_holder");
				int pin_no = rs.getInt("pin_no");
				int acc_balance = rs.getInt("acc_balance");
				
				out.println("<tr>"
						+ "<td>"+acc_no+"</td>"
						+ "<td>"+acc_holder+"</td>"
						+ "<td>"+pin_no+"</td>"
						+ "<td>"+acc_balance+"</td>"
						+ "</tr>");
			}
			
			out.println("</body></html>");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
