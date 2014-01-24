package com.display;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JInternalFrame;

import org.jscroll.widgets.JScrollInternalFrame;

import models.Variable;

public class LineLink {

	private Line2D.Float line;
	private JScrollInternalFrame parent;
	private JScrollInternalFrame child;
	


	public LineLink(JScrollInternalFrame parent, JScrollInternalFrame child){
		this.parent = parent;
		this.child = child;
		int x1 = (int) (this.parent.getX() + (this.parent.getWidth()/2.0f));
		int y1 = (int) (this.parent.getY() + (this.parent.getHeight())) + 20;
		int x2 = (int) (this.child.getX() + (this.child.getWidth()/2.0f));
		int y2 = (int) (this.child.getY()) + 20;
		line = new Line2D.Float(x1, y1, x2, y2);
		this.parent.addLineLinkAsParent(this);
	}

	public void update(){
		int x1 = (int) (this.parent.getX() + (this.parent.getWidth()/2.0f));
		int y1 = (int) (this.parent.getY() + (this.parent.getHeight())) + 20;
		int x2 = (int) (this.child.getX() + (this.child.getWidth()/2.0f));
		int y2 = (int) (this.child.getY()) + 20;
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
}
