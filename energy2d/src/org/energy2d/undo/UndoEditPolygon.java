package org.energy2d.undo;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;

import org.energy2d.math.Polygon2D;
import org.energy2d.model.Manipulable;
import org.energy2d.view.View2D;

public class UndoEditPolygon extends AbstractUndoableEdit {

	private static final long serialVersionUID = 1L;
	private List<Point2D.Float> oldPoints, newPoints;
	private View2D view;
	private Polygon2D polygon;
	private Manipulable selectedManipulable;

	public UndoEditPolygon(View2D view, Manipulable selectedManipulable, Polygon2D polygon) {
		this.view = view;
		this.selectedManipulable = selectedManipulable;
		this.polygon = polygon;
		oldPoints = new ArrayList<Point2D.Float>();
		newPoints = new ArrayList<Point2D.Float>();
		int n = polygon.getVertexCount();
		for (int i = 0; i < n; i++) {
			Point2D.Float pi = polygon.getVertex(i);
			oldPoints.add(new Point2D.Float(pi.x, pi.y));
		}
	}

	@Override
	public void undo() throws CannotUndoException {
		super.undo();
		int n = polygon.getVertexCount();
		for (int i = 0; i < n; i++) {
			Point2D.Float pi = polygon.getVertex(i);
			newPoints.add(new Point2D.Float(pi.x, pi.y));
		}
		polygon.setVertices(oldPoints);
		view.getModel().refreshPowerArray();
		view.getModel().refreshTemperatureBoundaryArray();
		view.getModel().refreshMaterialPropertyArrays();
		if (view.isViewFactorLinesOn())
			view.getModel().generateViewFactorMesh();
		view.setSelectedManipulable(selectedManipulable);
		view.repaint();
	}

	@Override
	public void redo() throws CannotRedoException {
		super.redo();
		polygon.setVertices(newPoints);
		view.getModel().refreshPowerArray();
		view.getModel().refreshTemperatureBoundaryArray();
		view.getModel().refreshMaterialPropertyArrays();
		if (view.isViewFactorLinesOn())
			view.getModel().generateViewFactorMesh();
		view.setSelectedManipulable(selectedManipulable);
		view.repaint();
	}

	@Override
	public String getPresentationName() {
		return "Edit Polygon";
	}

}
