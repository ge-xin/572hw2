package edu.usc.cs.xinge;

import java.io.FileWriter;
import java.io.IOException;

public class WriteCSV {
	private FileWriter fetch_csv;
	
	public FileWriter getFetch_csv() {
		return fetch_csv;
	}

	public void setFetch_csv(FileWriter fetch_csv) {
		this.fetch_csv = fetch_csv;
	}

	public FileWriter getVisit_csv() {
		return visit_csv;
	}

	public void setVisit_csv(FileWriter visit_csv) {
		this.visit_csv = visit_csv;
	}

	public FileWriter getUrls_csv() {
		return urls_csv;
	}

	public void setUrls_csv(FileWriter urls_csv) {
		this.urls_csv = urls_csv;
	}


	private FileWriter visit_csv;
	private FileWriter urls_csv;
	private FileWriter stat_csv;
	private FileWriter pic_csv;
	
	public WriteCSV(String fetch, String visit, String urls, String stat, String pic) throws IOException{
		fetch_csv = new FileWriter(fetch, false);
		visit_csv = new FileWriter(visit, false);
		urls_csv = new FileWriter(urls, false);
		stat_csv = new FileWriter(stat, false);
		pic_csv = new FileWriter(pic, false);
	}
	
	public void Finish() throws IOException{
		fetch_csv.flush();
		fetch_csv.close();
		
		visit_csv.flush();
		visit_csv.close();
		
		urls_csv.flush();
		urls_csv.close();
		
		stat_csv.flush();
		stat_csv.close();
		
		pic_csv.flush();
		pic_csv.close();
	}
	
	
	//write the fetch.csv
	public void WriteFetch(String url, int statusCode) throws IOException{
		String toWrite = url + ", " + statusCode + "\n";
		fetch_csv.write(toWrite);
		fetch_csv.flush();
	}
	
	//write the visit.csv
	public void WriteVisit(String url, float fileSize, int outlinks, String contentType) throws IOException{
		String toWrite = url + ", " + fileSize + ", " + outlinks + ", " + contentType + "\n";
		visit_csv.write(toWrite);
		visit_csv.flush();
	}
	
	
	//write the urls.csv
	public void WriteURLs(String url, String decision) throws IOException{
		String toWrite = url + ", " + decision + "\n";
		urls_csv.write(toWrite);
		urls_csv.flush();
	}
	
	//write the stat.csv
	public void WriteStat(String url, int code) throws IOException{
		
		String toWrite = url + ", " + code + "\n";
		stat_csv.write(toWrite);
		stat_csv.flush();
	}
		
	public void WritePic(String url, String type) throws IOException{
		String toWrite = url + ", " + type + "\n";
		pic_csv.write(toWrite);
		pic_csv.flush();
	}
	
	
}
