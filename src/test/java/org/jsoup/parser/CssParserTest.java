package org.jsoup.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/**
 Tests for the styler.

 @author Junyoung Park, carpe0308@naver.com */

 public class CssParserTest {
    @Test public void simpleBehaviourTest() {
        Document doc = Jsoup.parseBodyFragment("<style>div,button,input,select,table,textarea { font-size:12px;font-family:Dotum,Helvetica,\"Apple SD Gothic Neo\",sans-serif }</style>");
        Map<String, String> parsedRules = CssParser.parse(doc);

        String selector = "div,button,input,select,table,textarea";
        String attribute = "font-size:12px;font-family:Dotum,Helvetica,\"Apple SD Gothic Neo\",sans-serif";
        Map<String, String> expected = new HashMap();
        expected.put(selector, attribute);
        assertEquals(expected, parsedRules);
        assertEquals(expected.values().toString(), parsedRules.values().toString());
    }

    @Test public void simpleInlineTest() {
        Document doc = Jsoup.parseBodyFragment("<div style='font-size:12px;font-family:Dotum,Helvetica,\"Apple SD Gothic Neo\",sans-serif'></div>");
        Map<String, String> parsedRules = CssParser.parse(doc);

        String selector = "html > body > div";
        String attribute = "font-size:12px;font-family:Dotum,Helvetica,\"Apple SD Gothic Neo\",sans-serif";
        Map<String, String> expected = new HashMap();
        expected.put(selector, attribute);
        assertEquals(expected, parsedRules);
        assertEquals(expected.values().toString(), parsedRules.values().toString());
    }
}