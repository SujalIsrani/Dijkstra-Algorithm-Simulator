import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import javax.swing.*;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class DijkstraSimulator extends JFrame {

    // ** classes **
    Panel panel = new Panel();
    JFrame frame = new JFrame();
    Help help = new Help();
    Edge edge = new Edge();

    // ** lists **
    DijkstraSimulator.DotList<DijkstraSimulator.Dot> dotList = new DijkstraSimulator.DotList<>();
    DijkstraSimulator.EdgeList<DijkstraSimulator.Edge> edgeList = new DijkstraSimulator.EdgeList<>();

    
    // ** mouse listeners **
    ClickListner cl = new ClickListner(); 
    ClickListner2 cl2 = new ClickListner2();
    ClickListner3 cl3 = new ClickListner3();
    ClickListner4 cl4 = new ClickListner4();
    ClickListner5 cl5 = new ClickListner5();
    ClickListner6 cl6 = new ClickListner6(); // New mouse listener for avoiding nodes

    // ** buttons **
    JRadioButton rbtnAddVertex = new JRadioButton("Add Vertex");
    JRadioButton rbtnAddEdge = new JRadioButton("Add Edges");
    JRadioButton addWeightRadioButton = new JRadioButton("Add Weight");
    JRadioButton rbtnMoveVertex = new JRadioButton("Move Vertex");
    JRadioButton rbtnShortestPath = new JRadioButton("Shortest Path");
    JRadioButton rbtnModifiedDijkstra = new JRadioButton("Modified Dijkstra"); // New radio button for modified Dijkstra
    JRadioButton rbtnAvoidNode = new JRadioButton("Avoid Node"); // New radio button for avoiding nodes
    JRadioButton rbtnweight = new JRadioButton("Change a Weight to:");
    JButton btnaddAllEdge = new JButton("Add All Edges");
    JButton btnRandomWeight = new JButton("Random Weight");
    JButton btnHelp = new JButton("Help");
    JButton btnMinimalTree = new JButton("Minimal Spanning Tree");
    JButton btnClearCanvas = new JButton("Clear All");
    JLabel lblTitle = new JLabel("Dijkstra Algorithm Simulator");

    //**JTextField**//
    JTextArea jWeight = new JTextArea(5, 20);

    //**jlabel**
    JLabel label = new JLabel();

    //** two nodes to get the shortest path **
    Dot node1; //they are used to store the starting and destination vertex while getting the shortest path
    Dot node2;

    //** list to store avoided nodes **
    ArrayList<Dot> avoidedNodes = new ArrayList<>(); // New list to store avoided nodes

    //** constructor **
    public DijkstraSimulator() {
        super("Dijkstra Algorithm Simulator");
        setFrame();
    }

    public void setFrame() {
        this.setSize(1550, 850);
        this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.setBackground(Color.WHITE);
        this.addPanel();
        this.setButtons();
        this.setLabel();
        this.setVisible(true);
        //this.addlabel(); for title
    }

    public void paint(Graphics g) {
        super.paint(g);
        //        *draws the vertex*
        if (dotList.size() > 0) {
            for (int i = 0; i < dotList.size(); i++) {
                dotList.get(i).draw(g);
            }
        }

        //        *draws the edges*
        if (edgeList.size() > 0) {
            for (int i = 0; i < edgeList.size(); i++) {
                edgeList.get(i).draw(g);
            }
        }
    }

    public void addPanel() {
        panel.setBackground(Color.WHITE);
        panel.setBounds(500, 75, 1000, 600);
        panel.setVisible(true);
        this.add(panel);
    }

    public void addlabel() {
        JLabel label = new JLabel("You can add title here");
        label.setBounds(550, 20, 500, 50);
        label.setFont(new Font("Arial", Font.BOLD, 45)); // Set font size to 24
        this.add(label);
    }

    //************* Add buttons to the frame ******************
    public void setButtons() {

        lblTitle.setBounds(50, 30, 500, 30); // (x, y, width, height)
    lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 30)); // Set the font and style
    this.add(lblTitle); // Add the label to the frame

        int x = 50;
        int y = 700;
        int increment = 375;

        //************* REGULAR BUTTONS *************

        //********** Add all the Edges *********
        btnaddAllEdge.setBounds(x, y, 325, 50);
        btnaddAllEdge.setFont(new Font("Arial", Font.BOLD, 28));
        //this.add(btnaddAllEdge);
        btnaddAllEdge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                edgeDefaultColor();
                addAllEdges();
                removeMouseAdapters();
                setLabel(Color.GREEN, "Edges Added successfully.");
            }
        });
	

        x += increment;
        //***** Get Random Weight for the edge in edgeList *******
        btnRandomWeight.setBounds(x, y, 325, 50);
        btnRandomWeight.setFont(new Font("Arial", Font.BOLD, 28));
        this.add(btnRandomWeight);
        btnRandomWeight.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeMouseAdapters();
                edgeDefaultColor();
                for (int i = 0; i < edgeList.size(); i++) {
                    setLabel(Color.GREEN, "Random Weight added to the Edges.");
                    Random r = new Random();
                    int j = r.nextInt(200) + 1;
                    edgeList.get(i).setWeight(j);
                    repaint();
                }
            }
        });
        x += increment;
        //*** Display instruction to use the app ( activates the help class) ****
        btnHelp.setBounds(x, y, 325, 50);
        btnHelp.setFont(new Font("Arial", Font.BOLD, 28));
        this.add(btnHelp);
        btnHelp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeMouseAdapters();
                help = new Help();
            }
        });
        x += increment;
        //***** Get Minimal spanning tree *******
        btnMinimalTree.setBounds(x, y, 325, 50);
        btnMinimalTree.setFont(new Font("Arial", Font.BOLD, 25));
        this.add(btnMinimalTree);
        btnMinimalTree.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeMouseAdapters();

                dotDefaultColor();
                Dijkstra dj = new Dijkstra();
                dj.execute(dotList.get(1));
                dj.getMinimalTree();

                setLabel(Color.GREEN, "Minimal path found.");
            }
        });
        //* clear canvas */
        x += increment;
         btnClearCanvas.setBounds(50, 700, 325, 50);
         btnClearCanvas.setFont(new Font("Arial", Font.BOLD, 28));
         this.add(btnClearCanvas);
         
         btnClearCanvas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearCanvas();
            }
        });

        //************ RADIO BUTTONS ****************
        x = 50;
        y = 100;
        increment = 75;
        //        *radio button to add vertex*
        rbtnAddVertex.setBounds(x, y, 300, 50);
        rbtnAddVertex.setFont(new Font("Arial", Font.BOLD, 28));
        this.add(rbtnAddVertex);
        y += increment;
	
        //        *radio button to move the vertex*
        rbtnMoveVertex.setBounds(x, y, 300, 50);
        rbtnMoveVertex.setFont(new Font("Arial", Font.BOLD, 28));
        this.add(rbtnMoveVertex);
        y += increment;
        //        *radio button to add edge*
        rbtnAddEdge.setBounds(x, y, 300, 50);
        rbtnAddEdge.setFont(new Font("Arial", Font.BOLD, 28));
        this.add(rbtnAddEdge);
        y += increment;
    addWeightRadioButton.setBounds(x, y, 325, 50);
    addWeightRadioButton.setFont(new Font("Arial", Font.BOLD, 28));
	this.add(addWeightRadioButton);
	y += increment;
        rbtnShortestPath.setBounds(x, y, 300, 50);
        rbtnShortestPath.setFont(new Font("Arial", Font.BOLD, 28));
        this.add(rbtnShortestPath);
        y += increment;
        rbtnModifiedDijkstra.setBounds(x, y, 300, 50); // New radio button for modified Dijkstra
        rbtnModifiedDijkstra.setFont(new Font("Arial", Font.BOLD, 28));
        this.add(rbtnModifiedDijkstra);
        y += increment;
        rbtnAvoidNode.setBounds(x, y, 300, 50); // New radio button for avoiding nodes
        rbtnAvoidNode.setFont(new Font("Arial", Font.BOLD, 28));
        this.add(rbtnAvoidNode);
        y += increment;
        rbtnweight.setBounds(x, y, 300, 50);
        rbtnweight.setFont(new Font("Arial", Font.BOLD, 28));
        this.add(rbtnweight);

        //**change weight to text field**
        jWeight.setBounds(x + 310, y + 10, 88, 35);
        jWeight.setFont(new Font("Arial", Font.BOLD, 30));
        this.add(jWeight);

        //****** Grouping radio buttons ******
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbtnAddEdge);
        bg.add(addWeightRadioButton);
        bg.add(rbtnAddVertex);
        bg.add(rbtnShortestPath);
        bg.add(rbtnModifiedDijkstra); // Add the new radio button to the group
        bg.add(rbtnMoveVertex);
        bg.add(rbtnAvoidNode); // Add the new radio button to the group
        bg.add(rbtnweight);

        //****** selecting radio to button move vertex *****
        rbtnMoveVertex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLabel(Color.RED, "Press on a vertex and drag mouse to any location");
                panel.removeMouseListener(cl);
                panel.removeMouseListener(cl2);
                panel.removeMouseListener(cl4);
                panel.removeMouseListener(cl5);
                panel.removeMouseListener(cl6); // Remove the new mouse listener
                panel.addMouseListener(cl3);
            }
        });

        rbtnAddVertex.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLabel(Color.RED, "click inside the white box to create vertex.");
                panel.removeMouseListener(cl2);
                panel.removeMouseListener(cl3);
                panel.removeMouseListener(cl4);
                panel.removeMouseListener(cl5);
                panel.removeMouseListener(cl6); // Remove the new mouse listener
                panel.addMouseListener(cl);
            }
        });

        //****** selecting radio button to add edges *****
        rbtnAddEdge.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLabel(Color.RED, "click on a vertex.");

                panel.removeMouseListener(cl);
                panel.removeMouseListener(cl3);
                panel.removeMouseListener(cl4);
                panel.removeMouseListener(cl5);
                panel.removeMouseListener(cl6); // Remove the new mouse listener
                panel.addMouseListener(cl2);
            }
        });

        addWeightRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Here, you can prompt the user to enter the weight for the last added edge
                // For example, you can show a dialog box or input field for the user to enter the weight
                // Once the weight is entered, update the weight of the last added edge
                
                // Example prompt (you can replace this with your own UI logic):
            String weightString = JOptionPane.showInputDialog(null, "Enter the weight for the last added edge:");
            
                // Convert the input string to integer and update the weight of the last added edge
                try {
                    int weight = Integer.parseInt(weightString);
                    
                    // Update the weight of the last added edge
                    Edge lastAddedEdge = edgeList.get(edgeList.size() - 1); // Assuming edgeList is your list of edges
                    lastAddedEdge.setWeight(weight);
                    
                    // Repaint the panel to reflect the updated edge weight
                    repaint();
                    
                    // Inform the user that the weight has been added
                    setLabel(Color.GREEN, "Weight added successfully.");
                } catch (NumberFormatException ex) {
                    // Handle invalid input (e.g., non-numeric input)
                    setLabel(Color.RED, "Invalid input. Please enter a valid weight.");
                }
            }
        });

        //** Selecting this button will change the weight of the edge **
        rbtnweight.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLabel(Color.RED, "Enter weight you want and select an edge that you wish to change");
                panel.removeMouseListener(cl);
                panel.removeMouseListener(cl2);
                panel.removeMouseListener(cl3);
                panel.removeMouseListener(cl5);
                panel.removeMouseListener(cl6); // Remove the new mouse listener
                panel.addMouseListener(cl4);
            }
        });

        //*** selecting this button will give the shortest path *****
        rbtnShortestPath.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                edgeDefaultColor();
                setLabel(Color.RED, "Click on a starting vertex and then click on the destination.");

                node1 = new Dot();
                node2 = new Dot();

                panel.removeMouseListener(cl);
                panel.removeMouseListener(cl2);
                panel.removeMouseListener(cl3);
                panel.removeMouseListener(cl4);
                panel.removeMouseListener(cl6); // Remove the new mouse listener
                panel.addMouseListener(cl5);
            }
        });

        // New radio button for modified Dijkstra
        rbtnModifiedDijkstra.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                edgeDefaultColor();
                setLabel(Color.RED, "Click on a starting vertex and then click on the destination.");

                node1 = new Dot();
                node2 = new Dot();

                panel.removeMouseListener(cl);
                panel.removeMouseListener(cl2);
                panel.removeMouseListener(cl3);
                panel.removeMouseListener(cl4);
                panel.removeMouseListener(cl5);
                panel.addMouseListener(cl5); // Add the existing mouse listener for selecting nodes
            }
        });

        // New radio button for avoiding nodes
        rbtnAvoidNode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setLabel(Color.RED, "Click on the nodes you want to avoid.");

                panel.removeMouseListener(cl);
                panel.removeMouseListener(cl2);
                panel.removeMouseListener(cl3);
                panel.removeMouseListener(cl4);
                panel.removeMouseListener(cl5);
                panel.addMouseListener(cl6); // Add the new mouse listener for avoiding nodes
            }
        });
    }

    //**setting up the JLabel**
    public void setLabel() {
        label.setBounds(350, 670, 300, 30);
        label.setForeground(Color.RED);
        panel.add(label);
    }

    public void setLabel(Color c, String message) {
        label.setForeground(c);
        label.setText(message);
    }

    public void clearCanvas() {
        dotList.clear();
        edgeList.clear();
        avoidedNodes.clear();
        removeMouseAdapters();
        repaint();
        setLabel(Color.GREEN, "Canvas cleared.");
    }

   
    //*********** change back the color of the dots to red ***************
    public void dotDefaultColor() {
        for (int i = 0; i < dotList.size(); i++) {
            dotList.get(i).setColor(Color.RED);
        }
    }

    //*********** change back the color of the edges to blue ***************
    public void edgeDefaultColor() {
        for (int i = 0; i < edgeList.size(); i++) {
            edgeList.get(i).setColor(Color.BLUE);
        }
    }

    public void addAllEdges() {
        edgeList = new EdgeList<>();
        int size = dotList.size();
        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                edgeList.add(new Edge(dotList.get(i), dotList.get(j)));
                edgeList.get(i).calcWeight();
                repaint();
            }
        }
    }
    
    
    public void removeMouseAdapters() {
        panel.removeMouseListener(cl);
        panel.removeMouseListener(cl2);
        panel.removeMouseListener(cl3);
        panel.removeMouseListener(cl4);
        panel.removeMouseListener(cl5);
        panel.removeMouseListener(cl6); // Remove the new mouse listener
        node1 = new Dot(); // Reset node1
        node2 = new Dot(); // Reset node2
    }
   

    //**************** main function ******************
    public static void main(String[] args) {
        DijkstraSimulator g = new DijkstraSimulator();
    }

    //**************** mouse adapters classes for dot**************
    protected class ClickListner extends MouseAdapter {

        ClickListner() {
            super();
        }

        public void mouseClicked(MouseEvent e) {
            Dot dot = new Dot();
            int x = e.getX() + 500;
            int y = e.getY() + 100;
            dot.setColor(Color.RED);
            dot.setX(x);
            dot.setY(y);
            dotList.add(dot);
            repaint();
        }
    }

    //*************** mouse adapter for edgelist ****************
    protected class ClickListner2 extends MouseAdapter {
        int i = 0;

        ClickListner2() {
            super();
        }

        public void mouseClicked(MouseEvent e) {
            Dot dots = new Dot();
            int x = e.getX() + 500;
            int y = e.getY() + 100;

            //**Check if user clicked on specific dots**
            for (i = 0; i < dotList.size(); i++) {
                int z1 = dotList.get(i).getX(), z2 = dotList.get(i).getY();

                if (((x <= z1 + 10) && (x >= z1 - 10)) && ((y <= z2 + 10) && (y >= z2 - 10))) {
                    dotList.get(i).setColor(Color.GREEN);
                    setLabel(Color.RED, "Click on another vertex to add edge.");
                    repaint();
                    if (edge.hasA()) {
                        dots.setX(z1); //change back to z1,z2
                        dots.setY(z2);
                        edge.setB(dots);
                        break;
                    } else {
                        dots.setX(z1);
                        dots.setY(z2);
                        edge.setA(dots);
                        break;
                    }
                }
            }

            //**Check if there are two points and if there are, draw a line from point A to point B**
            if (edge.hasA() && edge.hasB()) {
                edge.calcWeight();
                edgeList.add(edge);
                setLabel(Color.GREEN, "Edge Added successfully!!!");
                edge = new Edge();
                dotDefaultColor(); //change color back to red
                repaint();
            }
        }
    }

    //*************** mouse Adapter to move the vertex **************
    protected class ClickListner3 extends MouseAdapter {

        int x, y, i = 0;
        int a, b, j; // this is the point from the vertex
        boolean pressed;

        ClickListner3() {
            super();
        }

        public void mousePressed(MouseEvent e) {
            x = e.getX() + 500;
            y = e.getY() + 100;
            for (this.i = 0; this.i < dotList.size(); this.i++) {
                a = dotList.get(this.i).getX();
                b = dotList.get(this.i).getY();
                if (((x <= a + 10) && (x >= a - 10)) && ((y <= b + 10) && (y >= b - 10))) {
                    pressed = true;
                    dotList.get(i).setColor(Color.GREEN);

                    setLabel(Color.RED, "Drag your mouse to any location and release to move the vertex");
                    repaint();
                    break;
                }
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (pressed) {
                x = e.getX() + 500;
                y = e.getY() + 100;
                dotList.get(this.i).setX(x);
                dotList.get(this.i).setY(y);

                setLabel(Color.GREEN, "Vertex moved successfully.");
                this.moveEdge();
                dotDefaultColor(); //change color to red
                repaint();
            } else {
                setLabel(Color.RED, "Moving vertex Unsuccessful!!!");
            }

        }

        //***** change the point on the edge after moving the vertex ******
        public void moveEdge() {
            if (edgeList.size() >= i) {
                for (j = 0; j < edgeList.size(); j++) {
                    if (edgeList.get(j).equalsEdge(a + 5, b + 5)) {

                        if (edgeList.get(j).equalsA(a + 5, b + 5)) {
                            edgeList.get(j).setA(new Dot(x, y));
                        } else {
                            edgeList.get(j).setB(new Dot(x, y));
                        }
                    }
                }
            }
        }
    }

    //************ Mouse Adapter to change the weight of the edge ***********
    protected class ClickListner4 extends MouseAdapter {
        int x, y, i = 0;

        public ClickListner4() {
            super();
        }

        public void mouseClicked(MouseEvent e) {
            x = e.getX() + 500;
            y = e.getY() + 100;
            int newWeight = 0;
            if (edgeList.size() > 0) {

                //check if the number entered is valid or not
                try {
                    newWeight = Integer.parseInt(jWeight.getText());
                } catch (NumberFormatException nfe) {
                    setLabel(Color.RED, nfe.getMessage());
                }

                for (i = 0; i < edgeList.size(); i++) {

                    if (edgeList.get(i).checkEdge(x, y) && newWeight != 0) {
                        edgeList.get(i).setWeight(newWeight);
                        setLabel(Color.GREEN, "New Weight added successfully.");
                        repaint();
                        break;
                    } else {
                        setLabel(Color.RED, "Error adding weight, please check the instructions.");
                    }
                }

            }

            repaint();
        }
    }

    //******** mouse Adapter to Get the shortest path from one node to another **********
    protected class ClickListner5 extends MouseAdapter {
        public ClickListner5() {
            super();
        }

        public void mouseClicked(MouseEvent e) {
            edgeDefaultColor();
            int x = e.getX() + 500;
            int y = e.getY() + 100;
            int i;
            node2 = new Dot();
            for (i = 0; i < dotList.size(); i++) {
                int z1 = dotList.get(i).getX(), z2 = dotList.get(i).getY();

                if (((x <= z1 + 10) && (x >= z1 - 10)) && ((y <= z2 + 10) && (y >= z2 - 10))) {

                    if (node1.isEmpty()) {
                        node1 = dotList.get(i);
                        dotList.get(i).setColor(Color.GREEN);
                        setLabel(Color.RED, "Click on destination vertex to find the shortest path.");
                    } else if (node2.isEmpty()) {
                        node2 = dotList.get(i);
                        dotList.get(i).setColor(Color.GREEN);
                    }
                    repaint();
                    break;
                }

            }

            //add the methods from Dijkstra's algorithm
            if (!node1.isEmpty() && !node2.isEmpty()) {
                Dijkstra dj = new Dijkstra();
                dj.execute(node1);
                if (rbtnShortestPath.isSelected()) {
                    dj.getPath(node1, node2);
                } else if (rbtnModifiedDijkstra.isSelected()) {
                    dj.getModifiedPath(node1, node2, avoidedNodes);
                }
            }
        }
    }

    // New mouse listener for avoiding nodes
    protected class ClickListner6 extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            int x = e.getX() + 500;
            int y = e.getY() + 100;
            int i;
            for (i = 0; i < dotList.size(); i++) {
                int z1 = dotList.get(i).getX(), z2 = dotList.get(i).getY();

                if (((x <= z1 + 10) && (x >= z1 - 10)) && ((y <= z2 + 10) && (y >= z2 - 10))) {
                    Dot avoidedNode = dotList.get(i);
                    if (avoidedNodes.contains(avoidedNode)) {
                        avoidedNodes.remove(avoidedNode);
                        avoidedNode.setColor(Color.RED); // Set the node color back to red
                        setLabel(Color.GREEN, "Node removed from avoided list.");
                    } else {
                        avoidedNodes.add(avoidedNode);
                        avoidedNode.setColor(Color.GRAY); // Set the avoided node color to gray
                        setLabel(Color.GREEN, "Node added to avoided list.");
                    }
                    repaint();
                    break;
                }
            }
        }
    }

    //************* Implementation of Dijkstra's Algorithm **************
    protected class Dijkstra {
        private ArrayList<Dot> nodes;
        private EdgeList<Edge> edges;
        private Set<Dot> visitedNodes;
        private Set<Dot> unvisitedNodes;
        private Map<Dot, Integer> totalWeight;
        private Map<Dot, Dot> prevNodes;

        public Dijkstra() {
            nodes = new ArrayList<>();
            for (int i = 0; i < dotList.size(); i++) {
                Dot dot = new Dot(dotList.get(i).getX(), dotList.get(i).getY());
                this.nodes.add(dot);
            }
            this.edges = edgeList;
        }

        public void execute(Dot start) {
            visitedNodes = new HashSet<>();
            unvisitedNodes = new HashSet<>();
            totalWeight = new HashMap<>();
            prevNodes = new HashMap<>();
            this.totalWeight.put(start, 0);
            unvisitedNodes.add(start);
            while (unvisitedNodes.size() > 0) {
                Dot node = getMinimum(unvisitedNodes);
                visitedNodes.add(node);
                unvisitedNodes.remove(node);
                findMinimalWeights(node);
            }
        }

        private Dot getMinimum(Set<Dot> dots) {
            Dot minimum = null;
            for (Dot dot : dots) {
                if (minimum == null) {
                    minimum = dot;
                } else {
                    if (this.getShortestDistance(dot) < this.getShortestDistance(minimum)) {
                        minimum = dot;
                    }
                }
            }
            return minimum;
        }

        public void findMinimalWeights(Dot node) {
            DotList<Dot> adjNodes = getNeighbours(node);
            for (int i = 0; i < adjNodes.size(); i++) {
                Dot target = adjNodes.get(i);
                if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                    totalWeight.put(target, getShortestDistance(node) + getDistance(node, target));
                    prevNodes.put(target, node);
                    unvisitedNodes.add(target);
                }
            }
        }

        private int getDistance(Dot node, Dot target) {
            int weight = 0;
            Edge edge = new Edge(node, target);
            int index = this.getIndexOf(edge);
            if (index != -1)
                weight = edges.get(index).getWeight();
            else {
                throw new RuntimeException("no such edge");
            }
            return weight;
        }

        private int getShortestDistance(Dot node) {
            Integer d = totalWeight.get(node);
            if (d == null) {
                return Integer.MAX_VALUE;
            } else
                return d;
        }

        private DotList<Dot> getNeighbours(Dot node) {
            DotList<Dot> temp = new DotList<>();
            for (int i = 0; i < nodes.size(); i++) {
                if (!visitedNodes.contains(nodes.get(i))) {
                    if (this.checkNeighbour(new Edge(node, nodes.get(i))) == true) {
                        temp.add(nodes.get(i));
                    }
                }

            }

            return temp;
        }

        public Map<Dot, Dot> getPrev() {
            return this.prevNodes;
        }

        public boolean checkNeighbour(Edge e) {
            int x1, x2, y1, y2, a, b, c, d; //a=x1 b=x2 c=y1 d=y2 where abcd are points of edge from edgelist
            boolean check = false;
            x1 = e.getPointA().getX();
            x2 = e.getPointB().getX();
            y1 = e.getPointA().getY();
            y2 = e.getPointB().getY();
            for (int i = 0; i < edges.size(); i++) {
                a = edges.get(i).getPointA().getX();
                b = edges.get(i).getPointB().getX();
                c = edges.get(i).getPointA().getY();
                d = edges.get(i).getPointB().getY();
                if (a == x1 && b == x2 && c == y1 && d == y2) {
                    check = true;
                    break;
                }
                if (a == x2 && b == x1 && c == y2 && d == y1) {
                    check = true;
                    break;
                }
            }
            return check;
        }

        public int getIndexOf(Edge e) {
            int x1, x2, y1, y2, a, b, c, d, index = -1; //a=x1 b=x2 c=y1 d=y2 where abcd are points of edge from edgelist
            x1 = e.getPointA().getX();
            x2 = e.getPointB().getX();
            y1 = e.getPointA().getY();
            y2 = e.getPointB().getY();
            for (int i = 0; i < edges.size(); i++) {
                a = edges.get(i).getPointA().getX();
                b = edges.get(i).getPointB().getX();
                c = edges.get(i).getPointA().getY();
                d = edges.get(i).getPointB().getY();
                if (a == x1 && b == x2 && c == y1 && d == y2) {
                    index = i;
                    break;
                }
                if (a == x2 && b == x1 && c == y2 && d == y1) {
                    index = i;
                    break;
                }
            }
            return index;
        }

        //this method will find the index of the edges which we have in the prevNodes and then change the color in the original Edge list
        public void getMinimalTree() {
            int i = 0;
            ArrayList<Dot> target = new ArrayList<>(prevNodes.keySet());
            ArrayList<Dot> previous = new ArrayList<>(prevNodes.values());
            if (target.size() == previous.size()) {
                for (int j = 0; j < prevNodes.size(); j++) {
                    i = this.getIndexOf(new Edge(target.get(j), previous.get(j)));
                    edgeList.get(i).setColor(Color.GREEN);
                }

            } else
                throw new RuntimeException("Error while getting minimum, target.size!=previous.size");
            repaint();
            dotDefaultColor();

        }

        //get the shortest path from one node to another
        public void getPath(Dot a, Dot b) {
            dotDefaultColor();
            if (!b.isEmpty()) {
                int x, y;
                ArrayList<Dot> keys = new ArrayList<>(prevNodes.keySet());
                LinkedList<Dot> path = new LinkedList<>();
                Dot start = b; //was b
                for (int i = 0; i < keys.size(); i++) {
                    x = keys.get(i).getX();
                    y = keys.get(i).getY();
                    if (x == start.getX() && y == start.getY()) {
                        start = keys.get(i);
                        break;
                    }
                }
                if (prevNodes.get(start) == null) {
                    return;
                }
                path.add(start);
                while (prevNodes.get(start) != null) {
                    path.add(prevNodes.get(start));
                    start = prevNodes.get(start);
                }
                //reverse the LinkedList
                Collections.reverse(path);

                int i;
                for (i = 0; i < path.size(); i++) {
                    try {
                        int index = this.getIndexOf(new Edge(path.get(i), path.get(i + 1)));
                        edgeList.get(index).setColor(Color.RED);

                    } catch (IndexOutOfBoundsException e) {
                        setLabel(Color.RED, "Error!!!" + e.getMessage());
                    }
                }
                repaint();
                setLabel(Color.GREEN, "Shortest path found ");
            }
        }

        // New method to get the modified path while avoiding the selected nodes
        public void getModifiedPath(Dot a, Dot b, ArrayList<Dot> avoidedNodes) {
            dotDefaultColor();
            edgeDefaultColor();
            if (!b.isEmpty()) {
                int x, y;
                ArrayList<Dot> keys = new ArrayList<>(prevNodes.keySet());
                LinkedList<Dot> path = new LinkedList<>();
                Dot start = b;
                boolean foundPath = false;
        
                for (int i = 0; i < keys.size(); i++) {
                    x = keys.get(i).getX();
                    y = keys.get(i).getY();
                    if (x == start.getX() && y == start.getY() && !avoidedNodes.contains(keys.get(i))) {
                        start = keys.get(i);
                        path.add(start);
                        foundPath = true;
                        break;
                    }
                }
        
                if (foundPath) {
                    while (prevNodes.get(start) != null && !avoidedNodes.contains(prevNodes.get(start))) {
                        path.addFirst(prevNodes.get(start));
                        start = prevNodes.get(start);
                    }
        
                    int i;
                    for (i = 0; i < path.size() - 1; i++) {
                        try {
                            int index = this.getIndexOf(new Edge(path.get(i), path.get(i + 1)));
                            edgeList.get(index).setColor(Color.RED);
        
                        } catch (IndexOutOfBoundsException e) {
                            setLabel(Color.RED, "Error!!!" + e.getMessage());
                        }
                    }
                    repaint();
                    setLabel(Color.GREEN, "Modified shortest path found ");
                } else {
                    setLabel(Color.RED, "No path found while avoiding the selected nodes.");
                }
            }
        }
    }
    //**************** Other helper classes ************

    protected class DotList<Dot> {

        private ArrayList<Dot> dotList;

        public DotList() {
            dotList = new ArrayList<>();
        }

        public void add(Dot d) {
            dotList.add(d);
        }

        public int size() {
            return dotList.size();
        }

        public Dot get(int i) {
            return dotList.get(i);
        }

        public ArrayList<Dot> getList() {
            return dotList;
        }
        
        public void clear() {
            dotList.clear();
        }

        public Iterator<Dot> iterator() {
            ArrayList<Dot> list = new ArrayList<>();
            for (int i = 0; i < dotList.size(); i++) {
                list.add(dotList.get(i));
            }
            return list.iterator();
        }
    }

    //*** This class contains the Vertex ***
    protected class Dot {

        private int x;
        private int y;
        private Color color;

        public Dot() {
            this.color = Color.RED;
            x = y = 0;
        }

        public Dot(int a, int b) {
            this.color = Color.RED;
            x = a;
            y = b;
        }

        public void setX(int a) {
            x = a;
        }

        public void setY(int b) {
            y = b;
        }

        public void setColor(Color c) {
            this.color = c;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public boolean isEmpty() {
            if (this.x == 0 && this.y == 0)
                return true;
            else
                return false;
        }

        public String getColor() {
            return color.toString();
        }

        public void draw(Graphics g) {
            g.setColor(color);
            g.fillOval(x, y, 10, 10);
        }
    }

    //*** This class contains the Edge ***
    protected class Edge {

        private int x1, y1, x2, y2;
        private int weight;
        private double slope;
        Color color = Color.BLUE;
        Dot dot;

        public Edge() {
            x1 = x2 = y1 = y2 = 0;
            weight = 0;
            dot = new Dot();
        }

        public Edge(Dot a, Dot b) {
            x1 = a.getX() + 5;
            x2 = b.getX() + 5;
            y1 = a.getY() + 5;
            y2 = b.getY() + 5;
            this.calcWeight();
        }

        public void setEdge(Dot a, Dot b) {
            x1 = a.getX();
            y1 = a.getY();
            x2 = b.getX();
            y2 = b.getY();
        }

        public void setA(Dot a) {
            x1 = a.getX() + 5;
            y1 = a.getY() + 5;
        }

        public void setB(Dot b) {
            x2 = b.getX() + 5;
            y2 = b.getY() + 5;
        }

        public void setColor(Color c) {
            this.color = c;
        }

        public int calcWeight() {
            weight = (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
            return weight;
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public void draw(Graphics g) {
            /*midpoint(x,y) is the position where we draw weight of the edge*/
            int midPointX = (int) ((x2 + x1) / 2);
            int midPointY = (int) ((y2 + y1) / 2);

            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(weight), midPointX, midPointY);
            g.setColor(color);
            g.drawLine(x1, y1, x2, y2);
        }

        public Dot getPointA() {
            Dot A = new Dot(x1, y1);
            return A;
        }

        public Dot getPointB() {
            Dot B = new Dot(x2, y2);
            return B;
        }

        public boolean hasA() {
            return (x1 > 0 && y1 > 0);
        }

        public boolean hasB() {
            return (x2 > 0 && y2 > 0);
        }

        public boolean equalsA(int x, int y) {
            return (x == x1 && y == y1);
        }

        public boolean equalsB(int x, int y) {
            return (x == x2 && y == y2);
        }

        public boolean equalsEdge(int x, int y) {
            return (this.equalsA(x, y) || this.equalsB(x, y));
        }

        public double calcSlope() {
            return slope = (double) (y2 - y1) / (x2 - x1);
        }

        //**** check if the user clicked on this edge or not ****
        public boolean checkEdge(int x, int y) {
            //this.calcSlope();
            int i, a, b;
            if (this.equal(x, y)) return true;

            //returns true if the (x,y) from the mouse click is on the line (x1,y1)&(x2,y2)
            for (i = 1; i <= 20; i++) {
                a = x;
                b = y + i;
                if (this.equal(a, b)) return true;

                a = x;
                b = y - i;
                if (this.equal(a, b)) return true;

                a = x - i;
                b = y;
                if (this.equal(a, b)) return true;

                a = x + i;
                b = y;
                if (this.equal(a, b)) return true;

                a = x + i;
                b = y + i;
                if (this.equal(a, b)) return true;

                a = x - i;
                b = y - i;
                if (this.equal(a, b)) return true;

                a = x + i;
                b = y - i;
                if (this.equal(a, b)) return true;

                a = x - i;
                b = y + i;
                if (this.equal(a, b)) return true;
            }

            if (i >= 10) {
                return false;
            } else
                return true;
        }

        public boolean equal(int x, int y) {
            int c = ((y) - y1) * (x2 - x1);
            int d = (y2 - y1) * ((x) - x1);
            if (c > d - 20 && c < d + 20) return true;
            else return false;
        }
    }

    //*** This class contains the list of the Edge ***
    public class EdgeList<Edge> {

        private ArrayList<Edge> edgeList;

        public EdgeList() {
            edgeList = new ArrayList<>();
        }

        public void add(Edge e) {
            edgeList.add(e);
        }

        public Edge get(int i) {
            return edgeList.get(i);
        }

        public ArrayList<Edge> getList() {
            return edgeList;
        }

        public int getIndex(Edge e) {
            return edgeList.indexOf(e);
        }

        public int size() {
            return edgeList.size();
        }
        public void clear() {
            edgeList.clear();
        }
    }

    //*** This is the Help class that will guide how to use this program ***
    protected class Help {

        JFrame frame;
        JTextArea jLabel;
        JButton btn;
        JPanel panel;

        public Help() {
            this.gui();
        }

        public void gui() {
            frame = new JFrame("HELP");
            frame.setVisible(true);
            frame.setSize(600, 600);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            panel = new JPanel();
            panel.setSize(frame.getSize());
            panel.setBackground(Color.WHITE);

            jLabel = new JTextArea();
            jLabel.setBounds(20, 20, 56, 360);
            jLabel.setEditable(false);

            btn = new JButton("OK");
            btn.setBounds(290, 500, 50, 20);

            panel.add(jLabel);
            frame.add(btn);
            jLabel.setText("\n\n"
                    + " - Select Add Vertex and left click on the right empty space to\n"
                    + " create a dot or Vertex.--> Create more than one vertex\n\n"
                    + " - Select Add Edge and Connect the vertex to create a link between\n"
                    + " two vertices.\n\n "
                    + " - Select Move Vertex to move vertex to another position.\n\n"
                    + " - Select Shortest Path to find the shortest path from one vertex to another.\n\n"
                    + " - Select Modified Dijkstra to find the shortest path while avoiding selected nodes.\n\n"
                    + " - Select Avoid Node and click on the nodes you want to avoid.\n\n"
                    + " - Select on Change weight to, and then enter a weight then Click on an Edge to\n"
                    + " change the weight of specific edge.\n\n"
                    + " - Click on Add All Edge to connect all the edges.\n\n"
                    + " - Click on Add Random Weight to provide random weight to the edges.\n\n"
                    + " - Click on Minimal Spanning Tree to get the minimal spanning tree\n\n"
                    + " - There might be some issue if you clicked on Minimal Spanning Tree and then Shortest Path.");

            frame.add(panel);

            //***** function of the "OK" button *****
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    frame.dispose();
                }
            });
        }
    }

    protected class Panel extends JPanel {

        public Panel() {
            this.setBounds(230, 10, 440, 600);
            this.setBackground(Color.GRAY);
            this.setVisible(true);
        }
    }

}//end