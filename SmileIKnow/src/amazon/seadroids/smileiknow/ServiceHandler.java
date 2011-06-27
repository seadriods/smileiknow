package amazon.seadroids.smileiknow;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;

public class ServiceHandler {

	private static final String SERVICE_HOSTNAME = "192.168.74.102";
	private static final int SERVICE_PORT = 3000;

	/**
	 * Gets the smile-I-know collage URL
	 */
	public static String getCollage() {
		/*
		String postData = "";
		try {
			postData = URLEncoder.encode("uid", "UTF-8") + "="
					+ URLEncoder.encode(SharedData.userId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sendRequest(postData, "/getCollage",true);
		 */
		return "http://"+SERVICE_HOSTNAME+":"+SERVICE_PORT+"/users/"+SharedData.userId+"/getCollage?m=mobile";
	}

	public static boolean processOrder(String productId) {
		String postData = "";

		postData = "order[user_id]=" + SharedData.userId
				+ "&order[product_id]=" + productId;

		return sendRequest(postData, "/orders",false) != null;

	}

	/**
	 * Sends an image to amazon smile-I-know service
	 */
	public static boolean sendImage(byte[] imageBytes) {

		String path = "/users/" + SharedData.userId + "/pushImg";

		HttpURLConnection connection = null;
		DataOutputStream outputStream = null;

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		try {

			URL url = new URL("http://" + SERVICE_HOSTNAME + ":" + SERVICE_PORT
					+ path);
			connection = (HttpURLConnection) url.openConnection();

			// Allow Inputs & Outputs
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);

			// Enable POST method
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);

			outputStream = new DataOutputStream(connection.getOutputStream());
			outputStream.writeBytes(twoHyphens + boundary + lineEnd);
			outputStream
					.writeBytes("Content-Disposition: form-data; name=\"img\";filename=\"\""
							+ lineEnd);

			outputStream.writeBytes(lineEnd);

			outputStream.write(imageBytes, 0, imageBytes.length);

			outputStream.writeBytes(lineEnd);
			outputStream.writeBytes(twoHyphens + boundary + twoHyphens
					+ lineEnd);

			// Responses from the server (code and message)
			int serverResponseCode = connection.getResponseCode();

			outputStream.flush();
			outputStream.close();

			return serverResponseCode == 200;
		} catch (Exception ex) {
			// Exception handling
			return false;
		}

	}

	/**
	 * Submits a post request to amazon smile-I-know service with the given post
	 * data and service path
	 * 
	 * @param postData
	 * @param path
	 *            : the path after hostname starting with a forward slash, e.g.
	 *            /path
	 * @param getOutput
	 *            true if an output is expected
	 * @return null if an error occurred, or the service output otherwise. An
	 *         empty string is returned if getOutput is set to false
	 * 
	 */
	private static String sendRequest(String postData, String path,
			boolean getOutput) {

		try {

			// Create socket
			InetAddress addr = InetAddress.getByName(SERVICE_HOSTNAME);
			Socket sock = new Socket(addr, SERVICE_PORT);

			// Send header
			BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(
					sock.getOutputStream(), "UTF-8"));
			wr.write("POST " + path + " HTTP/1.1\r\n");
			wr.write("Host: " + SERVICE_HOSTNAME + "\r\n");
			wr.write("Content-Length: " + postData.length() + "\r\n");
			wr.write("\r\n");

			// Send data
			wr.write(postData);
			wr.flush();

			String output = "";
			if (getOutput) {
				// Response
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						sock.getInputStream()));
				String line;

				while ((line = rd.readLine()) != null) {
					output += line;
				}
			}
			return output;

		} catch (Exception e) {
			return null;
		}

	}

}
