package fw;

import org.apache.commons.lang.StringEscapeUtils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

public class XmlGenerator {
	XmlFormatTemplate format = null;
	DB db = null;

	public XmlGenerator(XmlFormatTemplate format, DB db) {
		this.db = db;
		this.format = format;
	}

	public void output() throws Exception {
		XmlNode node = format.getRoot();
		print("<" + node.getName() + ">\r\n");
		List<XmlNode> children = node.getChildren();
		for (XmlNode n : children) {
			print(1, n, null);
		}
		print("</" + node.getName() + ">\r\n");
	}

	private void print(String s) {
		System.out.print(s);
	}

	public void print(int indent, XmlNode node, Object args[]) throws Exception {
		String sql = node.getSql();
		if (sql == null) {
			toXml(indent, node, null, null);
		}
		PreparedStatement stmt = db.getStatement(sql);
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				stmt.setObject(i + 1, args[i]);
			}
		}

		ResultSet rs = stmt.executeQuery();
		while (rs.next()) {
			toXml(indent, node, rs, null);
		}
	}

	private void toXml(int indent, XmlNode node, ResultSet rs, Object args[]) throws Exception {
		String elementName = node.getName();
		String spc = "";
		for (int j = 0; j < indent; j++) {
			spc += " ";
		}
		ResultSetMetaData metaData = rs.getMetaData();
		int count = metaData.getColumnCount();
		print(spc);
		print("<" + elementName + ">\r\n");

		String k, k_full;
		Object v;
		if (!node.getMuteResultSet()) {
			for (int i = 0; i < count; i++) {
				k_full = metaData.getColumnName(1 + i);
				k = abbr(elementName, k_full);
				v = rs.getObject(k_full);
				if (v == null) {
					print(" " + spc + "<" + k + "/>\r\n");
				} else {
					String val=StringEscapeUtils.escapeXml(""+v);
					print(" " + spc + "<" + k + ">" + val + "</" + k + ">\r\n");
				}
			}
		}

		List<XmlNode> children = node.getChildren();
		for (XmlNode child : children) {
			List<Object> argObjects = new ArrayList<Object>();
			String toPush[] = child.getPushVars();
			for (int i = 0; i < toPush.length; i++) {
				argObjects.add(rs.getObject(toPush[i]));
			}
			print(indent + 1, child, argObjects.toArray(new Object[]{}));
		}
		print(spc + "</" + elementName + ">\r\n");
	}

	private String abbr(String table, String field) {
		table = table.replaceAll("-", "_");
		if (field.startsWith(table + "_") && field.length() > table.length() + 2) {
			return field.substring(table.length() + 1);
		}
		return field;
	}
}
