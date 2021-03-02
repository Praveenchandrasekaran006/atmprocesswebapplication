//$Id$
package atmprocesswebapplication;

import java.sql.Statement;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import atmprocesswebapplication.ConnectToDatabase;


public abstract class AbstractLoadCashToAtmDatabase extends HttpServlet
{
	int hundreds_count = 0;
	int five_hundreds_count = 0;
	int thousands_count = 0;
	int hundreds = 0;
	int five_hundreds = 0;
	int thousands = 0;
	int total = 0;
	
	public void service(HttpServletRequest req, HttpServletResponse resp) 
	{	
		PrintWriter out = null;
		try
		{
			out = resp.getWriter();
			hundreds = Integer.parseInt(req.getParameter("hundreds"));
			five_hundreds = Integer.parseInt(req.getParameter("five_hundreds"));
			thousands = Integer.parseInt(req.getParameter("thousands"));
			
			int output = addAmountToAtm();
			if(output == 1)
				out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			else
				out.println("<html><body> <h1>Total Amount in ATM</h1> The total amount in ATM is "+total+" </body></html>");
		}
		catch(NumberFormatException e)
		{
			System.out.println(e);
			out.println("<html><body><h1> Invalid!! </h1> Please Enter Numbers </body></html>");
		}
		catch(Exception e)
		{
			System.out.println(e);
			out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
		}
			
	}
	
	public abstract int addAmountToAtm();
	
	public abstract void calculateTotalAmount();

	
}