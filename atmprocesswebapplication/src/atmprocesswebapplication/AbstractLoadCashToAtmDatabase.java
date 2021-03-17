//$Id$
package atmprocesswebapplication;

import java.sql.Statement;
import atmprocesswebapplication.LoadCashData;
import atmprocesswebapplication.LoadCashJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import atmprocesswebapplication.ConnectToDatabase;


public abstract class AbstractLoadCashToAtmDatabase extends HttpServlet
{
	int hundreds_count = 0;
	int five_hundreds_count = 0;
	int thousands_count = 0;
	int hundreds = 0;
	int five_hundreds = 0;
	int thousands = 0;
	int total = 0;
	
	public void service(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{	
		PrintWriter out = null;
		
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
			LoadCashJson val = new Gson().fromJson(line1, LoadCashJson.class);
			hundreds = val.hundreds;
			five_hundreds = val.five_hundreds;
			thousands = val.thousands;
			System.out.println(hundreds);
			
			int output = addAmountToAtm();
			if(output == 1)
				resp.sendError(500, "An Error Occured in Machine");
			else
			{
				LoadCashData lcd = new LoadCashData();
				lcd.setHundreds_val(hundreds);
				lcd.setfive_Hundreds_val( five_hundreds);
				lcd.setthousands_val( thousands);
				lcd.settotal_val( total);
				
				Gson obj = new Gson();
				String json = obj.toJson(lcd);
				System.out.println(json);
				
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
			    out.print(json);
			    out.flush(); 
			}
		}
		catch(JsonSyntaxException e)
		{
			System.out.println(e);
			resp.sendError(402, "Invalid!! Enter Numbers");
		}
		catch(Exception e)
		{
			System.out.println(e);
			resp.sendError(500, "An Error Occured in Machine");
		}
	}
	public abstract int addAmountToAtm();
	
	public abstract void calculateTotalAmount();
}


/*public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException
{
	PrintWriter out = resp.getWriter();
	LoadCashData lcd = new LoadCashData();
	lcd.hundreds_val = hundreds;
	lcd.five_hundreds_val = five_hundreds;
	lcd.thousands_val = thousands;
	lcd.total_val = total;
	
	resp.addHeader("Access-Control-Allow-Origin", "http://localhost:3000");
	Gson obj = new Gson();
	String json = obj.toJson(lcd);
	System.out.println(json);
	resp.setContentType(json);
	
	out.println(json);
}*/
//out.println("<html><body> <h1>Total Amount in ATM</h1> The total amount in ATM is "+total+" </body></html>");
//out.println("Invalid!!  Please Enter Numbers ");

/*hundreds = Integer.parseInt(req.getParameter("hundreds"));
five_hundreds = Integer.parseInt(req.getParameter("five_hundreds"));
thousands = Integer.parseInt(req.getParameter("thousands"));*/