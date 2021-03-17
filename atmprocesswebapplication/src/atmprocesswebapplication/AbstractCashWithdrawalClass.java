package atmprocesswebapplication;
import atmprocesswebapplication.CashWithdrawalData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import atmprocesswebapplication.ConnectToDatabase;
import atmprocesswebapplication.AbstractValidateAccountPinNumber;

public abstract class AbstractCashWithdrawalClass extends HttpServlet
{
	int acc_balance;
	int atm_total;
	int hundreds_count;
	int fivehundreds_count;
	int thousands_count;
	
	int thousands;
	int hundreds;
	int five_hundreds;
	int acc_num;
	PrintWriter out;
	Connection con;
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		acc_num = AccountNumber.getAccountNumber();
		
		try
		{
			resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
			out = resp.getWriter();
			
			StringBuffer jb = new StringBuffer();
			BufferedReader br = req.getReader();
			String line = null;
			while((line = br.readLine())!= null)
				jb.append(line);
			
			String line1 = jb.toString();
			CashWithdrawalJson val = new Gson().fromJson(line1, CashWithdrawalJson.class);
			int amount = val.amount;
			
			System.out.println(amount);
			
			int output = validateAmount(amount,acc_num);
						
			if(output == 1)
				resp.sendError(100, "Insufficient Amount in ATM");
			else if(output == 2)
				resp.sendError(101,"Insufficient Account Balance");
			else if(output == 4)
				resp.sendError(406, "Amount allowed for single transaction is 10000 maximum and 100 minimum");
			else if(output == 0)
				resp.sendError(500, "An Error Occured in Machine");
			else if(output == 5)
				resp.sendError(407," Invalid!!  Enter amount in multiples of 10");
			else
			{
				int output_1 = withdrawAmount(amount,acc_num);
				if(output_1 == 1)
				{
					CashWithdrawalData cwd = new CashWithdrawalData();
					cwd.hundreds_count = hundreds;
					cwd.five_hundreds_count = five_hundreds;
					cwd.thousands_count = thousands;
					cwd.acc_balance = acc_balance;
					
					Gson obj = new Gson();
					String json = obj.toJson(cwd);
					System.out.println(json);
					
					resp.setContentType("application/json");
					resp.setCharacterEncoding("UTF-8");
				    out.print(json);
				    out.flush(); 
				}
				else if(output_1 == 2)
					resp.sendError(100, "Insufficient Amount in ATM");
				else
					resp.sendError(500, "An Error Occured in Machine");
			}
						
		}
		catch(JsonSyntaxException e)
		{
			resp.sendError(402, "Invalid!! Enter Numbers");	
		}
		catch(Exception e)
		{
			resp.sendError(500, "An Error Occured in Machine");
			System.out.println(e);
		}
	}
	
	
	public int withdrawAmount(int amount, int acc_num)
	{
		
			int out_logic = cashWithdrawalLogic(acc_num,amount);
			updateAccountAtmDetails();
			
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
		int out = updateMiniStatement(acc_num,temp_amount);
		if(out == 1)
			return 0;
		return 1;
	}
	
	public abstract int validateAmount(int amount, int acc_num); 
	
	public abstract int updateMiniStatement(int acc_num, int amount);
	
	public abstract void updateAccountAtmDetails();
	
}


//out.println("<html><body><h1>Withdraw Successful</h1>"
//+"Hundreds_count = "+hundreds+"<br>"
//+"five hundreds count = "+five_hundreds+"<br>"
//+"thousands count = "+thousands+"<br>"
//+"The Balance Amount is "+acc_balance+"<br></body></html>");
//out.println("Invalid!! Please Enter Numbers ");
//out.println("Insufficient amount in Atm");
//int amount = Integer.parseInt(req.getParameter("amount"));