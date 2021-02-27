package atmprocesswebapplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransactionIdClass 
{
	
	public static int getTransactionId(int acc_num)
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			PreparedStatement pst = con.prepareStatement("select transaction_id from TransactionId where acc_no = ?");
			pst.setInt(1, acc_num);
			ResultSet rs = pst.executeQuery();
			rs.next();
			int transaction_id = rs.getInt("transaction_id");
			
			int update_transactionid = transaction_id;
			if(transaction_id < 5)
			{
				update_transactionid++;
			}
			else if(transaction_id == 5)
			{
				update_transactionid = 1;
			}
			
			pst = con.prepareStatement("update TransactionId set transaction_id = ? where acc_no = ?");
			pst.setInt(1, update_transactionid);
			pst.setInt(2, acc_num);
			pst.executeUpdate();
			pst.close();
			
			return transaction_id;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
	
	public static void setTransactionDetailsSender(int sender_acc_num, int receive_acc_num, int amount, int transaction_id)
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			PreparedStatement pst = con.prepareStatement("select acc_holder from CustomerDetails where acc_no = ?");
			pst.setInt(1, sender_acc_num);
			ResultSet rs = pst.executeQuery();
			rs.next();
			String acc_holder = rs.getString("acc_holder");
			
			if(acc_holder.contentEquals("suresh"))
				pst = con.prepareStatement("update suresh set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
			else if(acc_holder.contentEquals("ganesh"))
				pst = con.prepareStatement("update ganesh set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
			else if(acc_holder.contentEquals("magesh"))
				pst = con.prepareStatement("update magesh set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
			else if(acc_holder.contentEquals("naresh"))
				pst = con.prepareStatement("update naresh set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
			else if(acc_holder.contentEquals("harish"))
				pst = con.prepareStatement("update harish set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
					
			String remarks = "Fund Transfer to Acc "+receive_acc_num;
			pst.setString(1, remarks);
			pst.setString(2, "Debit");
			pst.setInt(3, amount);
			pst.setInt(4, transaction_id);
			pst.executeUpdate();
			pst.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	
	public static void setTransactionDetailsReceiver(int sender_acc_num, int receive_acc_num, int amount, int transaction_id)
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			PreparedStatement pst = con.prepareStatement("select acc_holder from CustomerDetails where acc_no = ?");
			pst.setInt(1, receive_acc_num);
			ResultSet rs = pst.executeQuery();
			rs.next();
			String acc_holder = rs.getString("acc_holder");
			
			if(acc_holder.contentEquals("suresh"))
				pst = con.prepareStatement("update suresh set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
			else if(acc_holder.contentEquals("ganesh"))
				pst = con.prepareStatement("update ganesh set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
			else if(acc_holder.contentEquals("magesh"))
				pst = con.prepareStatement("update magesh set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
			else if(acc_holder.contentEquals("naresh"))
				pst = con.prepareStatement("update naresh set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
			else if(acc_holder.contentEquals("harish"))
				pst = con.prepareStatement("update harish set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ?;");
					
			String remarks = "Fund Transfer from Acc "+sender_acc_num;
			pst.setString(1, remarks);
			pst.setString(2, "Credit");
			pst.setInt(3, amount);
			pst.setInt(4, transaction_id);
			pst.executeUpdate();
			pst.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}

	
	/*
	static int suresh_id;
	static int ganesh_id;
	static int magesh_id;
	static int harish_id;
	static int naresh_id;
	
	public static int setSureshTransactionId()
	{
		
		if(suresh_id < 5)
			suresh_id++;
		else
			suresh_id = 1;
		
		
		return suresh_id;
	}
	public static int setGaneshTransactionId()
	{
		if(ganesh_id < 5)
			ganesh_id++;
		else
			ganesh_id = 1;
		
		return ganesh_id;
	}
	public static int setMageshTransactionId()
	{
		if(magesh_id < 5)
			magesh_id++;
		else
			magesh_id = 1;
		
		return magesh_id;
	}
	public static int setNareshTransactionId()
	{
		if(naresh_id < 5)
			naresh_id++;
		else
			naresh_id = 1;
		
		return naresh_id;
	}
	public static int setHarishTransactionId()
	{
		if(harish_id < 5)
			harish_id++;
		else
			harish_id = 1;
		
		return harish_id;
	}*/