package com.example.myapplication.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.myapplication.AppPermission;
import com.example.myapplication.MapJSONTaskAndParsor;
import com.example.myapplication.R;
import com.example.myapplication.SeoulApplication;
import com.example.myapplication.bin.InfoBoardBin;
import com.example.myapplication.naverapi.naverapi.NMapViewerResourceProvider;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;

import java.util.ArrayList;

import static com.example.myapplication.AppPermission.checkAndRequestPermission;


public class Container_MyLocationFragment extends Fragment implements View.OnClickListener{
    private NMapContext mMapContext;
    private View mLayout;
    private ArrayList<InfoBoardBin> mInfoList;
    private boolean mGpsPower  = false, mOptionPower = false;
    private int mPermissionCheck;
    private String mSelectQuery;
    private Context mContext;

    NMapView mapView;
    public static NMapViewerResourceProvider mMapViewerResourceProvider;          // 뷰어 상에서 리소스 제공을 위한 필요 클래스
    public static NMapOverlayManager mOverlayManager;                             // 뷰어 상에 오버레이시키기 위한 필요 클래스
    public static NMapPOIdata Category_filter_poiData;
    public static NMapController controller;                                       // 지도 컨트롤 하기위한 클래스

    public static NMapController getController() {
        return controller;
    }

    public static void setController(NMapController controller) {
        Container_MyLocationFragment.controller = controller;
    }

    public static NMapPOIdata getCategory_filter_poiData() {
        return Category_filter_poiData;
    }

    public static void setCategory_filter_poiData(NMapPOIdata category_filter_poiData) {
        Category_filter_poiData = category_filter_poiData;
    }

    public static NMapViewerResourceProvider getmMapViewerResourceProvider() {
        return mMapViewerResourceProvider;
    }

    public static void setmMapViewerResourceProvider(NMapViewerResourceProvider mMapViewerResourceProvider) {
        Container_MyLocationFragment.mMapViewerResourceProvider = mMapViewerResourceProvider;
    }

    public static NMapOverlayManager getmOverlayManager() {
        return mOverlayManager;
    }

    public static void setmOverlayManager(NMapOverlayManager mOverlayManager) {
        Container_MyLocationFragment.mOverlayManager = mOverlayManager;
    }


    NMapLocationManager locationManager;                            // 현재 위치 좌표를 반환시키기위한 클래스
    NMapMyLocationOverlay mMyLocationOverlay;
    ImageButton btnNavi, btnOption;
    private static final String CLIENT_ID = "oO7LJGD_cio_7XphMLou";// 애플리케이션 클라이언트 아이디 값

    public Container_MyLocationFragment() {
        // Required empty public constructor
    }
    public static Container_MyLocationFragment newInstance() {
        Container_MyLocationFragment fragment = new Container_MyLocationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayout = inflater.inflate(R.layout.fragment_container__my_location, container, false);

        btnNavi = mLayout.findViewById(R.id.NearHereInfo_btnNavi);
        btnOption = mLayout.findViewById(R.id.NearHereInfo_btnOption);

        btnNavi.setOnClickListener(this);
        btnOption.setOnClickListener(this);
        return mLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapContext = new NMapContext(super.getActivity());
        mMapContext.onCreate();
        mContext = SeoulApplication.getAppContext();
        mInfoList = new ArrayList<InfoBoardBin>();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView = (NMapView)getView().findViewById(R.id.NearHereInfo_mapView);
        mapView.setClientId(CLIENT_ID);// 클라이언트 아이디 설정
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mMapContext.setupMapView(mapView);

        //NAVER MAP 뷰어 상에 나타내기 위해 ResourceProvider와 OverlayManger를 생성
        mMapViewerResourceProvider = new NMapViewerResourceProvider(getContext());
        mOverlayManager = new NMapOverlayManager(getContext(), mapView, mMapViewerResourceProvider);
        controller = mapView.getMapController();

        locationManager = new NMapLocationManager(mapView.getContext());
        mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(locationManager, null);
        locationManager.setOnLocationChangeListener(new NMapLocationManager.OnLocationChangeListener() {
            @Override
            public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
                mMyLocationOverlay.refresh();
                controller.setMapCenter(nGeoPoint);
                return false;
            }

            @Override
            public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {
                Toast.makeText(mContext,"현재 위치를 탐색하지 못했습니다. 다시 시도해주세요!",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
                Toast.makeText(mContext,"위치가 불가능한 지역입니다. 다시 시도해주세요!",Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.NearHereInfo_btnNavi:
                mGpsPower = !mGpsPower;
                if(mGpsPower){
                    btnNavi.setBackgroundResource(R.color.NearHereinfo_btn_on);
                    mPermissionCheck= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)+ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION);
                    //권한 없을때
                    if(mPermissionCheck<0){
                        checkAndRequestPermission(getActivity(), AppPermission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION);
                        checkAndRequestPermission(getActivity(), AppPermission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

                        mGpsPower = false;
                        btnNavi.setBackgroundResource(R.color.NearHereinfo_btn_off);
                    }
                    //권한 생겼을때
                    else {
                        FindMyLocation();
                    }
                }
                else{
                    btnNavi.setBackgroundResource(R.color.NearHereinfo_btn_off);
                    mOverlayManager.removeMyLocationOverlay();
                    locationManager.disableMyLocation();

                    locationManager = new NMapLocationManager(mapView.getContext());
                    mMyLocationOverlay = mOverlayManager.createMyLocationOverlay(locationManager, null);
                    locationManager.setOnLocationChangeListener(new NMapLocationManager.OnLocationChangeListener() {
                        @Override
                        public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
                            mMyLocationOverlay.refresh();
                            controller.setMapCenter(nGeoPoint);
                            return false;
                        }

                        @Override
                        public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {
                            Toast.makeText(mContext,"현재 위치를 탐색하지 못했습니다. 다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
                            Toast.makeText(mContext,"위치가 불가능한 지역입니다. 다시 시도해주세요!",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                break;
            case R.id.NearHereInfo_btnOption:
//                mOptionPower = !mOptionPower;
//                if(mOptionPower)
//                    btnOption.setBackgroundResource(R.color.NearHereinfo_btn_on);
//                    CreateFilterDiag
//                else
//                    btnOption.setBackgroundResource(R.color.NearHereinfo_btn_off);
                CreateFilterDiag(R.layout.item_filter_dialog);
                break;
        }
    }


    //Category 선택을 위한 다이얼로그창
    public void CreateFilterDiag(final int layoutid){
        mSelectQuery=null;
        View dlgView = (View)View.inflate(getContext(), layoutid,null);
        DialogClickListener dialogClickListener = new DialogClickListener();

        dlgView.findViewById(R.id.dialog_icon_picture).setOnClickListener(dialogClickListener);
        dlgView.findViewById(R.id.dialog_icon_all).setOnClickListener(dialogClickListener);
        dlgView.findViewById(R.id.dialog_icon_art).setOnClickListener(dialogClickListener);
        dlgView.findViewById(R.id.dialog_icon_handicraft).setOnClickListener(dialogClickListener);
        dlgView.findViewById(R.id.dialog_icon_music).setOnClickListener(dialogClickListener);
        dlgView.findViewById(R.id.dialog_icon_theater).setOnClickListener(dialogClickListener);
        //카테고리에서 검색 반경을 위한 Spinner 이벤트 처리
//        SpinnerSearch_radius = (Spinner) dlgView.findViewById(R.id.category_spinner_radius);
//        SpinnerSearch_radius.setSelection(SearchRadiueSelect_postion);
//        SpinnerSearch_radius.setOnItemSelectedListener(this);

        //카테고리 다이얼로그 생성 밑 버튼 이벤트
        AlertDialog.Builder dlg_CategorySelect=new AlertDialog.Builder(getContext(),R.style.MyAlertDialogStyle);
        dlg_CategorySelect.setView(dlgView)
                //.setIcon(R.drawable.fab_filter)
                .setTitle("필터 설정")
                .setCancelable(false)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(mSelectQuery!=null){
                            MapJSONTaskAndParsor jsonTaskAndParsor = new MapJSONTaskAndParsor(mInfoList,getContext());
                            jsonTaskAndParsor.execute(mSelectQuery);
                            Toast.makeText(mContext,mSelectQuery,Toast.LENGTH_SHORT).show();
                        }else
                            Toast.makeText(mContext,"카테고리를 선택해주세요!",Toast.LENGTH_SHORT).show();
                    }
                })
                .show();

    }
    class DialogClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dialog_icon_all:
                    Toast.makeText(mContext, "'전체' 클릭!", Toast.LENGTH_SHORT).show();
                    mSelectQuery = MapJSONTaskAndParsor.INFOBOARD_ALL_URL;
                    break;
                case R.id.dialog_icon_music:
                    Toast.makeText(mContext, "'음악' 클릭!", Toast.LENGTH_SHORT).show();
                    mSelectQuery = MapJSONTaskAndParsor.INFOBOARD_CATEGORY_URL + 0;
                    break;
                case R.id.dialog_icon_art:
                    Toast.makeText(mContext, "'예술' 클릭!", Toast.LENGTH_SHORT).show();
                    mSelectQuery = MapJSONTaskAndParsor.INFOBOARD_CATEGORY_URL + 1;
                    break;
                case R.id.dialog_icon_handicraft:
                    Toast.makeText(mContext, "'공예' 클릭!", Toast.LENGTH_SHORT).show();
                    mSelectQuery = MapJSONTaskAndParsor.INFOBOARD_CATEGORY_URL + 2;
                    break;
                case R.id.dialog_icon_theater:
                    Toast.makeText(mContext, "'연극' 클릭!", Toast.LENGTH_SHORT).show();
                    mSelectQuery = MapJSONTaskAndParsor.INFOBOARD_CATEGORY_URL + 3;
                    break;
                case R.id.dialog_icon_picture:
                    Toast.makeText(mContext, "'사진' 클릭!", Toast.LENGTH_SHORT).show();
                    mSelectQuery = MapJSONTaskAndParsor.INFOBOARD_CATEGORY_URL + 4;
                    break;
            }
        }
    }
    private void FindMyLocation(){
        if (locationManager.isMyLocationEnabled()) {
            if (locationManager.enableMyLocation(true)) {
                if(locationManager.isMyLocationFixed()){
                    if(mMyLocationOverlay != null){
                        mMyLocationOverlay.refresh();
                        controller.setMapCenter(locationManager.getMyLocation());
                        controller.reload();
                    }
                    else{
                        if(mMyLocationOverlay.hasPathData()){
                            mMyLocationOverlay.refresh();
                            controller.setMapCenter(locationManager.getMyLocation());
                        }
                    }

                }
                else{
                    locationManager.disableMyLocation();
                    Toast.makeText(mContext,"현재 위치탐색 실패!",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                locationManager.disableMyLocation();
                Toast.makeText(mContext,"현재 위치탐색 불가능",Toast.LENGTH_SHORT).show();
            }

        } else {
            boolean isMyLocationEnabled = locationManager.enableMyLocation(true);
            if (!isMyLocationEnabled) {
                new AlertDialog.Builder(getContext()).setTitle("위치 서비스 설정")
                        .setMessage("일루와 앱에서 내 위치를 보려면, 단말기의 설정에서 '위치서비스' 사용을 허용해주세요.")
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mGpsPower = false;
                                btnNavi.setBackgroundResource(R.color.NearHereinfo_btn_off);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                startActivity(intent);}
                        })
                        .show();
                return;
            }
        }
    }

    @Override
    public void onStart(){
        super.onStart();

        mMapContext.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapContext.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mMapContext.onPause();
    }

    @Override
    public void onStop() {

        mMapContext.onStop();

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mMapContext.onDestroy();

        super.onDestroy();
    }
}
