package ashutoshgangwar.credentialsmanager;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public abstract class AddEntryLayout extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JTextField url, username, password;
	
	protected abstract void onValidEntryProvided(String url,
			String username, String password);
	
	protected AddEntryLayout() {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(15, 15, 15, 15),
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(
								BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
								"Add Entry"),
						BorderFactory.createEmptyBorder(15, 15, 15, 15))));
		
		JPanel entryFieldContainer = new JPanel(new GridLayout(3, 3));
		
		entryFieldContainer.add(new JLabel("Website:"));
		
		url = new JTextField(13);
		entryFieldContainer.add(url);
		
		entryFieldContainer.add(new JLabel("Username:"));
		
		username = new JTextField(13);
		entryFieldContainer.add(username);
		
		entryFieldContainer.add(new JLabel("Password:"));
		
		password = new JTextField(13);
		entryFieldContainer.add(password);
		
		add(entryFieldContainer);
		
		JPanel buttonWrapper = new JPanel();
		JButton saveButton = new JButton("Save");
		saveButton.addActionListener(this);
		
		buttonWrapper.setBorder(BorderFactory.createEmptyBorder(5, 180, 0, 0));
		buttonWrapper.add(saveButton);
		add(buttonWrapper);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(url.getText().isEmpty() || username.getText().isEmpty()
				|| password.getText().isEmpty()) {
			JOptionPane.showMessageDialog(this, "All fields are required!",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		onValidEntryProvided(url.getText(), username.getText(), password.getText());
	}
}
