package ashutoshgangwar.credentialsmanager;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.JFrame;

public class Main {
	
	protected static DataFileManager dataManager;
	
	public static void main(String[] args) throws IOException {
		dataManager = new DataFileManager();
		final JFrame window = new JFrame(Const.APP_NAME);
		
		window.setLocationRelativeTo(null);
		window.setResizable(false);
		
		window.getContentPane().add(new PasswordLayout(!dataManager.dataExists()){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onFinish() {
				window.getContentPane().removeAll();
				window.getContentPane().add(new EntryDisplayLayout(window));
				window.getContentPane().invalidate();
				window.validate();
				window.repaint();
				window.pack();
			}
		});
		
		window.pack();
		window.setVisible(true);
		
		window.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				dataManager.commitToFile();
				System.exit(0);
			}

			@Override
			public void windowClosed(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				
			}
			
		});
	}
}
