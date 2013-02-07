package com.customized.tools.ui.swt;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.ToolBar;

import com.customized.tools.common.ResourceLoader;

public abstract class AbstractTable {
	
	private static final int WIDTH = 800 ;
	private static final int HEIGHT = 600 ;
	
	Display display = null;
	Shell shell = null;
	Table table = null;
	ToolBar toolBar = null;
	
	public AbstractTable() {
		this("", null);
	}
	
	public AbstractTable(String title, String image) {
		this(WIDTH, HEIGHT, title, image) ;
	}
	
	public AbstractTable(int width, int height, String title, String image) {
		
		if (width == 0)
			width = WIDTH;

		if (height == 0)
			height = HEIGHT;
		
		display = new Display();
		shell = new Shell(display);
		
		shell.setSize(width, height);
		shell.setText(title);
		shell.setImage(new Image(display, getResourceAsStream(image)));
	
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2 ;
		shell.setLayout(gridLayout);
		
		table = new Table(shell, SWT.BORDER | SWT.CHECK | SWT.MULTI | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
	    gd.horizontalSpan = 2;
	    table.setLayoutData(gd);
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
		toolBar = new ToolBar(shell, SWT.CHECK | SWT.MULTI | SWT.HORIZONTAL);
		
	}
	
	private InputStream getResourceAsStream(String name) {
		return ResourceLoader.getInstance().getResourceAsStream(name);
	}

	protected abstract void fillTableContent(Table table );
	
	protected abstract void fillToolBarItem(ToolBar toolBar);
	
	public void start() {
		
		fillTableContent(table);
		
		fillToolBarItem(toolBar);
						
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}

	public void stop() {
		display.dispose();
	}
	
}
