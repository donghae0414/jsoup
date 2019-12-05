package org.jsoup.style;

import java.util.Map;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;

public class Styler {
    private Map<String, String> rules;

    /**
     Create a new styler, that applies documents using the supplied css rules.
     @param rules rules to applied with
     */
    public Styler(Map<String, String> rules) {
        Validate.notNull(rules);
        this.rules = rules;
    }

    /**
     Creates a new, styling document, from the original naked document, containing elements those styles are applied by the css rules.
     The original document is not modified. Only elements from the naked document's <code>body</code> are used.
     @param dirtyDocument Base document to style.
     @return Styled document.
     */
    public Document applyStyle(Document nakedDocument) {
        Validate.notNull(nakedDocument);

        Document clean = Document.createShell(nakedDocument.baseUri());
        if (nakedDocument.body() != null) // frameset documents won't have a body. the clean doc will have empty body.
            copyStyledNodes(nakedDocument.body(), clean.body());

        return clean;
    }

    /**
     Iterates the input and copies styled tags (tags, attributes, text) into the destination.
     */
    private final class StylingVisitor implements NodeVisitor {
        private final Element root;
        private Element destination; // current element to append nodes to

        private StylingVisitor(Element root, Element destination) {
            this.root = root;
            this.destination = destination;
        }

        public void head(Node source, int depth) {
            if (source instanceof Element) {
                // TODO: implement
            } else { }
        }

        public void tail(Node source, int depth) {
            if (source instanceof Element) {
                // TODO: implement
            }
        }
    }

    private void copyStyledNodes(Element source, Element dest) {
        StylingVisitor StylingVisitor = new StylingVisitor(source, dest);
        NodeTraversor.traverse(StylingVisitor, source);
    }
}