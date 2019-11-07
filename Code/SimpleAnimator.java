package kann.ch7.p71;
/*
 * TITLE: Program 7.1b
 *
 * @(#)SimpleAnimator 2002/07/21
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
import javax.swing.*;

/**
 *   Purpose: 	This class creates a JFrame that an animation
 *   		can be run on.  It then adds a ball object to
 *		the JFrame so that it can be drawn at a regular
 *		interval, and then does a sleep and repaint
 *		call in the paint method so that the paint
 *		method is called over and over, calling the
 *		balls paint method, and thus producing the
 *		animation.
 *
 *		This animator is not generic, and has no
 *		animation controls.
 */
@SuppressWarnings("serial")
public class SimpleAnimator extends JFrame {

    Ball ball = new Ball(Color.red, 20, 20, 450, 450);

    /**
     *  Constructor which adds the ball, and then sets the size
     *  of the frame and shows it on the screen.
     */
    public SimpleAnimator() {
        Container con = this.getContentPane();
        con.add(ball);
    }

    /**
     *  This method simply dispatches paint events to the
     *  ball object, waits for 0.1 seconds, and does it
     *  again. 
     */
    public void paint(Graphics g) {
        super.paint(g);
        try {
            Thread.sleep(50);
            repaint();
        } catch(InterruptedException e) {
        }
    }

    /**
     *  Start the program.
     */
    @SuppressWarnings("deprecation")
	public static void main(String args[]) {
        SimpleAnimator sa = new SimpleAnimator();
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

