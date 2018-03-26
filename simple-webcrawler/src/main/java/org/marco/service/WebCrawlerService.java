package org.marco.service;

import java.io.IOException;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerService {
	private static final int MAX_DEPTH = 2;
	private HashSet<String> links = new HashSet<>();
	
	public void navigatePageByPage(String URL) {
		navigatePageByPage(URL, 0);
	}
	
	private void navigatePageByPage(String URL, Integer depth) {
		try {
			navigateInDepth(URL, depth);
		} catch (IOException e) {
			System.err.println("For '" + URL + "': " + e.getMessage());
		}
	}

	private void navigateInDepth(String URL, Integer depth) throws IOException {
		if (!isUrlNotProcessedAndInDepth(URL, depth)) return;
		System.out.println(">> Depth: " + depth + " [" + URL + "]");
		links.add(URL);
		
		Elements linksOnPage = Jsoup.connect(URL)
				.get()
				.select("a[href]");

		linksOnPage.stream()
			.forEach(page -> navigatePageByPage(page.attr("abs:href"), depth+1));
	}

	private boolean isUrlNotProcessedAndInDepth(String URL, Integer depth) {
		return !links.contains(URL) && (depth < MAX_DEPTH);
	}
}