import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;

public class Test {

    public static void main(String[] args) {
        String httpsUrl = "https://s3.amazonaws.com/"; //326947223243/beta-uniup-worker

        try {
            // Trust all certificates (for demonstration purposes only, do not use in production)
            /*TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {

                            for(X509Certificate cert:certs)
                            try {
                                // Load your X509Certificate object here
                               // X509Certificate cert =;
                                String pemFormat = convertToPEM(cert);
                                System.out.println(pemFormat);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
            };*/

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, null, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create URL object
            URL url = new URL(httpsUrl);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Set request properties (optional)
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Print the response
            System.out.println("Response: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String convertToPEM(X509Certificate cert) throws CertificateEncodingException, IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(outStream)) {
            writer.write("-----BEGIN CERTIFICATE-----\n");
            writer.write(Base64.getMimeEncoder().encodeToString(cert.getEncoded()));
            writer.write("\n-----END CERTIFICATE-----\n");
        }
        return outStream.toString();
    }

}
