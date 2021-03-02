//$Id$
package atmprocesswebapplication;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractTransferMoneyClass extends HttpServlet
{
	int sender_acc_balance;
	int sender_acc_num;
	int receive_acc_num;
	int amount;
	
	public void service(HttpServletRequest req, HttpServletResponse resp)
	{
		PrintWriter out = null;
		try
		{
			out = resp.getWriter();
			sender_acc_num = AccountNumber.getAccountNumber();
			receive_acc_num = Integer.parseInt(req.getParameter("recv_acc_number"));
			amount = Integer.parseInt(req.getParameter("amount"));
			int acc_vali_output = validateReceiverAccountNumber(receive_acc_num);
			
			if(acc_vali_output == 1)
			{
				int amt_vali_output = validateAmount(amount, sender_acc_num);
				if(amt_vali_output == 1)
				{
					int out_perform = performMoneyTransfer();
					if(out_perform == 1)
						out.println("<html><body><h1>Amount Transfer Successfull </h1> Your account Balance is "+sender_acc_balance+"</body></html>");
					else
						out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
				}
				else if(amt_vali_output == 2)
					out.println("<html><body><h1> Cannot Transfer The Entered Amount </h1> Enter the amount within the given limit</body></html>");
				else if(amt_vali_output == 3)
					out.println("<html><body><h1> Insufficient Amount in Sender's Account </h1></body></html>");
				else
					out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			}
			else if(acc_vali_output == 2)
				out.println("<html><body><h1> Invalid Account Number </h1> The Entered Account Number not Available</body></html>");
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
	
	public abstract int validateReceiverAccountNumber(int acc_num);
	
	public abstract int validateAmount(int amount, int account);
	
	public abstract int performMoneyTransfer();
	
}
