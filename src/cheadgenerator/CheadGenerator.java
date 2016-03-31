/*
 * Copyright (C) 2016 MCME
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cheadgenerator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Eriol_Eandur
 */
public class CheadGenerator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length==0) {
            Logger.getGlobal().info("No data file specified.");
            return;
        }
        File source = new File(args[0]);
        if(!source.exists()) {
            Logger.getGlobal().info("File not found.");
            return;
        }
        try {
            try (Scanner scanner = new Scanner(source)) {
                while(scanner.hasNext()) {
                    String line = scanner.nextLine();
                    line = line.substring(line.indexOf('{'));
    Logger.getGlobal().info(line);                
                    JsonObject jInput = (JsonObject) new JsonParser().parse(line);
                    JsonObject jDisplay = (JsonObject) jInput.get("display");
                    String name = jDisplay.get("Name").getAsString();
    Logger.getGlobal().info(name);                

                    JsonObject jOwner = (JsonObject) jInput.get("SkullOwner");
                    String uuidString = jOwner.get("Id").getAsString();
    Logger.getGlobal().info(uuidString);                

                    JsonObject jProperties = (JsonObject) jOwner.get("Properties");
                    JsonArray jTextures = (JsonArray) jProperties.get("textures");
                    JsonObject jUrl = (JsonObject) jTextures.get(0);
                    String textureUrl = jUrl.get("Value").getAsString();
    Logger.getGlobal().info(textureUrl);                
                    createDataFile(name, uuidString, textureUrl);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CheadGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static void createDataFile(String name, String uuidString, String textureUrl) {
        if(name.endsWith("/")) {
            name = name.substring(0,name.length()-1);
        }
        int separator = name.lastIndexOf("/");
        if(separator > -1) {
            String path = name.substring(0, separator);
            File subDir = new File("accepted/"+path);
            if(!subDir.exists()) {
                subDir.mkdirs();
            }
        }
        File file = new File("accepted/"+name+".yml");
        try {
            try(    FileWriter fw = new FileWriter(file);
                    PrintWriter writer = new PrintWriter(fw)) {
                writer.println("owner: "+uuidString);
                writer.println("headId: "+uuidString);
                writer.println("texture: "+textureUrl);
            }
        } catch (IOException ex) {
            Logger.getLogger(CheadGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
