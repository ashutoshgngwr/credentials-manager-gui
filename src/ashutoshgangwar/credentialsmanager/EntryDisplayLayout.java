package ashutoshgangwar.credentialsmanager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class EntryDisplayLayout extends JPanel implements ActionListener, TableModelListener{

	private static final long serialVersionUID = 1L;
	
	private JFrame window;
	private JTable table;
	private JMenuItem deleteSelected;
	
	protected EntryDisplayLayout(JFrame window) {
		super();
		this.window = window;
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(15, 15, 15, 15),
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(
								BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
								"Saved Entries"),
						BorderFactory.createEmptyBorder(15, 15, 15, 15))));
		
		createMenuBar();
		initTable();
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		file.setMnemonic(KeyEvent.VK_F);
		
		JMenuItem exit = new JMenuItem("Save & Exit");
		exit.setMnemonic(KeyEvent.VK_E);
		exit.setActionCommand("exit");
		exit.addActionListener(this);
		
		file.add(exit);
		
		menuBar.add(file);
		
		JMenu entry = new JMenu("Entry");
		entry.setMnemonic(KeyEvent.VK_N);
		
		JMenuItem addEntry = new JMenuItem("Add Entry");
		addEntry.setMnemonic(KeyEvent.VK_A);
		addEntry.setActionCommand("add");
		addEntry.addActionListener(this);
		
		deleteSelected = new JMenuItem("Delete selected");
		deleteSelected.setMnemonic(KeyEvent.VK_D);
		deleteSelected.setActionCommand("delete");
		deleteSelected.addActionListener(this);
		deleteSelected.setEnabled(false);
		
		entry.add(addEntry);
		entry.add(deleteSelected);
		menuBar.add(entry);
		
		JMenu help = new JMenu("Help");
		help.setMnemonic(KeyEvent.VK_H);
		
		JMenuItem about = new JMenuItem("About");
		about.setMnemonic(KeyEvent.VK_B);
		about.setActionCommand("about");
		about.addActionListener(this);
		
		help.add(about);
		menuBar.add(help);
		
		window.setJMenuBar(menuBar);
	}
	
	private void initTable() {
		Object[] columnNames = {"Website", "Username", "Password"};
		Object[][] data = new Object[Main.dataManager.getEntries().size()][3];
		ArrayList<CredentialEntry> enteries = Main.dataManager.getEntries();
		
		for(int i = 0; i < enteries.size(); i++) {
			CredentialEntry e = enteries.get(i);
			data[i][0] = e.getUrl();
			data[i][1] = e.getUsername();
			data[i][2] = e.getPassword();
		}
		
		DefaultTableModel model = new DefaultTableModel(data, columnNames);
		table = new JTable(model){
			private static final long serialVersionUID = 1L;
			@Override
			public Class<?> getColumnClass(int column) {
				return String.class;
			}
		};
		
		Dimension d = table.getPreferredSize();
		d.setSize(d.getWidth(), 100);
		table.setPreferredScrollableViewportSize(d);
		
		JScrollPane tableWrapper = new JScrollPane(table);
		add(tableWrapper);
		
		model.addTableModelListener(this);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				deleteSelected.setEnabled(table.getSelectedRow() != -1);
			}
		});
		
		add(new JLabel("<html><i><br/>Tip: Double click on cell to edit entries!</i></html>"));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()) {
			case "add":
				addEntry();
				break;
				
			case "delete":
				deleteSelected();
				break;
				
			case "exit":
				Main.dataManager.commitToFile();
				System.exit(0);
				break;
			case "about":
				JOptionPane.showMessageDialog(this,
						"<html><h2>" + Const.APP_NAME + "</h2>"
						+ "A small utility tool to safely remember your "
						+ "username and passwords!"
						+ "<br/><br/><b>Created by Ashutosh Gangwar</b></html>",
						"About", JOptionPane.INFORMATION_MESSAGE);
				break;
		}
	}
	
	private void deleteSelected() {
		int[] rows = table.getSelectedRows();
		
		for(int i = rows.length - 1; i >= 0; i--) {
			Main.dataManager.deleteEntry(rows[i]);
			((DefaultTableModel) table.getModel()).removeRow(rows[i]);
		}
	}
	
	private void addEntry() {
		final JFrame addEntryFrame = new JFrame();
		addEntryFrame.setResizable(false);
		window.setLocationRelativeTo(window);
		
		addEntryFrame.getContentPane().add(new AddEntryLayout() {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onValidEntryProvided(String url,
					String username, String password) {
				addEntryFrame.dispose();
				Main.dataManager.addEntry(new CredentialEntry(url, username, password));
				((DefaultTableModel) table.getModel()).addRow(new Object[] {
						url, username, password
				});
			}
		});
		
		addEntryFrame.pack();
		addEntryFrame.setVisible(true);
	}

	@Override
	public void tableChanged(TableModelEvent e) {
		if(e.getType() == TableModelEvent.UPDATE) {
			for(int i = e.getFirstRow(); i <= e.getLastRow(); i++) {
				if(((String) table.getValueAt(i, 0)).isEmpty())
					table.setValueAt(Main.dataManager.getEntry(i).getUrl(), i, 0);
				else
					Main.dataManager.getEntry(i).setUrl((String) table.getValueAt(i, 0));
				if(((String) table.getValueAt(i, 1)).isEmpty())
					table.setValueAt(Main.dataManager.getEntry(i).getUsername(), i, 1);
				else
					Main.dataManager.getEntry(i).setUsername((String) table.getValueAt(i, 1));
				if(((String) table.getValueAt(i, 2)).isEmpty())
					table.setValueAt(Main.dataManager.getEntry(i).getPassword(), i, 2);
				else
					Main.dataManager.getEntry(i).setPassword((String) table.getValueAt(i, 2));
			}
		}
	}
}
