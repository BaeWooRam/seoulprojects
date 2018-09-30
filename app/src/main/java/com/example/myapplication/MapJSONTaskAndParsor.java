package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.myapplication.bin.InfoBoardBin;
import com.example.myapplication.bin.mapPoint;
import com.example.myapplication.fragment.Container_MyLocationFragment;
import com.example.myapplication.naverapi.naverapi.NMapPOIflagType;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapCircleData;
import com.nhn.android.maps.overlay.NMapCircleStyle;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;

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

public class MapJSONTaskAndParsor extends AsyncTask<String, String, String> {
    public final static String INFOBOARD_CATEGORY_URL = "http://182.222.75.26:3000/infoboard/category/";
    public final static String INFOBOARD_SEARCH_URL = "http://182.222.75.26:3000/infoboard/search/";
    public final static String INFOBOARD_ALL_URL = "http://182.222.75.26:3000/infoboard";
    public final static String INFOBOARD_NUMBER_URL = "http://182.222.75.26:3000/infoboard/";

    ProgressDialog progressDialog;
    ArrayList<InfoBoardBin> list;
    Context context;

    public MapJSONTaskAndParsor(ArrayList<InfoBoardBin> list, Context context) {
        this.list = list;
        this.context = context;

    }
    @Override
    protected void onPreExecute() {
        //진행바
        progressDialog = new ProgressDialog(context);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("데이터를 가져오는 중입니다...");
        progressDialog.show();

        super.onPreExecute();
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

        //init
        if(Container_MyLocationFragment.getCategory_filter_poiData()!=null){
            Container_MyLocationFragment.getCategory_filter_poiData().removeAllPOIdata();
            Container_MyLocationFragment.getController().reload();
        }



        int arraySize = JsonParser_CurrentMapInfo(result,list,context);
        if(arraySize > 0){
            int markerId = NMapPOIflagType.PIN;

            Container_MyLocationFragment.setCategory_filter_poiData(new NMapPOIdata(arraySize, Container_MyLocationFragment.getmMapViewerResourceProvider()));
            Container_MyLocationFragment.getCategory_filter_poiData().beginPOIdata(arraySize); //POI 아이템 추가를 시작한다.

            for(InfoBoardBin bin: list){
                Log.i("point","x : "+bin.getPoint().getPointx()+" , y: "+bin.getPoint().getPointy());
                NMapPOIitem item = Container_MyLocationFragment.getCategory_filter_poiData().addPOIitem(bin.getPoint().getPointy(),bin.getPoint().getPointx(),bin.getTitle(), markerId, bin.getNum());
                if (item != null) {
                    item.setRightAccessory(true, NMapPOIflagType.CLICKABLE_ARROW); //마커 선택 시 표시되는 말풍선의 오른쪽 아이콘을 설정한다.
                    item.setRightButton(true); //마커 선택 시 표시되는 말풍선의 오른쪽 버튼을 설정한다.
                }

            }

            Container_MyLocationFragment.getCategory_filter_poiData().endPOIdata(); //POI 아이템 추가를 종료한다.

            NMapPOIdataOverlay poiDataOverlay = Container_MyLocationFragment.getmOverlayManager().createPOIdataOverlay(Container_MyLocationFragment.getCategory_filter_poiData(), null); //POI 데이터를 인자로 전달하여 NMapPOIdataOverlay 객체를 생성한다.

            poiDataOverlay.showAllPOIdata(10);
            poiDataOverlay.setOnStateChangeListener(new NMapPOIdataOverlay.OnStateChangeListener() {
                @Override
                public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

                }

                @Override
                public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {
                    try{
                        Toast.makeText(context,""+nMapPOIitem.getId(),Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(context,"해당 사이트에 접속 실패했어요ㅠㅅㅠ", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            progressDialog.dismiss();
        }
    }

    //각 파서들 넣으면 될듯
    public int JsonParser_CurrentMapInfo(String result, ArrayList<InfoBoardBin> list, Context context) {
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
                bin.setContents_uri(ob.getString("content_url"));
                bin.setPoint(getMapPoint(bin.getAddress(), context));

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
    // 반경 그리기 위한 메소드
    public NMapCircleData CreatePathCircle(NMapPathDataOverlay pathDataOverlay, NMapOverlayManager mOverlayManager, int linetype, NGeoPoint point, float radius, NMapView nMapView){
        if(pathDataOverlay != null){
            //pathDataOverlay = mOverlayManager.createPathDataOverlay();
            // set circle data
            NMapCircleData circleData = new NMapCircleData(1);
            circleData.initCircleData();
            circleData.addCirclePoint(point.getLatitude(), point.getLongitude(), radius);
            circleData.endCircleData();
            pathDataOverlay.addCircleData(circleData);

            // set circle style
            NMapCircleStyle circleStyle = new NMapCircleStyle(nMapView.getContext());
            circleStyle.setLineType(linetype);
            circleStyle.setStrokeColor(0x002266, 0x90);
            circleStyle.setStrokeWidth(1f);
            circleStyle.setFillColor(0xA8F1FF, 0x4f);
            circleData.setCircleStyle(circleStyle);
            Log.i("Circle","radius : "+radius);
            return  circleData;
        }
        return  null;
    }
}



