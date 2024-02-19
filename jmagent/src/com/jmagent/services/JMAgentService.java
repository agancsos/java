package com.jmagent.services;
import java.util.HashMap;
import java.util.Map;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.util.ArrayList;
import com.common.WebHelpers;
import com.common.XmlHelpers;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import com.jmagent.models.*;


public class JMAgentService implements Runnable {
    private String userDataPath;
    private DataService ds;
    private static JMAgentService instance;
    private ConfigurationService cs;
    private boolean testMode;
    private String limit;
    private HashMap<String, String> supportedEndpoints;
    private ArrayList<Thread> threads;
    private ArrayList<IEntry> cache;
    private String intervalSeconds;

    private JMAgentService() {
        this.ds = DataService.getInstance();
        this.cs = ConfigurationService.getInstance(null);
        this.limit = "50";
        this.supportedEndpoints = new HashMap<String, String> () {{
            put("Indeed", String.format("https://www.indeed.com/jobs?start=0&limit=%s&", limit));
        }};
        this.threads = new ArrayList<Thread>();
        this.cache = new ArrayList<IEntry>();
        this.intervalSeconds = "30";
    }

    public static JMAgentService getInstance() {
        if (instance == null) {
            instance = new JMAgentService();
        }
        return instance;
    }

    public void setUserDataPath(String path) {
        this.userDataPath = path;
        this.ds.setUserDataPath(path);
        this.cs = ConfigurationService.getInstance(String.format("%s/config.plist", this.userDataPath));
        this.testMode = this.cs.getProperty("testing").equals("1") ? true : false;
    }

    private void poll() {
        JMAgentService child = new JMAgentService();
        Thread t = new Thread(child);
        this.threads.add(t);
        t.start();    
    }

    public void run() {
        Profile profile = ProfileService.getInstance().getDefaultProfile();
        CompanyService companyService = CompanyService.getInstance();
        ResultsService resultService = ResultsService.getInstance();    
        for (Map.Entry<String, String> endpoint : this.supportedEndpoints.entrySet()) {
                if (endpoint.getKey().equals("Indeed")) {
                    String uri = String.format("%sq=%s&l=%s&explvl=%s", endpoint.getValue(), 
                    profile.getFilter().getKeywords().replace(" ", "%20").replace(";", ","), 
                    profile.getFilter().getLocation(), 
                    profile.getFilter().getLevel());
                try {
                    String raw = WebHelpers.invokeGet(uri, new HashMap<String, String>());
                    if (raw.startsWith("\uFEFF")) {
                        raw = raw.substring(1);
                    }
                    Document document = XmlHelpers.htmlToXml(XmlHelpers.tidyup(raw));
                    document.normalizeDocument();
                    NodeList divs = XmlHelpers.selectNodes(document, "//div[@class='job_seen_beacon']");
                    IndeedEntry.extractEntries(divs).forEach(e -> {
                        cache.add(e);
                    });
                }
                catch (Exception ex1) { 
                    //System.out.println(ex1.getMessage());
                }
            }
        }
        this.cache.forEach(e -> {
            System.out.println(e.toString());
            Company c = new Company();
            c.setName(e.getCompany());
            Result r = new Result();
            r.setProfile(profile);
            r.setTitle(e.getTitle());
            c.setID(companyService.add(c));
            r.setCompany(c);
            r.setCode(e.getCode());
            r.setSourceEndpoint(e.getEndpoint());
            resultService.add(r);
        });
    }

    public void startAgent() {
        while (true) {
            this.poll();
            try {
                Thread.sleep(Integer.parseInt(this.intervalSeconds) * 1000);
            }
            catch (Exception ex) { }
        }
    }

    public void setLimit(String limit) { this.limit = limit; }
    public String getLimit() { return this.limit; }
    public void setIntervalSeconds(String interval) { this.intervalSeconds = interval; }
    public String getIntervalSeconds() { return this.intervalSeconds; }
}