package edu.usc.cs.xinge;

import java.io.FileWriter;
import java.io.IOException;

public class WriteCSV {
	private FileWriter fetch_csv;
	private FileWriter visit_csv;
	private FileWriter urls_csv;
	
	
	public WriteCSV(String fetch, String visit, String urls) throws IOException{
		fetch_csv = new FileWriter(fetch, false);
		visit_csv = new FileWriter(visit, false);
		urls_csv = new FileWriter(urls, false);
	}
	
	public void Finish() throws IOException{
		fetch_csv.flush();
		fetch_csv.close();
		
		visit_csv.flush();
		visit_csv.close();
		
		urls_csv.flush();
		urls_csv.close();
	}
	
	
	//write the fetch.csv
	public void WriteFetch(String url, int statusCode) throws IOException{
		String toWrite = url + ", " + statusCode + "\n";
		fetch_csv.write(toWrite);
	}
	
	//write the visit.csv
	public void WriteVisit(String url, int size, int outlinks, String contentType) throws IOException{
		String toWrite = url + ", " + size + ", " + outlinks + ", " + contentType + "\n";
		visit_csv.write(toWrite);
	}
	
	
	//write the urls.csv
	public void WriteURLs(String url, String decision) throws IOException{
		String toWrite = url + ", " + decision + "\n";
		urls_csv.write(toWrite);
	}
	
	
	
	
}
