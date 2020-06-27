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
import java.util.Map.Entry;
import java.util.Scanner;

public class Test {

	public static void main(String[] args) throws IOException {

		try {
			File myObj = new File("src/bugreport.txt");
			Scanner myReader = new Scanner(myObj);
			String pid = "4667";
			String searchString[] = { "WARNING", "OOM", "OutOfMemoryError" };
			List<String> al = new ArrayList<String>();
			List<String> al1 = new ArrayList<String>();
			List<String> al2 = new ArrayList<String>();
			List<String> al4 = new ArrayList<String>();
			Map<String, List<String>> mp = new HashMap<String, List<String>>();
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

								al.add(msg);
							}
							if (msg.contains("AndroidRuntime:")) {
								al2.add(msg);
							}
							for (String s : searchString) {
								// System.out.println(msg);
								if (msg.toLowerCase().contains(s.toLowerCase())) {
									al1.add(msg);
									break;
								}
							}
						}
					}catch(Exception e){
						}
					

				}

			}

			ArrayList<Integer> indexAl = new ArrayList<>();
			for (int i = 0; i < al2.size(); i++) {
				if (al2.get(i).trim().contains("FATAL EXCEPTION")) {
					indexAl.add(i);
				}
			}

			for (int a : indexAl) {
				List<String> al3 = new ArrayList<String>();
				for (int i = a + 3; i < al2.size(); i++) {

					if (al2.get(i).contains("FATAL EXCEPTION:")) {
						break;
					}
					al3.add(al2.get(i).substring(16).trim());
				}
				al4.add(al2.get(a + 2).substring(16).trim());
				mp.put(al2.get(a + 2).substring(16).trim(), al3);
			}

			System.out.println("FATAL EXCEPTION");
			System.out.println("==================");
			System.out.println("");
			System.out.println("Exception Message| # of Occurrences");
			countFrequencies(al4);
			System.out.println("");
			System.out.println("Stacktrace:");
			System.out.println("==================");
			System.out.println("");
			int x = 1;
			for (Entry<String, List<String>> entry : mp.entrySet()) {
				String k = entry.getKey();
				List<String> v = entry.getValue();
				System.out.println("#" + x + ")" + " " + k);
				for (int i = 0; i < v.size(); i++) {
					System.out.println("     " + v.get(i));
				}

				x++;
			}
			System.out.println("");
			System.out.println("");
			System.out.println("Errors");
			System.out.println("==================");
			System.out.println("");
			System.out.println("Error Message| # of Occurrences");
			countFrequencies(al);
			System.out.println("");
			System.out.println("");
			System.out.println("Matching Strings");
			System.out.println("==================");
			System.out.println("");
			System.out.println("Matching String| # of Occurrences");
			countFrequencies(al1);
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
}
