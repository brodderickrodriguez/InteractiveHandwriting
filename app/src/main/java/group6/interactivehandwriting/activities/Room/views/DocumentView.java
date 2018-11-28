package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ScaleGestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import group6.interactivehandwriting.activities.Room.draw.RoomViewActionUtility;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.app.actions.draw.EndDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.MoveDrawAction;
import group6.interactivehandwriting.common.app.actions.draw.StartDrawAction;
import group6.interactivehandwriting.common.network.NetworkLayer;

import static android.view.MotionEvent.INVALID_POINTER_ID;

public class DocumentView extends ImageView {
    private static final String DEBUG_TAG_V = "DocumentView";

    private static final float TOUCH_TOLERANCE = 4;

    private CanvasManager canvasManager;
    private Profile profile;
    private String deviceName;

    private NetworkLayer networkLayer;

    private Drawable mImage;
    private float mPosX;
    private float mPosY;

    private float mLastTouchX;
    private float mLastTouchY;
    private float mScaleFactor = 1.0f;
    private int mActivePointerId = INVALID_POINTER_ID;

    private ScaleGestureDetector mScaleDetector;

    float scale=1f;
    Matrix m=new Matrix();
    Matrix oldm=new Matrix();
    int mode;
    int tap=0;int none=0;int tap2=0;int drag=0;int pinch=0;
    float posx=200f;float posy=200f;float oldx=200f;float oldy=200f;
    float movx=0f;float movy=0f,x=0f,y=0f;
    float nowsp=0f;float oldsp=0f;
    MotionEvent olde;

    // 0 - zoom/resize .. 1 - drawing
    private int allowDrawing = 1;


    public DocumentView(Context context) {
        super(context);
        canvasManager = new CanvasManager(this);

        mScaleDetector = new ScaleGestureDetector(context, new DocumentScaleListener());
        this.setScaleType(ImageView.ScaleType.MATRIX);
    }


    public boolean setNetworkLayer(NetworkLayer layer) {
        if (layer != null) {
            this.profile = layer.getMyProfile();
            this.networkLayer = layer;
            this.networkLayer.receiveDrawActions(canvasManager);
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        canvasManager.updateSize(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasManager.update(canvas);
        canvas.save();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (allowDrawing) {
            case 0: /* Resize / Zoom */
                tap=tap2=drag=pinch=none;
                int mask=e.getActionMasked();

                posx=e.getX();
                posy=e.getY();
                float midx= this.getWidth()/2f;
                float midy=this.getHeight()/2f;
                int fingers=e.getPointerCount();
                switch(mask)
                {
                    case MotionEvent.ACTION_POINTER_UP:
                        tap2=1;
                        break;

                    case MotionEvent.ACTION_UP:
                        tap=1;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        drag=1;
                }
                if(fingers==2){
                    nowsp=Math.abs(e.getX(0)-e.getX(1));
                }
                if((fingers==2)&&(drag==0)){
                    tap2=1;
                    tap=0;
                    drag=0;
                }
                if((fingers==2)&&(drag==1)){
                    tap2=0;
                    tap=0;
                    drag=0;
                    pinch=1;
                }

                if(pinch==1)

                {
                    if(nowsp>oldsp) {
                        //scale += 0.1;
                        scale += 0.03;
                    }
                    if(nowsp<oldsp) {
                        //scale -= 0.1;
                        scale -= 0.03;
                    }
                    tap2=tap=drag=0;
                }
               /* if(tap2==1)
                {
                    scale-=0.1;
                    tap=0;
                    drag=0;
                }
                if(tap==1)
                {
                    tap2=0;
                    drag=0;
                    scale+=0.1;
                }*/
                if(drag==1)
                {
                    movx=posx-oldx;
                    movy=posy-oldy;
                    x+=movx;
                    y+=movy;
                    tap=0;
                    tap2=0;
                }
                m.setTranslate(x,y);
                m.postScale(scale,scale,midx,midy);
                this.setImageMatrix(m);
                this.invalidate();
                tap=tap2=drag=none;
                oldx=posx;
                oldy=posy;
                oldsp=nowsp;
                this.setImageMatrix(m);
                invalidate();
                return true;
            case 1: /* Draw */
                performClick();
                float x = e.getX();
                float y = e.getY();
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchStarted(x, y);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        touchMoved(x, y);
                        break;
                    case MotionEvent.ACTION_UP:
                        touchReleased();
                        break;
                }
                break;
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private void touchStarted(float x, float y) {
        StartDrawAction action = RoomViewActionUtility.touchStarted(x, y);
        canvasManager.handleDrawAction(profile, action);

        if (networkLayer != null) {
            networkLayer.startDraw(action);
        }
    }

    private void touchMoved(float x, float y) {
        if (RoomViewActionUtility.didTouchMove(x, y, TOUCH_TOLERANCE)) {
            MoveDrawAction action = RoomViewActionUtility.touchMoved(x, y);
            canvasManager.handleDrawAction(profile, action);

            if (networkLayer != null) {
                networkLayer.moveDraw(action);
            }
        }
    }

    private void touchReleased() {
        EndDrawAction action = RoomViewActionUtility.touchReleased();
        canvasManager.handleDrawAction(profile, action);

        if (networkLayer != null) {
            networkLayer.endDraw(action);
        }
    }

    public ImageView getView() {
        return this;
    }

    public int getDrawingStatus() {
        return this.allowDrawing;
    }

    public void setDrawingStatus(int allowDrawingIn) {
        this.allowDrawing = allowDrawingIn;
    }

    public class DocumentScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){

            mScaleFactor *= scaleGestureDetector.getScaleFactor();

            mScaleFactor = Math.max(0.1f,

                    Math.min(mScaleFactor, 10.0f));

            getView().setScaleX(mScaleFactor);

            getView().setScaleY(mScaleFactor);
            return true;

        }
    }
}