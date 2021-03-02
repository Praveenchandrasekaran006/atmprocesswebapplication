//$Id$
package atmprocesswebapplication;
import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atmprocesswebapplication.AbstractValidateAccountPinNumber;

public abstract class AbstractCheckBalanceClass extends HttpServlet
{
	int acc_num;
	int acc_balance;
	PrintWriter out;
	
	public void service(HttpServletRequest req, HttpServletResponse resp)  
	{
		acc_num = AccountNumber.getAccountNumber();	
		try
		{
			out = resp.getWriter();
			int out_balance = getBalance();
			if(out_balance == 1)
				out.println("<html><body><h1> Account Balance </h1> <p>The account Balance is "+acc_balance+"</p></body></html>");
			else
				out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
		}
		catch(Exception e)
		{
			out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			System.out.println(e);
		}		
	}
	
	public abstract int getBalance();
	
}
