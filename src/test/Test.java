package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) throws IOException {

		try {
			File myObj = new File("resources/bugreport.txt");
			Scanner myReader = new Scanner(myObj);
			String pid = getValueFromPropertiesFile("PID");
			String srchString=getValueFromPropertiesFile("SEARCHSTRING").trim();
			String searchString[] = srchString.split(",");
			List<String> errorList = new ArrayList<String>();
			List<String> matchingStringList = new ArrayList<String>();
			List<String> fatalExceptionList = new ArrayList<String>();
			List<String> ftalExceptionStakeTraceList = new ArrayList<String>();
			Map<String, List<String>> fatalExceptionMap = new HashMap<String, List<String>>();
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				if (data.startsWith("01-") || data.startsWith("02-") || data.startsWith("03-") || data.startsWith("04-")
						|| data.startsWith("05-") || data.startsWith("06-") || data.startsWith("07-")
						|| data.startsWith("08-") || data.startsWith("09-") || data.startsWith("10-")
						|| data.startsWith("11-") || data.startsWith("12-")) {
					String[] a = data.split(" ");
					String logLevels = null;
					String msg = null;
					try{
						if (a[4].trim().equals(pid) && a[4].trim()!=null|| a[3].trim().equals(pid)&& a[3].trim()!=null) {
							String data1 = data.substring(18).trim();
							String pids = data1.substring(0, 5).trim();

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
							if (logLevels.equals("E")) {

								errorList.add(msg);
							}
							if (msg.contains("AndroidRuntime:")) {
								fatalExceptionList.add(msg);
							}
							for (String s : searchString) {
								//To match only exact word from Input 
								if (msg.contains(s)) {
									matchingStringList.add(msg);
									break;
								}
							}
						}
					}catch(Exception e){
						}
					

				}

			}

			ArrayList<Integer> indexAl = new ArrayList<>();
			for (int i = 0; i < fatalExceptionList.size(); i++) {
				if (fatalExceptionList.get(i).trim().contains("FATAL EXCEPTION")) {
					indexAl.add(i);
				}
			}

			for (int a : indexAl) {
				List<String> al3 = new ArrayList<String>();
				for (int i = a + 3; i < fatalExceptionList.size(); i++) {

					if (fatalExceptionList.get(i).contains("FATAL EXCEPTION:")) {
						break;
					}
					al3.add(fatalExceptionList.get(i).substring(16).trim());
				}
				ftalExceptionStakeTraceList.add(fatalExceptionList.get(a + 2).substring(16).trim());
				fatalExceptionMap.put(fatalExceptionList.get(a + 2).substring(16).trim(), al3);
			}
			System.out.println("Logs for PID: "+pid);
			System.out.println("====================================");
			System.out.println();
			System.out.println("FATAL EXCEPTION");
			System.out.println("==================");
			if(ftalExceptionStakeTraceList.size()>0){
			System.out.println("");
			System.out.println("Exception Message| # of Occurrences");
			countFrequencies(ftalExceptionStakeTraceList);
			System.out.println("");
			System.out.println("Stacktrace:");
			System.out.println("==================");
			System.out.println("");
			int x = 1;
			for (Entry<String, List<String>> entry : fatalExceptionMap.entrySet()) {
				String k = entry.getKey();
				List<String> v = entry.getValue();
				System.out.println("#" + x + ")" + " " + k);
				for (int i = 0; i < v.size(); i++) {
					System.out.println("     " + v.get(i));
				}

				x++;
			}
			}else{
				System.out.println("No FATAL Exceptions for this PID!!");
			}
			System.out.println("");
			System.out.println("");
			System.out.println("Errors");
			System.out.println("==================");
			if(errorList.size()>0){
			System.out.println("");
			System.out.println("Error Message| # of Occurrences");
			countFrequencies(errorList);
			}
			else{
				System.out.println("No Errors for this PID!!");
			}
			System.out.println("");
			System.out.println("");
			System.out.println("Matching Strings: "+srchString);
			System.out.println("=======================================================");
			if(matchingStringList.size()>0){
			System.out.println("");
			System.out.println("Matching String| # of Occurrences");
			countFrequencies(matchingStringList);
			}else{
				System.out.println("No Matching Stirng for this PID!!");
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static void countFrequencies(List<String> al) {
		Map<String, Integer> hm = new HashMap<String, Integer>();

		for (String i : al) {
			Integer j = hm.get(i);
			hm.put(i, (j == null) ? 1 : j + 1);
		}

		for (Map.Entry<String, Integer> val : hm.entrySet()) {
			System.out.println(val.getKey() + "| " + val.getValue());
		}
	}
	
	public static String getValueFromPropertiesFile(String key) throws IOException{
		InputStream input = new FileInputStream("resources/Input.properties");
		Properties prop = new Properties();
		prop.load(input);
		String value=prop.getProperty(key);
		return value;
	}
}
