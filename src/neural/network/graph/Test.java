package neural.network.graph;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class Test {
	public static void main(String args[])
	{
		LocalDateTime now = LocalDateTime.now();
		
		Calendar firstDate = new GregorianCalendar(2018, 1, 28);
		Calendar secondDate = new GregorianCalendar(now.getYear(), now.getMonthValue(), now.getDayOfMonth());
		
		int months  = (firstDate.get(Calendar.YEAR) - secondDate.get(Calendar.YEAR)) * 12 +
			       (firstDate.get(Calendar.MONTH)- secondDate.get(Calendar.MONTH)) + 
			       (firstDate.get(Calendar.DAY_OF_MONTH) >= secondDate.get(Calendar.DAY_OF_MONTH)? 0: -1); 
			System.out.println("Diff is.." + months); 
	}
}
