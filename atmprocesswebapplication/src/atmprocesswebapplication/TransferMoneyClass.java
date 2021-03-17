//$Id$
package atmprocesswebapplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class TransferMoneyClass extends AbstractTransferMoneyClass
{
	public int performMoneyTransfer()
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
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
			return 1;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
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
			rs.close();
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
