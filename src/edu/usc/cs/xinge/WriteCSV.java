package edu.usc.cs.xinge;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Set;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.Statement;

import edu.uci.ics.crawler4j.url.WebURL;

public class WriteCSV {
	private FileWriter fetch_csv;
	private FileWriter visit_csv;
	private FileWriter urls_csv;
	private FileWriter stat_csv;
	private FileWriter pic_csv;
	private FileWriter pagerank_csv;
	private Connection conn;
	
	public WriteCSV(String fetch, String visit, String urls, String stat, String pic, String pagerank) throws IOException{
		fetch_csv = new FileWriter(fetch, false);
		visit_csv = new FileWriter(visit, false);
		urls_csv = new FileWriter(urls, false);
		stat_csv = new FileWriter(stat, false);
		pic_csv = new FileWriter(pic, false);
		pagerank_csv = new FileWriter(pagerank, false);
		
		//Connect the database, and initialize variables
		Connection con = null;
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = (Connection) DriverManager.getConnection("jdbc:mysql://127.0.0.1/572", "root", "123");
			if(!con.isClosed())	System.out.println("Successfully connected to MySQL sever using TCP/IP");
			else System.out.println("Failed connected to MySQL server using TCP/IP");
				
		}catch(Exception e){
			System.err.println("Exception:" + e.getMessage());
		}
	}
	
	public void Finish() throws IOException, SQLException{
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
		
		pagerank_csv.flush();
		pagerank_csv.close();
		
		//Close the database connection.
		try{
			if(conn != null);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			conn.close();
		}
	}
	
	
	//write the fetch.csv
	public void WriteFetch(String url, int statusCode) throws IOException{
		String toWrite = url + ", " + statusCode + "\n";
		fetch_csv.write(toWrite);
		//fetch_csv.flush();
	}
	
	//write the visit.csv
	public void WriteVisit(String url, String fileSize, int outlinks, String contentType) throws IOException{
		String toWrite = url + ", " + fileSize + ", " + outlinks + ", " + contentType + "\n";
		visit_csv.write(toWrite);
		//visit_csv.flush();
	}
	
	
	//write the urls.csv
	public void WriteURLs(String url, String decision) throws IOException{
		String toWrite = url + ", " + decision + "\n";
		urls_csv.write(toWrite);
		//urls_csv.flush();
	}
	
	//write the stat.csv
	public void WriteStat(String url, int code) throws IOException{
		
		String toWrite = url + ", " + code + "\n";
		stat_csv.write(toWrite);
		//stat_csv.flush();
	}
		
	public void WritePic(String url, String type) throws IOException{
		String toWrite = url + ", " + type + "\n";
		pic_csv.write(toWrite);
		//pic_csv.flush();
	}
	
	//write the pagerankdata.csv
	public void WriteGraph(String url, Set<WebURL> links) throws IOException{
		String toWrite = url;
		Iterator<WebURL> it = links.iterator();
		while(it.hasNext()){
			toWrite = toWrite + ", " + it.next().getURL().toLowerCase();
		}
		
		//a line is finished, create a enter
		toWrite += "\n";
		pagerank_csv.write(toWrite);
		//pagerank_csv.flush();
	}
	
	//create a new item into the map <url, filename>
	public void WriteMap(String url, String filename){
		try{
			String insql = "INSERT INTO `572`.`map` (url, filename) VALUES (?, ?);";
			PreparedStatement ps = (PreparedStatement) conn.prepareStatement(insql);
			ps.setString(1, url);
			ps.setString(2, filename);
			
			ps.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

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
	
	public FileWriter getStat_csv() {
		return stat_csv;
	}

	public void setStat_csv(FileWriter stat_csv) {
		this.stat_csv = stat_csv;
	}

	public FileWriter getPic_csv() {
		return pic_csv;
	}

	public void setPic_csv(FileWriter pic_csv) {
		this.pic_csv = pic_csv;
	}

	public FileWriter getPagerank_csv() {
		return pagerank_csv;
	}

	public void setPagerank_csv(FileWriter pagerank_csv) {
		this.pagerank_csv = pagerank_csv;
	}
}
