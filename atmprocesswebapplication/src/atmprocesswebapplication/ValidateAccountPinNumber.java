//$Id$
package atmprocesswebapplication;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class ValidateAccountPinNumber extends AbstractValidateAccountPinNumber
{
	public int checkAccountPin()
	{
		String query = "select * from CustomerDetails";
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			int flag = 0;
			
			while(rs.next())
			{
				int acc_no = rs.getInt("acc_no");
				acc_holder = rs.getString("acc_holder");
				int pin_no = rs.getInt("pin_no");
				
				if(acc_no == acc_num)
				{
					flag = 1;
					if(pin_no == pin_num)
					{
						AccountNumber.setAccountNumber(acc_no);
						st.close();
						return 1;
					}
					else
					{
						st.close();
						return 2;
					}
				}
			}
			if(flag == 0)
			{
				st.close();
				return 3;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
}
