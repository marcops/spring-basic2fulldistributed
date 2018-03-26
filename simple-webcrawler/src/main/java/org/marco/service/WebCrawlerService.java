package org.marco.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
public class WebCrawlerService {
	private HashSet<String> links = new HashSet<>();
	
	public void navigatePageByPage(String URL) throws MalformedURLException {
		navigatePageByPage(URL, new URL(URL).getHost());
	}
	
	private void navigatePageByPage(String URL, String domain) {
		try {
			navigateInDepth(URL, domain);
		} catch (IOException e) {
			System.err.println("For '" + URL + "': " + e.getMessage());
		}
	}

	private void navigateInDepth(String URL, String domain) throws IOException {
		if (links.contains(URL)) return;
		System.out.println("[" + URL + "]");

		if(!URL.contains(domain)) return;
		links.add(URL);
		
		Elements linksOnPage = Jsoup.connect(URL)
				.get()
				.select("a[href]");

		linksOnPage.stream()
			.forEach(page -> navigatePageByPage(page.attr("abs:href"), domain));
	}

}