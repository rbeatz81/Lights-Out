package lightsout;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


/**
 * 
 * @author Rockie Beatty
 */
@SuppressWarnings("serial")
public class LightsOut extends JPanel implements MouseListener, MouseMotionListener
{
    private static final int ROWS = 5;
    private static final int COLS = ROWS;
    private JButton[][] buttonGrid = new JButton[ROWS][COLS];
    private boolean inManual = false;
    private int moves;

    /**
     * Launches the LightsOut board
     */
    public static void main (String[] args)
    {

        SwingUtilities.invokeLater( () -> new LightsOut());
    }

    /**
     * Lays out the LightsOut board
     */
    public LightsOut ()
    {
        JFrame frame = new JFrame();
        
        frame.setTitle("5x5 Lights Out - Rockie Beatty");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setContentPane(makeContents());
        frame.setVisible(true);
        newGame();

    }

    /**
     * Creates and returns a collection of components to display in the GUI
     */
    public JPanel makeContents ()
    {
        // Create a 5x5 grid of buttons

        JPanel grid = new JPanel();
        grid.setLayout(new GridLayout(5, 5));

        // Lay out two buttons left to right
        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());
        JButton btn_NewGame = new JButton("New Game");

        
        controls.add(btn_NewGame);
        JButton btn_Manual = new JButton("Enter Manual Set Up");
        controls.add(btn_Manual);
        JLabel theseMoves = (new JLabel("Number of Moves:  " + moves));
        

        ActionListener buttonListener = new ActionListener()
        {

            @Override
            public void actionPerformed (ActionEvent e)
            {
                JButton selectedBtn = (JButton) e.getSource();

                //if new game is clicked
                if (selectedBtn == btn_NewGame)
                {
                    moves = 0;
                    theseMoves.setText("Number of Moves:  " + moves);
                    newGame();
                    

                }
                //if Manual Mode is clicked
                if (selectedBtn == btn_Manual)
                {

                    //swaps the mode from in and out
                    inManual = manualSetup(inManual);
                    if (inManual)
                    {
                        //resets move counter if in manual mode & changes button text
                        moves = 0;
                        theseMoves.setText("Number of Moves:  " + moves);
                        btn_Manual.setText("Exit Manual Setup");

                    }
                    if (!inManual)
                    {
                        //changes button text
                        btn_Manual.setText("Enter Manual Setup");
                    }

                }

                for (int row = 0; row < buttonGrid.length; row++)
                {
                    for (int col = 0; col < buttonGrid[row].length; col++)
                    {
                        if (buttonGrid[row][col] == selectedBtn)
                        {
                            //if in normal mode, change color of box clicked and its neighbors and increase moves, check if a winner
                            if (!inManual)
                            {
                                selectedBtn.setBackground(changeColor(selectedBtn.getBackground()));
                                changeNeighborsColor(row, col);
                                ++moves;
                                theseMoves.setText("Number of Moves:  " + moves);
                                
                                isWinner();
                            }
                            //in manualmode do not impact neighbor tiles and do not count moves
                            if (inManual)
                            {
                                selectedBtn.setBackground(changeColor(selectedBtn.getBackground()));
                            }
                        }
                    }
                }
            }
        };

        //add buttons
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                JButton toAdd = new JButton("");
                toAdd.setBackground(Color.white);
                buttonGrid[i][j] = (toAdd);
                buttonGrid[i][j].addActionListener(buttonListener);
                grid.add(toAdd);
            }
        }
        //add action listener
        btn_NewGame.addActionListener(buttonListener);
        btn_Manual.addActionListener(buttonListener);

        // Lay out two labels top to bottom
        JPanel labels = new JPanel();
        labels.setLayout(new BoxLayout(labels, BoxLayout.X_AXIS));
//        labels.add(new JLabel("Target:  ")); // number of blacks
        labels.add(theseMoves);
        


        // Compose everything into the root panel
        JPanel root = new JPanel();

        BorderLayout manager = new BorderLayout();

        root.setLayout(manager);

        root.add(controls, "North");
        root.add(labels, "South");
        root.add(grid, "Center");

        // Return the root panel
        return root;
    }

    /**
     * Returns the opposite color
     */
    public Color changeColor (Color currentColor)
    {
        if (currentColor == Color.white)
        {
            currentColor = Color.black;
            return currentColor;
        }

        currentColor = Color.white;
        return currentColor;

    }

    /**
     * Returns true if no white buttons are found
     */
    public boolean isWinner ()
    {

        JButton btn = new JButton();

        for (int i = 0; i < 5; ++i)
        {

            for (int j = 0; j < 5; ++j)
            {

                btn = buttonGrid[i][j];
                if (btn.getBackground() == Color.white)
                {
                    return false;

                }
            }

        }
        JOptionPane.showMessageDialog(null, "You Have Won The Game!!");
        return true;

    }

    /**
     * switches the currents status of inMan. if false, returns true
     * 
     * When manual setup button is clicked, the user can change the color of any tile manually without setting off all
     * of the neighbors when the Exit btn is clicked then it returns to normal
     */
    public boolean manualSetup (boolean inMan)
    {
        if (inMan)
        {
            return false;
        }
        return true;

    }

    /**
     * Sets up the board to play a new game, it is random generated and must be possible to win. start all lights off then
     * initiates 15 random moves
     */
    public void newGame ()
    {
        JButton btn = new JButton();
        for (int i = 0; i < 5; ++i)
        {

            for (int j = 0; j < 5; ++j)
            {

                btn = buttonGrid[i][j];
                btn.setBackground(Color.white);
            }

        }
        
        randomMove();

    }

    /**
     * changes random boxes to off. repeats 15x
     */
    public void randomMove ()
    {
        int row;
        int col;
        JButton btn = new JButton();
        Random r = new Random();

        for (int index = 0; index < 15; ++index)
        {
            row = r.nextInt(5);
            col = r.nextInt(5);
            btn = buttonGrid[row][col];

            btn.setBackground(changeColor(btn.getBackground()));
            changeNeighborsColor(row, col);

           

        }

    }

    /**
     * Returns the opposite color that the neighbor squares had
     */
    public void changeNeighborsColor (int row, int col)
    {
        JButton btn = new JButton();
        // top row
        if (row == 0)
        {
            // left corner
            if (col == 0)
            {
                btn = buttonGrid[0][1];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[1][0];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }
            // top mid left
            if (col == 1)
            {
                btn = buttonGrid[0][0];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[1][1];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[0][2];
                btn.setBackground(changeColor(btn.getBackground()));
                return;
            }
            // top mid
            if (col == 2)
            {
                btn = buttonGrid[0][1];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[1][2];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[0][3];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }
            // top mid right
            if (col == 3)
            {
                btn = buttonGrid[0][2];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[1][3];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[0][4];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }
            // right corner
            if (col == 4)
            {
                btn = buttonGrid[0][3];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[1][4];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }

        }
        // bottom row
        if (row == 4)
        {
            // left corner
            if (col == 0)
            {
                btn = buttonGrid[3][0];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[4][1];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }
            // bottom mid left
            if (col == 1)
            {
                btn = buttonGrid[4][0];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[3][1];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[4][2];
                btn.setBackground(changeColor(btn.getBackground()));
                return;
            }
            // bottom mid
            if (col == 2)
            {
                btn = buttonGrid[4][1];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[3][2];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[4][3];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }
            // bottom mid right
            if (col == 3)
            {
                btn = buttonGrid[4][2];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[3][3];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[4][4];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }
            // bottom right corner
            if (col == 4)
            {
                btn = buttonGrid[3][4];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[4][3];
                btn.setBackground(changeColor(btn.getBackground()));
                return;
            }

        }

        // Right column (not including corners)
        if (col == 4)
        {

            // Right Upper
            if (row == 1)
            {
                btn = buttonGrid[0][4];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[1][3];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[2][4];
                btn.setBackground(changeColor(btn.getBackground()));
                return;
            }
            // right mid
            if (row == 2)
            {
                btn = buttonGrid[1][4];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[2][3];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[3][4];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }
            // right lower
            if (row == 3)
            {
                btn = buttonGrid[2][4];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[3][3];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[4][4];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }

        }
        // Left column (not including corners)
        if (col == 0)
        {

            // Right Upper
            if (row == 1)
            {
                btn = buttonGrid[0][0];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[1][1];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[2][0];
                btn.setBackground(changeColor(btn.getBackground()));
                return;
            }
            // right mid
            if (row == 2)
            {
                btn = buttonGrid[1][0];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[2][1];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[3][0];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }
            // right lower
            if (row == 3)
            {
                btn = buttonGrid[2][0];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[3][1];
                btn.setBackground(changeColor(btn.getBackground()));
                btn = buttonGrid[4][0];
                btn.setBackground(changeColor(btn.getBackground()));
                return;

            }

        }

        // any center pieces
        else
        {
            btn = buttonGrid[row - 1][col];
            btn.setBackground(changeColor(btn.getBackground()));
            btn = buttonGrid[row + 1][col];
            btn.setBackground(changeColor(btn.getBackground()));
            btn = buttonGrid[row][col + 1];
            btn.setBackground(changeColor(btn.getBackground()));
            btn = buttonGrid[row][col - 1];
            btn.setBackground(changeColor(btn.getBackground()));
            return;

        }

    }

    @Override
    public void mouseDragged (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseMoved (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseClicked (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited (MouseEvent e)
    {
        // TODO Auto-generated method stub

    }
}
