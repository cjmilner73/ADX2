package sds.com.adx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
 
public class IOStream {
     
    private static final String TAG = "IOStream";

  private static final String FILENAME = "allWithoutHK.txt";
//    private static final String FILENAME = "all.txt";
//   private static final String FILENAME = "oneTick.txt";


    Context thisContext;

    IOStream( Context c ) {
    	
    	thisContext = c;
    	
    }
    
    
    public void writeData() {
    	
    	writeToFile("Chris");
    	
    }
    public ArrayList<Tick> readData() {
		
    	ArrayList<Tick> returnList = new ArrayList<Tick>();
    	
    	returnList = readFromFile();

        return returnList;
    }
    
    
    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(thisContext.openFileOutput(FILENAME, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e(TAG, "File write failed: " + e.toString());
        }
         
    }
 
    private ArrayList<Tick> readFromFile() {
                 
        ArrayList<Tick> returnList = new ArrayList<Tick>();
        
        try {
        	
            AssetManager assetManager = thisContext.getAssets();
            InputStream inputStream = assetManager.open(FILENAME);
//            InputStream inputStream = thisContext.openFileInput(FILENAME);
             
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
                 
                while ( (receiveString = bufferedReader.readLine()) != null ) {
//                    stringBuilder.append(receiveString);
//                	  Log.d(TAG,"Got String: " + receiveString);
                	  returnList.add(new Tick(0,receiveString, "FTSE", 0,999999));
                }
                 
                inputStream.close();
                
            }
        }
        catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e(TAG, "Can not read file: " + e.toString());
        }

        return returnList;
    }
}
