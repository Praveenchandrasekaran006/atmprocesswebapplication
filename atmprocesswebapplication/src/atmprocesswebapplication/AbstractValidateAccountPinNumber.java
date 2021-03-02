//$Id$
package atmprocesswebapplication;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import atmprocesswebapplication.ConnectToDatabase;

public abstract class AbstractValidateAccountPinNumber extends HttpServlet 
{
	PrintWriter out = null;
	int acc_num;
	int pin_num;
	String acc_holder;
	
	public void service(HttpServletRequest req, HttpServletResponse resp) 
	{
		try
		{
			out = resp.getWriter();
			acc_num = Integer.parseInt(req.getParameter("acc_number"));
			pin_num = Integer.parseInt(req.getParameter("pin_number"));
			
			int out_check = checkAccountPin();
			if(out_check == 1)
				out.println("<html><body><h1>Welcome "+acc_holder+"</h1>"
						+"<div>	<form action = "+"check"+">	"
						+ " <h2> Check Balance </h2> <input type = "+"Submit"+"><br>"
						+ "</form></div>"
						+"<div> <form action = "+"CashWithdrawal.html"+">"
						+ "<h2> Cash Withdrawal </h2><input type = "+"Submit"+"><br>"
						+ "</form></div>"
						+"<div>	<form action = "+"TransferMoney.html"+">"
						+ "<h2> Transfer Money </h2><input type = "+"Submit"+"><br>"
						+ "</form>	</div>"
						+"<div>	<form action = "+"ministate"+">"
						+ "<h2> Mini Statement </h2><input type = "+"Submit"+"><br>"
						+ "</form></div>"
						+"</body></html>");
			else if(out_check == 2)
				out.println("<html><body><p>Incorrect Pin Number</P></body></html>");
			else if(out_check == 3)
		    	out.println("<html><body><p>Incorrect Account Number</P></body></html>");
			else 
				out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			
		}
		catch(NumberFormatException e)
		{
			out.println("<html><body><h1> Invalid!! </h1> Please Enter Numbers </body></html>");
			System.out.println(e);
		}
		catch(Exception e)
		{
			out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			System.out.println(e);
		}
	
	}
	
	public abstract int checkAccountPin();
	
}
