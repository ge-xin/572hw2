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
	private FileWriter fetch_csv;
	private FileWriter visit_csv;
	private FileWriter urls_csv;
	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String crawlStorageFolder = "data/crawl/"; int numberOfCrawlers = 7;
		CrawlConfig config = new CrawlConfig(); 
		config.setCrawlStorageFolder(crawlStorageFolder);
		config.setMaxDepthOfCrawling(5);
		config.setPolitenessDelay(1000);
		
		//Set the pages fetching limit.
		config.setMaxPagesToFetch(50);
		config.setUserAgentString("USC_CS_Crawler");
		
		/*
         * Instantiate the controller for this crawl.
         */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher); 
		CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
		/*
		* For each crawl, you need to add some seed urls. These are the first
		* URLs that are fetched and then the crawler starts following links * which are found in these pages
		*/
		controller.addSeed("http://gould.usc.edu/");
		/*
		* Start the crawl. This is a blocking operation, meaning that your code
		* 
		* will reach the line after this only when crawling is finished.
		*/
		//Crawler crawler = new Crawler();
		
		
		WriteCSV writer = new WriteCSV("fetch.csv", "visit.csv", "urls.csv");
		Crawler.configure(crawlStorageFolder, writer);
		
		controller.start(Crawler.class, numberOfCrawlers);
		
		writer.Finish();
		
		controller.waitUntilFinish();
	    System.out.println("Crawler is finished.");
	}
	
}
