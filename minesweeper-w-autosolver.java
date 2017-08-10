/*William Chou
 * P.4 APCS
 * 3/9/2015
 * 
 * Length: 2 hours
 * 
 * This lab was one of the most
 * interesting labs of the year.
 * I did the autoplay minesweeper
 * for version 2.0.  I feel like
 * the strategy was pretty easy to
 * understand, and the basic autoplay
 * method did not take too long to
 * write.  However, there were some
 * special cases, such as times when
 * guessing was required or a tile
 * had to be revealed some other way.
 * For the lab, I printed out all the
 * steps that the program was taking
 * in the console window.  Overall,
 * this lab was pretty hard, but it
 * was very fun and interesting to think
 * about and code.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.*;
import javax.swing.Timer;
import java.net.URL;

/* 1. Add the capability to capture mouse clicks using MouseListener
 * 2. Add the capability to capture mouse drags using MouseMotionListener
 */

public class Minesweeper2WChouPeriod4 {

    public static void main(String[] args) {

        MyGUI gui = new MyGUI();
    }
}

class MyGUI implements ActionListener, MouseListener, MouseMotionListener {

    // Attributes
    Color color = Color.lightGray;
    Color darkGray = new Color(192,192,192);
    Color transparent = new Color(0,0,0,0);
    
    MyDrawingPanel drawingPanel;
    JPanel mainPanel;
    Timer timer;
    Timer autoplayTimer;
    boolean flagStage = false;
    JFrame window;
    
    int x;
    int y;
    
    Color[][] topArray;
    TileObject[][] bottomArray;
    
    int gridSize = 25;
    int pixelSize = 20;
    
    JLabel timeArea;
    JLabel minesArea;
    
    int time = 0;
    int totalMines = 40;
    int numMines = totalMines;
    
    int guessCount = 0;
    
    static int[] horizontal = {1, 0, -1, -1, -1, 0, 1, 1};
    static int[] vertical = {-1, -1, -1, 0, 1, 1, 1, 0};
    
    BufferedImage tile;
    BufferedImage flag;
    BufferedImage question;
    BufferedImage mine;
    BufferedImage clearedtile;
    BufferedImage tilenumber1;
    BufferedImage tilenumber2;
    BufferedImage tilenumber3;
    BufferedImage tilenumber4;
    BufferedImage tilenumber5;
    BufferedImage tilenumber6;
    BufferedImage tilenumber7;
    BufferedImage tilenumber8;

    JEditorPane aboutpane;
    JEditorPane howtopane;
    JScrollPane about;
    JScrollPane howto;
    
    JLabel win;
    JLabel lose;
    
    MyGUI() {
        createGUI();

    }

    public void createGUI() {
        File tileFile = new File("tileimage.png");
        File flagFile = new File("flagimage.png");
        File questionFile = new File("questionimage.png");
        File mineFile = new File("mineimage.png");
        File cleartileFile = new File("cleartileimage.png");
        File tile1File = new File("tilenumber1.png");
        File tile2File = new File("tilenumber2.png");
        File tile3File = new File("tilenumber3.png");
        File tile4File = new File("tilenumber4.png");
        File tile5File = new File("tilenumber5.png");
        File tile6File = new File("tilenumber6.png");
        File tile7File = new File("tilenumber7.png");
        File tile8File = new File("tilenumber8.png");
        try {
            tile = ImageIO.read(tileFile);
            flag = ImageIO.read(flagFile);
            question = ImageIO.read(questionFile);
            mine = ImageIO.read(mineFile);
            clearedtile = ImageIO.read(cleartileFile);
            tilenumber1 = ImageIO.read(tile1File);
            tilenumber2 = ImageIO.read(tile2File);
            tilenumber3 = ImageIO.read(tile3File);
            tilenumber4 = ImageIO.read(tile4File);
            tilenumber5 = ImageIO.read(tile5File);
            tilenumber6 = ImageIO.read(tile6File);
            tilenumber7 = ImageIO.read(tile7File);
            tilenumber8 = ImageIO.read(tile8File);
        }
        catch(IOException e) {
            ;
        }
        
        // Create Java Window
        window = new JFrame("Minesweeper");
        window.setBounds(100, 40, gridSize * pixelSize + 45, gridSize * pixelSize + 200);
        //window.setBounds(100, 100, 445, 600);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create GUI elements

        // JPanel to draw in
        drawingPanel = new MyDrawingPanel();
        drawingPanel.setBounds(20, 20, gridSize * pixelSize, gridSize * pixelSize);
        drawingPanel.setBorder(BorderFactory.createEtchedBorder());
        pixelSize = (drawingPanel.getWidth() / gridSize);
        
        int xTime = (int)(window.getWidth() / 4.0 - 50);
        int yTime = (int)(window.getHeight() - (window.getHeight() * (1.0 / 4)));
        timeArea = new JLabel();
        timeArea.setBounds(xTime, yTime, 100, 50);
        timeArea.setHorizontalAlignment((int)JLabel.CENTER_ALIGNMENT);
        timeArea.setText("" + 0);
        timeArea.setBorder(BorderFactory.createTitledBorder("Time Elapsed"));
        
        int xMines = (int)(window.getWidth() * (3.0 / 4) - 60);
        int yMines = (int)(window.getHeight() - (window.getHeight() * (1.0 / 4)));
        minesArea = new JLabel();
        minesArea.setBounds(xMines, yMines, 120, 50);
        minesArea.setHorizontalAlignment((int)JLabel.CENTER_ALIGNMENT);
        minesArea.setText("" + numMines);
        minesArea.setBorder(BorderFactory.createTitledBorder("Mines Remaining"));
        
        // JMenuBar
        JMenuBar bar = new JMenuBar();
        
        // JMenu
        JMenu menuFile = new JMenu("Game");
        JMenu menuOptions = new JMenu("Options");
        JMenu menuHelp = new JMenu("Help");
        
        // JMenuItem
        JMenuItem fileNew = new JMenuItem("New Game", 'n');
        JMenuItem fileExit = new JMenuItem("Exit", 'e');
        JMenuItem optionsMines = new JMenuItem("Total Mines", 't');
        JMenuItem optionsAutoplay = new JMenuItem("Auto Play", 'a');
        JMenuItem helpAbout = new JMenuItem("About", 'a');
        JMenuItem helpHowTo = new JMenuItem("How to play", 'h');
        
        // Add JMenuItems to JMenus
        menuFile.add(fileNew);
        menuFile.add(fileExit);
        menuOptions.add(optionsMines);
        menuOptions.add(optionsAutoplay);
        menuHelp.add(helpAbout);
        menuHelp.add(helpHowTo);
        
        // Add JMenus to JMenuBar
        bar.add(menuFile);
        bar.add(menuOptions);
        bar.add(menuHelp);

        // Add GUI elements to the Java window's ContentPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        
        mainPanel.add(drawingPanel);

        mainPanel.add(timeArea);
        mainPanel.add(minesArea);
        
        timer = new Timer(1000, this);
        autoplayTimer = new Timer(1000, this);
        
        fileNew.addActionListener(this);
        fileExit.addActionListener(this);
        optionsMines.addActionListener(this);
        optionsAutoplay.addActionListener(this);
        helpAbout.addActionListener(this);
        helpHowTo.addActionListener(this);
        
        drawingPanel.addMouseListener(this);
        drawingPanel.addMouseMotionListener(this);
        
        topArray = new Color[gridSize][gridSize];
        for(int i = 0; i < topArray.length; i++) {
            for(int j = 0; j < topArray[0].length; j++) {
                topArray[i][j] = color;
            }
        }
        
        bottomArray = new TileObject[gridSize][gridSize];
        
        loadBottomArray();
        
        try {
            aboutpane = new JEditorPane(new URL("file:about.html"));
            aboutpane.setEditable(false);
            howtopane = new JEditorPane(new URL("file:howto.html"));
            howtopane.setEditable(false);
        }
        catch (IOException e) {
            ;
        }
        
        about = new JScrollPane();
        about.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        about.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        about.setBounds(400, 400, 200, 150);
        about.getViewport().add(aboutpane);
        
        howto = new JScrollPane();
        howto.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        howto.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        howto.setBounds(400, 400, 500, 600);
        howto.setPreferredSize(new Dimension(500, 600));
        howto.getViewport().add(howtopane);
        
        win = new JLabel("Congratulations!  You win!");
        lose = new JLabel("Sorry!  You lose!");
        
        window.getContentPane().add(mainPanel);
        window.setJMenuBar(bar);

        // Let there be light
        window.setVisible(true);

    }



    public void actionPerformed(ActionEvent e) {
        
        if(e.getSource() == autoplayTimer) {
            autoPlay();
        }

        if(e.getActionCommand() != null) {
            if(e.getActionCommand().equals("New Game")) {
                clearDraw();
                timer.stop();
            }
            
            if(e.getActionCommand().equals("Exit")) {
                System.exit(0);
            }
            
            if(e.getActionCommand().equals("About")) {
                JOptionPane.showMessageDialog(null, about, "About", JOptionPane.PLAIN_MESSAGE);
            }
            if(e.getActionCommand().equals("How to play")) {
                JOptionPane.showMessageDialog(null, howto, "How to play", JOptionPane.PLAIN_MESSAGE);
            }
            
            if(e.getActionCommand().equals("Total Mines")) {
                JTextField fieldMines = new JTextField("(Enter total mines here)");
                JOptionPane.showMessageDialog(null, fieldMines, "Total Mines", JOptionPane.PLAIN_MESSAGE);
                totalMines = Integer.parseInt(fieldMines.getText());
                numMines = totalMines;
                
                clearDraw();
            }
            
            if(e.getActionCommand().equals("Auto Play")) {
                autoplayTimer.start();
            }
            
        }
        
        timeArea.setText("" + time);
        time++;

    }
    
    public void autoPlay() {
        if(gameEnd() == true) {
            autoplayTimer.stop();
        }

        if((double)tilesRevealed() >= (double)0.20 * gridSize * gridSize) {
            // flag mines
            if(flagStage == true) {
                for(int i = 0; i < bottomArray.length; i++) {
                    for(int j = 0; j < bottomArray[0].length; j++) {
                        int blankCount = 0;
                        int flagCount = 0;
                        if(topArray[i][j] == transparent && bottomArray[i][j].getNumMines() != 0) {
                            blankCount += countTiles(i, j, Color.lightGray);
                            flagCount += countTiles(i, j, Color.red);
                            if(blankCount != 0 && blankCount + flagCount == bottomArray[i][j].getNumMines()) {
                                flagTiles(i, j);
                                guessCount = 0;
                            }
                            else if(guessCount > 1 && blankCount + flagCount > bottomArray[i][j].getNumMines()) {
                                System.out.println("Guessing!");
                                guessFlag(i, j);
                                guessCount = 0;
                            }
                        }
                        else if(guessCount > 2) {
                            if(topArray[i][j] != transparent && topArray[i][j] != Color.red) {
                                topArray[i][j] = transparent;
                            }
                        }
                    }
                }
                System.out.println("Flagging tiles");
                
                drawingPanel.repaint();
                
                guessCount++;
            
                flagStage = false;
                
                System.out.println("-------------------------------");
            }
            
            // click tiles
            else {
                for(int i = 0; i < topArray.length; i++) {
                    for(int j = 0; j < topArray[0].length; j++) {
                        if(topArray[i][j] == transparent) {
                            if(countTiles(i, j, Color.red) == bottomArray[i][j].getNumMines()) {
                                clickTiles(i, j);
                            }
                        }
                    }
                }
                System.out.println("Clicking safe tiles");
                drawingPanel.repaint();
                flagStage = true;
                System.out.println("-------------------------------");
            }
            
        }
        else {
            Random r = new Random();
            
            int x = r.nextInt(gridSize);
            int y = r.nextInt(gridSize);
            while(topArray[x][y] == transparent) {
                x = r.nextInt(gridSize);
                y = r.nextInt(gridSize);
            }
            System.out.println("Choosing random point at (" + x + ", " + y + ")");
            System.out.println("-------------------------------");
            sweepMines(topArray, x, y);
            drawingPanel.repaint();
            if((double)tilesRevealed() >= (double)0.20 * gridSize * gridSize)
                flagStage = true;
        }
        
        checkWin();
    }
    
    public boolean gameEnd() {
        int count = 0;
        for(int i = 0; i < topArray.length; i++) {
            for(int j = 0; j < topArray[0].length; j++) {
                if(topArray[i][j] == transparent) {
                    count++;
                }
            }
        }
        
        return count == gridSize*gridSize;
    }
    
    public int tilesRevealed() {
        int count = 0;
        for(int i = 0; i < topArray.length; i++) {
            for(int j = 0; j < topArray[0].length; j++) {
                if(topArray[i][j] == transparent) {
                    count++;
                }
            }
        }
        return count;
    }
    
    public void setGridSize(int n) {
        gridSize = n;
        System.out.println("New grid size: " + gridSize);
    }
    
    public void mouseClicked(MouseEvent e) {
        //Graphics g = drawingPanel.getGraphics();
        if(e.getX() % pixelSize >= 0) {
            x = e.getX() - (e.getX() % pixelSize);
        }
        if(e.getY() % pixelSize >= 0) {
            y = e.getY() - (e.getY() % pixelSize);
        }
        if(e.getButton() == MouseEvent.BUTTON1) {
            if(timer.isRunning() == false) {
                timer.start();
            }
            
            if(topArray[x / pixelSize][y / pixelSize] != Color.red && topArray[x / pixelSize][y / pixelSize] != Color.blue) {
                sweepMines(topArray, x / pixelSize, y / pixelSize);
            }
            
            checkWin();
        }
        else if(e.getButton() == MouseEvent.BUTTON3) {
            if(topArray[x / pixelSize][y / pixelSize] != transparent) {
                if(topArray[x / pixelSize][y / pixelSize] == Color.blue) {
                    topArray[x / pixelSize][y / pixelSize] = darkGray;
                }
                else if(topArray[x / pixelSize][y / pixelSize] == Color.red) {
                    topArray[x / pixelSize][y / pixelSize] = Color.blue;
                    numMines++;
                }
                else {
                    topArray[x / pixelSize][y / pixelSize] = Color.red;
                    numMines--;
                }
                minesArea.setText("" + numMines);
            }
        }
        //g.fillRect(x + 1, y + 1, 19, 19);
        drawingPanel.repaint();
    }
    
    public void sweepMines(Color[][] grid, int row, int col) {
        if(bottomArray[row][col].getColor() == Color.red && topArray[row][col] != Color.red) {
            revealBoard();
            drawingPanel.repaint();
            timer.stop();
            autoplayTimer.stop();
            JOptionPane.showMessageDialog(null, lose, "You lose!", JOptionPane.PLAIN_MESSAGE);
        }
        else if(bottomArray[row][col].getNumMines() != 0) {
            grid[row][col] = transparent;
        }
        else if(bottomArray[row][col].getColor() == darkGray && grid[row][col] != transparent &&
        bottomArray[row][col].getNumMines() == 0) {
            grid[row][col] = transparent;
            
            if(row < gridSize - 1) {
                sweepMines(grid, row + 1, col);
            }
            if(row < gridSize - 1 && col < gridSize - 1) {
                sweepMines(grid, row + 1, col + 1);
            }
            if(col < gridSize - 1) {
                sweepMines(grid, row, col + 1);
            }
            if(row > 0 && col < gridSize - 1) {
                sweepMines(grid, row - 1, col + 1);
            }
            if(row > 0) {
                sweepMines(grid, row - 1, col);
            }
            if(row > 0 && col > 0) {
                sweepMines(grid, row - 1, col - 1);
            }
            if(col > 0) {
                sweepMines(grid, row, col - 1);
            }
            if(row < gridSize - 1 && col > 0) {
                sweepMines(grid, row + 1, col - 1);
            }
        }
        else {
            ;
        }
    }
    
    public void loadBottomArray() {
        for(int i = 0; i < bottomArray.length; i++) {
            for(int j = 0; j < bottomArray[0].length; j++) {
                bottomArray[i][j] = new TileObject(darkGray, 0, true);
            }
        }
        
        Random r = new Random();
        for(int i = 0; i < totalMines; i++) {
            int x = r.nextInt(gridSize);
            int y = r.nextInt(gridSize);
            while(bottomArray[x][y].getColor() == Color.red) {
                x = r.nextInt(gridSize);
                y = r.nextInt(gridSize);
            }
            bottomArray[x][y].setColor(Color.red);
        }

        for(int i = 0; i < bottomArray.length; i++) {
            for(int j = 0; j < bottomArray[0].length; j++) {
                bottomArray[i][j].setNumMines(countNeighbors(i, j, bottomArray));
            }
        }
    }
    
    public void revealBoard() {
        for(int i = 0; i < topArray.length; i++) {
            for(int j = 0; j < topArray[0].length; j++) {
                topArray[i][j] = transparent;
            }
        }
    }
    
    public void checkWin() {
        int count = 0;
        for(int i = 0; i < topArray.length; i++) {
            for(int j = 0; j < topArray[0].length; j++) {
                if(topArray[i][j] == transparent) {
                    count++;
                }
            }
        }
        
        if(count == gridSize*gridSize - totalMines) {
            revealBoard();
            drawingPanel.repaint();
            timer.stop();
            autoplayTimer.stop();
            JOptionPane.showMessageDialog(null, win, "You win!", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    public int countNeighbors(int row, int col, TileObject[][] array) {
        int count = 0;
        for(int i = 0; i < 8; i++) {
            if(row + vertical[i] >= 0 && row + vertical[i] < gridSize && 
            col + horizontal[i] >= 0 && col + horizontal[i] < gridSize &&
            array[row + vertical[i]][col + horizontal[i]].getColor() != darkGray) {
                count++;
            }
        }
        return count;
    }
    
    public int countTiles(int row, int col, Color color) {
        int count = 0;
        for(int i = 0; i < 8; i++) {
            if(topArray[row][col] == transparent && bottomArray[row][col].getNumMines() != 0) {
                if(row + vertical[i] >= 0 && row + vertical[i] < gridSize && 
                col + horizontal[i] >= 0 && col + horizontal[i] < gridSize) {
                    if(topArray[row + vertical[i]][col + horizontal[i]] == color) {
                        count++;
                    }
                }
            }
        }
        return count;
    }
    
    public void flagTiles(int row, int col) {
        int count = 0;
        for(int i = 0; i < 8; i++) {
            if(row + vertical[i] >= 0 && row + vertical[i] < gridSize && 
            col + horizontal[i] >= 0 && col + horizontal[i] < gridSize &&
            topArray[row + vertical[i]][col + horizontal[i]] != transparent &&
            topArray[row + vertical[i]][col + horizontal[i]] != Color.red) {
                topArray[row + vertical[i]][col + horizontal[i]] = Color.red;
                count++;
            }
        }
        numMines -= count;
        minesArea.setText("" + numMines);
    }
    
    public void guessFlag(int row, int col) {
        for(int i = 0; i < 8; i++) {
            if(row + vertical[i] >= 0 && row + vertical[i] < gridSize && 
            col + horizontal[i] >= 0 && col + horizontal[i] < gridSize &&
            topArray[row + vertical[i]][col + horizontal[i]] != transparent &&
            topArray[row + vertical[i]][col + horizontal[i]] != Color.red) {
                topArray[row + vertical[i]][col + horizontal[i]] = Color.red;
                break;
            }
        }
        numMines--;
        minesArea.setText("" + numMines);
    }
    
    public void clickTiles(int row, int col) {
        for(int i = 0; i < 8; i++) {
            if(row + vertical[i] >= 0 && row + vertical[i] < gridSize && 
            col + horizontal[i] >= 0 && col + horizontal[i] < gridSize &&
            topArray[row + vertical[i]][col + horizontal[i]] != transparent &&
            topArray[row + vertical[i]][col + horizontal[i]] != Color.red) {
                sweepMines(topArray, row + vertical[i], col + horizontal[i]);
            }
        }
    }
    
    public void mouseEntered(MouseEvent e) {
    
    }
    
    public void mouseExited(MouseEvent e) {
    
    }
    
    public void mousePressed(MouseEvent e) {
        
    }
    
    public void mouseReleased(MouseEvent e) {
        
    }
    
    public void mouseDragged(MouseEvent e) {

    }

    public void mouseMoved(MouseEvent e) {
        
    }

    public void clearDraw() {
        
        for(int i = 0; i < topArray.length; i++) {
            for(int j = 0; j < topArray[0].length; j++) {
                topArray[i][j] = color;
            }
        }

        loadBottomArray();
        
        numMines = totalMines;
        minesArea.setText("" + numMines);
        
        time = 0;
        timeArea.setText("" + time);
        
        drawingPanel.repaint();
        drawingPanel.paintComponent(drawingPanel.getGraphics());

    }


    private class MyDrawingPanel extends JPanel {

        // Not required, but gets rid of the serialVersionUID warning.  Google it, if desired.
        static final long serialVersionUID = 1234567890L;

        public void paintComponent(Graphics g) {

            g.setColor(Color.white);
            g.fillRect(2, 2, this.getWidth()-2, this.getHeight()-2);

            g.setColor(new Color(78,78,87));
            for (int x = 0; x < this.getWidth(); x += 20)
                g.drawLine(x, 0, x, this.getHeight());

            for (int y = 0; y < this.getHeight(); y += 20)
                g.drawLine(0, y, this.getWidth(), y);
            
            for(int i = 0; i < bottomArray.length; i++) {
                for(int j = 0; j < bottomArray[0].length; j++) {
                    if(bottomArray[i][j].getColor() == Color.red) {
                        g.drawImage(mine, (i*pixelSize) + 1, (j*pixelSize) + 1, pixelSize - 1, pixelSize - 1, null);
                    }
                    else if(bottomArray[i][j].getNumMines() == 0) {
                        g.drawImage(clearedtile, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                    }                    
                    else if(bottomArray[i][j].getNumMines() != 0) {
                        if(bottomArray[i][j].getNumMines() == 1) {
                            g.drawImage(tilenumber1, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                        }
                        else if(bottomArray[i][j].getNumMines() == 2) {
                            g.drawImage(tilenumber2, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                        }
                        else if(bottomArray[i][j].getNumMines() == 3) {
                            g.drawImage(tilenumber3, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                        }
                        else if(bottomArray[i][j].getNumMines() == 4) {
                            g.drawImage(tilenumber4, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                        }
                        else if(bottomArray[i][j].getNumMines() == 5) {
                            g.drawImage(tilenumber5, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                        }
                        else if(bottomArray[i][j].getNumMines() == 6) {
                            g.drawImage(tilenumber6, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                        }
                        else if(bottomArray[i][j].getNumMines() == 7) {
                            g.drawImage(tilenumber7, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                        }
                        else if(bottomArray[i][j].getNumMines() == 8) {
                            g.drawImage(tilenumber8, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                        }
                    }
                }
            }
            
            for(int i = 0; i < topArray.length; i++) {
                for(int j = 0; j < topArray[0].length; j++) {
                    if(topArray[i][j] == Color.red) {
                        g.drawImage(flag, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                    }
                    else if(topArray[i][j] == Color.blue) {
                        g.drawImage(question, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                    }
                    else if(topArray[i][j] != transparent) {
                        g.drawImage(tile, (i*pixelSize), (j*pixelSize), pixelSize, pixelSize, null);
                    }
                }
            }
            
        }
    }
}

class TileObject {
    Color myColor;
    int myNum;
    boolean isClear;
    public TileObject(Color color, int num, boolean isBlank) {
        myColor = color;
        myNum = num;
        isClear = isBlank;
    }
    
    public Color getColor() {
        return myColor;
    }
    
    public int getNumMines() {
        return myNum;
    }
    
    public boolean getClear() {
        return isClear;
    }
    
    public void setColor(Color col) {
        myColor = col;
    }
    
    public void setNumMines(int num) {
        myNum = num;
    }
    
}