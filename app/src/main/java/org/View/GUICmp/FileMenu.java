package org.View.GUICmp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.FileManager;
import org.Controller.UMLEditor;

//TODO finish mock
public class FileMenu extends JMenu {
	UMLEditor e;
	UMLDiagram d;

	public FileMenu(UMLEditor e, UMLDiagram d) {
		super("File");
		this.e = e;
		this.d = d;

		JMenuItem undo, redo, LoadJson, SaveJson, SaveImg;
		undo = new JMenuItem("Undo");
		redo = new JMenuItem("Redo");
		LoadJson = new JMenuItem("Load Json");
		SaveJson = new JMenuItem("Save Json");
		SaveImg = new JMenuItem("Save Image");

		undo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				UndoHelp();
			}
		});

		redo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RedoHelp();
			}
		});

		SaveJson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaveJsonHelp();
			}
		});

		LoadJson.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LoadJsonHelp();
			}
		});

		SaveImg.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SaveImageHelp();
			}
		});

		add(undo);
		add(redo);
		add(LoadJson);
		add(SaveJson);
		add(SaveImg);
	}

	private void UndoHelp() {
		try {
			e.undo();
			d.updatemdl(e.getModel());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void RedoHelp() {
		try {
			e.redo();
			d.updatemdl(e.getModel());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void LoadJsonHelp() {
		File f = getFileHelp(true, new FileNameExtensionFilter("JSON file", "json", "JSON"));
		FileManager m = new FileManager();
		try {
			d.updatemdl(m.load(f.getPath()));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void SaveJsonHelp() {
		File f = getFileHelp(false, new FileNameExtensionFilter("JSON file", "json", "JSON"));
		FileManager m = new FileManager();
		try {
			m.save(f.getPath(), e.getModel());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}

	private void SaveImageHelp() {
		File f = getFileHelp(false, new FileNameExtensionFilter("png file", "png"));
		d.save(f.getPath());
	}

	private File getFileHelp(Boolean load, FileNameExtensionFilter filter) {
		JFileChooser c = new JFileChooser();
		c.setFileFilter(filter);
		int retval = load ? c.showOpenDialog(getParent()) : c.showSaveDialog(getParent());
		if (retval == JFileChooser.APPROVE_OPTION) {
			return c.getSelectedFile();
		}
		// TODO better return value on fail
		return null;
	}

}
