package org.jsoup.style;

import java.util.Map;
import java.util.Set;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
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

        Document styled = Document.createShell(nakedDocument.baseUri());
        copyStyledNodes(nakedDocument.head().parent(), styled.head().parent());

        return styled;
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
                Element elem = (Element) source;
                Set<String> keys = rules.keySet();
                for (String key: keys) {
                    if(elem.is(key)) {
                        elem.applyStyle(rules.get(key));
                    }
                }

                if (destination != root && elem.hasParent()) {
                    destination.appendChild(elem);
                    destination = elem;
                }
            } else {
                destination.appendChild(source);
            }
        }

        public void tail(Node source, int depth) {
            if (source instanceof Element) {
                destination = destination.parent(); // would have descended, so pop destination stack
            }
        }
    }

    private void copyStyledNodes(Element source, Element dest) {
        StylingVisitor StylingVisitor = new StylingVisitor(source, dest);
        NodeTraversor.traverse(StylingVisitor, source);
    }
}