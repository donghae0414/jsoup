package org.jsoup.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CssParser {
	
	private static Document document;
	private static HashMap<String, String> styles = new HashMap<>();
	
	static final String[] inheritedStyles = { "border-collapse","border-spacing","caption-side","color","cursor"
			,"direction","empty-cells","font-family","font-size","font-style","font-variant","font-weight","font-size-adjust"
			,"font-stretch","font","letter-spacing","line-height","list-style-image","list-style-position","list-style-type"
			,"list-style","orphans","quotes","tab-size","text-align","text-align-last","text-decoration-color","text-indent"
			,"text-justify","text-shadow","text-transform","visibility","white-space","widows","word-break","word-wrap","word-spacing" };
	
	public CssParser() {
	}

	public static HashMap<String, String> getStyles() {
		return styles;
	}

	public static String getSelectedStyle(String property) {
		return styles.get(property);
	}
	
	public static HashMap<String, String> parse(Document doc) {
		if (document != null && document.equals(doc))
			return styles;
		
		document = doc.clone();
		styles.clear();
		
		// get css styles from stylesheet link
		Elements cssLinks = doc.getElementsByTag("link");
		for (Element cssLink : cssLinks) {
			if (cssLink.attr("rel").equals("stylesheet")) {
				String cssFilePath = cssLink.attr("href");
				String cssUrl;
				if (cssFilePath.startsWith("http"))
					cssUrl = cssFilePath;
				else
					cssUrl = "https://" + doc.baseUri() + cssFilePath;
				try {
					Document cssDoc = Jsoup.connect(cssUrl).get();
					String cssBody = cssDoc.body().text();
					
					addStyles(cssBody);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// get inline styles from document
		Elements allElements = doc.getAllElements();
		for (Element element : allElements) {
			if (element.hasAttr("style")) {
				styles.put(element.cssSelector(), element.attr("style"));
			}
		}
		
		return styles;
	}

	private static void addStyles(String cssBody) {
		StringTokenizer styleTokenizer = new StringTokenizer(cssBody, "}");
		while (styleTokenizer.hasMoreTokens()) {
			String token = styleTokenizer.nextToken();
			StringTokenizer nameStyleTokenizer = new StringTokenizer(token, "{");
			if (nameStyleTokenizer.countTokens() != 2)
				continue;
			
			String property = nameStyleTokenizer.nextToken();
			if (property.contains(";")) {
				int idx = property.indexOf(';');
				property = property.substring(idx + 1);
			}
			String style = nameStyleTokenizer.nextToken();
			
			styles.put(property, style);
		}
	}
	
	
}
