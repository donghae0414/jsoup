package org.jsoup.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CssParser {
	
	private static Document cloneDocument;
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
	
	public static HashMap<String, String> parse(Document target) {
		if (cloneDocument != null && cloneDocument.equals(target))
			return styles;
		
		// document = doc.clone();
		cloneDocument = target.clone();
		styles.clear();
		
		// get css styles from stylesheet link
		Elements cssLinks = target.getElementsByTag("link");
		for (Element cssLink : cssLinks) {
			if (cssLink.attr("rel").equals("stylesheet")) {
				String cssFilePath = cssLink.attr("href");
				String cssUrl;
				if (cssFilePath.startsWith("http"))
					cssUrl = cssFilePath;
				else
					cssUrl = "https://" + target.baseUri() + cssFilePath;
				try {
					Document cssDoc = Jsoup.connect(cssUrl).get();
					String cssBody = cssDoc.body().text();
					
					addStyles(cssBody);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		// get styles from tags
		Elements styleTags = target.getElementsByTag("style");
		for (Element styleTag : styleTags) {
			addStyles(styleTag.data());
		}

		// get inline styles from document
		Elements allElements = target.getAllElements();
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
			StringTokenizer propertyTokenizer = new StringTokenizer(token, "{");
			if (propertyTokenizer.countTokens() != 2)
				continue;
			
			String property = propertyTokenizer.nextToken();
			if (property.contains(";")) {
				int idx = property.indexOf(';');
				property = property.substring(idx + 1);
			}

			String style = propertyTokenizer.nextToken();
			styles.put(property.trim(), style.trim());
		}
	}	
}
