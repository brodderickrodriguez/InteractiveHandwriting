package group6.interactivehandwriting.activities.Room.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.AttributeSet;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;

public class DocumentView extends android.support.v7.widget.AppCompatImageView {

    private CanvasManager canvasManager;

    private Bitmap pdfPages[];
    private int curPDFPage;

    public DocumentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        canvasManager = new CanvasManager(this);

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        canvasManager.updateSize(w, h);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();

        canvasManager.update(canvas);
        canvas.restore();
    }

    public void setPDF(Bitmap bitmapArr[]) {
        this.pdfPages = bitmapArr;
        this.curPDFPage = 0;
        setImageBitmap(bitmapArr[0]);
    }

    public void incPDFPage() {
        int pageCount = this.pdfPages.length;
        if (pageCount == 0) {
            return;
        }
        if (this.curPDFPage == pageCount - 1) {
            this.curPDFPage = 0;
        }
        else {
            this.curPDFPage++;
        }
        setImageBitmap(this.pdfPages[this.curPDFPage]);
    }

    public void decPDFPage() {
        int pageCount = this.pdfPages.length;
        if (pageCount == 0) {
            return;
        }
        if (this.curPDFPage == 0) {
            this.curPDFPage = pageCount - 1;
        }
        else {
            this.curPDFPage--;
        }
        setImageBitmap(this.pdfPages[this.curPDFPage]);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

}
