package fw;

import nu.xom.Builder;
import nu.xom.Document;

import java.io.File;

public class Main {

    public static void main(String args[]) throws Exception {
        if (args.length!=1) {
            System.err.println("missing template file");
            return;
        }
        Document d = parseTemplate(args[0]);

        String url = "jdbc:mysql://localhost/"+"finland".replaceAll("land","fisher");
        DB db =new DB("com.mysql.jdbc.Driver",
                url,
                "ff",
                "ff");
        XmlFormatTemplate x = new XmlFormatTemplate(d);
        x.init();
        XmlGenerator xmlgen = new XmlGenerator(x,db);
        xmlgen.output();
    }

    public static Document parseTemplate(String s) throws Exception {
        Builder parser = new Builder();
        Document d = parser.build(new File(s));
        return d;
    }

}
