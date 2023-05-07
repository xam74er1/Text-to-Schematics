import fr.xam74er1.text2schem.WebsocketClientEndpoint;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

public class TestApp {

    public static void main(String[] args) {
        try {
            String ip = "a5d49cfb6e8d642a77.gradio.live";
            String userName = "xam74er1";
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("wss://"+ip+"/queue/join"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    System.out.println(message);
                    JSONObject json = new JSONObject(message);
                    if(json.has("msg")){
                        String msg = json.getString("msg");
                        if(msg.equalsIgnoreCase("send_data")){
                            clientEndPoint.sendMessage("{\"fn_index\":0,\"data\":[\"police car\"],\"event_data\":null,\"session_hash\":\""+userName+"\"}");
                        }else if(msg.equalsIgnoreCase("process_completed")){

                            try {


                                // Get the "data" array from the "output" object
                                JSONArray data = json.getJSONObject("output").getJSONArray("data");

                                // Get the "orig_name" field from the first element of the "data" array
                                String origName = data.getJSONObject(0).getString("orig_name");
                                String path = data.getJSONObject(0).getString("name");

                                // Print the value of the "orig_name" field
                                System.out.println("Orig name: " + origName);

                                String fileUrl = "https://"+ip+"/file="+path;
                                clientEndPoint.downloadFile(fileUrl,origName);

                            } catch (Exception e) {
                                System.out.println("Error parsing JSON: " + e.getMessage());
                            }

                        }else if(msg.equalsIgnoreCase("estimation")){
                            int queuSize = json.getInt("queue_size");
                            double time = json.getDouble("avg_event_process_time");
                            System.out.println("prio"+queuSize+" time"+time);

                        }

                    }

                }
            });


            clientEndPoint.sendMessage("{\"fn_index\":0,\"session_hash\":\""+userName+"\"}");
            // send message to websocket


            while (clientEndPoint.getUserSession()!=null){
                Thread.sleep(1000);
            }
            // wait 5 seconds for messages from websocket


        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());
        }
    }
}
