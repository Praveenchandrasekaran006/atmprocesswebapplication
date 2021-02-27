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

public class TransferMoneyClass extends HttpServlet
{
	int sender_acc_balance;
	public void service(HttpServletRequest req, HttpServletResponse resp)
	{
		PrintWriter out = null;
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			out = resp.getWriter();
			int sender_acc_num = AccountNumber.getAccountNumber();
			int receive_acc_num = Integer.parseInt(req.getParameter("recv_acc_number"));
			int amount = Integer.parseInt(req.getParameter("amount"));
			int acc_vali_output = validateReceiverAccountNumber(receive_acc_num);
			
			if(acc_vali_output == 1)
			{
				int amt_vali_output = validateAmount(amount, sender_acc_num);
				if(amt_vali_output == 1)
				{
					int sender_transaction_id = TransactionIdClass.getTransactionId(sender_acc_num);
					int receive_transaction_id = TransactionIdClass.getTransactionId(receive_acc_num);
					
					sender_acc_balance -= amount;  
					PreparedStatement pst = con.prepareStatement("update CustomerDetails set acc_balance = ? where acc_no = ?;");
					pst.setInt(1, sender_acc_balance);
					pst.setInt(2, sender_acc_num);
					pst.executeUpdate();
					
					pst = con.prepareStatement("select acc_balance from CustomerDetails where acc_no = ?;");
					pst.setInt(1, receive_acc_num);
					ResultSet rs = pst.executeQuery();
					rs.next();
					int receiver_acc_balance = rs.getInt("acc_balance");
					receiver_acc_balance += amount;
					
					pst = con.prepareStatement("update CustomerDetails set acc_balance = ? where acc_no = ?;");
					pst.setInt(1, receiver_acc_balance);
					pst.setInt(2, receive_acc_num);
					pst.executeUpdate();
					pst.close();
					
					TransactionIdClass.setTransactionDetailsSender(sender_acc_num, receive_acc_num ,amount, sender_transaction_id);
					TransactionIdClass.setTransactionDetailsReceiver(sender_acc_num, receive_acc_num ,amount, receive_transaction_id);
					
					out.println("<html><body><h1>Amount Transfer Successfull </h1> Your account Balance is "+sender_acc_balance+"</body></html>");
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
		}
		catch(Exception e)
		{
			out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			System.out.println(e);
		}
		
	}
	
	public int validateReceiverAccountNumber(int acc_num)
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			String query = "select acc_no from CustomerDetails";
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			int flag = 0;
			while(rs.next())
			{
				int acc_no = rs.getInt("acc_no");
				if(acc_no == acc_num)
					flag = 1;
			}
			st.close();
			
			if(flag == 1)
				return 1;
			else
				return 2;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
	
	public int validateAmount(int amount, int account)
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			PreparedStatement pst = con.prepareStatement("select acc_balance from CustomerDetails where acc_no = ?;");
			pst.setInt(1, account);
			ResultSet rs = pst.executeQuery();
			rs.next();
			sender_acc_balance = rs.getInt("acc_balance");
			
			if(amount > 10000 || amount < 1000)
			{
				return 2;
			}
			if(sender_acc_balance < amount)
			{
				return 3;
			}
			return 1;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
}
