package kann.ch7.p72;

/*
 * TITLE: Program 7.2
 *
 * @(#)SimpleAnimator1 2002/07/21
 * @author Charles W. Kann III
 *
 * Copyright (c) 2002 CRC Press
 * All Rights Reserved.
 *
 * Permission to use, copy, modify, and distribute this
 * software and its documentation for NON-COMMERCIAL purposes
 * and without fee is hereby granted provided that this
 * copyright notice appears in all copies.
 *
 * THE AUTHOR MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE
 * SUITABILITY OF THE SOFTWARE, EITHER EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. THE AUTHOR SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR
 * DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 *    Purpose:	This class adds a control panel to the class
 * 		in Program 7.l.  It shows that the paint 
 *		method runs in the GUI thread, and why doing
 *		a sleep in the GUI thread is not a good idea.
 */
@SuppressWarnings("serial")
public class SimpleAnimator1 extends JFrame {

    private Ball ball = new Ball(Color.red, 20, 20, 450, 450);
    private int sleepTime = 100;

    /**
     *  Constructor that builds the simple GUI with
     *  a scrollbar to keep track of speed and an exit
     *  button.  The ball object is then added to the
     *  Frame to be animated. 
     */
    public SimpleAnimator1() {
        Container con = this.getContentPane();
 
        // ======================================== 
        // Control Panel.  It consists of the button
        // and scrollbar defined below.
        JPanel controlPanel = new JPanel();

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                 System.exit(0);
            }
        });
        controlPanel.add(exitButton);

        controlPanel.add(new JLabel("Animator Speed"));
        final JScrollBar speedControl = new JScrollBar(JScrollBar.HORIZONTAL,
                4900, 10, 50, 5000);
        speedControl.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
            	System.out.println(e.getValue());
            sleepTime = 5000 - e.getValue();
            }
        });
        controlPanel.add(speedControl);

        con.add(controlPanel, BorderLayout.SOUTH);
        // =========================================

        // Add the ball to the JFrame.
        con.add(ball, BorderLayout.CENTER);

    }

    /**
     *  This method simply dispatches paint events to the
     *  ball object, waits for 0.1 seconds, and does it
     *  again. 
     */
    public void paint(Graphics g) {
        super.paint(g);
        try {
            Thread.sleep(sleepTime);
            repaint();
        } catch(InterruptedException e) {
        }
    }

    /**
     *  Start the program.
     */

    @SuppressWarnings("deprecation")
	public static void main(String args[]) {
        //Display the Frame.
        SimpleAnimator1 sa = new SimpleAnimator1();
        sa.setSize(500,500);
        sa.show();
    }
}
 
/**
 *    Purpose:	This class implements a ball JPanel on
 *  		which it can draw a ball to displayed on 
 *		the JFrame it has been added to.  It 
 *		animates the ball by moving it on the
 *		screen a little each time the paint
 *		method is called.
 */
@SuppressWarnings("serial")
class Ball extends JPanel {
    Color ballColor;
    int xStart, yStart, xLimit, yLimit;
    Path myPath;

    /**
     *   Constructor that sets the ball up to run, and
     *   sets the limits for the size of the screen area
     *   it is to run on.  It also sets the initial path
     *   to move the ball.
     */
    public Ball(Color ballColor, int xStart, int yStart,
                int xLimit, int yLimit) {
        this.ballColor = ballColor;
        this.xStart = xStart;
        this.yStart = yStart;
        this.xLimit = xLimit;
        this.yLimit = yLimit;

        myPath = new StraightLinePath(xStart, yStart, xLimit, yLimit, 50);
    }

    /**
     *    This method draws the ball at the correct position 
     *    on the screen given by the path.  When the end of 
     *    path is reached, a new path is created to move the
     *    ball in the opposite direction.
     */
    public void paint(Graphics g) {
        super.paint(g);
        Point pos = myPath.nextPosition();

        g.setColor(ballColor);
        g.fillOval((int)pos.getX(), (int)pos.getY(), 15, 15);

        if (! myPath.hasMoreSteps()) {
            if (pos.getX() == xStart)
                myPath = new StraightLinePath(xStart, yStart, xLimit, yLimit, 50);
            else
                myPath = new StraightLinePath(xLimit, yLimit, xStart, yStart, 50);
        }
    }
}
