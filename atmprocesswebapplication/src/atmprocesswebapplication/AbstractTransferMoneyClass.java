//$Id$
package atmprocesswebapplication;

import atmprocesswebapplication.TransferMoneyData;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public abstract class AbstractTransferMoneyClass extends HttpServlet
{
	int sender_acc_balance;
	int sender_acc_num;
	int receive_acc_num;
	int amount;
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		PrintWriter out = null;
		try
		{
			resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
			
			out = resp.getWriter();		
			sender_acc_num = AccountNumber.getAccountNumber();
			
			StringBuffer jb = new StringBuffer();
			BufferedReader br = req.getReader();
			String line = null;
			while((line = br.readLine())!= null)
			jb.append(line);
			
			String line1 = jb.toString();
			
			TransferMoneyJson val = new Gson().fromJson(line1, TransferMoneyJson.class);
			receive_acc_num = val.receive_acc_no;
			amount = val.amount;
			
			//System.out.println(receive_acc_num);
			int acc_vali_output = validateReceiverAccountNumber(receive_acc_num);
			
			if(acc_vali_output == 1)
			{
				int amt_vali_output = validateAmount(amount, sender_acc_num);
				if(amt_vali_output == 1)
				{
					int out_perform = performMoneyTransfer();
					if(out_perform == 1)
					{
						TransferMoneyData tmd = new TransferMoneyData();
						tmd.acc_balance = sender_acc_balance;

     					Gson obj = new Gson();
						String json = obj.toJson(tmd);
						System.out.println(json);
						
						resp.setContentType("application/json");
						resp.setCharacterEncoding("UTF-8");
					    out.print(json);
					    out.flush();
					}
					else
						resp.sendError(500,"An Error occured in Machine");
				}
				else if(amt_vali_output == 2)
					resp.sendError(404, "Cannot Transfer!! Enter amount in given limit");
				else if(amt_vali_output == 3)
					resp.sendError(101, "Insufficient Amount in Sender's Account");
				else
					resp.sendError(500,"An Error occured in Machine");
			}
			else if(acc_vali_output == 2)
				resp.sendError(405, "Invalid!! Receiver Account Number");
			else
				resp.sendError(500,"An Error occured in Machine");		
		}
		catch(JsonSyntaxException e)
		{
			resp.sendError(402, "Invalid!! Enter Numbers");
			System.out.println(e);
		}
		catch(Exception e)
		{
			resp.sendError(500,"An Error occured in Machine");
			System.out.println(e);
		}
		
	}
	
	public abstract int validateReceiverAccountNumber(int acc_num);
	
	public abstract int validateAmount(int amount, int account);
	
	public abstract int performMoneyTransfer();
	
}





//out.println("Insufficient Amount in Sender's Account ");
//out.print(" Invalid Account Number  The Entered Account Number not Available");
//out.println("Cannot Transfer the Entered Amount...  Enter the amount within the given limit");
//receive_acc_num = Integer.parseInt(req.getParameter("recv_acc_number"));
//amount = Integer.parseInt(req.getParameter("amount"));
//out.println("<html><body><h1>Amount Transfer Successfull </h1> Your account Balance is "+sender_acc_balance+"</body></html>");