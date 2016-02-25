package edu.usc.cs.xinge;

import java.io.FileWriter;
import java.util.List;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.usc.cs.xinge.CrawlStat;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String crawlStorageFolder = "data/crawl/"; int numberOfCrawlers = 500;
		CrawlConfig config = new CrawlConfig(); 
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(5);
		config.setPolitenessDelay(50);
		
		
		//Set the pages fetching limit.
		config.setMaxPagesToFetch(5300);
		config.setUserAgentString("USC_CS_Crawler");
		
		/*
	     * Since images are binary content, we need to set this parameter to
	     * true to make sure they are included in the crawl.
	     */
	    config.setIncludeBinaryContentInCrawling(true);
	    
	    
		/*
         * Instantiate the controller for this crawl.
         */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		robotstxtConfig.setEnabled(true);
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher); 
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		//CrawlController controller = new CrawlController(config, pageFetcher, null);
		/*
		* For each crawl, you need to add some seed urls. These are the first
		* URLs that are fetched and then the crawler starts following links * which are found in these pages
		*/
		controller.addSeed("http://gould.usc.edu/");
		//controller.addSeed("http://www-scf.usc.edu/~csci572/");
		
		
		/*
		* Start the crawl. This is a blocking operation, meaning that your code
		* 
		* will reach the line after this only when crawling is finished.
		*/
		//Crawler crawler = new Crawler();
		
		
		WriteCSV writer = new WriteCSV("fetch.csv", "visit.csv", "urls.csv", "stat.csv", "pic.csv");
		Crawler.configure(crawlStorageFolder, writer);
		
		controller.start(Crawler.class, numberOfCrawlers);
		
		writer.Finish();
		
		controller.waitUntilFinish();
	    System.out.println("Crawler is finished.");
	}
	
}
