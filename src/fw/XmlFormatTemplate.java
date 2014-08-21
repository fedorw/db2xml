package fw;

import nu.xom.*;

public class XmlFormatTemplate {
    Document doc = null;
    XmlNode xmldoc = null;

    public void printTree() {
        XmlNode n = xmldoc;
        System.out.println(n);
    }

    public XmlNode getRoot() {
        return xmldoc;
    }

    public XmlFormatTemplate(Document doc) {
        this.doc = doc;
    }

    public void init() {
        Element e = doc.getRootElement();
        String name = e.getLocalName();

        XmlNode root = new XmlNode();
        root.setName(name);

        xmldoc = root;

        Elements childs = e.getChildElements();
        int count = childs.size();
        for (int i = 0; i < count; i++) {
            Element elem = childs.get(i);
            XmlNode n = parseElement(elem);
            root.addChild(n);
        }
    }

    public XmlNode parseElement(Element e) {
        XmlNode node = new XmlNode();
        String name = e.getLocalName();
        node.setName(name);

        // push variable to childnodes
        Attribute att = e.getAttribute("push");
        if (att != null) {
            node.setPushVars(toArray(att.getValue()));
        }
        att = e.getAttribute("mute");
        if (att != null) {
            if ("true".equals(att.getValue()) || "1".equals(att.getValue())) {
                node.setMuteResultSet(true);
            }
        }
        if (e.getChildCount() > 0) {
            // fetch sql-query
            Node n = e.getChild(0);
            if (n instanceof Text) {
                String sql = n.getValue();
                sql = sql.replaceAll("\n", "");
                sql = sql.trim();
                node.setSql(sql);
            }
        }
        // parse sub-nodes recursively
        Elements childs = e.getChildElements();
        int count = childs.size();
        for (int i = 0; i < count; i++) {
            Element elem = childs.get(i);
            XmlNode n = parseElement(elem);
            node.addChild(n);
        }
        return node;
    }

    public String[] toArray(String s) {
        if (s == null) {
            return new String[]{};
        }
        String ss[] = s.split(",");
        for (int i = 0; i < ss.length; i++) {
            ss[i] = ss[i].trim();
        }
        return ss;
    }

}

