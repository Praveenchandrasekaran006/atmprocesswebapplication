//$Id$
package atmprocesswebapplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoadCashToAtmDatabase extends AbstractLoadCashToAtmDatabase
{
	
	public int addAmountToAtm()
	{
		String query = "select * from AtmDetails;";
		String query_1 = "truncate table AtmDetails"; 
		
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{			
			Statement st = con.createStatement();	
			ResultSet rs = st.executeQuery(query);			
			while(rs.next())
			{
				hundreds_count = rs.getInt("hundreds_count");
				five_hundreds_count = rs.getInt("five_hundreds_count");
				thousands_count = rs.getInt("thousands_count");	
			}
			st.executeUpdate(query_1);
			st.close();	
						
			calculateTotalAmount();
			
			PreparedStatement pst = con.prepareStatement("insert into AtmDetails (hundreds_count, five_hundreds_count, thousands_count, total) values (?,?,?,?);");
			pst.setInt(1, hundreds);
			pst.setInt(2, five_hundreds);
			pst.setInt(3, thousands);
			pst.setInt(4, total);
			pst.executeUpdate();
			pst.close();
			return 0;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 1;	
	}
	
	public void calculateTotalAmount()
	{
		hundreds += hundreds_count;
		five_hundreds += five_hundreds_count;
		thousands += thousands_count;
		total = (hundreds*100)+(five_hundreds*500)+(thousands*1000);
	}
}
