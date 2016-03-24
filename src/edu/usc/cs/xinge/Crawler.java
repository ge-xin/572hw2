package edu.usc.cs.xinge;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

import com.google.common.io.Files;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class Crawler extends WebCrawler {
	/*
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
			+ "|png|mp3|mp3|zip|gz))$");
	*/
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js"
			+ "|mp3|mp3|zip|gz))$");
	private final static Pattern JPEG_FILTERS = Pattern.compile(".*(jpeg|jpg)");
	private final static Pattern GIF_FILTERS = Pattern.compile(".*(gif)");
	private final static Pattern PNG_FILTERS = Pattern.compile(".*(png)");
	
	//the regexp detect whether it belongs to law school
	private final static Pattern LAW_FILTER = Pattern.compile(".*gould.usc.edu.*");
	private final static Pattern LAW_FILTER2 = Pattern.compile(".*law.*usc.edu.*");
	private final static Pattern LAW_FILTER3 = Pattern.compile(".*onlinellm.usc.edu.*");
	
	//detect whether it belongs to usc
	private final static Pattern USC_FILTER = Pattern.compile(".*usc.edu.*");
	
	/**
	* This method receives two parameters. The first parameter is the page
	* in which we have discovered this new url and the second parameter is
	* the new url. You should implement this function to specify whether
	* the given url should be crawled or not (based on your crawling logic). 
	* In this example, we are instructing the crawler to ignore urls that
	* have css, js, git, ... extensions and to only accept urls that start 
	* with "http://www.viterbi.usc.edu/". In this case, we didn't need the 
	* referringPage parameter to make the decision.
	*/
	
	
	private static WriteCSV writer;
	
	public static WriteCSV getWriter() {
		return writer;
	}



	public static void setWriter(WriteCSV writer) {
		Crawler.writer = writer;
	}



	public static File getStorageFolder() {
		return storageFolder;
	}



	public static void setStorageFolder(File storageFolder) {
		Crawler.storageFolder = storageFolder;
	}
	private static File storageFolder;
	//private static String[] crawlDomains;
	
	
	
	//public static void configure(String[] domain, String storageFolderName) {
	public static void configure(String storageFolderName, WriteCSV writer) throws IOException {
	    //Crawler.crawlDomains = domain;
	    storageFolder = new File(storageFolderName);
	    if (!storageFolder.exists()) {
	      storageFolder.mkdirs();
	    }
	    
	    //set the write csv files' FileWriter
	    setWriter(writer);
	}
	
   
	
	protected void RecordURL(String href){
		if(LAW_FILTER.matcher(href).matches() || LAW_FILTER2.matcher(href).matches()
				|| LAW_FILTER3.matcher(href).matches()){
			 //it is a url that inside the law school website
			try{
				writer.WriteURLs(href, "OK");
		    }catch(Exception e){
				logger.error("Error occured while writing urls.csv " + href + " OK ", e);
			}
		}else if(USC_FILTER.matcher(href).matches()){
			try{
				writer.WriteURLs(href, "USC");
			}catch(Exception e){
				 logger.error("Error occured while writing urls.csv " + href + " USC ", e);
			}
				 
	    }else{
				 //it is not in law school, and it is not in USC
				 //it is out usc
			try{
				writer.WriteURLs(href, "outUSC");
			}catch(Exception e){
				logger.error("Error occured while writing urls.csv " + href + " outUSC ", e);
			}
				 
	    }
	}
	
	/**
	   * This function is called once the header of a page is fetched. It can be
	   * overridden by sub-classes to perform custom logic for different status
	   * codes. For example, 404 pages can be logged, etc.
	   *
	   * @param webUrl WebUrl containing the statusCode
	   * @param statusCode Html Status Code number
	   * @param statusDescription Html Status COde description
	   */
	@Override
	protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
	    // Do nothing by default
	    // Sub-classed can override this to add their custom functionality
		//System.out.println("handle page status");
		if(statusCode >= 200 && statusCode < 300){ //Success
			try {
				writer.WriteStat(webUrl.getURL(), statusCode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			//Write URL visited and HTTP code Received in to fetch.csv
			try{
				//System.out.println("I am writing fetch.csv");
				writer.WriteFetch(webUrl.getURL(), statusCode);
			}catch(Exception e){
				logger.error("Error occured while writing fetch.csv" + webUrl.getURL() + statusCode, e);
			}
			
		}else if(statusCode >= 300 && statusCode <400){ //Aborted
			try {
				writer.WriteStat(webUrl.getURL(), statusCode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
				
				
				//Write URL visited and HTTP code Received in to fetch.csv
				try{
					//System.out.println("I am writing fetch.csv");
					writer.WriteFetch(webUrl.getURL(), statusCode);
				}catch(Exception e1){
					logger.error("Error occured while writing fetch.csv" + webUrl.getURL() + statusCode, e1);
				}
			}
		}else{ //failed
			try {
				writer.WriteStat(webUrl.getURL(), statusCode);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		RecordURL(href);
		System.out.println(referringPage.getContentType());
		
		boolean startWith = (href.startsWith("http://gould.usc.edu/")
				|| href.startsWith("http://mylaw2.usc.edu/")
				||href.startsWith("http://weblaw.usc.edu/")
					||href.startsWith("http://lawlibguides.usc.edu/"));
		
		
		
		return (!FILTERS.matcher(href).matches() && startWith);
	}
	
	public boolean isDocxFile(Page page){
		String type = page.getContentType();
		return (type.indexOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document") != -1);
	}
	
	public boolean isDocFile(Page page){
		String type = page.getContentType();
		return (type.indexOf("application/msword") != -1);
	}
	
	public boolean isPDFFile(Page page){
		String type = page.getContentType();
		return (type.indexOf("application/msword") != -1);
	}
	
	public boolean isPicFile(Page page){
		String type = page.getContentType();
		boolean isJPG = (type.indexOf("image/jpeg") != -1);
		boolean isPNG = (type.indexOf("image/png") != -1);
		boolean isGIF = (type.indexOf("image/gif") != -1);
		return (isJPG || isPNG || isGIF);
	}
	
	/**
	* This function is called when a page is fetched and ready * to be processed by your program.
	*/
	@Override
    public void visit(Page page) {
		String url = page.getWebURL().getURL(); System.out.println("URL: " + url);
		
		
		//Download the HTML file
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData(); 
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			
			try {
				writer.WriteVisit(url, ((float)(html.length() / 1024)) + "KB", links.size(), "HTML");
				writer.WriteGraph(url, links);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    
		    String hashedName = UUID.randomUUID().toString();
		    // store image
		    String filename = storageFolder.getAbsolutePath() + "/" + hashedName + ".html";
		    try {
		      Files.write(page.getContentData(), new File(filename));
		      logger.info("Stored: {}", url);
		      
		      //Write the map
		      writer.WriteMap(url, hashedName);
		    } catch (IOException iox) {
		      logger.error("Failed to write file: " + filename, iox);
		    }
		    
		}
		
		//Download the Binary file
		if (page.getParseData() instanceof BinaryParseData) {
			float fileSize = page.getContentData().length / 1024;
			
			BinaryParseData binaryParseData = (BinaryParseData) page.getParseData(); 
			Set<WebURL> links = binaryParseData.getOutgoingUrls();
			
			
			if(isDocFile(page)){// this is a doc file
	    		//logger.log(null, "This binary file" + url + "is DOC");
	    		
	    		try {
					writer.WriteVisit(url,  fileSize + "KB", links.size(), "DOC");
					writer.WriteGraph(url, links);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		
	    		String hashedName = UUID.randomUUID().toString();
	 		    // store image
	 		    String filename = storageFolder.getAbsolutePath() + "/" + hashedName + ".doc";
	 		    try {
	 		      Files.write(page.getContentData(), new File(filename));
	 		      logger.info("Stored: {}", url);
	 		      
	 		      //Write the map
			      writer.WriteMap(url, hashedName);
	 		    } catch (IOException iox) {
	 		      logger.error("Failed to write file: " + filename, iox);
	 		    }
	 		    
	    		
	    	}else if(isDocxFile(page)){ //this is a docx file
	    		//System.out.println("Binary Begins");
	    		//logger.log(null, "This binary file" + url + "is DOCX");
	    		
	    		try {
					writer.WriteVisit(url, fileSize + "KB", links.size(), "DOCX");
					writer.WriteGraph(url, links);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		
	    		String hashedName = UUID.randomUUID().toString();
	 		    // store binary data, which is pdf, doc, docx
	 		    String filename = storageFolder.getAbsolutePath() + "/" + hashedName + ".docx";
	 		    try {
	 		      Files.write(page.getContentData(), new File(filename));
	 		      logger.info("Stored: {}", url);
	 		      
	 		      //Write the map
			      writer.WriteMap(url, hashedName);
	 		    } catch (IOException iox) {
	 		      logger.error("Failed to write file: " + filename, iox);
	 		    }
	    		
	    	}else if(isPDFFile(page)){ //This is a PDF file
	    		//logger.log(null, "This binary file" + url + "is PDF");
	    		
	    		try {
					writer.WriteVisit(url, fileSize + "KB", links.size(), "PDF");
					writer.WriteGraph(url, links);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    		String hashedName = UUID.randomUUID().toString();
	 		    // store image
	 		    String filename = storageFolder.getAbsolutePath() + "/" + hashedName + ".pdf";
	 		    try {
	 		      Files.write(page.getContentData(), new File(filename));
	 		      logger.info("Stored: {}", url);

	 		      //Write the map
			      writer.WriteMap(url, hashedName);
			      
	 		    } catch (IOException iox) {
	 		      logger.error("Failed to write file: " + filename, iox);
	 		    }
	 		}else if(isPicFile(page)){
	 			//according to the new definition, I should add it into visit.csv
	 			String type = page.getContentType();
	 			type = type.substring(type.lastIndexOf("/"));
	 			try {
					writer.WriteVisit(url, fileSize + "KB", links.size(), type);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	 			//write into pic.csv to account the num of each type
	 			try {
					writer.WritePic(url, page.getContentType());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	 		}
	    	else{
	    		logger.log(null, "This binary file" + url + "is not what we want");
	    		
	    	}
			
			
	    }
	}
}
