package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.nearby.connection.Payload;

import java.net.NetworkInterface;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.common.actions.UserAction;
import group6.interactivehandwriting.common.exceptions.InvalidPayloadException;
import group6.interactivehandwriting.common.network.NetworkMessage;

public class ImageUtilityView extends ImageView implements NetworkMessage {
    private Canvas canvas;
    private Bitmap mBitmap;
    public ImageUtilityView(Context context, AttributeSet attrs){
        super(context, attrs);

    }

    public ImageUtilityView(Context context) {
        super(context);
    }

    @Override
    public void onDraw(Canvas canvas) {

    }

    @Override
    public Payload pack() {
        return null;
    }

    @Override
    public NetworkMessage unpack(Payload payload) {
        return null;
    }

    public void setBitmap(Bitmap bmp) {
        this.mBitmap = bmp;
    }
}
