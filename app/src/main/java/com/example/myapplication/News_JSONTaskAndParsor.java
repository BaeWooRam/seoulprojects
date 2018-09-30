package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.example.myapplication.adapter.RecyclerAdapter;
import com.example.myapplication.bin.InfoBoardBin;

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

public class News_JSONTaskAndParsor extends AsyncTask<String, String, String> {
    public final static String INFOBOARD_CATEGORY_URL = "http://182.222.75.26:3000/infoboard/category/";
    public final static String INFOBOARD_SEARCH_URL = "http://182.222.75.26:3000/infoboard/search/";
    public final static String INFOBOARD_ALL_URL = "http://182.222.75.26:3000/infoboard";
    public final static String INFOBOARD_NUMBER_URL = "http://182.222.75.26:3000/infoboard/";

    RecyclerView newsrecycler;
    ArrayList<InfoBoardBin> list;
    RecyclerAdapter adapter;


    public News_JSONTaskAndParsor(RecyclerView newsrecycler, ArrayList<InfoBoardBin> list,RecyclerAdapter adapter) {
        this.list = list;
        this.newsrecycler = newsrecycler;
        this.adapter = adapter;
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
        int size = JsonParser_News(result,list);
        if(size > 0){
            String output = "";

            for(InfoBoardBin bin: list){
                output += "\n글쓴이 : "+bin.getWriter()+"\n제목 : "+bin.getTitle()+"\n\n";
            }
            adapter.setItems(list);
            adapter.notifyDataSetChanged();
        }
    }

    //각 파서들 넣으면 될듯
    public int JsonParser_News(String result, ArrayList<InfoBoardBin> list) {
        try {
            JSONArray array = new JSONArray(result);

            for (int i = 0; i < array.length(); i++) {
                JSONObject ob = array.getJSONObject(i);

                InfoBoardBin bin = new InfoBoardBin();
                bin.setNum(ob.getInt("num"));
                bin.setTitle(ob.getString("title"));
                bin.setWriter(ob.getString("writer"));
                bin.setAddress(ob.getString("address"));
                bin.setContent(ob.getString("content"));
                bin.setContents_uri(ob.getString("content_url"));

                list.add(bin);
            }
        } catch (Exception e) {
            System.out.println("Error : " + e.toString());
            return 0;
        }
        return list.size();
    }
}



