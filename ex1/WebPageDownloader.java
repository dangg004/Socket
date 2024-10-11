package ex1;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebPageDownloader {
    
    public static void downloadWebPage(String webpageUrl, String localFilePath) {
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        HttpURLConnection connection = null;

        try {
            // Create a URL object
            URL url = new URL(webpageUrl);
            
            // Open a connection to the web server
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            // Open input stream to read from the web server
            inputStream = new BufferedInputStream(connection.getInputStream());
            
            // Create output stream to write to local file
            fileOutputStream = new FileOutputStream(localFilePath);
            
            // Buffer for reading data from the input stream
            byte[] buffer = new byte[1024];
            int bytesRead;
            
            // Write the web page content to the local file
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            
            System.out.println("Webpage downloaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Close streams and connection
            try {
                if (inputStream != null) inputStream.close();
                if (fileOutputStream != null) fileOutputStream.close();
                if (connection != null) connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void main(String[] args) {
        // URL of the webpage to download
        String webpageUrl = "https://www.google.com";
        
        // Local file path where the webpage will be saved
        String localFilePath = "downloaded_homepage.html";
        
        // Call the function to download the webpage
        downloadWebPage(webpageUrl, localFilePath);
    }
}
