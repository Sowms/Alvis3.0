
package in.ac.iitm.alvis301;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 *
 * @author SavithaSam
 */

public final class GraphWindow extends javax.swing.JFrame {

    /**
     * Creates new form GraphWindow
     */
    boolean start;
    boolean goal;
    boolean running;
    boolean obstacle;
    public static GraphWindow gw;
    public static int time = 100;
    public String density;
    public ArrayList <Edge> path;
    public BFSAlgorithm bfs;
    public DFSAlgorithm dfs;
    public static GraphWindow getInstance() {
        return gw;
    }
    public static void setInstance(GraphWindow k) {
        gw=k;
    }
    public void showGraph() throws InterruptedException {
        graphPanel1.repaint();
    }
    public GraphWindow() {
        initComponents();
        path = new ArrayList<Edge>();
        jButton1.setVisible(false);
        jSlider1.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
            JSlider source = (JSlider)e.getSource();
            if (!source.getValueIsAdjusting()) {
                time = (int)source.getValue();
            }
        }  
        });
        graphPanel1.addMouseListener(new MouseListener(){   
            public void mouseClicked(MouseEvent e) {
                if(!start && !goal)
                    return;
                Graph g = Graph.getInstance();
                HashMap nodes = g.getNodes();
                double x = e.getX();
                double y = e.getY();
                for (int i=(int) (x-4); i<x+4;i++)
                    for (int j=(int) (y-4);j<y+4;j++) {
                        Integer id = g.getNodeID(i, j);
                        if (id == null || nodes.get(id)==null)
                            continue;
                        int ID = (start) ? g.getStartID() : g.getGoalID();
                        if (ID == -1) {  
                            if (start)
                                g.choose_start(i, j);
                            else
                                g.choose_goal(i, j);
                            }
                            else {
                            Node n = g.getNode(ID);
                            if (start)
                                n.setState(State.unvisited);
                            nodes.put(ID, n);
                            if (start)
                                g.choose_start(i, j);
                            else
                                g.choose_goal(i, j);
                            }
                            break;
                    }
                graphPanel1.repaint();
                }
                public void mousePressed(MouseEvent e) {
                    if(obstacle) {
                        graphPanel1.x1=e.getX();
                        graphPanel1.y1=e.getY();
                    }
                }
            public void mouseReleased(MouseEvent e) {
                if(obstacle) {                        
                    graphPanel1.x2=e.getX();
                    graphPanel1.y2=e.getY();
                    graphPanel1.repaint();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(GraphWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    obstacle=false;
                    Graph g=Graph.getInstance();
                    HashMap <ArrayList<Double>,Integer> posID=g.getPosIDs();
                    HashMap<Integer,Node> nodes=g.getNodes();
                    HashMap<Integer,Edge> edges=g.getEdges();
                    for(int i=graphPanel1.x1;i<graphPanel1.x2;i++ ) {
                        for(int j=graphPanel1.y1;j<graphPanel1.y2;j++ ) {
                            ArrayList<Double> point=new ArrayList<Double>();
                            point.add((double)i);
                            point.add((double)j);
                            Integer nodeID=posID.get(point);
                            if (nodeID == null || g.getNode(nodeID)== null)
                                continue;
                            Node n=nodes.get(nodeID);
                            ArrayList<Edge> adjEdgeList=n.getAdjEdgeList();
                            for (Edge adjEdgeList2 : adjEdgeList) {
                                edges.remove(adjEdgeList2.getEdgeID());
                                Edge e1 = adjEdgeList2;
                                Node node1=g.getNode(e1.getNodeID1());
                                Node node2=g.getNode(e1.getNodeID2());
                                Node node_s=node1.equals(n)?node2:node1;
                                ArrayList<Edge> adjEdgeList1 = node_s.getAdjEdgeList();
                                adjEdgeList1.remove(e1);
                                ArrayList<Node> adjList = node_s.getAdjList();
                                adjList.remove(n);
                                node_s.setAdjEdgeList(adjEdgeList1);
                                node_s.setAdjList(adjList);
                                nodes.put(node_s.getNodeID(), node_s);
                            }
                            nodes.remove(n.getNodeID());
                        }
                    }
                    g.setEdges(edges);
                    g.setNodes(nodes);
                    Graph.setInstance(g);
                    graphPanel1.repaint();
                }
            }
            public void mouseEntered(MouseEvent e) {
            }
            public void mouseExited(MouseEvent e) {
            }
        });
        jButton1.setEnabled(false);
        jButton4.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jSlider1 = new javax.swing.JSlider(0,2000,1000);
        jSlider1.setMajorTickSpacing(1000);
        jSlider1.setMinorTickSpacing(100);
        jSlider1.setPaintTicks(true);
        jSlider1.setPaintLabels(true);

        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        graphPanel1 = new GraphPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Step Through");

        jButton2.setText("Start");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Goal");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Obstacle");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Run");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Pipe");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout graphPanel1Layout = new javax.swing.GroupLayout(graphPanel1);
        graphPanel1.setLayout(graphPanel1Layout);
        graphPanel1Layout.setHorizontalGroup(
            graphPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 604, Short.MAX_VALUE)
        );
        graphPanel1Layout.setVerticalGroup(
            graphPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 427, Short.MAX_VALUE)
        );

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("New");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        
        
        jMenu1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Save");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    jMenuItem2ActionPerformed(evt);
                } catch (IOException ex) {
                    Logger.getLogger(GraphWindow.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        jMenu1.add(jMenuItem2);

        jMenu1.setText("File");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Home");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }

            
        });
        
        
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 236, Short.MAX_VALUE)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addGap(18, 18, 18)
                .addComponent(jButton6)
                .addContainerGap(253, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 603, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(graphPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(graphPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>                        

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) throws IOException {                                           
        FileDialog fDialog = new FileDialog(this, "Save", FileDialog.SAVE);
        fDialog.setVisible(true);
        String fpath = fDialog.getDirectory() + fDialog.getFile();
        File f = new File(fpath);
        BufferedImage im = new BufferedImage(graphPanel1.getWidth(), graphPanel1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        graphPanel1.paint(im.getGraphics());
        ImageIO.write(im, "PNG", f);
    }
    
    public void jMenuItem3ActionPerformed(ActionEvent evt) {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread currentThread : threadSet) {
            String threadName = currentThread.toString();
            if (threadName.contains("Thread") && threadName.contains("main")) {
                System.out.println(threadName);
                currentThread.interrupt();
                break;
            }
        }
        
        this.dispose();
        HomeWindow Main = new HomeWindow();
        Main.setVisible(true);
        Main.setTitle("AlVis 3.0");
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        Main.setLocation(screenSize.width/3, screenSize.height/3);//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {     
        jButton1.setEnabled(false);
        jButton2.setEnabled(true);
        jButton3.setEnabled(true);
        jButton4.setEnabled(false);
        jButton5.setEnabled(false);
        jButton6.setEnabled(false);
        GraphCreator c = new GraphCreator();
        c.create(density);
        graphPanel1.repaint();
    }                                          

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        start = true;
        goal = false;
    }
    
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        HashMap <Integer,Node> nodes;
        HashMap <Integer,Node> edges;
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        for (Thread currentThread : threadSet) {
            String threadName = currentThread.toString();
            if (threadName.contains("Thread") && threadName.contains("main")) {
                System.out.println(threadName);
                currentThread.interrupt();
                break;
            }
        }
        
        path = new ArrayList<Edge>();
        Graph g = Graph.getInstance();
        nodes = g.getNodes();
        edges = g.getEdges();    
        for (Map.Entry pairs : nodes.entrySet()) {
            Node n = (Node) pairs.getValue();
            State state = n.getState();
            if(state!=State.unvisited) {
                n.setState(State.pipe);
            }
            if(n.getNodeID()==g.getGoalID())
                 n.setState(State.goal);
            if(n.getNodeID()==g.getStartID())
                 n.setState(State.start);
            nodes.put(n.getNodeID(), n);
        }
        for (Map.Entry pairs : edges.entrySet()) {
            Edge e = (Edge) pairs.getValue();
            State state = e.getState();
            if(state==State.path) {
                e.setState(State.pipepath);
                path.add(e);
            }  
            else if(state!=State.unvisited) {
                e.setState(State.pipe);
            }
        }
        g.setNodes(nodes);
        Graph.setInstance(g);
        graphPanel1.repaint();
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);    
    }     
    //Call your algorithm here for testing
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        JList list = new JList(new String[] {"BFS", "DFS", "Astar"});
        JOptionPane.showMessageDialog(null, list, "Choose Algorithm", JOptionPane.PLAIN_MESSAGE);
        String choice = Arrays.toString(list.getSelectedIndices());
        if(choice.equals("[0]")) {
            bfs = new BFSAlgorithm(1);
            bfs.setGraph();
            bfs.start();
            graphPanel1.repaint();
        }
        else if (choice.equals("[1]")) {
            dfs = new DFSAlgorithm(1);
            dfs.setGraph();
            dfs.start();
            graphPanel1.repaint();
        }
        jButton6.setEnabled(true);
    }                                        

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {                                         
            start = false;
            goal = true;
            jButton4.setEnabled(true);
            jButton5.setEnabled(true);
    }                                        

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        obstacle=true;
        start=false;
        goal=false;
        jButton2.setEnabled(false);
        jButton3.setEnabled(false);
    }                                        
  
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GraphWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GraphWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GraphWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GraphWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GraphWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify                     
    private GraphPanel graphPanel1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSlider jSlider1;
    // End of variables declaration                   
}
