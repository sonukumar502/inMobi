package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) throws IOException {
		
		
		try {
		      File myObj = new File("src/bugreport.txt");
		      Scanner myReader = new Scanner(myObj);
		      String pid="1566";
		      String searchString[]={"WARNING","OOM","OutOfMemoryError"};
		      List<String> al= new ArrayList<String>();
		      List<String> al1= new ArrayList<String>();
		      while (myReader.hasNextLine()) {
		        String data = myReader.nextLine();
		        if(data.startsWith("0")||data.startsWith("1")){
		        	//System.out.println(data);
		        	String[] a=data.split(" ");
		        	String logLevels=null;
	        		String msg=null;
		        	if(a[4].trim().equals(pid)||a[3].trim().equals(pid)){
		        		String data1=data.substring(18).trim();
		        		String pids=data1.substring(0,5).trim();
		        		//System.out.println(pids.length());
		        		//System.out.println(data1);
						if (pids.length() == 4) {
							logLevels = data1.substring(11, 12);
							msg = data1.substring(13);
						} else if (pids.length() == 3) {
							logLevels = data1.substring(10, 11);
							msg = data1.substring(12);
						} else if (pids.length() == 5) {
							logLevels = data1.substring(12, 13);
							msg = data1.substring(14);
						}
						if(logLevels.equals("E")){
		        		//System.out.println(pids+" "+logLevels+" "+msg);
							//System.out.println(msg.trim());
							al.add(msg);}
						for(String s:searchString){
			        		//System.out.println(msg);
			        		if(msg.toLowerCase().contains(s.toLowerCase())){
			        			al1.add(msg);
			        			break;
			        		}
			        	}
		        		}
		        	
		        	
		        	
		        }
		        	
		      }
		      System.out.println("Errors");
		      System.out.println("==================");
		      System.out.println("");
		      System.out.println("Error Message| # of Occurrences");
		      countFrequencies(al);
		      System.out.println("Matching Strings");
		      System.out.println("==================");
		      System.out.println("");
		      System.out.println("Matching String| # of Occurrences");
		      //System.out.println(al1.size());
		      countFrequencies(al1);
		      //System.out.println(al);
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
	}
	public static void countFrequencies(List<String> al) 
    { 
        // hashmap to store the frequency of element 
        Map<String, Integer> hm = new HashMap<String, Integer>(); 
  
        for (String i : al) { 
            Integer j = hm.get(i); 
            hm.put(i, (j == null) ? 1 : j + 1); 
        } 
  
        // displaying the occurrence of elements in the arraylist 
        for (Map.Entry<String, Integer> val : hm.entrySet()) { 
            System.out.println(val.getKey() + "| "+ val.getValue()); 
        } 
    } 
}
