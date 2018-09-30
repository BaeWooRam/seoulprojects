package com.example.myapplication;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication.bin.InfoBoardBin;
import com.example.myapplication.bin.SponsorBoardBin;
import com.example.myapplication.bin.mapPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Spon_JSONTaskAndParsor extends AsyncTask<String, String, String> {
    public final static String INFOBOARD_CATEGORY_URL = "http://182.222.75.26:3000/infoboard/";
    public final static String INFOBOARD_SEARCH_URL = "http://182.222.75.26:3000/infoboard/search/";
    public final static String INFOBOARD_ALL_URL = "http://182.222.75.26:3000/infoboard";

    ArrayList<SponsorBoardBin> slist;
    ArrayList<InfoBoardBin> list;




    public Spon_JSONTaskAndParsor(ArrayList<SponsorBoardBin> slist, ArrayList<InfoBoardBin> list ) {
        this.list = list;
        this.slist = slist;

    }

    @Override
    protected String doInBackground(String... urls) {
        list = new ArrayList<InfoBoardBin>();

        try {
            //JSONObject를 만들고 key value 형식으로 값을 저장해준다.
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("user_id", "android");

            HttpURLConnection con = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(urls[0]);//url을 가져온다.
                con = (HttpURLConnection) url.openConnection();
                con.connect();//연결 수행

                //입력 스트림 생성
                InputStream stream = con.getInputStream();

                //속도를 향상시키고 부하를 줄이기 위한 버퍼를 선언한다.
                reader = new BufferedReader(new InputStreamReader(stream));

                //실제 데이터를 받는곳
                StringBuffer buffer = new StringBuffer();

                //line별 스트링을 받기 위한 temp 변수
                String line = "";

                //아래라인은 실제 reader에서 데이터를 가져오는 부분이다. 즉 node.js서버로부터 데이터를 가져온다.
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                //다 가져오면 String 형변환을 수행한다. 이유는 protected String doInBackground(String... urls) 니까
                return buffer.toString();

                //아래는 예외처리 부분이다.
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                //종료가 되면 disconnect메소드를 호출한다.
                if (con != null) {
                    con.disconnect();
                }
                try {
                    //버퍼를 닫아준다.
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }//finally 부분
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //doInBackground메소드가 끝나면 여기로 와서 텍스트뷰의 값을 바꿔준다.
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(JsonParser_SponsorInfo(result,slist) > 0){
            String output = "";
            for(SponsorBoardBin bin: slist){
                output += "\n글쓴이 : "+bin.getWriter()+"\n제목 : "+bin.getTitle()+"\n\n";
            }
            Log.i("dkanrjsk",output);
        }
    }

    //각 파서들 넣으면 될듯
    public int JsonParser_News(String result, ArrayList<InfoBoardBin> list, Context context) {
        try {
            JSONArray array = new JSONArray(result);

            //System.out.println(array.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject ob = array.getJSONObject(i);

                InfoBoardBin bin = new InfoBoardBin();
                bin.setNum(ob.getInt("num"));
                bin.setTitle(ob.getString("title"));
                bin.setWriter(ob.getString("writer"));
                bin.setAddress(ob.getString("address"));
                bin.setContent(ob.getString("content"));
                bin.setContents_uri(ob.getString("sthumbnail_url"));
                bin.setSthumbnail_uri(ob.getString("content_url"));

                list.add(bin);
            }
        } catch (Exception e) {
            System.out.println("Error : " + e.toString());
            return 0;
        }
        return list.size();
    }

    //각 파서들 넣으면 될듯
    public int JsonParser_SponsorInfo(String result, ArrayList<SponsorBoardBin> list) {
        try {
            JSONArray array = new JSONArray(result);

            //System.out.println(array.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject ob = array.getJSONObject(i);

                SponsorBoardBin bin = new SponsorBoardBin();
                bin.setNum(ob.getInt("num"));
                bin.setTitle(ob.getString("title"));
                bin.setWriter(ob.getString("writer"));
                bin.setContent(ob.getString("content"));
                bin.setContents_uri(ob.getString("sthumbnail_url"));
                bin.setSthumbnail_uri(ob.getString("content_url"));

                list.add(bin);

            }

        } catch (Exception e) {
            System.out.println("Error : " + e.toString());
            return 0;
        }
        return list.size();
    }

    public mapPoint getMapPoint(String address, Context context) {
        Log.i("address",address);
        final Geocoder geocoder = new Geocoder(context);
        try {
            List<Address> list = geocoder.getFromLocationName(address, 10); // 읽을 개수
            if (list.size() > 0) {
                Address addr = list.get(0);
                Log.i("latitude and longitude",""+addr.getLatitude()+addr.getLongitude());
                double lat = addr.getLatitude();
                double lon = addr.getLongitude();

                return new mapPoint(lat, lon);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        return null;
    }
}



