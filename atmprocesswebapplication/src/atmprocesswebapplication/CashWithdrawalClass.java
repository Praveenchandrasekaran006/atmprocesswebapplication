package atmprocesswebapplication;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import atmprocesswebapplication.ConnectToDatabase;
import atmprocesswebapplication.ValidateAccountPinNumber;

public class CashWithdrawalClass extends HttpServlet
{
	int acc_balance;
	int atm_total;
	int hundreds_count;
	int fivehundreds_count;
	int thousands_count;
	
	int thousands;
	int hundreds;
	int five_hundreds;
	
	PrintWriter out;
	Connection con;
	
	public void service(HttpServletRequest req, HttpServletResponse resp) 
	{
		int acc_num = AccountNumber.getAccountNumber();
		
		try
		{
			out = resp.getWriter();
			int amount = Integer.parseInt(req.getParameter("amount"));
			int output = validateAmount(amount,acc_num);
						
			if(output == 1)
				out.println("Insufficient Amount in Atm");
			else if(output == 2)
				out.println("Insufficient Account Balance");
			else if(output == 4)
				out.println("Amount allowed for single transaction is 10000 maximum and 100 minimum");
			else if(output == 0)
				out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			else if(output == 5)
				out.println("<html><body><h1> Invalid!! </h1> Enter amount in multiples of 10 </body></html>");
			else
			{
				int output_1 = withdrawAmount(amount,acc_num);
				if(output_1 == 1)
				{
					out.println("Hundreds_count = "+hundreds);
					out.println("five hundreds count = "+five_hundreds);
					out.println("thousands count = "+thousands);
					out.println("The account balance is "+acc_balance);
					out.println("Withdraw Successful The Balance Amount is "+acc_balance);					
				}
				else if(output_1 == 2)
					out.println("<html><body><h1>Insufficient amount in Atm</h1></body></html>");
				else
					out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
			}
						
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
	
	
	public int withdrawAmount(int amount, int acc_num)
	{
		
			int out_logic = cashWithdrawalLogic(acc_num,amount);
			DBLayerCashWithdrawal.updateAccountAtmDetails(acc_num,acc_balance,thousands_count,fivehundreds_count,hundreds_count,atm_total);
			
			return out_logic;
	}
	
	public int cashWithdrawalLogic(int acc_num,int amount)
	{

		int temp_amount = amount; 
		hundreds = 0;
		five_hundreds = 0;
		thousands = 0;
		while(amount > 0)
		{
			if(amount <= 5000 && amount >= 100)
			{
				if(thousands_count != 0 && amount >= 1000 && thousands < 3)
				{
					thousands += 1;
					thousands_count--;
					amount -= 1000;
				}
				while((hundreds_count != 0) && amount >= 100 && hundreds < 10)
				{
					hundreds++;
					--hundreds_count;
					amount -= 100;
				}
				
				while(amount >= 500 && (fivehundreds_count != 0) && five_hundreds < 6)
				{
					five_hundreds++;
					fivehundreds_count--;
					amount -= 500;
				}
				
				if(amount < 500 && amount >= 100)
				{
					int value = 500-amount;
					hundreds = hundreds-(value/100);
					hundreds_count += (value/100);
					five_hundreds++;
					fivehundreds_count--;
					amount = 0;
				}
			}
			else if(amount > 5000)
			{
				if(thousands_count >= 3)
				{
					thousands +=3;
					thousands_count-=3;
					amount -= 3000;
				}
				if(fivehundreds_count >=2)
				{
					five_hundreds += 2;
					fivehundreds_count-=2;
					amount-=1000;
				}
			}
			if(amount == temp_amount)
				return 2;
			
		}
		
		atm_total -= temp_amount;
		acc_balance -= temp_amount;
		int out = DBLayerCashWithdrawal.updateMiniStatement(acc_num,temp_amount);
		if(out == 1)
			return 0;
		return 1;
	}
	
	public int validateAmount(int amount, int acc_num) 
	{
		try
		{
			con = ConnectToDatabase.getConnectionObj();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from AtmDetails");
			rs.next();
			
			hundreds_count = rs.getInt("hundreds_count");
			fivehundreds_count = rs.getInt("five_hundreds_count");
			thousands_count = rs.getInt("thousands_count");
			atm_total = rs.getInt("total");
			rs.close();
			st.close();
			
			PreparedStatement pst = con.prepareStatement("select acc_balance from CustomerDetails where acc_no = ?;");
			pst.setInt(1, acc_num);
			ResultSet rs1 = pst.executeQuery();
			rs1.next();
			
			acc_balance = rs1.getInt("acc_balance");
			rs1.close();
			pst.close();
			con.close();
			
			if(amount%100 != 0)
				return 5;
			if(!(amount <=10000 && amount >=100))
				return 4;
			if(amount > atm_total)
				return 1;
			if(amount > acc_balance)
				return 2;
			if(amount <=10000 && amount >=100)
				return 3;
			  
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
	
}




