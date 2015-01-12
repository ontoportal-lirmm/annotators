package org.sifrproject.annotators;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;

//TODO remove: import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
//TODO remove : import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class AnnotatorUtils {

    private static String bioPortalUri = "http://bioportal.lirmm.fr:8082";
    private static String apiKey = "aa5b0e2c-5a2e-4a01-b3a2-32cf024f3f27";

    public static String getBioPortalOntologiesURLParameter(String group) {
        List<String[]> listOnto = AnnotatorUtils.getBioPortalOntologiesWithIds(group);

        String url = "ontologies=";
        for (int i = 0; i < listOnto.size(); i++) {
            if (i == (listOnto.size() - 1))
                url += listOnto.get(i)[1];
            else
                url += listOnto.get(i)[1] + ",";
        }
        return url;
    }

    public static List<String[]> getBioPortalOntologiesWithIds(String group) {
        List<String[]> ontologies = new ArrayList<String[]>();

        // query bioportal
        JsonArray results = null;
        CloseableHttpClient client = HttpClientBuilder.create().build(); //TODO remove - new DefaultHttpClient();
        HttpGet method = null;
        method = new HttpGet(bioPortalUri + "/ontologies?apikey=" + apiKey);

        // Execute the POST method
        CloseableHttpResponse response = null;
        try {
            response = client.execute(method);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStreamReader is = new InputStreamReader(response.getEntity().getContent());

            /*
             * BufferedReader in = new BufferedReader(is); String inputLine;
             * while ((inputLine = in.readLine()) != null)
             * System.out.println(inputLine); in.close();
             */

            JsonReader rdr = Json.createReader(is);
            results = rdr.readArray();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            response.close();
            //TODO - remove: EntityUtils.consume(response.getEntity());
            //TODO - remove: response.getEntity().consumeContent();
        }catch (IOException e1) {
            e1.printStackTrace();
        }
        //

        for (JsonObject result : results.getValuesAs(JsonObject.class)) {

            // filter, get only IBC group ontologies
            // "Group uri : "+result.getJsonObject("links").getString("groups"),
            // true);
            JsonStructure groupResult = null;
            method = new HttpGet(result.getJsonObject("links").getString("groups") + "?apikey=" + apiKey);
            // Execute the POST method
            response = null;
            try {
                response = client.execute(method);
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                InputStreamReader is = new InputStreamReader(response.getEntity().getContent());

                JsonReader rdr = Json.createReader(is);
                groupResult = rdr.read();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                response.close(); //TODO - remove: getEntity().consumeContent();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            //
            //

            if (groupResult instanceof JsonObject) {
                if (((JsonObject) groupResult).getString("acronym").equals(group))
                    ontologies.add(new String[] { result.getString("name"), result.getString("@id").substring(result.getString("@id").lastIndexOf('/') + 1) });
            } else if (groupResult instanceof JsonArray) {
                JsonArray arr = (JsonArray) groupResult;
                for (int i = 0; i < arr.size(); i++) {
                    if (arr.getJsonObject(i).getString("acronym").equals(group))
                        ontologies
                                .add(new String[] { result.getString("name"), result.getString("@id").substring(result.getString("@id").lastIndexOf('/') + 1) });
                }
            }
        }
        
        try{
            client.close(); // TODO - remove: getConnectionManager().shutdown();
        }catch(IOException e){
            e.printStackTrace();
        }

        return ontologies;
    }

    public static String getBioPortalUri() {
        return bioPortalUri;
    }
}
