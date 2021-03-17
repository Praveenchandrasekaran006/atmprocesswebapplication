//$Id$
package atmprocesswebapplication;

import atmprocesswebapplication.InputValidateData;
import atmprocesswebapplication.Error;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import atmprocesswebapplication.ConnectToDatabase;

public abstract class AbstractValidateAccountPinNumber extends HttpServlet 
{
	PrintWriter out = null;
	int acc_num;
	int pin_num;
	String acc_holder;
	
	
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
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
			InputValidateData val = new Gson().fromJson(line1, InputValidateData.class);
			
			acc_num = val.acc_no;
			pin_num = val.pin_no  ;
			System.out.println(acc_num);;
			
			OutputValidateData val1 = new Gson().fromJson(line1, OutputValidateData.class);
			
			int out_check = checkAccountPin();
			if(out_check == 1)
			{
				val1.output = out_check;
	
				Gson obj = new Gson();
				String json = obj.toJson(val1);
				System.out.println(json);
				
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
			    out.print(json);
			    out.flush(); 
				
			}
			else if(out_check == 2)
				resp.sendError(400, "Incorrect Pin Number");
			else if(out_check == 3)
				resp.sendError(401, "Incorrect Account Number");
			else
				resp.sendError(500, "An error Occured in Machine");
		}
		catch(JsonSyntaxException e)
		{
			resp.sendError(402, "Invalid!! Enter Numbers");
			System.out.println(e);
		}
		catch(Exception e)
		{
			resp.sendError(500,"An Error Occured in Machine");
			System.out.println(e);
		}
	
	}
	
	public abstract int checkAccountPin();
	
}
//out.println("An error Occured in machine... Your account balance not affected");
//out.println(" Invalid!!  Please Enter Numbers ");
//out.println("An error Occured in machine Your account balance not affected");

/*out.println("<html><body><h1>Welcome "+acc_holder+"</h1>"
		+"<div>	<form action = "+"check"+">	"
		+ " <h2> Check Balance </h2> <input type = "+"Submit"+"><br>"
		+ "</form></div>"
		+"<div> <form action = "+"CashWithdrawal.html"+">"
		+ "<h2> Cash Withdrawal </h2><input type = "+"Submit"+"><br>"
		+ "</form></div>"
		+"<div>	<form action = "+"TransferMoney.html"+">"
		+ "<h2> Transfer Money </h2><input type = "+"Submit"+"><br>"
		+ "</form>	</div>"
		+"<div>	<form action = "+"ministate"+">"
		+ "<h2> Mini Statement </h2><input type = "+"Submit"+"><br>"
		+ "</form></div>"
		+"</body></html>");
		*/
/*	
acc_num = Integer.parseInt(req.getParameter("acc_number"));
pin_num = Integer.parseInt(req.getParameter("pin_number"));
*/