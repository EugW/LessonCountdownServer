package pro.eugw.lessoncountdownserver;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Objects;

import static pro.eugw.lessoncountdownserver.Main.log;

class RequestHandler {

    RequestHandler(Socket socket) throws IOException {
        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        PrintWriter printWriter = new PrintWriter(out, true);
        switch (bufferedReader.readLine()) {
            case "T": {
                log().debug("T accept");
                String Class = bufferedReader.readLine();
                Integer Sub = Integer.valueOf(bufferedReader.readLine());
                log().debug(Class);
                log().debug(Sub);
                File file = initTable(Class);
                JsonObject object = new JsonParser().parse(new FileReader(file)).getAsJsonObject();
                JsonObject finO = new JsonObject();
                for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
                    JsonArray array = entry.getValue().getAsJsonArray();
                    JsonArray finA = new JsonArray();
                    for (JsonElement element : array) {
                        String[] s = element.getAsString().split("-");
                        if (s[2].contains("/")) {
                            s[2] = s[2].split("/")[Sub - 1];
                        }
                        finA.add(s[0] + "-" + s[1] + "-" + s[2]);
                    }
                    finO.add(entry.getKey(), finA);
                }
                log().debug(finO);
                printWriter.println(finO);
                in.close();
                out.close();
            }
            break;
            case "H": {
                log().debug("H accept");
                switch (bufferedReader.readLine()) {
                    case "G": {
                        log().debug("G accept");
                        in.close();
                        out.close();
                    }
                    break;
                }
            }
            break;
            case "U": {
                log().debug("U accept");
            }
            break;
            case "M": {
                log().debug("M accept");
                File file = new File("table", bufferedReader.readLine());
                if (!file.exists())
                    printWriter.println(0);
                StringBuilder hexString = new StringBuilder();
                try {
                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] byteData = md.digest(new JsonParser().parse(new FileReader(file)).getAsJsonObject().toString().getBytes());
                    hexString = new StringBuilder();
                    for (byte aByteData : byteData) {
                        String hex = Integer.toHexString(0xff & aByteData);
                        if (hex.length() == 1) hexString.append('0');
                        hexString.append(hex);
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                log().debug(hexString);
                String hex = bufferedReader.readLine();
                log().debug(hex);
                printWriter.println(Objects.equals(hex, hexString.toString()));
            }
            break;
        }
    }

    private File initHomework(String s) {
        File dir = new File("homework");
        File file = new File(dir, s);
        return initFile(file, dir);
    }

    private File initTable(String s) {
        File dir = new File("table");
        File file = new File(dir, s);
        return initFile(dir, file);
    }

    private File initFile(File dir, File file) {
        if (!dir.exists())
            if (dir.mkdirs())
                System.out.println("CRT");
        if (!file.exists())
            try {
                if (file.createNewFile())
                    System.out.println("CRT");
            } catch (IOException e) {
                e.printStackTrace();
            }
        return file;

    }

}