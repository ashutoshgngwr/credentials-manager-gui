package ashutoshgangwar.credentialsmanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DataFileManager {
	
	private File dataFile;
	private String passwordHash, password;
	private BufferedReader reader;
	
	private ArrayList<CredentialEntry> entries = new ArrayList<>();
	
	protected DataFileManager() {
		dataFile = new File(Const.DATA_FILE_NAME);
		parsePasswordHash();
	}
	
	private byte[] getKey() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] key = md.digest(password.getBytes());
			key = Arrays.copyOf(key, 16);
			return key;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private void parsePasswordHash() {
		try {
		reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(dataFile)));
		
		passwordHash = reader.readLine();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void parseFile() {
		try {
			byte[] key = getKey();
			String line;
			while((line = reader.readLine()) != null) {
				String[] data = line.split(",");
				if(data.length == 3)
					entries.add(new CredentialEntry(data[0],
							decrypt(data[1], key), decrypt(data[2], key)));
			}
			
			reader.close();
		} catch (IOException e) {
			if(dataFile.exists())
				dataFile.delete();
		}
	}
	
	protected boolean dataExists() {
		return dataFile.exists() && dataFile.length() > 0;
	}
	
	protected boolean checkPassword(String password) {
		if(passwordHash.equals(md5(password))) {
			this.password = password;
			return true;
		}
		
		return false;
	}
	
	protected void setPassword(String password) {
		this.password = password;
	}
	
	protected String getPassword() {
		return this.password;
	}
	
	protected void addEntry(CredentialEntry entry) {
		this.entries.add(entry);
	}
	
	protected ArrayList<CredentialEntry> getEntries() {
		return this.entries;
	}
	
	protected CredentialEntry getEntry(int index) {
		return this.entries.get(index);
	}
	
	protected void deleteEntry(int position) {
		this.entries.remove(position);
	}
	
	protected void commitToFile() {
		if(password == null)
			return;
		
		try {
			if(!dataFile.exists())
				dataFile.createNewFile();
			
			byte[] key = getKey();
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
			writer.write(md5(password) + "\n");
			
			for(CredentialEntry e : entries) {
				writer.write(e.getUrl() + ","
						+ encrypt(e.getUsername(), key) + ","
						+ encrypt(e.getPassword(), key) + "\n");
			}
			
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String md5(String md5) {
		try {
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());
			StringBuffer sb = new StringBuffer();
			
			for (int i = 0; i < array.length; ++i)
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));

			return sb.toString();
		} catch (java.security.NoSuchAlgorithmException e) {
			System.out.println(e.getMessage());
		}
		
		return null;
	}
	
	private String encrypt(String data, byte[] rawKey) {
		if(data == null || data.isEmpty())
			return "";
		try {
			Cipher cipher = Cipher.getInstance("AES");
			SecretKeySpec key = new SecretKeySpec(rawKey, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
		return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	private String decrypt(String data, byte[] rawKey) {
		if(data == null || data.isEmpty())
			return "";
		try {
			Cipher cipher = Cipher.getInstance("AES");
		    SecretKeySpec key = new SecretKeySpec(rawKey, "AES");
		    cipher.init(Cipher.DECRYPT_MODE, key);
		    return new String(cipher.doFinal(Base64.getDecoder().decode(data)),"UTF-8");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}