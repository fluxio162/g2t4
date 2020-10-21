package com.example.setup;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

/**
 *  This Class is based on the Restclass we made during the workshop.
 *  It sends the Data one time and the Server takes it instantly.
 */
public class RestClient implements Runnable {

    private final String urlset;
    private final String timeflipMac;
    private final String content;
    private final CredentialsProvider credentialsProvider;

    /**
     * This is our Constructor for our RestClient.
     * @param timeflipMac The MAC Adress of the cube.
     * @param content The content which we will send. Can be a Warning or the acutal data of one side.
     * @param urli The url which we send the Data.
     */
    public RestClient(String timeflipMac, String content, String urli) {
        this.timeflipMac = timeflipMac;
        this.content = content;
        credentialsProvider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials("admin", "passwd");
        credentialsProvider.setCredentials(AuthScope.ANY, credentials);
        this.urlset = "http://" + urli + "/rest";
    }
    @Override
    public void run(){
        try {
            sendData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This sends the data to the RestServer
     * @return True if it works or false if it didn't work.
     * @throws IOException
     */
    public boolean sendData() throws IOException {

        HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(credentialsProvider).build();

        HttpPost httpPost = new HttpPost(urlset);
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("sender", "admin");
        requestJson.put("timeflipMac", timeflipMac);
        requestJson.put("content",content);


        httpPost.setEntity(new StringEntity(requestJson.toString()));

        HttpResponse response = httpClient.execute(httpPost);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            HttpEntity entity = response.getEntity();
            String responseString = IOUtils.toString(entity.getContent());
            JSONObject responseJson = new JSONObject(responseString);

            String mac = responseJson.getString("timeflipMac");
            String content = responseJson.getString("content");

            return true;
        } else {
            System.err.printf("Error with sending");
            return false;
        }
    }

}
