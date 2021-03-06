package atmprocesswebapplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransactionIdClass 
{
	public static int getMiniTransactionId(int acc_num)
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			PreparedStatement pst = con.prepareStatement("select transaction_id from TransactionId where acc_no = ?");
			pst.setInt(1, acc_num);
			ResultSet rs = pst.executeQuery();
			rs.next();
			int transaction_id = rs.getInt("transaction_id");
			
			return transaction_id;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
	public static int getTransactionId(int acc_num)
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			PreparedStatement pst = con.prepareStatement("select transaction_id from TransactionId where acc_no = ?");
			pst.setInt(1, acc_num);
			ResultSet rs = pst.executeQuery();
			rs.next();
			int transaction_id = rs.getInt("transaction_id");
			
			
			int update_transactionid = transaction_id+1;
			
			pst = con.prepareStatement("update TransactionId set transaction_id = ? where acc_no = ?");
			pst.setInt(1, update_transactionid);
			pst.setInt(2, acc_num);
			pst.executeUpdate();
			pst.close();
			
			return update_transactionid;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
	
	public static void setTransactionDetailsSender(int sender_acc_num, int receive_acc_num, int amount, int transaction_id)
	{
		try
		{
			
			Connection con = ConnectToDatabase.getConnectionObj();
			PreparedStatement pst = con.prepareStatement("insert into MiniStatement (acc_no, transaction_id, transaction_remark, transaction_type, transaction_amt) values (?,?,?,?,?);");
			String remarks = "Fund Transfer to Acc "+receive_acc_num;
			
			pst.setInt(1, sender_acc_num);
			pst.setInt(2, transaction_id);
			pst.setString(3, remarks);
			pst.setString(4, "Debit");
			pst.setInt(5, amount);
			
			pst.executeUpdate();
			pst.close();
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	public static void setTransactionDetailsReceiver(int sender_acc_num, int receive_acc_num, int amount, int transaction_id)
	{
		try
		{
			
			Connection con = ConnectToDatabase.getConnectionObj();
			PreparedStatement pst = con.prepareStatement("insert into MiniStatement (acc_no, transaction_id, transaction_remark, transaction_type, transaction_amt) values (?,?,?,?,?);");		
			String remarks = "Fund Transfer from Acc "+sender_acc_num;

			pst.setInt(1, receive_acc_num);
			pst.setInt(2, transaction_id);
			pst.setString(3, remarks);
			pst.setString(4, "Credit");
			pst.setInt(5, amount);
			
			pst.executeUpdate();
			pst.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}

//if(transaction_id < 5)
//{
//	update_transactionid++;
//}
//else if(transaction_id == 5)
//{
//	update_transactionid = 1;
//}

//PreparedStatement pst = con.prepareStatement("update MiniStatement set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ? and acc_no = ?;");
//pst.setString(1, remarks);
//pst.setString(2, "Debit");
//pst.setInt(3, amount);
//pst.setInt(4, transaction_id);
//pst.setInt(5, sender_acc_num);

//PreparedStatement pst = con.prepareStatement("update MiniStatement set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ? and acc_no = ?;");
//pst.setString(1, remarks);
//pst.setString(2, "Credit");
//pst.setInt(3, amount);
//pst.setInt(4, transaction_id);
//pst.setInt(5, receive_acc_num);