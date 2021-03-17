//$Id$
package atmprocesswebapplication;
import atmprocesswebapplication.InputValidateData;
import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import atmprocesswebapplication.AbstractValidateAccountPinNumber;

public abstract class AbstractCheckBalanceClass extends HttpServlet
{
	int acc_num;
	int acc_balance;
	PrintWriter out;
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException  
	{
		acc_num = AccountNumber.getAccountNumber();	
		try
		{
			resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
			out = resp.getWriter();
			
			CheckBalanceData cbd = new CheckBalanceData();
	
			int out_balance = getBalance();
			if(out_balance == 1)
			{
				cbd.balance = acc_balance;
				Gson obj = new Gson();
				String json = obj.toJson(cbd);
				System.out.println(json);
				
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
			    out.print(json);
			    out.flush();
				
			}
			else
				resp.sendError(500, "An Error occured in machine");
		}
		catch(Exception e)
		{
			resp.sendError(500, "An Error occured in machine");
			System.out.println(e);
		}		
	}
	
	public abstract int getBalance();
	
}

//out.println("An error Occured in machine Your account balance not affected");
//out.println("<html><body><h1> Account Balance </h1> <p>The account Balance is "+acc_balance+"</p></body></html>");