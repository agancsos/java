package com.jmagent.models;
import java.util.ArrayList;
import org.w3c.dom.*;
import com.common.XmlHelpers;

public class IndeedEntry extends IEntry {
    

    public IndeedEntry() {
        this.company = "";
        this.location = "";
        this.title = "";
        this.details = "";
        this.code = "";
        this.endpoint = "";
    }

    public static ArrayList<IEntry> extractEntries(NodeList nodes) {
        ArrayList<IEntry> result = new ArrayList<IEntry>();
        for (int i = 0; i < nodes.getLength(); i++) {
            result.add(extract(nodes.item(i), i));
        }
        return result;
    }

    private static IndeedEntry extract(Node raw, int i) {
        IndeedEntry entry = new IndeedEntry();
        try {
            Document doc = raw.getOwnerDocument();
            entry.title = XmlHelpers.selectNodes(doc, "//h2/span[@title]").item(i).getTextContent().replace("\n", "");
            entry.details = ((Element)XmlHelpers.selectNodes(doc, "//div[@class='job-snippet']").item(i)).getTextContent();
            if (!XmlHelpers.selectNodes(doc, "//*[@class='companyName']").item(i).getTextContent().equals("")) {
                entry.company = XmlHelpers.selectNodes(doc, "//*[@class='companyName']").item(i).getTextContent().replace("\n", "");
            }
            else {
                entry.company = XmlHelpers.selectNodes(doc, "//*[@data-tn-element='companyName']").item(i).getTextContent().replace("\n", "");
            }
            entry.location = XmlHelpers.selectNodes(doc, "//div[@class='companyLocation']").getLength() == 0 ? "Remote" :
                XmlHelpers.selectNodes(doc, "//div[@class='companyLocation']").item(i).getTextContent().replace("\n", "");
            entry.code = ((Element)XmlHelpers.selectNodes(doc, "//a[@data-jk]").item(i)).getAttribute("data-jk");
            entry.endpoint = String.format("https://www.indeed.com/viewjob?jk=%s", entry.code);
        }
        catch (Exception ex) { 
            //ex.printStackTrace();
        }
        return entry;
    }

    public String toString() {
        return String.format("%s; %s; %s; %s", this.company, this.title, this.location, this.code);
    }
}