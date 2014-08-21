package fw;

import java.util.*;

public class XmlNode {
    private String name;
    private String sql;
    private List<XmlNode> children = new ArrayList<XmlNode>();
    private String pushVars[] = new String[]{};
    private boolean muteResultSet = false;

    public XmlNode() {
    }

    public void setMuteResultSet() {
        setMuteResultSet(true);
    }

    public void setMuteResultSet(boolean v) {
        muteResultSet = v;
    }

    public boolean getMuteResultSet() {
        return muteResultSet;
    }


    public void setName(String name)

    {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<XmlNode> getChildren() {
        return children;
    }

    public void addChild(XmlNode n) {
        children.add(n);
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getSql() {
        return sql;
    }

    public void setPushVars(String pushVars[]) {
        this.pushVars = pushVars;
    }

    public String[] getPushVars() {
        return pushVars;
    }

    public String toString() {
        return toString(1);
    }

    public String toString(int indent) {
        String s = name + " = " + sql;
        for (int i = 0; i < children.size(); i++) {
            XmlNode c = children.get(i);
            s += "\n";
            for (int j = 0; j < indent; j++) {
                s += "   ";
            }
            s += " -> " + c.toString(indent + 1);
        }
        return s;
    }

}

