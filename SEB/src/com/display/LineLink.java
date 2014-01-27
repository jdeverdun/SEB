package com.display;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JInternalFrame;

import org.jscroll.widgets.DesktopMediator;
import org.jscroll.widgets.JScrollInternalFrame;

import models.Variable;

public class LineLink {

	private Line2D.Float line;
	private JScrollInternalFrame parent;
	private JScrollInternalFrame child;
	


	public LineLink(JScrollInternalFrame parent, JScrollInternalFrame child){
		this.parent = parent;
		this.child = child;
		DesktopMediator desktopMediator = parent.getJsDesktopPane().getDesktopMediator();
		Rectangle viewP = desktopMediator.getDesktopScrollpane().getViewport().getViewRect();
		
		int x1 = (int) (this.parent.getX() + (this.parent.getWidth()/2.0f) - viewP.x);
		int y1 = (int) (this.parent.getY() + (this.parent.getHeight()) - viewP.y);
		int x2 = (int) (this.child.getX() + (this.child.getWidth()/2.0f) - viewP.x);
		int y2 = (int) (this.child.getY() - viewP.y);
		line = new Line2D.Float(x1, y1, x2, y2);
		this.parent.addLineLinkAsParent(this);
	}

	public void update(){
		DesktopMediator desktopMediator = parent.getJsDesktopPane().getDesktopMediator();
		Rectangle viewP = desktopMediator.getDesktopScrollpane().getViewport().getViewRect();

		int x1 = (int) (this.parent.getX() + (this.parent.getWidth()/2.0f) - viewP.x);
		int y1 = (int) (this.parent.getY() + (this.parent.getHeight()) - viewP.y);
		int x2 = (int) (this.child.getX() + (this.child.getWidth()/2.0f) - viewP.x);
		int y2 = (int) (this.child.getY() - viewP.y);
		line.setLine(x1, y1, x2, y2);
		parent.getJsDesktopPane().repaint();
	}

	public JScrollInternalFrame getParent() {
		return parent;
	}

	public void setParent(JScrollInternalFrame parent) {
		this.parent = parent;
	}

	public JScrollInternalFrame getChild() {
		return child;
	}

	public void setChild(JScrollInternalFrame child) {
		this.child = child;
	}
	
	/**
	 * @return the line
	 */
	public Line2D.Float getLine() {
		return line;
	}

	/**
	 * @param line the line to set
	 */
	public void setLine(Line2D.Float line) {
		this.line = line;
	}
	
	@Override
	public boolean equals(Object v) {
		if (v instanceof LineLink){
			return parent == ((LineLink)v).getParent() && child == ((LineLink)v).getChild();
		}
		return false;
	}

	public void delete() {
		parent.getTubePanel().removeLineLinkAsParent(this);
		child.getTubePanel().removeLineLinkAsChild(this);
	}
	public String toString(){
		return getParent() + " ----> "+getChild();
	}
}
