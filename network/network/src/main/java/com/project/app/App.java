package com.project.app;
import javax.swing.*;
import javax.swing.border.Border;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class App extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;

	private JButton verifyButton;
	private JLabel verificationLabel;
	private JButton sendEmailButton;
	private JTextArea originalTokenTextArea;
	private JTextArea tamperTokenTextArea;
	private JTextField emailTextField;
	private JLabel emailLabel;
	private JLabel tamperTextAreaLabel;
	private JLabel originalTextAreaLabel;
	private JPanel tokenBoxPanel;

	App(){
		
		
		sendEmailButton = new JButton("Send Email");
		sendEmailButton.addActionListener(this);
		sendEmailButton.setFont(new Font("Arial",Font.BOLD, 20));
		emailTextField = new JTextField(20);
		emailLabel = new JLabel("Email:",10);
		emailLabel.setFont(new Font("Arial",Font.BOLD, 20));
		
		tokenBoxPanel = new JPanel();
		GridLayout gridLayout = new GridLayout(2,2);
		gridLayout.setHgap(10);
		gridLayout.setVgap(10);
		tokenBoxPanel.setLayout(gridLayout);
		
		originalTextAreaLabel = new JLabel("Original Token");
		originalTextAreaLabel.setFont(new Font("Arial",Font.BOLD, 28));
		originalTextAreaLabel.setHorizontalAlignment(JLabel.CENTER);
		
		tamperTextAreaLabel = new JLabel("Tampered Token");
		tamperTextAreaLabel.setFont(new Font("Arial",Font.BOLD, 28));
		tamperTextAreaLabel.setHorizontalAlignment(JLabel.CENTER);
	
		
		originalTokenTextArea = new JTextArea(5,15);
		originalTokenTextArea.setWrapStyleWord(true);
		originalTokenTextArea.setLineWrap(true);
		originalTokenTextArea.setEditable(false);
		originalTokenTextArea.setFont(new Font("Arial",Font.PLAIN, 14));
		originalTokenTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		tamperTokenTextArea = new JTextArea(5,20);
		tamperTokenTextArea.setWrapStyleWord(true);
		tamperTokenTextArea.setLineWrap(true);
		tamperTokenTextArea.setFont(new Font("Arial",Font.PLAIN, 14));
		tamperTokenTextArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		tokenBoxPanel.add(originalTextAreaLabel);
		tokenBoxPanel.add(tamperTextAreaLabel);
		tokenBoxPanel.add(originalTokenTextArea);
		tokenBoxPanel.add(tamperTokenTextArea);
		Border blackline = BorderFactory.createLineBorder(Color.black,3);
		tokenBoxPanel.setBorder(blackline);
		
		verificationLabel = new JLabel();
		verificationLabel.setHorizontalAlignment(JLabel.CENTER);
		verificationLabel.setFont(new Font("Arial",Font.BOLD,20));
		verifyButton = new JButton("Verify");
		verifyButton.setFont(new Font("Arial",Font.BOLD,20));
		verifyButton.addActionListener(this);
		
		setSize(600,500);
		setLayout(new FlowLayout());
		add(emailLabel);
		add(emailTextField);
		add(sendEmailButton);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private HttpURLConnection sendRequest(String url,String request) throws IOException {
		URL requestUrl = new URL(url);
		HttpURLConnection httpURLConnection = (HttpURLConnection) requestUrl.openConnection();
		httpURLConnection.setRequestMethod("POST");
		httpURLConnection.setRequestProperty("Content-Type",  "application/json" );
		httpURLConnection.setRequestProperty("Content-Length",String.valueOf(request.length()));
		httpURLConnection.setRequestProperty("Content-Language", "en-US");  
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setDoOutput(true);
		DataOutputStream dataOutputStream = new DataOutputStream(
				httpURLConnection.getOutputStream()
		);
		System.out.print(request);
		dataOutputStream.writeBytes(request);
		dataOutputStream.flush();
		dataOutputStream.close();
		return httpURLConnection;
	}
	private String receiveResponse(HttpURLConnection httpURLConnection) throws IOException {
		if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = bufferedReader.readLine()) != null) {
				response.append(inputLine);
			}
			bufferedReader.close();
			return response.toString();
		}
	
		return "";
	}
	@SuppressWarnings("unchecked")
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sendEmailButton) {
			sendEmailButton.setEnabled(false);
			String email = emailTextField.getText();
			HttpURLConnection httpURLConnection;
			JSONObject request = new JSONObject();
			request.put("Email", email);
			try {
				httpURLConnection = sendRequest("http://localhost:8080/login",request.toJSONString());
				String responseString = receiveResponse(httpURLConnection);
			
				JSONObject response = null;
				try {
					JSONParser jsonParser = new JSONParser();
					response = (JSONObject) jsonParser.parse(responseString);
				}catch (ParseException  pe) {
					pe.printStackTrace();
				}	
				if(response.containsKey("Status") && ((long) response.get("Status")) == (long) 1) {
					add(tokenBoxPanel);
					originalTokenTextArea.setText(response.get("Token").toString());
					tamperTokenTextArea.setText(response.get("Token").toString());
					add(verifyButton);
					repaint();
				}
			}catch(IOException ioe) {
				ioe.printStackTrace();
			}
			sendEmailButton.setEnabled(true);
		}else if(e.getSource() == verifyButton) {
			verifyButton.setEnabled(false);
			String tamperedToken = tamperTokenTextArea.getText();
			JSONObject request = new JSONObject();
			request.put("Token", tamperedToken);
			HttpURLConnection httpURLConnection;
			try {
				httpURLConnection = sendRequest("http://localhost:8080/verify",request.toJSONString());
				String responseString = receiveResponse(httpURLConnection);
				System.out.println(responseString);
				JSONObject response = null;
				try {
					JSONParser jsonParser = new JSONParser();
					response = (JSONObject) jsonParser.parse(responseString);
				}catch (ParseException  pe) {
					pe.printStackTrace();
				}	
				if(response.containsKey("Status") && response.containsKey("Message")) {
					add(verificationLabel);
					verificationLabel.setText((String) response.get("Message"));
					
				}	
			} catch (IOException ioe) {
	
				ioe.printStackTrace();
			}
			verifyButton.setEnabled(true);
		}
		
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		App app =  new App();

	}
}
