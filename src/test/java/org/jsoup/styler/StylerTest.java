package org.jsoup.styler;

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

 public class StylerTest {
    @Test public void simpleBehaviourTest() {
        String selector = "div,button,input,select,table,textarea";
        String attribute = "font-size:12px;font-family:Dotum,'돋움',Helvetica,\"Apple SD Gothic Neo\",sans-serif";
        Map<String, String> rules = new HashMap();
        rules.put(selector, attribute);

        List<String> expected = new ArrayList<>();
        expected.add("font-size:12px;font-family:Dotum,Helvetica,\"Apple SD Gothic Neo\",sans-serif");

        String h = "<div style='font-size:12px;font-family:Dotum,Helvetica,\"Apple SD Gothic Neo\",sans-serif'><p class=foo><a href='http://evil.com'>Hello <button id=bar>there</button>!</a></p></div>";
        Document nakedHtml = Jsoup.parse(h);
        Document styledHtml = Jsoup.applyExternalStyle(nakedHtml);
        assertEquals(1, styledHtml.getElementsByTag("div").first().getAppliedStyle().size());
        assertEquals(expected.toString(), styledHtml.getElementsByTag("div").first().getAppliedStyle().toString());
    }
}
