package starter.base.method;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DataUtils {

    private static String response;

    public static String generateStringFromResource(String path) throws IOException {

        byte[] input = Files.readAllBytes(Paths.get(path));
        return new String(input);
    }

    public static String getTestData(String path, String jsonPath) throws IOException {
        response = generateStringFromResource(path);
        JsonPath js = new JsonPath(response);
        return js.getString(jsonPath);
    }

    public static <T> T getDataFromJsonpath(Response request, String jpath) {
        JsonPath jsonpath = request.jsonPath();

        return jsonpath.get(jpath);
    }

    public static void writeUsingFileWriter(String data, String filename) {
        File file = new File(System.getProperty("user.dir") + "/src/test/java/outputfile/"+filename+".txt");
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            fr.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //close resources
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readUsingFileReader(String fileName) throws IOException {
        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while((line = br.readLine()) != null){
            //process the line
            System.out.println(line);
            return line;
        }
        br.close();
        fr.close();
        return null;
    }
}