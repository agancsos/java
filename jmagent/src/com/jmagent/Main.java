package com.jmagent;
import com.jmagent.services.JMAgentService;

public class Main {
    public static void main(String[] args) {
        boolean isHelp        = false;
        JMAgentService agent  = JMAgentService.getInstance();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-h") || args[i].equals("--help")) { 
                isHelp = true; 
            }
            else if (args[i].equals("-p") || args[i].equals("--path")) { 
                agent.setUserDataPath(args[i + 1]); 
            }
            else if (args[i].equals("-l") || args[i].equals("--limit")) { 
                agent.setLimit(args[i + 1]); 
            }
            else if (args[i].equals("-i") || args[i].equals("--interval")) { 
                agent.setIntervalSeconds(args[i + 1]); 
            }
        }

        if (isHelp) {
            Helpers.helpMenu();
        }
        else {
            agent.startAgent();
        }
    }
}