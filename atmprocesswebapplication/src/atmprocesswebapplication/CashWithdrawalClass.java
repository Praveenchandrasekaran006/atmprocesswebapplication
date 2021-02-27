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
	PrintWriter out;
	String acc_holder;
	
	public void service(HttpServletRequest req, HttpServletResponse resp) 
	{
		int acc_num = AccountNumber.getAccountNumber();
				
		try(Connection con = ConnectToDatabase.getConnectionObj())
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
					PreparedStatement pst = null;
					
					int transaction_id = TransactionIdClass.getTransactionId(acc_num);
					if(transaction_id == 0)
						out.println("<html><body><h1>An error Occured in machine</h1> Your account balance not affected</body></html>");
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
									
					String remarks = "Debited "+amount+" from ATM";
					pst.setString(1, remarks);
					pst.setString(2, "Debit");
					pst.setInt(3, amount);
					pst.setInt(4, transaction_id);
					pst.executeUpdate();
					pst.close();
										
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
	
	public int validateAmount(int amount, int acc_num) 
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{
			
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from AtmDetails");
			rs.next();
			
			hundreds_count = rs.getInt("hundreds_count");
			fivehundreds_count = rs.getInt("five_hundreds_count");
			thousands_count = rs.getInt("thousands_count");
			atm_total = rs.getInt("total");
			rs.close();
			st.close();
			
			PreparedStatement pst = con.prepareStatement("select acc_holder,acc_balance from CustomerDetails where acc_no = ?;");
			pst.setInt(1, acc_num);
			ResultSet rs1 = pst.executeQuery();
			rs1.next();
			acc_holder = rs1.getString("acc_holder");
			acc_balance = rs1.getInt("acc_balance");
			rs1.close();
			pst.close();
			
			if(amount%10 != 0)
				return 5;
			if(amount > atm_total)
				return 1;
			if(amount > acc_balance)
				return 2;
			
			if(amount <=10000 && amount >=100)
				return 3;
			else
				return 4;  
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
	
	public int withdrawAmount(int amount, int acc_num)
	{
		try(Connection con = ConnectToDatabase.getConnectionObj())
		{	
			int thousands = 0;
			int hundreds = 0;
			int five_hundreds = 0;
			int temp_amount = amount; 
			
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
					System.out.println(amount);
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
			out.println("Hundreds_count = "+hundreds);
			out.println("five hundreds count = "+five_hundreds);
			out.println("thousands count = "+thousands);
			out.println("Withdraw Successful The Balance Amount is "+acc_balance);
			return 1;
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return 0;
	}
}




/*
System.out.println(hundreds_count);
			System.out.println(fivehundreds_count);
			System.out.println(thousands_count);
			System.out.println();
			System.out.println();


			System.out.println(hundreds_count);
			System.out.println(fivehundreds_count);
			System.out.println(thousands_count);
			
			System.out.println();
			System.out.println();
			
			System.out.println(hundreds);
			System.out.println(five_hundreds);
			System.out.println(thousands);

while(amount <= 1000 && (hundreds_count != 0) && amount >= 100 && hundreds < 10)
					{
						hundreds++;
						--hundreds_count;
						amount -= 100;
					}

"<html>"
+ "<body>"
+"<form action = "+"withdrawal"+">"
+"<h1> Withdrawal</h1>"
+ "Enter the Amount To be withdrawn <input type="+"text"+" name = "+"withdraw_amt"+"><br>"
+ "<input type = "+"Submit"+"><br>"
+"<h2> Note:</h2><br>"
+"The withdrawal limit for single transaction is 10,000 maximum and 100 minimum <br>"
+"</form>"
+ "</body>"
+ "</html>"
*/