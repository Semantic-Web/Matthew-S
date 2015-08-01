/**
 * 
 */
package geoNames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import javax.swing.*;
import org.geonames.InsufficientStyleException;
import org.geonames.Style;
import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

/**
 * @author Kevin A. Medina
 *
 */
public class driver {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) 
	{
		  
		WebService.setUserName(""); // add your username here
		  
		ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
		
		while(true)
		{
			//GET USER QUERY
			String state = JOptionPane.showInputDialog("Enter State").toUpperCase();
			String city = JOptionPane.showInputDialog("Enter City").toUpperCase();
			
			//USE GEONAMES TO GET DESIRED INFORMATION
			ToponymSearchResult searchResult = null;
			try 
			{
				searchCriteria.addCountryCode("US");
				searchCriteria.setQ(state);
				searchCriteria.setName(city);
				searchCriteria.setStyle(Style.FULL);
				searchResult = WebService.search(searchCriteria);
				
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			
			Toponym cityToponym = new Toponym();
			cityToponym.setPopulation((long) 0);
			double latitude = 0;
			double longitude = 0;
			if(searchResult!=null)
			{
				for (Toponym toponym : searchResult.getToponyms()) 
				{
			    	latitude = toponym.getLatitude();
			    	longitude = toponym.getLongitude();
				}//end for-each loop
			}//end if searchResult != null
			else { System.out.println("Search Result is NULL");}
			
			//GET CITY TAX INFORMATION		
			BufferedReader csvFileReader = null;
			String inputLine = null;
			double taxes2015 = 0, taxes2014 = 0, assessedValue2015 = 0,assessedValue2014 = 0;
			int count2015 = 0, count2014 = 0;
			
			try
			{
				csvFileReader = new BufferedReader(new FileReader("Essex_County_Data_Set.csv"));
				
				while((inputLine = csvFileReader.readLine()) != null)
				{
					String[] line = inputLine.split(",");
					
					if(city.equalsIgnoreCase(line[0]))
					{	
						switch(line[9])
						{
						case "2015":
							count2015++;
							taxes2015 += Double.parseDouble(line[8]);
							assessedValue2015 += Double.parseDouble(line[7]);
							break;
						case "2014":
							count2014++;
							taxes2014 += Double.parseDouble(line[8]);
							assessedValue2014 += Double.parseDouble(line[7]);
							break;
						default:
							break;
						}				
					}				
				}
			}catch(Exception e)
			{
				System.out.println("Unable to Open File");
				e.printStackTrace();
				
			}finally
			{
				if(csvFileReader != null)
				{
					try{csvFileReader.close();}
					catch(Exception e)
					{
						System.out.println("Unable to Close Reader");
						e.printStackTrace();
					}
				}
			}
			
			double averageTax2015 = taxes2015/count2015;
			double averageTax2014 = taxes2014/count2014;
			double averageValue2015 = assessedValue2015/count2015;
			double averageValue2014 = assessedValue2014/count2014;
			
			double tax2015Change = ((averageTax2015/averageTax2014) - 1) * 100;
			double value2015Change = ((averageValue2015/averageValue2014) - 1) * 100;		
			double taxPercentValue2015 = (averageTax2015/averageValue2015) * 100;
			double taxPercentValue2014 = (averageTax2014/averageValue2014) * 100; ;
			double taxPercentValueChange = taxPercentValue2015 - taxPercentValue2014;
			
			
			//DISPLAY INFORMATION FOR THE CITY
			JFrame frame = new JFrame();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setTitle(city+", "+state+" Coordinates: "+latitude+", "+longitude);		
			frame.setSize(1000,1000);
			
			DecimalFormat df = new DecimalFormat("###.###");
			
			String[] columnNames = {"","2014","2015","% Change (2014-2015)"};
			
			Object[][] rowData = {{"Average Taxes Due","$ "+df.format(averageTax2014),"$ "+df.format(averageTax2015),df.format(tax2015Change)+" %"},
								 {"Average Assessed Value","$ "+df.format(averageValue2014),"$ "+df.format(averageValue2015),df.format(value2015Change)+" %"},
								 {"Average Tax as a % of Assessed Value",df.format(taxPercentValue2014)+" %",df.format(taxPercentValue2015)+" %",df.format(taxPercentValueChange)+" %"}};
			
			JTable taxTable = new JTable(rowData,columnNames);
			taxTable.setFillsViewportHeight(false);
			
			JScrollPane scrollPane = new JScrollPane(taxTable);
			JLabel title = new JLabel("TAX PROPERTIES");
			frame.setPreferredSize(new Dimension(1000,150));
			frame.setLayout(new BorderLayout());
			frame.add(title, BorderLayout.PAGE_START);
			frame.add(scrollPane, BorderLayout.CENTER);
			frame.pack();
			frame.setVisible(true);
		
		}
	}
}
