package atmprocesswebapplication;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MiniStatementClass extends HttpServlet
{
	public void service(HttpServletRequest req, HttpServletResponse resp)
	{
		int acc_num = AccountNumber.getAccountNumber();
		PrintWriter out = null;
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{	
			out = resp.getWriter();
			String query = null;
			if(acc_num == 101)
				query = "select * from suresh;";
			else if(acc_num == 102)
				query = "select * from ganesh;";
			else if(acc_num == 103)
				query = "select * from magesh;";
			else if(acc_num == 104)
				query = "select * from naresh;";
			else if(acc_num == 105)
				query = "select * from harish;";
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			out = resp.getWriter();
			
			out.println("<html><head>" 
			        +"<style>" 
					+"table, th, td {"
			        +"  border: 1px solid black;" 
					+"  border-collapse: collapse;}"
			        +"th, td {"
			        +"  padding: 5px;}" 
			        +"th { text-align: left;}" 
			        +"</style>"
			        +"</head><body><h1>Mini Statement<table style=\"width:100%\">");
			
			out.println("<tr>"
					+ "<th>Transaction Id</th>"
					+ "<th>Transaction Remarks</th>"
					+ "<th>Transaction Type</th>"
					+ "<th>Transaction Amount</th>"
					+ "</tr>");
			
			while(rs.next())
			{
				int transaction_id = rs.getInt("transaction_id");
				String  transaction_remark = rs.getString("transaction_remark");
				String transaction_type = rs.getString("transaction_type");
				String transaction_amt = rs.getString("transaction_amt");
				
				out.println("<tr>"
						+ "<td>"+transaction_id+"</td>"
						+ "<td>"+transaction_remark+"</td>"
						+ "<td>"+transaction_type+"</td>"
						+ "<td>"+transaction_amt+"</td>"
						+ "</tr>");
			}
		}
		catch(Exception e)
		{
			out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			System.out.println(e);
		}
		
	}
}
/*
PreparedStatement pst = con.prepareStatement("select acc_holder from CustomerDetails where acc_no = ?");
pst.setInt(1, acc_num);
ResultSet rs = pst.executeQuery();
rs.next();
String acc_holder = rs.getString("acc_holder");
*/