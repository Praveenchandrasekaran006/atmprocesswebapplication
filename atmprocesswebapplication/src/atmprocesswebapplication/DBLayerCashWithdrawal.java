
package atmprocesswebapplication;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;



public class DBLayerCashWithdrawal 
{
	
	public static int updateMiniStatement(int acc_num, int amount)
	{
		try
		{		
			int transaction_id = TransactionIdClass.getTransactionId(acc_num);
			if(transaction_id == 0)
				return 1;
						
			Connection con = ConnectToDatabase.getConnectionObj();
			PreparedStatement pst = con.prepareStatement("update MiniStatement set transaction_remark = ?, transaction_type = ?, transaction_amt = ? where  transaction_id = ? and acc_no = ?;");
			
			String remarks = "Debited "+amount+" from ATM";
			pst.setString(1, remarks);
			pst.setString(2, "Debit");
			pst.setInt(3, amount);
			pst.setInt(4, transaction_id);
			pst.setInt(5, acc_num);
			pst.executeUpdate();
			pst.close();
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
			return 1;
		}
		return 0;
	}
	
	public static void updateAccountAtmDetails(int acc_num,int acc_balance,int thousands_count,int fivehundreds_count,int hundreds_count,int atm_total)
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			String query = "truncate table AtmDetails";
			Statement st = con.createStatement();
			st.executeUpdate(query);
			st.close();
								
			PreparedStatement pst = con.prepareStatement("update CustomerDetails set acc_balance = ? where acc_no = ?;");
			pst.setInt(1, acc_balance);
			pst.setInt(2, acc_num);
			pst.executeUpdate();
			pst.close();
			
			pst = con.prepareStatement("insert into AtmDetails (hundreds_count, five_hundreds_count, thousands_count, total) values (?,?,?,?)");
	        pst.setInt(3, thousands_count);
	        pst.setInt(2, fivehundreds_count);
	        pst.setInt(1, hundreds_count);
	        pst.setInt(4, atm_total);
	        pst.executeUpdate();
			pst.close();
			con.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}

