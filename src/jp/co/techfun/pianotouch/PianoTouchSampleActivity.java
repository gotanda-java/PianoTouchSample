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

// �s�A�m���Activity
public class PianoTouchSampleActivity extends Activity {

    // �������Ր����`����萔
    private static final int WHITE_KEY_NUMBER = 12;
    // �������Ր����`����萔
    private static final int BLACK_KEY_NUMBER = 8;

    // �����E���������Đ��pMediaPlayer�z��̏�����
    private MediaPlayer[] whiteKeyPlayer;
    private MediaPlayer[] blackKeyPlayer;

    // **onCreate���\�b�h(��ʏ����\���C�x���g)
    @SuppressLint("Recycle")
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // ���C�A�E�gXML�̐ݒ�
        setContentView(R.layout.pianotouchsample);

        // // **GestureDetecotor�N���X�̃C���X�^���X����
        // gesDetect = new GestureDetector(this, onGestureListener);

        // �����E���������Đ��pMediaPlayer�z��̏�����
        whiteKeyPlayer = new MediaPlayer[WHITE_KEY_NUMBER];
        blackKeyPlayer = new MediaPlayer[BLACK_KEY_NUMBER];

        // ���\�[�X�t�@�C�����甒�������pmidi�t�@�C���ǂݍ���
        TypedArray mids =
            getResources().obtainTypedArray(R.array.mids_whiteKey);

        for (int i = 0; i < mids.length(); i++) {

            int mds = mids.getResourceId(i, -1);

            if (mds != -1) {

                whiteKeyPlayer[i] = MediaPlayer.create(this, mds);

            }
        }

        // ���\�[�X�t�@�C�����獕�������pmidi�t�@�C���ǂݍ���
        mids = getResources().obtainTypedArray(R.array.mids_blackKey);

        for (int i = 0; i < mids.length(); i++) {

            int mds = mids.getResourceId(i, -1);

            if (mds != -1) {

                blackKeyPlayer[i] = MediaPlayer.create(this, mds);

            }
        }

        // ���C�A�E�gXML��蔒���̃��C�A�E�g���擾���A�^�b�`�C�x���g��o�^
        LinearLayout keyWhiteLayout =
            (LinearLayout) findViewById(R.id.layout_key_white);

        // �擾�������C�A�E�g���甒���L�[(�{�^��)�ɂP���^�b�`�C�x���g��o�^
        for (int i = 0; i < keyWhiteLayout.getChildCount(); i++) {

            ImageButton keyWhiteBtn =
                (ImageButton) keyWhiteLayout.getChildAt(i);

            keyWhiteBtn.setOnTouchListener(new ButtonTouchListenerFling(
                whiteKeyPlayer[i], R.drawable.k_w, R.drawable.k_w_p));

        }

        // ���C�A�E�gXML��荕����view���擾���A�^�b�`�C�x���g��o�^
        LinearLayout keyBlackLayout =
            (LinearLayout) findViewById(R.id.layout_key_black);

        // �擾�������C�A�E�g���甒���L�[(�{�^��)�ɂP���^�b�`�C�x���g��o�^
        for (int i = 0; i < keyBlackLayout.getChildCount(); i++) {

            ImageButton keyBlackBtn =
                (ImageButton) keyBlackLayout.getChildAt(i);

            keyBlackBtn.setOnTouchListener(new ButtonTouchListenerFling(
                blackKeyPlayer[i], R.drawable.k_b, R.drawable.k_b_p));

        }

        // �{�����[���{�^���Ń��f�B�A�̉��ʒ����ɐݒ�
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

    }

    // /////////////////
    // �^�b�`���X�i�[�N���X
    public class ButtonTouchListenerFling implements View.OnTouchListener {
        // GestureDetector����
        private GestureDetector gesDetect;
        // �^�b�`���ꂽ�L�[�ɑΉ����鉹���t�B�[���h
        private MediaPlayer mp;

        // ������Ԃ̉摜
        private int defaultPic;

        // �^�b�`���̉摜
        private int touchPic;

        // �R���X�g���N�^
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

        // onTouch���\�b�h(�^�b�`�C�x���g)
        @SuppressWarnings("deprecation")
        @Override
        public boolean onTouch(View view, MotionEvent event) {

            gesDetect.onTouchEvent(event);
            // Log.i("Sound", view + " + " + event);
            // Log.i("Sound", ""+view.getId());

            // ���\�[�X�̎擾
            Resources res = view.getResources();
            // Log.i("Sound", "***" + res);

            switch (event.getActionMasked()) {

            // �^�b�`�����ꍇ
            case MotionEvent.ACTION_DOWN:
                // ���Ղ̉摜���^�b�`���摜�ɐݒ�
                view.setBackgroundDrawable(res.getDrawable(touchPic));
                // �������Đ�
                // startPlay();

//                Log.i("Sound", "#######" +mp. );
                break;

            // �^�b�`�����ꂽ�ꍇ
            case MotionEvent.ACTION_UP:
                // ���Ղ̔w�i���f�t�H���g�摜�ɐݒ�
                view.setBackgroundDrawable(res.getDrawable(defaultPic));
                // �������~
                // stopPlay();

                break;

            // ��L�ȊO
            default:

                break;
            }

            return true;
        }

        // startPlay���\�b�h(�����Đ�����)
        private void startPlay() {
            mp.seekTo(0);
            mp.start();
        }

        // stopPlay���\�b�h(������~����)
        private void stopPlay() {
            mp.pause();
        }

        // ���G�ȃ^�b�`�C�x���g���擾
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
    // // **�^�b�`�C�x���g��GestureDetector#onTouchEvent���\�b�h��
    // gesDetect.onTouchEvent(event);
    // return false;
    // }

    // onDestroy���\�b�h(�A�N�e�B�r�e�B�j���C�x���g)
    @Override
    public void onDestroy() {
        super.onDestroy();
        // ���������Đ��pMediaPlayer�̉��
        for (int i = 0; i < whiteKeyPlayer.length; i++) {
            if (whiteKeyPlayer[i].isPlaying()) {
                whiteKeyPlayer[i].stop();
            }
            whiteKeyPlayer[i].release();
        }

        // ���������Đ��pMediaPlayer�̉��
        for (int i = 0; i < blackKeyPlayer.length; i++) {
            if (blackKeyPlayer[i].isPlaying()) {
                blackKeyPlayer[i].stop();
            }
            blackKeyPlayer[i].release();
        }
    }

}
