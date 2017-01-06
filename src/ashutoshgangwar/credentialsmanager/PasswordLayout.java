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
import javax.swing.JPasswordField;

public abstract class PasswordLayout extends JPanel implements ActionListener {
	
	private static final long serialVersionUID = 8058923049193374814L;
	
	private JPasswordField passwordField, confirmPasswordField;
	
	protected PasswordLayout(boolean firstTime) {
		super();
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(20, 50, 30, 50),
				BorderFactory.createCompoundBorder(
						BorderFactory.createTitledBorder(
								BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true),
								firstTime ? "Create Password" : "Enter Password"),
						BorderFactory.createEmptyBorder(15, 25, 15, 25))));
		
		if(firstTime)
			initCreatePasswordLayout();
		else
			initEnterPasswordLayout();
		
		JPanel buttonWrapper = new JPanel();
		JButton continueButton = new JButton("Continue");
		continueButton.addActionListener(this);
		
		buttonWrapper.setBorder(BorderFactory.createEmptyBorder(5, 180, 0, 0));
		buttonWrapper.add(continueButton);
		add(buttonWrapper);
		
	}

	private void initEnterPasswordLayout() {
		JPanel passwordFieldContainer = new JPanel(new GridLayout(1, 2));
		
		passwordFieldContainer.add(new JLabel("Password:"));
		
		passwordField = new JPasswordField(13);
		passwordFieldContainer.add(passwordField);
		
		add(passwordFieldContainer);
	}

	private void initCreatePasswordLayout() {
		JPanel passwordFieldContainer = new JPanel(new GridLayout(2, 2));
		
		passwordFieldContainer.add(new JLabel("Create Password:"));
		
		passwordField = new JPasswordField(13);
		passwordFieldContainer.add(passwordField);
		
		passwordFieldContainer.add(new JLabel("Confirm Password:"));
		
		confirmPasswordField = new JPasswordField(13);
		passwordFieldContainer.add(confirmPasswordField);
		
		add(passwordFieldContainer);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String password = new String(passwordField.getPassword());
		
		if(password.isEmpty()) {
			JOptionPane.showMessageDialog(this,
					"Enter password to continue!", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(confirmPasswordField != null) {
			String cPassword = new String(confirmPasswordField.getPassword());
			if(cPassword.equals(password)) {
				Main.dataManager.setPassword(password);
			} else {
				JOptionPane.showMessageDialog(this,
						"Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
				passwordField.setText("");
				confirmPasswordField.setText("");
				return;
			}
		} else {
			if(!Main.dataManager.checkPassword(password)) {
				JOptionPane.showMessageDialog(this,
						"Incorrect password!", "Error", JOptionPane.ERROR_MESSAGE);
				passwordField.setText("");
				return;
			} else {
				Main.dataManager.parseFile();
			}
		}
		
		onFinish();
	}
	
	protected abstract void onFinish();
}
