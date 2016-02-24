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
	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
			+ "|png|mp3|mp3|zip|gz))$");
	
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
	
   
	
	
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
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
		
		return !FILTERS.matcher(href).matches() && href.startsWith("http://gould.usc.edu/");
	}
	
	
	/**
	* This function is called when a page is fetched and ready * to be processed by your program.
	*/
	@Override
    public void visit(Page page) {
		String url = page.getWebURL().getURL(); System.out.println("URL: " + url);
		
		//Get the http code
		WebURL curURL = new WebURL();
		curURL.setURL(url);
		
		PageFetchResult fetchResult = new PageFetchResult();
		
		CrawlConfig config = new CrawlConfig();
		PageFetcher pageFetcher = new PageFetcher(config);
		
		int statusCode = 0;
		try{
			fetchResult = pageFetcher.fetchPage(curURL);
			statusCode = fetchResult.getStatusCode();
		}catch(Exception e){
			logger.error("Error occurred while fetching url: " + curURL.getURL(), e);
		}finally{
			if (fetchResult != null){
		        fetchResult.discardContentIfNotConsumed();
		    }
		}
		
		
		//Write URL visited and HTTP code Received in to fetch.csv
		try{
			//System.out.println("I am writing fetch.csv");
			writer.WriteFetch(url, statusCode);
		}catch(Exception e){
			logger.error("Error occured while writing fetch.csv" + url + statusCode, e);
		}
		
		
		
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData(); 
			String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			
			Set<WebURL> links = htmlParseData.getOutgoingUrls();
			System.out.println("Text length: " + text.length()); 
			System.out.println("Html length: " + html.length()); 
			System.out.println("Number of outgoing links: " + links.size());
			
			//For the URLs attempts to fetch
			// URL, HTTP Code Received
			
			//URL has been fetched.
			
			//Get the HTTP Code
			
			
			//For the files if successfully downloads
			//URL successfully download, size of file, the num of outlinks, resulting content type
			
			
			
			
			
			//Download and store the data
			// We are only interested in processing images which are bigger than 10k
		    if (FILTERS.matcher(url).matches()){// ||
		    		//!(page.getParseData() instanceof BinaryParseData ||
		    				//page.getContentData().length < 10 * 1024)) {
		      return;
		    }

		    // get a unique name for storing this image
		    //String extension = url.substring(url.lastIndexOf("."));
		    //String hashedName = UUID.randomUUID().toString() + extension;
		    
		    
		    String hashedName = UUID.randomUUID().toString();
		    // store image
		    String filename = storageFolder.getAbsolutePath() + "/" + hashedName;
		    try {
		      Files.write(page.getContentData(), new File(filename));
		      logger.info("Stored: {}", url);
		    } catch (IOException iox) {
		      logger.error("Failed to write file: " + filename, iox);
		    }
		}
	}
}
