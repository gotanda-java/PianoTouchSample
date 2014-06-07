package jp.co.techfun.pianotouch;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;

// ピアノ画面Activity
public class PianoTouchSampleActivity extends Activity {

    // 白鍵鍵盤数を定義する定数
    private static final int WHITE_KEY_NUMBER = 12;
    // 黒鍵鍵盤数を定義する定数
    private static final int BLACK_KEY_NUMBER = 8;

    // 白鍵・黒鍵音声再生用MediaPlayer配列の初期化
    private MediaPlayer[] whiteKeyPlayer;
    private MediaPlayer[] blackKeyPlayer;

    // **onCreateメソッド(画面初期表示イベント)
    @SuppressLint("Recycle")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // レイアウトXMLの設定
        setContentView(R.layout.pianotouchsample);

        // // **GestureDetecotorクラスのインスタンス生成
        // gesDetect = new GestureDetector(this, onGestureListener);

        // 白鍵・黒鍵音声再生用MediaPlayer配列の初期化
        whiteKeyPlayer = new MediaPlayer[WHITE_KEY_NUMBER];
        blackKeyPlayer = new MediaPlayer[BLACK_KEY_NUMBER];

        // リソースファイルから白鍵音声用midiファイル読み込み
        TypedArray mids =
            getResources().obtainTypedArray(R.array.mids_whiteKey);

        for (int i = 0; i < mids.length(); i++) {

            int mds = mids.getResourceId(i, -1);

            if (mds != -1) {

                whiteKeyPlayer[i] = MediaPlayer.create(this, mds);

            }
        }

        // リソースファイルから黒鍵音声用midiファイル読み込み
        mids = getResources().obtainTypedArray(R.array.mids_blackKey);

        for (int i = 0; i < mids.length(); i++) {

            int mds = mids.getResourceId(i, -1);

            if (mds != -1) {

                blackKeyPlayer[i] = MediaPlayer.create(this, mds);

            }
        }

        // レイアウトXMLより白鍵のレイアウトを取得し、タッチイベントを登録
        LinearLayout keyWhiteLayout =
            (LinearLayout) findViewById(R.id.layout_key_white);

        // 取得したレイアウトから白鍵キー(ボタン)に１つずつタッチイベントを登録
        for (int i = 0; i < keyWhiteLayout.getChildCount(); i++) {

            ImageButton keyWhiteBtn =
                (ImageButton) keyWhiteLayout.getChildAt(i);

            keyWhiteBtn.setOnTouchListener(new ButtonTouchListenerFling(
                whiteKeyPlayer[i], R.drawable.k_w, R.drawable.k_w_p));

        }

        // レイアウトXMLより黒鍵のviewを取得し、タッチイベントを登録
        LinearLayout keyBlackLayout =
            (LinearLayout) findViewById(R.id.layout_key_black);

        // 取得したレイアウトから白鍵キー(ボタン)に１つずつタッチイベントを登録
        for (int i = 0; i < keyBlackLayout.getChildCount(); i++) {

            ImageButton keyBlackBtn =
                (ImageButton) keyBlackLayout.getChildAt(i);

            keyBlackBtn.setOnTouchListener(new ButtonTouchListenerFling(
                blackKeyPlayer[i], R.drawable.k_b, R.drawable.k_b_p));

        }

        // ボリュームボタンでメディアの音量調整に設定
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    // /////////////////
    // タッチリスナークラス
    public class ButtonTouchListenerFling implements View.OnTouchListener {
        // GestureDetector生成
        private GestureDetector gesDetect;
        // タッチされたキーに対応する音声フィールド
        private MediaPlayer mp;

        // 初期状態の画像
        private int defaultPic;

        // タッチ時の画像
        private int touchPic;

        // コンストラクタ
        @SuppressWarnings("deprecation")
        public ButtonTouchListenerFling(MediaPlayer tMp, int tDefaultPic,
            int tTouchPic) {
            // GestureDetector gesDetect;
            gesDetect = new GestureDetector(onGestureListener);

            defaultPic = tDefaultPic;
            touchPic = tTouchPic;
            mp = tMp;
            // Log.i("Sound",""+tMp);
        }

        // onTouchメソッド(タッチイベント)
        @SuppressWarnings("deprecation")
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            gesDetect.onTouchEvent(event);
            // Log.i("Sound", view + " + " + event);
            // Log.i("Sound", ""+view.getId());

            // リソースの取得
            Resources res = view.getResources();
            // Log.i("Sound", "***" + res);

            switch (event.getActionMasked()) {

            // タッチした場合
            case MotionEvent.ACTION_DOWN:
                // 鍵盤の画像をタッチ時画像に設定
                view.setBackgroundDrawable(res.getDrawable(touchPic));
                // 音声を再生
                // startPlay();

//                Log.i("Sound", "#######" +mp. );
                break;

            // タッチが離れた場合
            case MotionEvent.ACTION_UP:
                // 鍵盤の背景をデフォルト画像に設定
                view.setBackgroundDrawable(res.getDrawable(defaultPic));
                // 音声を停止
                // stopPlay();

                break;

            // 上記以外
            default:

                break;
            }

            return true;
        }

        // startPlayメソッド(音声再生処理)
        private void startPlay() {
            mp.seekTo(0);
            mp.start();
        }

        // stopPlayメソッド(音声停止処理)
        private void stopPlay() {
            mp.pause();
        }

        // 複雑なタッチイベントを取得
        private final SimpleOnGestureListener onGestureListener =
            new SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    // TODO Auto-generated method stub
                    Log.v("Gesture", "onDoubleTap");
                    return super.onDoubleTap(e);
                }

                @Override
                public boolean onDoubleTapEvent(MotionEvent e) {
                    // TODO Auto-generated method stub
                    Log.v("Gesture", "onDoubleTapEvent");
                    return super.onDoubleTapEvent(e);
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    // TODO Auto-generated method stub
                    Log.v("Gesture", "onDown");
                    startPlay();
                    return super.onDown(e);
                }

                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2,
                    float velocityX, float velocityY) {
                    // TODO Auto-generated method stub
                    Log.v("Gesture", "onFling");

                    // startPlay();
                    return super.onFling(e1, e2, velocityX, velocityY);
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    // TODO Auto-generated method stub
                    Log.v("Gesture", "onLongPress");
                    super.onLongPress(e);
                }

                @SuppressWarnings("static-access")
                @Override
                public boolean onScroll(MotionEvent e1, MotionEvent e2,
                    float distanceX, float distanceY) {
                    float dis = 0;
                    dis = distanceX - distanceY;
                    if (dis <= 0 && dis <= -20) {
//                        blackKeyPlayer[5].seekTo(0);
                        blackKeyPlayer[5].start();

                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
//                        blackKeyPlayer[6].seekTo(0);
                        blackKeyPlayer[6].start();
                    } else if (dis >= 0 && dis >= 20) {
//                        blackKeyPlayer[3].seekTo(0);
                        blackKeyPlayer[3].start();

                        try {
                            Thread.currentThread().sleep(100);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
//                        blackKeyPlayer[2].seekTo(0);
                        blackKeyPlayer[2].start();
                    }
                    return super.onScroll(e1, e2, distanceX, distanceY);
                }

                @Override
                public void onShowPress(MotionEvent e) {
                    // TODO Auto-generated method stub
                    Log.v("Gesture", "onShowPress");
                    super.onShowPress(e);
                }

                @Override
                public boolean onSingleTapConfirmed(MotionEvent e) {
                    // TODO Auto-generated method stub
                    Log.v("Gesture", "onSingleTapConfirmed");
                    return super.onSingleTapConfirmed(e);
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    // TODO Auto-generated method stub
                    Log.v("Gesture", "onSingleTapUp");
                    return super.onSingleTapUp(e);
                }
            };

    }

    // @Override
    // public boolean onTouchEvent(MotionEvent event) {
    // // TODO Auto-generated method stub
    // // **タッチイベントをGestureDetector#onTouchEventメソッドに
    // gesDetect.onTouchEvent(event);
    // return false;
    // }

    // onDestroyメソッド(アクティビティ破棄イベント)
    @Override
    public void onDestroy() {
        super.onDestroy();
        // 白鍵音声再生用MediaPlayerの解放
        for (int i = 0; i < whiteKeyPlayer.length; i++) {
            if (whiteKeyPlayer[i].isPlaying()) {
                whiteKeyPlayer[i].stop();
            }
            whiteKeyPlayer[i].release();
        }

        // 黒鍵音声再生用MediaPlayerの解放
        for (int i = 0; i < blackKeyPlayer.length; i++) {
            if (blackKeyPlayer[i].isPlaying()) {
                blackKeyPlayer[i].stop();
            }
            blackKeyPlayer[i].release();
        }
    }

}
