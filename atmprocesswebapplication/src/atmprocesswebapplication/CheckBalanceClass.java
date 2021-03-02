//$Id$
package atmprocesswebapplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CheckBalanceClass extends AbstractCheckBalanceClass
{
	public int getBalance()
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			PreparedStatement pst = con.prepareStatement("select acc_balance from CustomerDetails where acc_no = ?; ");	
			pst.setInt(1, acc_num);
			ResultSet rs = pst.executeQuery();
			rs.next();	
			acc_balance = rs.getInt("acc_balance");
			rs.close();
			pst.close();
			return 1;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
}
