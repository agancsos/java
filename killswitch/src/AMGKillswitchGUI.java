/*
	Name       : amgkillswitchgui.java
	Author     : Abel Gancsos
	(c)        : Abel Gancsos Productions
	v.         : v. 1.0.0
	Description:
*/

/*              IMPORTS                   */
import java.util.*;
import java.text.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;
/******************************************/

public class AMGKillswitchGUI extends JFrame implements Runnable{
	public KillswitchData session = new KillswitchData();
    JFrame window         = new JFrame("Killswitch");
    JPanel content        = new JPanel(new GridLayout(2,1));
    JPanel footer         = new JPanel(new GridLayout(1,2));
	JSplitPane splitter   = null;
	JScrollPane stages    = null;
	JScrollPane logsView  = null;
    public JList<String> stageList = null;
	public JList<String> logsList = null;
	private DefaultListModel logsModel = new DefaultListModel();

	public String target  = "stuff";
	public String dbPath  = "audit.db";
	public Boolean silent = false;

	public java.util.Timer ticker = new java.util.Timer();

    @Override
    public void run() {
		session.targetPath = target;
		session.dataFile   = dbPath;
		session = new KillswitchData(dbPath);
        
		//window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.addWindowListener(new WindowAdapter(){
			@Override
            public void windowClosing(WindowEvent e){
                exitApplication();
            }
        });
        window.addKeyListener(new KeyAdapter(){
			@Override
            public void keyPressed(KeyEvent e){
                keyDetector(e);
            }
        });
        /****************************************************************/

		/*                        Content View                             */	
    	stageList = new JList<String>(session.stages);
		stages = new JScrollPane(stageList);
		stageList.setSelectionModel(new DefaultListSelectionModel(){
			@Override
			public void setSelectionInterval(int index0,int index1){
				try{
					super.setSelectionInterval(-1,-1);
				}
				catch(Exception e){
				}
			}
		});
		stageList.setCellRenderer(new DefaultListCellRenderer() {
			@Override
            public Component getListCellRendererComponent(JList list, Object value, int index,boolean isSelected, boolean cellHasFocus) {
            	Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				setText((String)value);
				switch(session.getStageStatus((String)value)){
					case "Complete":
						setBackground(Color.green);
						break;
					case "Running":
						setBackground(Color.blue);
						break;
					default:
						setBackground(Color.gray);
						break;
				}
                return c;
        	}
		});
		//logsList = new JList<String>(session.getFiles());
		logsView = new JScrollPane(logsList);
		splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,stages, logsView);
		content.add(splitter);
		/*******************************************************************/

        /*                        Footer labels                            */
        String year = (new SimpleDateFormat("yyyy")).format(new Date());
        JLabel copyrightLabel = new JLabel("(c) " + year + " Abel Gancsos Productions");
        copyrightLabel.setForeground(Color.WHITE);
        footer.add(copyrightLabel);

        JLabel versionLabel = new JLabel("v. 1.0.0");
        versionLabel.setForeground(Color.WHITE);
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        footer.add(versionLabel);
        /********************************************************************/


        /*                    Put components together                 */
        window.getContentPane().add(content,0);
        window.getContentPane().add(footer,1);
        window.getContentPane().setLayout(new BoxLayout(window.getContentPane(), BoxLayout.Y_AXIS));
        window.getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        content.setPreferredSize(new Dimension(800,550));
        footer.setPreferredSize(new Dimension(800,30));
        footer.setBackground(Color.BLUE);
        content.setLayout(new GridLayout(1,1));
        footer.setMaximumSize(new Dimension(30000,30));
        window.setPreferredSize(new Dimension(800,600));
        window.pack();
        manualRefresh();
        ticker.scheduleAtFixedRate(new TimerTask(){
            @Override
            public void run(){
                manualRefresh();
            }
        },10 * 1000,10 * 1000);
        /**************************************************************/
		
		// Display window
		if(!silent){
       		window.setVisible(true);
		}
		//session.run();      //Start killswitch
		ticker.cancel();
		//exitApplication();
    }
	public void manualRefresh(){
		/*logsModel.removeAllElements();
		for(String file :  session.getFiles()){
			try{
				logsModel.addElement((String)file);
			}
			catch(Exception e){
			}
		}*/
		if(splitter != null){
			splitter.remove(logsView);
			logsView = null;
            splitter.revalidate();
            splitter.repaint();
			logsList = new JList<String>(session.getFiles());
			logsView = new JScrollPane(logsList);
			splitter.add(logsView);
			splitter.revalidate();
			splitter.repaint();
		}
	}
    public void exitApplication(){
        System.exit(0);
    }
    public void keyDetector(KeyEvent e){
        if(e.getKeyCode() == KeyEvent.VK_I && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)){
        }
        else if(e.getKeyCode() == KeyEvent.VK_R && ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)){
        }
    }
}
