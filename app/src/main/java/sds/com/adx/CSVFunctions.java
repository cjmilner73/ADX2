package sds.com.adx;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;
 
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
 
public class CSVFunctions {

	static DatabaseHandler db;

	private static final String TAG = "CSVFunctions";

	public static String getStringfromURL(Context thisContext, String url, String symbol, String type){
    
        InputStream is = null;
        JSONObject jArray = null;
        StringBuilder sb = new StringBuilder();
        final String TAG = "CSVFunctions";

//		Log.d(TAG,"URL: " + url);

		String line = null;
		String res = null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

		db = new DatabaseHandler(thisContext);

		// Download JSON data from URL
        try{
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent(); 
        }catch(Exception e){
                Log.d(TAG, "Error in http connection " + e.toString());
        }
 
        // Convert response to string
        try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			if ( type.equals("Intra") ) {

				while ((line = reader.readLine()) != null) {

					sb.append(line + "\n");
//					Log.d(TAG,"sb.append = " + sb.toString());
				}
			} else {

				while ((line = reader.readLine()) != null) {
					if (! line.contains("Date") ) {
						db.insertLine(symbol, line);
						Log.d(TAG,"Insert Line: " + line);
					}
				}
			}
                is.close();
 

        }catch(Exception e){
//                Log.e(TAG, "Error converting result " + e.toString());
        }
 
//        try{
// 
//            jArray = new JSONObject(result);
//            
//        }catch(JSONException e){
//                Log.e("log_tag", "Error parsing data "+e.toString());
//        }
//        Log.d("CSV","sb: " + sb.toString());

        if ( sb != null ) {
			res = sb.toString();
			sb.setLength(0);
            return res;
        } else { return "NULL"; }
    }
    
    public static Vector getQuotesStatic() {

		Vector allQuotes = new Vector();



//		Quote val1 = new Quote(0,"RBS","2013-07-19",30.20f,29.41f,29.87f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val2 = new Quote(0,"RBS","2013-07-22",30.28f,29.32f,30.24f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val3 = new Quote(0,"RBS","2013-07-23",30.45f,29.96f,30.10f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val4 = new Quote(0,"RBS","2013-07-24",29.35f,28.74f,28.90f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val5 = new Quote(0,"RBS","2013-07-25",29.35f,28.56f,28.92f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val6 = new Quote(0,"RBS","2013-07-26",29.29f,28.41f,28.48f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val7 = new Quote(0,"RBS","2013-07-29",28.83f,28.08f,28.56f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val8 = new Quote(0,"RBS","2013-07-30",28.73f,27.43f,27.56f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val9 = new Quote(0,"RBS","2013-07-31",28.67f,27.66f,28.47f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val10 = new Quote(0,"RBS","2013-08-02",28.85f,27.83f,28.28f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val11 = new Quote(0,"RBS","2013-08-05",28.64f,27.40f,27.49f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val12 = new Quote(0,"RBS","2013-08-06",27.68f,27.09f,27.23f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val13 = new Quote(0,"RBS","2013-08-07",27.21f,26.18f,26.35f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val14 = new Quote(0,"RBS","2013-08-08",26.87f,26.13f,26.33f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val15 = new Quote(0,"RBS","2013-08-09",27.41f,26.63f,27.03f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val16 = new Quote(0,"RBS","2013-08-12",26.94f,26.13f,26.22f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val17 = new Quote(0,"RBS","2013-08-13",26.52f,25.43f,26.01f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val18 = new Quote(0,"RBS","2013-08-14",26.52f,25.35f,25.46f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val19 = new Quote(0,"RBS","2013-08-15",27.09f,25.88f,27.03f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val20 = new Quote(0,"RBS","2013-08-16",27.69f,26.96f,27.45f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val21 = new Quote(0,"RBS","2013-08-19",28.45f,27.14f,28.36f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val22 = new Quote(0,"RBS","2013-08-20",28.53f,28.01f,28.43f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val23 = new Quote(0,"RBS","2013-08-21",28.67f,27.88f,27.95f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val24 = new Quote(0,"RBS","2013-08-22",29.01f,27.99f,29.01f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val25 = new Quote(0,"RBS","2013-08-23",29.87f,28.76f,29.38f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val26 = new Quote(0,"RBS","2013-08-26",29.80f,29.14f,29.36f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val27 = new Quote(0,"RBS","2013-08-27",29.75f,28.71f,28.91f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val28 = new Quote(0,"RBS","2013-08-28",30.65f,28.93f,30.61f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val29 = new Quote(0,"RBS","2013-08-29",30.60f,30.03f,30.05f,0.0f, 0.0f, 0.0f,0.0f,0.0f);
//		Quote val30 = new Quote(0,"RBS","2013-08-30",30.76f,29.39f,30.19f,0.0f, 0.0f, 0.0f,0.0f,0.0f);

		Quote val1 = new Quote(0,"RBS" ,"2013-07-19", 30.20, 29.41, 29.87, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val2 = new Quote(0,"RBS" ,"2013-07-22", 30.28, 29.32, 30.24, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val3 = new Quote(0,"RBS" ,"2013-07-23", 30.45, 29.96, 30.10, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val4 = new Quote(0,"RBS" ,"2013-07-24", 29.35, 28.74, 28.90, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val5 = new Quote(0,"RBS" ,"2013-07-25", 29.35, 28.56, 28.92, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val6 = new Quote(0,"RBS" ,"2013-07-26", 29.29, 28.41, 28.48, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val7 = new Quote(0,"RBS" ,"2013-07-29", 28.83, 28.08, 28.56, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val8 = new Quote(0,"RBS" ,"2013-07-30", 28.73, 27.43, 27.56, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val9 = new Quote(0,"RBS" ,"2013-07-31", 28.67, 27.66, 28.47, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val10 = new Quote(0,"RBS" ,"2013-08-02", 28.85, 27.83, 28.28, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val11 = new Quote(0,"RBS" ,"2013-08-05", 28.64, 27.40, 27.49, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val12 = new Quote(0,"RBS" ,"2013-08-06", 27.68, 27.09, 27.23, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val13 = new Quote(0,"RBS" ,"2013-08-07", 27.21, 26.18, 26.35, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val14 = new Quote(0,"RBS" ,"2013-08-08", 26.87, 26.13, 26.33, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val15 = new Quote(0,"RBS" ,"2013-08-09", 27.41, 26.63, 27.03, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val16 = new Quote(0,"RBS" ,"2013-08-12", 26.94, 26.13, 26.22, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val17 = new Quote(0,"RBS" ,"2013-08-13", 26.52, 25.43, 26.01, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val18 = new Quote(0,"RBS" ,"2013-08-14", 26.52, 25.35, 25.46, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val19 = new Quote(0,"RBS" ,"2013-08-15", 27.09, 25.88, 27.03, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val20 = new Quote(0,"RBS" ,"2013-08-16", 27.69, 26.96, 27.45, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val21 = new Quote(0,"RBS" ,"2013-08-19", 28.45, 27.14, 28.36, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val22 = new Quote(0,"RBS" ,"2013-08-20", 28.53, 28.01, 28.43, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val23 = new Quote(0,"RBS" ,"2013-08-21", 28.67, 27.88, 27.95, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val24 = new Quote(0,"RBS" ,"2013-08-22", 29.01, 27.99, 29.01, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val25 = new Quote(0,"RBS" ,"2013-08-23", 29.87, 28.76, 29.38, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val26 = new Quote(0,"RBS" ,"2013-08-26", 29.80, 29.14, 29.36, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val27 = new Quote(0,"RBS" ,"2013-08-27", 29.75, 28.71, 28.91, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val28 = new Quote(0,"RBS" ,"2013-08-28", 30.65, 28.93, 30.61, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val29 = new Quote(0,"RBS" ,"2013-08-29", 30.60, 30.03, 30.05, 0.0, 0.0, 0.0, 0.0, 0);
		Quote val30 = new Quote(0,"RBS" ,"2013-08-30", 30.76, 29.39, 30.19, 0.0, 0.0, 0.0, 0.0, 0);

//		Quote val1 = new Quote(0,"RBS","2013-07-19",30.20f,29.41f,29.87f,0.0f, 0.0f, 0.0f);
//		Quote val2 = new Quote(0,"RBS","2013-07-22",30.28f,29.32f,30.24f,0.0f, 0.0f, 0.0f);
//		Quote val3 = new Quote(0,"RBS","2013-07-23",30.45f,29.96f,30.10f,0.0f, 0.0f, 0.0f);
//		Quote val4 = new Quote(0,"RBS","2013-07-24",29.35f,28.74f,28.90f,0.0f, 0.0f, 0.0f);
//		Quote val5 = new Quote(0,"RBS","2013-07-25",29.35f,28.56f,28.92f,0.0f, 0.0f, 0.0f);
//		Quote val6 = new Quote(0,"RBS","2013-07-26",29.29f,28.41f,28.48f,0.0f, 0.0f, 0.0f);
//		Quote val7 = new Quote(0,"RBS","2013-07-29",28.83f,28.08f,28.56f,0.0f, 0.0f, 0.0f);
//		Quote val8 = new Quote(0,"RBS","2013-07-30",28.73f,27.43f,27.56f,0.0f, 0.0f, 0.0f);
//		Quote val9 = new Quote(0,"RBS","2013-07-31",28.67f,27.66f,28.47f,0.0f, 0.0f, 0.0f);
//		Quote val10 = new Quote(0,"RBS","2013-08-02",28.85f,27.83f,28.28f,0.0f, 0.0f, 0.0f);
//		Quote val11 = new Quote(0,"RBS","2013-08-05",28.64f,27.40f,27.49f,0.0f, 0.0f, 0.0f);
//		Quote val12 = new Quote(0,"RBS","2013-08-06",27.68f,27.09f,27.23f,0.0f, 0.0f, 0.0f);
//		Quote val13 = new Quote(0,"RBS","2013-08-07",27.21f,26.18f,26.35f,0.0f, 0.0f, 0.0f);
//		Quote val14 = new Quote(0,"RBS","2013-08-08",26.87f,26.13f,26.33f,0.0f, 0.0f, 0.0f);
//		Quote val15 = new Quote(0,"RBS","2013-08-09",27.41f,26.63f,27.03f,0.0f, 0.0f, 0.0f);
//		Quote val16 = new Quote(0,"RBS","2013-08-12",26.94f,26.13f,26.22f,0.0f, 0.0f, 0.0f);
//		Quote val17 = new Quote(0,"RBS","2013-08-13",26.52f,25.43f,26.01f,0.0f, 0.0f, 0.0f);
//		Quote val18 = new Quote(0,"RBS","2013-08-14",26.52f,25.35f,25.46f,0.0f, 0.0f, 0.0f);
//		Quote val19 = new Quote(0,"RBS","2013-08-15",27.09f,25.88f,27.03f,0.0f, 0.0f, 0.0f);
//		Quote val20 = new Quote(0,"RBS","2013-08-16",27.69f,26.96f,27.45f,0.0f, 0.0f, 0.0f);
//		Quote val21 = new Quote(0,"RBS","2013-08-19",28.45f,27.14f,28.36f,0.0f, 0.0f, 0.0f);
//		Quote val22 = new Quote(0,"RBS","2013-08-20",28.53f,28.01f,28.43f,0.0f, 0.0f, 0.0f);
//		Quote val23 = new Quote(0,"RBS","2013-08-21",28.67f,27.88f,27.95f,0.0f, 0.0f, 0.0f);
//		Quote val24 = new Quote(0,"RBS","2013-08-22",29.01f,27.99f,29.01f,0.0f, 0.0f, 0.0f);
//		Quote val25 = new Quote(0,"RBS","2013-08-23",29.87f,28.76f,29.38f,0.0f, 0.0f, 0.0f);
//		Quote val26 = new Quote(0,"RBS","2013-08-26",29.80f,29.14f,29.36f,0.0f, 0.0f, 0.0f);
//		Quote val27 = new Quote(0,"RBS","2013-08-27",29.75f,28.71f,28.91f,0.0f, 0.0f, 0.0f);
//		Quote val28 = new Quote(0,"RBS","2013-08-28",30.65f,28.93f,30.61f,0.0f, 0.0f, 0.0f);
//		Quote val29 = new Quote(0,"RBS","2013-08-29",30.60f,30.03f,30.05f,0.0f, 0.0f, 0.0f);
//		Quote val30 = new Quote(0,"RBS","2013-08-30",30.76f,29.39f,30.19f,0.0f, 0.0f, 0.0f);
//		Quote val31 = new Quote(0,"MKS","01-01-2013",30.20f,29.41f,29.87f,0.0f, 0.0f, 0.0f);
//		Quote val32 = new Quote(0,"MKS","01-01-2013",30.28f,29.32f,30.24f,0.0f, 0.0f, 0.0f);
//		Quote val33 = new Quote(0,"MKS","01-01-2013",30.45f,29.96f,30.10f,0.0f, 0.0f, 0.0f);
//		Quote val34 = new Quote(0,"MKS","01-01-2013",29.35f,28.74f,28.90f,0.0f, 0.0f, 0.0f);
//		Quote val35 = new Quote(0,"MKS","01-01-2013",29.35f,28.56f,28.92f,0.0f, 0.0f, 0.0f);
//		Quote val36 = new Quote(0,"MKS","01-01-2013",29.29f,28.41f,28.48f,0.0f, 0.0f, 0.0f);
//		Quote val37 = new Quote(0,"MKS","01-01-2013",28.83f,28.08f,28.56f,0.0f, 0.0f, 0.0f);
//		Quote val38 = new Quote(0,"MKS","01-01-2013",28.73f,27.43f,27.56f,0.0f, 0.0f, 0.0f);
//		Quote val39 = new Quote(0,"MKS","01-01-2013",28.67f,27.66f,28.47f,0.0f, 0.0f, 0.0f);
//		Quote val40 = new Quote(0,"MKS","01-01-2013",28.85f,27.83f,28.28f,0.0f, 0.0f, 0.0f);
//		Quote val41 = new Quote(0,"MKS","01-01-2013",28.64f,27.40f,27.49f,0.0f, 0.0f, 0.0f);
//		Quote val42 = new Quote(0,"MKS","01-01-2013",27.68f,27.09f,27.23f,0.0f, 0.0f, 0.0f);
//		Quote val43 = new Quote(0,"MKS","01-01-2013",27.21f,26.18f,26.35f,0.0f, 0.0f, 0.0f);
//		Quote val44 = new Quote(0,"MKS","01-01-2013",26.87f,26.13f,26.33f,0.0f, 0.0f, 0.0f);
//		Quote val45 = new Quote(0,"MKS","01-01-2013",27.41f,26.63f,27.03f,0.0f, 0.0f, 0.0f);
//		Quote val46 = new Quote(0,"MKS","01-01-2013",26.94f,26.13f,26.22f,0.0f, 0.0f, 0.0f);
//		Quote val47 = new Quote(0,"MKS","01-01-2013",26.52f,25.43f,26.01f,0.0f, 0.0f, 0.0f);
//		Quote val48 = new Quote(0,"MKS","01-01-2013",26.52f,25.35f,25.46f,0.0f, 0.0f, 0.0f);
//		Quote val49 = new Quote(0,"MKS","01-01-2013",27.09f,25.88f,27.03f,0.0f, 0.0f, 0.0f);
//		Quote val50 = new Quote(0,"MKS","01-01-2013",27.69f,26.96f,27.45f,0.0f, 0.0f, 0.0f);
//		Quote val51 = new Quote(0,"MKS","01-01-2013",28.45f,27.14f,28.36f,0.0f, 0.0f, 0.0f);
//		Quote val52 = new Quote(0,"MKS","01-01-2013",28.53f,28.01f,28.43f,0.0f, 0.0f, 0.0f);
//		Quote val53 = new Quote(0,"MKS","01-01-2013",28.67f,27.88f,27.95f,0.0f, 0.0f, 0.0f);
//		Quote val54 = new Quote(0,"MKS","01-01-2013",29.01f,27.99f,29.01f,0.0f, 0.0f, 0.0f);
//		Quote val55 = new Quote(0,"MKS","01-01-2013",29.87f,28.76f,29.38f,0.0f, 0.0f, 0.0f);
//		Quote val56 = new Quote(0,"MKS","01-01-2013",29.80f,29.14f,29.36f,0.0f, 0.0f, 0.0f);
//		Quote val57 = new Quote(0,"MKS","01-01-2013",29.75f,28.71f,28.91f,0.0f, 0.0f, 0.0f);
//		Quote val58 = new Quote(0,"MKS","01-01-2013",30.65f,28.93f,30.61f,0.0f, 0.0f, 0.0f);
//		Quote val59 = new Quote(0,"MKS","01-01-2013",30.60f,30.03f,30.05f,0.0f, 0.0f, 0.0f);
//		Quote val60 = new Quote(0,"MKS","01-01-2013",30.76f,29.39f,30.19f,0.0f, 0.0f, 0.0f);
		allQuotes.add(val30);
		allQuotes.add(val29);
		allQuotes.add(val28);
		allQuotes.add(val27);
		allQuotes.add(val26);
		allQuotes.add(val25);
		allQuotes.add(val24);
		allQuotes.add(val23);
		allQuotes.add(val22);
		allQuotes.add(val21);
		allQuotes.add(val20);
		allQuotes.add(val19);
		allQuotes.add(val18);
		allQuotes.add(val17);
		allQuotes.add(val16);
		allQuotes.add(val15);
		allQuotes.add(val14);
		allQuotes.add(val13);
		allQuotes.add(val12);
		allQuotes.add(val11);
		allQuotes.add(val10);
		allQuotes.add(val9);
		allQuotes.add(val8);
		allQuotes.add(val7);
		allQuotes.add(val6);
		allQuotes.add(val5);
		allQuotes.add(val4);
		allQuotes.add(val3);
		allQuotes.add(val2);
		allQuotes.add(val1);
//		allQuotes.add(val31);
//		allQuotes.add(val32);
//		allQuotes.add(val33);
//		allQuotes.add(val34);
//		allQuotes.add(val35);
//		allQuotes.add(val36);
//		allQuotes.add(val37);
//		allQuotes.add(val38);
//		allQuotes.add(val39);
//		allQuotes.add(val40);
//		allQuotes.add(val41);
//		allQuotes.add(val42);
//		allQuotes.add(val43);
//		allQuotes.add(val44);
//		allQuotes.add(val45);
//		allQuotes.add(val46);
//		allQuotes.add(val47);
//		allQuotes.add(val48);
//		allQuotes.add(val49);
//		allQuotes.add(val50);
//		allQuotes.add(val51);
//		allQuotes.add(val52);
//		allQuotes.add(val53);
//		allQuotes.add(val54);
//		allQuotes.add(val55);
//		allQuotes.add(val56);
//		allQuotes.add(val57);
//		allQuotes.add(val58);
//		allQuotes.add(val59);
//		allQuotes.add(val60);


    	return allQuotes;
	
    }


}
