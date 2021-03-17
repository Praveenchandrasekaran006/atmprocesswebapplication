package atmprocesswebapplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class MiniStatementClass extends HttpServlet
{
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		int acc_num = AccountNumber.getAccountNumber();
		PrintWriter out = null;
		try
		{	
			resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
			Connection con = ConnectToDatabase.getConnectionObj();
			PreparedStatement pst = con.prepareStatement("select * from MiniStatement where acc_no = ?;");
			
			pst.setInt(1, acc_num);
			ResultSet rs = pst.executeQuery();
			
			
			out = resp.getWriter();
			MiniStatementData msd = new MiniStatementData();
			List<String> json = new ArrayList<String>();
			Gson obj = new Gson();
			String ministate = null;
			
			while(rs.next())
			{
				msd.transaction_id = rs.getInt("transaction_id");
				msd.transaction_remark = rs.getString("transaction_remark");
				msd.transaction_type = rs.getString("transaction_type");
				msd.transaction_amt = rs.getInt("transaction_amt");
				
				ministate = obj.toJson(msd);
				json.add(ministate);
			}
			con.close();
			
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
+"</head><body><h1>Mini Statement<table style=\"width:100%\">");

out.println("<tr>"
+ "<th>Transaction Id</th>"
+ "<th>Transaction Remarks</th>"
+ "<th>Transaction Type</th>"
+ "<th>Transaction Amount</th>"
+ "</tr>");*/
//msd.acc_no = rs.getInt("acc_no");
/*out.println("<tr>"
+ "<td>"+transaction_id+"</td>"
+ "<td>"+transaction_remark+"</td>"
+ "<td>"+transaction_type+"</td>"
+ "<td>"+transaction_amt+"</td>"
+ "</tr>");*/