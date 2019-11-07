package kann.ch7.p74;

/*
 * TITLE: Program 7.4
 *
 * @(#)Animator 2002/07/21
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
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;

/** 
 *    Purpose:	This class extends the Generic Animator of 
 *		Program 7.3c to add a control panel to 
 *		control the speed, and add some buttons.
 */

@SuppressWarnings("serial")
public class Animator extends JPanel implements Runnable {

    @SuppressWarnings("rawtypes")
	private Vector elementsToDraw = new Vector();
    private long sleepTime = 100;
    private boolean animatorStopped = true, firstTime = true;
    private ControlFrame controlFrame;
    private JFrame animFrame;

    /**
     *  Constructor that creates the JFrame for the animator.  Note
     *  the JFrame is shown in the show() method because this starts
     *  the GUI thread.  Starting the thread in the constructor
     *  can lead to a race condition.
     */
    public Animator() {
        // Create the control Frame.
        controlFrame = new ControlFrame(this);

        // set up the frame to draw in.
        animFrame = new JFrame("Generic Animator");
        Container animContainer = animFrame.getContentPane();
        animContainer.add(this);
        animFrame.setSize(700, 450);
        animFrame.setLocation(0,100);
    }

    /**
     *  setVisible is called to display or hide the animator.
     *  Note that only display = true is implemented, and this
     *  function only works at this point if it is called once.
     *  It is left as an exercise to implement it correctly.
     *  If display = false, the Control Thread needs to be
     *  suspended.  If display = true, the control thread
     *  should be started only the first time, after that it
     *  should be unsuspended.  This can be accomplished as
     *  using control variables in the paint method for Program
     *  7.4 and after, and should not be done using the suspend
     *  and resume methods.
     */
    public void setVisible(boolean display) {
        if (display == true) {
            if (firstTime) {
                firstTime = false;

                // Show the control Frame
                controlFrame.setVisible(true);

                // Show the animator.  This starts the GUI thread.
                animFrame.setVisible(true);

                // Put the animator in another thread so that the
                // calling object can continue.
                (new Thread(this)).start();
            }
        }
    }


    /** Thread that runs the animator.  This thread sleeps for some 
     *  amount of time specified by sleepTime, then calls repaint 
     *  which forces a call to the paint method, but in the GUI thread.
     *  Note that the animatorStopped button allows the animator to 
     *  single step and pause the animation.  The notify is done in 
     *  the control frame from the button.
     */
    public void run() {
        while (true) {
            try {
                synchronized(this) {
                    if (animatorStopped == true) {
                        wait();
                    }
                }
                if (animatorStopped != true)
                    Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                System.out.println("Program Interrupted");
                System.exit(0);
            }
            repaint();
        }
    }

    /**  
     *  The paint method redraws all the objects registered with this
     *  animator.  It is run in the GUI thread.  The repaint 
     *  command causes this to be called, and it passes the graphics 
     *  object g to each of the registered objects to have them draw 
     *  themselves.
     */
    @SuppressWarnings("rawtypes")
	public void paint(Graphics g) {
        super.paint(g);
        Enumeration e = elementsToDraw.elements();
        while (e.hasMoreElements())
            ((Drawable) e.nextElement()).draw(g);
    } 

    /** 
     *  addElement adds each drawable to the vector for use by the
     *   DrawElements method.
     */
    @SuppressWarnings("unchecked")
	public void addDrawable(Drawable d) {
        elementsToDraw.addElement(d);
    }

    /** 
     *  removeElement is used to remove drawables from the vector.  
     */
    public void removeDrawable(Drawable d) {
        elementsToDraw.removeElement(d);
    }

    /**
        This is an inner class which implements the control panel for
        the application.  It is not important to the behavior of the
        Animator component, which can be understood without understanding
        this control panel.

        Note that there is no need to synchronize any of these methods
        because the values they change can only be changed in the GUI
        thread, and are read only elsewhere, so no race conditions
        exist.
    */
    private class ControlFrame  {
        Animator ca;
        static final int RUNNING = 0;
        static final int WAITING = 1;
        int state = WAITING;
        JFrame controlFrame;

        public ControlFrame(Animator Parent) {
            ca = Parent;
            controlFrame = new JFrame("Controller");
            Container controlContainer = controlFrame.getContentPane();

            controlContainer.setLayout(new FlowLayout());

            final JButton startButton = new JButton("Start");
                startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (state == RUNNING) {
                        state = WAITING;
                        startButton.setText("Start");
                        ca.animatorStopped = true;
                    }
                    else {
                        state = RUNNING;
                        startButton.setText("Stop");
                        synchronized (ca) {
                            ca.animatorStopped = false;
                            ca.notify();
                        }
                    }
                 }
            });
            controlContainer.add(startButton);

            final JButton stepButton = new JButton("Step");
            stepButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (state == RUNNING) {
                        state = WAITING;
                        startButton.setText("Start");
                        ca.animatorStopped = true;
                    }
                    synchronized (ca) {
                        ca.notify();
                    }
                }
            });
            controlContainer.add(stepButton);

            JButton exitButton = new JButton("Exit");
            exitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            controlContainer.add(exitButton);

            controlContainer.add(new Label("Animator Speed"));

            final JScrollBar speedControl = new JScrollBar(JScrollBar.HORIZONTAL,
                        500, 25, 100, 1000);
            speedControl.addAdjustmentListener(new AdjustmentListener() {
                public void adjustmentValueChanged(AdjustmentEvent e) {
                    ca.sleepTime = 1000 - e.getValue();
                }
            });
            controlContainer.add(speedControl);

            controlFrame.setSize(500, 100);
        }
     
        public void setVisible(boolean show) {
            controlFrame.setVisible(show);
        }

    }
}
