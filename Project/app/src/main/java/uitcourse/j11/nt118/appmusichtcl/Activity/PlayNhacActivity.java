package uitcourse.j11.nt118.appmusichtcl.Activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.AdapterListUpdateCallback;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uitcourse.j11.nt118.appmusichtcl.Adapter.Fragment_Danh_Sach_Cac_Bai_Hat;
import uitcourse.j11.nt118.appmusichtcl.Adapter.ViewPagerPlaylistnhac;
import uitcourse.j11.nt118.appmusichtcl.Fragment.Fragment_Dia_Nhac;
import uitcourse.j11.nt118.appmusichtcl.Model.Baihat;
import uitcourse.j11.nt118.appmusichtcl.R;
import uitcourse.j11.nt118.appmusichtcl.Service.APIService;
import uitcourse.j11.nt118.appmusichtcl.Service.Dataservice;

public class PlayNhacActivity extends AppCompatActivity {


    Toolbar toolbarplaynhac;
    TextView txtTimesong, txtTotaltimesong;
    SeekBar sktime;
    ImageView imgplay, imgrepeat,imgnext,imgpre,imgrandom,imgdownload;
    ViewPager viewPagerplaynhac;


    public static ArrayList<Baihat> mangbaihat = new ArrayList<>();
    public static ViewPagerPlaylistnhac adapternhac;
    Fragment_Dia_Nhac fragment_dia_nhac;
    Fragment_Danh_Sach_Cac_Bai_Hat fragment_danh_sach_cac_bai_hat;
    MediaPlayer mediaPlayer ; // Dùng để chơi nhạc
    int position = 0; // dùng để lấy vị trí cho chức năng next, previous
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_nhac);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GetDataFromIntent();
        init();

        eventClick();
    }

    private void eventClick() {

        imgdownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri url=Uri.parse(mangbaihat.get(position).getLinkBaiHat());
                DownloadManager.Request request = new DownloadManager.Request(url);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, mangbaihat.get(position).getTenBaiHat());
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED); // to notify when download is complete
                //request.allowScanningByMediaScanner();// if you want to be available from media players
                DownloadManager manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                manager.enqueue(request);

                /*
                Dataservice dataservice = APIService.getService();
                Call<ResponseBody> call = dataservice.DownloadBaiHat(mangbaihat.get(position).getLinkBaiHat());
                Log.d("TUONG",mangbaihat.get(position).getLinkBaiHat());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Log.d("TUONG", "server contacted and has file");

                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(),mangbaihat.get(position).getTenBaiHat());

                            Log.d("TUONG", "file download was a success? " + writtenToDisk);
                        } else {
                            Log.d("TUONG", "server contact failed");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });*/
            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(adapternhac.getItem(1)!=null)
                {
                    if(mangbaihat.size()>0)
                    {
                        fragment_dia_nhac.Playnhac(mangbaihat.get(0).getHinhBaiHat());
                        handler.removeCallbacks(this);
                    }
                    else
                    {
                        handler.postDelayed(this,300);
                    }
                }
            }
        },500);
        imgplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying())
                {
                    mediaPlayer.pause();
                    imgplay.setImageResource(R.drawable.iconplay);
                }
                else
                {
                    mediaPlayer.start();
                    imgplay.setImageResource(R.drawable.iconpause);
                }
            }
        });
        imgrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeat == false)
                {
                    if(checkrandom == true)
                    {
                        checkrandom =false;
                        imgrepeat.setImageResource(R.drawable.iconsyned);
                        imgrandom.setImageResource(R.drawable.iconsuffle);
                    }
                    imgrepeat.setImageResource(R.drawable.iconsyned);
                    repeat=true;
                }
                else
                {
                    imgrepeat.setImageResource(R.drawable.iconrepeat);
                    repeat = false;
                }
            }
        });
        imgrandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkrandom == false)
                {
                    if(repeat == true)
                    {
                        repeat =false;
                        imgrandom.setImageResource(R.drawable.iconshuffled);
                        imgrepeat.setImageResource(R.drawable.iconrepeat);
                    }
                    imgrandom.setImageResource(R.drawable.iconshuffled);
                    checkrandom=true;

                }
                else
                {
                    imgrandom.setImageResource(R.drawable.iconsuffle);
                    checkrandom = false;
                }

            }
        });

        sktime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });

        imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mangbaihat.size()>0)
                {
                    if(mediaPlayer.isPlaying()||mediaPlayer!=null)
                    {
                        // Dừng phát
                        mediaPlayer.stop();
                        // Đồng bộ
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

                    if(position<(mangbaihat.size()))
                    {
                        imgplay.setImageResource(R.drawable.iconpause);
                        position++;
                        if(repeat==true)
                        {
                            if(position==0)
                            {
                                position= mangbaihat.size();
                            }
                            position -= 1;
                        }
                        if(checkrandom ==true)
                        {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if(index == position)
                            {
                                position = index -1;
                            }
                            position=index;
                        }
                        if(position > (mangbaihat.size()-1))
                        {
                            position=0;

                        }

                        // Play bài hát
                        new PlayMP3().execute(mangbaihat.get(position).getLinkBaiHat());

                        // Set lại hình đĩa nhạc
                        fragment_dia_nhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());

                        // set lại tên bài trên action bar
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        UpdateTime();
                    }
                }

                // Không cho người dùng nhấn nút Pre và Next quá nhanh

                imgnext.setClickable(false);
                imgpre.setClickable(false);

                // Sau khoảng thời gian thì cho phép nút click đc .
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        imgnext.setClickable(true);
                        imgpre.setClickable(true);

                    }
                },5000);


            }
        });

        imgpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mangbaihat.size()>0)
                {
                    if(mediaPlayer.isPlaying()||mediaPlayer!=null)
                    {
                        // Dừng phát
                        mediaPlayer.stop();
                        // Đồng bộ
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }

                    if(position<(mangbaihat.size()))
                    {
                        imgplay.setImageResource(R.drawable.iconpause);
                        position--;

                        // Khi pre về < 0
                        if(position<0)
                        {
                            position = mangbaihat.size()-1;
                        }
                        if(repeat==true)
                        {
                            position += 1;
                        }
                        if(checkrandom ==true)
                        {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if(index == position)
                            {
                                position = index -1;
                            }
                            position=index;
                        }

                        // Play bài hát
                        new PlayMP3().execute(mangbaihat.get(position).getLinkBaiHat());

                        // Set lại hình đĩa nhạc
                        fragment_dia_nhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());

                        // set lại tên bài trên action bar
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());
                        UpdateTime();
                    }
                }

                // Không cho người dùng nhấn nút Pre và Next quá nhanh

                imgnext.setClickable(false);
                imgpre.setClickable(false);

                // Sau khoảng thời gian thì cho phép nút click đc .
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        imgnext.setClickable(true);
                        imgpre.setClickable(true);

                    }
                },5000);

            }
        });

    }

    private void GetDataFromIntent() {

        Intent intent = getIntent();
        mangbaihat.clear();
        if(intent!=null)
        {

            if(intent.hasExtra("cakhuc")){
                Baihat baihat = intent.getParcelableExtra("cakhuc");
                //Toast.makeText(this, baihat.getTenBaiHat(), Toast.LENGTH_SHORT).show();
                mangbaihat.add(baihat);
            }

            if(intent.hasExtra("cacbaihat")){
                ArrayList<Baihat> baihatArrayList = intent.getParcelableArrayListExtra("cacbaihat");
                for(int i = 0; i<baihatArrayList.size(); i++){
                    Log.e("ZZZZZ", baihatArrayList.get(i).getTenBaiHat());
                    //mangbaihat = baihatArrayList;
                }
                mangbaihat = baihatArrayList;
            }
        }
    }

    private void init() {

        toolbarplaynhac = findViewById(R.id.toolbarplaynhac);
        txtTimesong = findViewById(R.id.textviewtimesong);
        txtTotaltimesong = findViewById(R.id.textviewtotaltimesong);
        sktime = findViewById(R.id.seekbarsong);
        imgplay = findViewById(R.id.imagebuttonplay);
        imgrepeat = findViewById(R.id.imagebuttonrepeat);
        imgnext = findViewById(R.id.imagebuttonnext);
        imgrandom = findViewById(R.id.imagebuttonsuffle);
        imgpre = findViewById(R.id.imagebuttonpre);
        imgdownload = findViewById(R.id.imagebuttodownload);
        viewPagerplaynhac = findViewById(R.id.viewpagerplaynhac);

        setSupportActionBar(toolbarplaynhac);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarplaynhac.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                mediaPlayer.stop();
                mangbaihat.clear();
            }
        });
        toolbarplaynhac.setTitleTextColor(Color.WHITE);



        fragment_dia_nhac = new Fragment_Dia_Nhac();
        fragment_danh_sach_cac_bai_hat = new Fragment_Danh_Sach_Cac_Bai_Hat();
        adapternhac = new ViewPagerPlaylistnhac(getSupportFragmentManager());

        adapternhac.AddFragment(fragment_danh_sach_cac_bai_hat);
        adapternhac.AddFragment(fragment_dia_nhac);
        viewPagerplaynhac.setAdapter(adapternhac);

        fragment_dia_nhac= (Fragment_Dia_Nhac) adapternhac.getItem(1);

        if(mangbaihat.size()>0)
        {
            getSupportActionBar().setTitle(mangbaihat.get(0).getTenBaiHat());
            new PlayMP3().execute(mangbaihat.get(0).getLinkBaiHat());
            imgplay.setImageResource(R.drawable.iconpause);
        }

    }


    class PlayMP3 extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            return strings[0];
        }

        @Override
        protected void onPostExecute(String baihat) {
            super.onPostExecute(baihat);
            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        mediaPlayer.stop();
                        mediaPlayer.reset();
                    }
                });

                mediaPlayer.setDataSource(baihat);
                mediaPlayer.prepare();

            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
            TimeSong();
            UpdateTime();

        }
    }

    private void TimeSong()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txtTotaltimesong.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        sktime.setMax(mediaPlayer.getDuration());
    }

    // Update Time của bài hát
    private  void UpdateTime()
    {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(mediaPlayer!=null)
                {
                    sktime.setProgress(mediaPlayer.getCurrentPosition());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                    txtTimesong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                    handler.postDelayed(this,300);
                    // Lắng nghe khi bài hát đã hoàn tất
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            next =true;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

            }
        },300);

        Handler handler1 = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(next == true)
                {
                    if(position<(mangbaihat.size()))
                    {
                        imgplay.setImageResource(R.drawable.iconpause);
                        position++;
                        if(repeat==true)
                        {
                            if(position==0)
                            {
                                position= mangbaihat.size();
                            }
                            position -= 1;
                        }
                        if(checkrandom ==true)
                        {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if(index == position)
                            {
                                position = index -1;
                            }
                            position=index;
                        }
                        if(position > (mangbaihat.size()-1))
                        {
                            position=0;

                        }

                        // Play bài hát
                        new PlayMP3().execute(mangbaihat.get(position).getLinkBaiHat());

                        // Set lại hình đĩa nhạc
                        fragment_dia_nhac.Playnhac(mangbaihat.get(position).getHinhBaiHat());

                        // set lại tên bài trên action bar
                        getSupportActionBar().setTitle(mangbaihat.get(position).getTenBaiHat());

                    }


                // Không cho người dùng nhấn nút Pre và Next quá nhanh

                imgnext.setClickable(false);
                imgpre.setClickable(false);

                // Sau khoảng thời gian thì cho phép nút click đc .
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        imgnext.setClickable(true);
                        imgpre.setClickable(true);

                    }
                },5000);
                next = false;
                handler1.removeCallbacks(this );



            }else
                {
                    handler.postDelayed(this,1000);
                }
            }
        },1000);

    }

    private boolean writeResponseBodyToDisk(ResponseBody body,String namefile) {
        try {
            // todo change the file location/name according to your needs
            Log.d("TUONG",namefile+".mp3");
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), namefile + ".mp3");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d("TUONG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}
