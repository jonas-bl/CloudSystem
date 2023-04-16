package dev.javaprojekt.cloudsystem.rest;

import com.google.gson.Gson;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServer;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerInfo;
import dev.javaprojekt.cloudsystem.cloud.server.CloudServerManager;
import dev.javaprojekt.cloudsystem.cloud.server.ServerTemplateManager;
import dev.javaprojekt.cloudsystem.cloud.util.logger.CloudLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static spark.Spark.*;

public class RestApplication {

    public static void start() {
        port(8080);

        Gson gson = new Gson();


        System.out.println("Started");

        get("/servers", (request, response) -> {
            HashMap<String, ArrayList<String>> list = new HashMap<>();
            ServerTemplateManager.getInstance().getTemplates().keySet().forEach(tstring -> {
                list.put(tstring, new ArrayList<>());
                CloudServerManager.getInstance().getServersByTemplate(tstring).forEach(s -> list.get(tstring).add(s.getServerUUID().toString()));
            });
            String jsonString = gson.toJson(list);
            response.type("application/json");
            return jsonString;
        });

        get("/serverinfo", (request, response) -> {
            String serveruuid = request.headers("uniqueid");
            CloudServer server = CloudServerManager.getInstance().getServerByUUID(UUID.fromString(serveruuid));
            if (server == null) {
                return "Error";
            }
            CloudServerInfo info = server.getCloudServerInfo();
            HashMap<String, Object> serverinfo = new HashMap<>();
            serverinfo.put("UUID", server.getServerUUID());
            serverinfo.put("Name", server.getName());
            serverinfo.put("Port", server.getPort());
            serverinfo.put("Address", server.getAdress());
            serverinfo.put("OnlinePlayers", info.getOnlinePlayers());
            serverinfo.put("OnlineCount", info.getOnlinePlayers().size());
            serverinfo.put("MaxPlayers", info.getMaxPlayers());
            serverinfo.put("Status", server.getServerState());
            serverinfo.put("MOTD", info.getMotd());
            serverinfo.put("Extra", info.getExtra());
            String jsonString = gson.toJson(serverinfo);
            response.type("application/json");
            return jsonString;
        });

        // Define a password-protected route
        before("/*", (request, response) -> {
            // Get the Basic Auth credentials from the request header
            String authHeader = request.headers("Authorization");
            if (authHeader == null) {
                // Unauthorized access, send 401 response
                response.status(401);
                //response.header("Authentication", "Invalid");
                response.body("Unauthorized");
                halt();
            } else {
                // Decode the credentials from the request header
                String token = new String(java.util.Base64.getDecoder().decode(authHeader));

                // Validate the username and password
                if (!validateCredentials(token)) {
                    // Invalid credentials, send 401 response
                    response.status(401);
                    // response.header("Authentication", "Invalid");
                    response.body("Unauthorized");
                    halt();
                }
            }
        });
    }

    public static void main(String[] args) {
        start();
    }

    private static boolean validateCredentials(String token) {
        // MzR6dHVyZ2hmZGprbHNpZXc4MzQ3ejV0cnVnZGZoanNpdWVyejg3NDU2ODNld3Vpc2RoZg==
        return token.equals("34zturghfdjklsiew8347z5trugdfhjsiuerz8745683ewuisdhf");
    }

    public static void shutdown() {
        stop();
        CloudLogger.getInstance().log("REST Service stopped!");
    }
}
