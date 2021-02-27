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

public class ValidateAccountPinNumber extends HttpServlet 
{
	PrintWriter out = null;
	public void service(HttpServletRequest req, HttpServletResponse resp) 
	{
		String query = "select * from CustomerDetails";
		
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			out = resp.getWriter();
			int acc_num = Integer.parseInt(req.getParameter("acc_number"));
			int pin_num = Integer.parseInt(req.getParameter("pin_number"));
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			
			
			int flag = 0;
			
			while(rs.next())
			{
				int acc_no = rs.getInt("acc_no");
				int pin_no = rs.getInt("pin_no");
				
				if(acc_no == acc_num)
				{
					flag = 1;
					if(pin_no == pin_num)
					{
						AccountNumber.setAccountNumber(acc_no);
						
						out.println("<html><body><h1>Welcome "+rs.getString("acc_holder")+"</h1>"
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
					
						break;
					}
					else
					{
						out.println("<html><body><p>Incorrect Pin Number</P></body></html>");
						break;
					}
				}	
			}
			if(flag == 0)
				out.println("<html><body><p>Incorrect Account Number</P></body></html>");
		
			st.close();
		}
		catch(NumberFormatException e)
		{
			out.println("<html><body><h1> Invalid!! </h1> Please Enter Numbers </body></html>");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	
	}

}
