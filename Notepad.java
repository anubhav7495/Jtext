import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.*;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rsyntaxtextarea.templates.*;

public class Notepad extends JFrame {
	JPanel cp;
	JMenuBar menuBar;
	JMenu file, edit;
	JMenuItem open, save, exit, cut, copy, paste, selectAll;
	JFileChooser fileChooser;
	RSyntaxTextArea textArea;

	Notepad() {
		cp = new JPanel(new BorderLayout());
		file = new JMenu("File");
		edit = new JMenu("Edit");
		open = new JMenuItem("Open");
		save = new JMenuItem("Save");
		exit = new JMenuItem("Exit");
		cut = new JMenuItem("Cut");
		copy = new JMenuItem("Copy");
		paste = new JMenuItem("Paste");
		selectAll = new JMenuItem("SelectAll");
		textArea = new RSyntaxTextArea();
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		textArea.setCodeFoldingEnabled(true);
		JPopupMenu popup = textArea.getPopupMenu();
		RTextScrollPane sp = new RTextScrollPane(textArea);
		fileChooser = new JFileChooser();
		menuBar = new JMenuBar();

		RSyntaxTextArea.setTemplatesEnabled(true);
		CodeTemplateManager ctm = RSyntaxTextArea.getCodeTemplateManager();

		CodeTemplate ct = new StaticCodeTemplate("pl", "System.out.println();", null);
		ctm.addTemplate(ct);

		ct = new StaticCodeTemplate("for", "for (int i=0; i< ; i++) {\n\t\n}\n", null);
    ctm.addTemplate(ct);

		ct = new StaticCodeTemplate("main", "public static void main(String args[]) {\n\t\n}\n", null);
		ctm.addTemplate(ct);

		cp.add(sp);

		setContentPane(cp);
		setTitle("Java Text Editor");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);

		file.add(open);file.add(save);file.add(exit);
		edit.add(cut);edit.add(copy);edit.add(paste);edit.add(selectAll);
		menuBar.add(file);
		menuBar.add(edit);
		setJMenuBar(menuBar);

		OpenListener openL = new OpenListener();
		SaveListener saveL = new SaveListener();
		ExitListener exitL = new ExitListener();
		EditListener editL = new EditListener();
		open.addActionListener(openL);
		save.addActionListener(saveL);
		exit.addActionListener(exitL);
		cut.addActionListener(editL);
    copy.addActionListener(editL);
    paste.addActionListener(editL);
    selectAll.addActionListener(editL);

		setSize(800, 600);
		setVisible(true);
	}

	class OpenListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(cp)) {
				File file = fileChooser.getSelectedFile();
				textArea.setText("");
				Scanner in = null;
				try {
					in = new Scanner(file);
					while(in.hasNext()) {
						String line = in.nextLine();
						textArea.append(line+"\n");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					in.close();
				}
			}
		}
	}

	class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog(cp)) {
				File file = fileChooser.getSelectedFile();
				PrintWriter out = null;
				try {
					out = new PrintWriter(file);
					String output = textArea.getText();
					System.out.println(output);
					out.println(output);
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					try {out.flush();} catch(Exception ex1) {}
					try {out.close();} catch(Exception ex1) {}
				}
			}
		}
	}

	class ExitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}

	class EditListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==cut)
        textArea.cut();
      if(e.getSource()==paste)
        textArea.paste();
      if(e.getSource()==copy)
        textArea.copy();
      if(e.getSource()==selectAll)
        textArea.selectAll();
		}
	}

	public static void main(String args[]) {
		Notepad n = new Notepad();
	}
}
