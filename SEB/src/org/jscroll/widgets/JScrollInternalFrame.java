/*
 * JScroll - the scrollable desktop pane for Java.
 * Copyright (C) 2003 Tom Tessier
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

package org.jscroll.widgets;

import javax.swing.*;

import org.jscroll.JScrollDesktopPane;

import com.display.LineLink;
import com.display.TubePanel;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * This class provides a custom internal frame. Each internal frame
 * is assigned an associated toggle button and an optional radio button
 * menu item. These buttons reside in the
 * {@link org.jscroll.widgets.DesktopResizableToolBar
 * DesktopResizableToolBar} and
 * {@link org.jscroll.widgets.DesktopMenu DesktopMenu}.
 * classes respectively.
 *
 * @author <a href="mailto:tessier@gabinternet.com">Tom Tessier</a>
 * @author <a href="mailto:francesco.furfari@guest.cnuce.cnr.it">Francesco Furfari</a>
 *                                                <!-- Fran did some small fix, can't remember what -->
 * @version 1.0.2 14-Mar-2003
 * @version 1.0  9-Aug-2001
 */
public class JScrollInternalFrame extends JInternalFrame {
    private JToggleButton associatedButton;
    private JRadioButtonMenuItem associatedMenuButton;
    private boolean isClosable;
	private boolean moved; // voir si la frame a deja ete deplace par la fonction de tri
    private int initialWidth;
    private int initialHeight;
    private JScrollDesktopPane jsDesktopPane;
    public static Dimension DEFAULT_DIMENSION = new Dimension(250, 140);
    private TubePanel tubePanel;

    /**
     *  creates the JScrollInternalFrame
     *
     * @param title the string displayed in the title bar of the internal frame
     * @param icon the ImageIcon displayed in the title bar of the internal frame
     * @param frameContents the contents of the internal frame
     * @param isClosable determines whether the frame is closable
     */
    public JScrollInternalFrame(String title, ImageIcon icon,
    		TubePanel frameContents,JScrollDesktopPane parentPane, boolean isClosable) {
        super(title, // title
            false, //resizable
            isClosable, //closable
            false, //maximizable
            false); //iconifiable
        setJsDesktopPane(parentPane);
        tubePanel = frameContents;
        this.add(tubePanel);
        getJsDesktopPane().addJIFrameToList(this);
        this.isClosable = isClosable;

        setBackground(Color.white);
        setForeground(Color.blue);
        setSize(DEFAULT_DIMENSION);
        
        if (icon != null) {
            setFrameIcon(icon);
        }

        // add the window contents
        getContentPane().add(tubePanel);
        //pack();

        saveSize();

        setVisible(true); // turn the frame on
    }

    /**
     * tell the frame to save its size for later restoration
     */
    public void saveSize() {
        initialWidth = getWidth();
        initialHeight = getHeight();
    }

    /**
     * constructor provided for compatibility with JInternalFrame
     */
    public JScrollInternalFrame() {
        super();
        saveSize();
    }

    /**
     *  sets the associated menu button
     *
     * @param associatedMenuButton the menu button to associate with
     * the internal frame
     */
    public void setAssociatedMenuButton(
        JRadioButtonMenuItem associatedMenuButton) {
        this.associatedMenuButton = associatedMenuButton;
    }

    /**
     *  returns the associated menu button
     *
     * @return the JRadioButtonMenuItem object associated with this internal frame
     */
    public JRadioButtonMenuItem getAssociatedMenuButton() {
        return associatedMenuButton;
    }

    /**
     *  sets the associated toggle button
     *
     * @param associatedButton the toggle button to associate with
     * the internal frame
     */
    public void setAssociatedButton(JToggleButton associatedButton) {
        this.associatedButton = associatedButton;
    }

    /**
     *  returns the associated toggle button
     *
     * @return the JToggleButton object associated with this internal frame
     */
    public JToggleButton getAssociatedButton() {
        return associatedButton;
    }

    /**
     *  returns the initial dimensions of this internal frame. Necessary so that
     * internal frames can be restored to their default sizes when the cascade
     * frame positioning mode is chosen in
     * {@link org.jscroll.widgets.FramePositioning FramePositioning}.
     *
     * @return the Dimension object representing the initial dimensions of
     * this internal frame
     */
    public Dimension getInitialDimensions() {
        return new Dimension(initialWidth, initialHeight);
    }

    /**
     *  returns the toString() representation of this object. Useful for
     * debugging purposes.
     *
     * @return the toString() representation of this object
     */
    public String toString() {
        return "JScrollInternalFrame: " + getTitle();
    }

    /**
     *  selects the current frame, along with any toggle and menu
     * buttons that may be associated with it
     */
    public void selectFrameAndAssociatedButtons() {
        // select associated toolbar button
        if (associatedButton != null) {
            associatedButton.setSelected(true);
            ((RootToggleButton) associatedButton).flagContentsChanged(false);
        }

        // select menu button
        if (associatedMenuButton != null) {
            associatedMenuButton.setSelected(true);
        }

        try {
            setSelected(true);
            setIcon(false); // select and de-iconify the frame
        } catch (java.beans.PropertyVetoException pve) {
            System.out.println(pve.getMessage());
        }

        setVisible(true); // and make sure the frame is turned on
    }

    /**
     *  saves the size of the current internal frame for those frames whose
     * initial width and heights have not been set. Called when the internal
     * frame is added to the JDesktopPane.
     * <BR><BR>
     * Manually-built internal frames won't display properly without this.
     * <BR><BR>
     * Fix by <a href="mailto:francesco.furfari@guest.cnuce.cnr.it">Francesco Furfari</a>
     *
     */
    public void addNotify() {
        super.addNotify();

        if ((initialWidth == 0) && (initialHeight == 0)) {
            saveSize();
        }
    }

	public TubePanel getTubePanel() {
		return tubePanel;
	}

	public void setTubePanel(TubePanel tubePanel) {
		this.tubePanel = tubePanel;
	}

	public ConcurrentLinkedQueue<LineLink> getLineLinks() {
		return getTubePanel().getLineLinks();
	}

	/**
	 * @return the jsDesktopPane
	 */
	public JScrollDesktopPane getJsDesktopPane() {
		return jsDesktopPane;
	}

	/**
	 * @param jsDesktopPane the jsDesktopPane to set
	 */
	public void setJsDesktopPane(JScrollDesktopPane jsDesktopPane) {
		this.jsDesktopPane = jsDesktopPane;
	}

	public void addLineLinkAsParent(LineLink lineLink) {
		getTubePanel().addLineLinkAsParent(lineLink);
		getJsDesktopPane().repaint();
	}

	public void updateLineLink() {
		for(LineLink ll : getTubePanel().getLineLinks()){
			ll.update();
		}
	}

	public void delete() {
		getTubePanel().delete();
		getLineLinks().clear();
		dispose();
	}

	/**
	 * @return the moved
	 */
	public boolean isMoved() {
		return moved;
	}

	/**
	 * @param moved the moved to set
	 */
	public void setMoved(boolean moved) {
		this.moved = moved;
	}

	public void setLineLinks(ConcurrentLinkedQueue<LineLink> newqueue) {
		getTubePanel().setLineLinks(newqueue);
	}

	/**
	 * Met a jours le tubepanel (si les graphs sont dispo etc)
	 */
	public void updateButtons() {
		getTubePanel().refreshDisplayFromTube();
	}


}
