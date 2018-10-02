package group6.interactivehandwriting.common.actions;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.google.android.gms.nearby.connection.Payload;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import group6.interactivehandwriting.common.network.NetworkMessage;

//TODO: Change how documents are shown on the screen.
//TODO: Add Drawable functionality.
public class ModifyDocumentAction extends UserAction implements OnPageChangeListener, OnLoadCompleteListener {
    private UserActionType userActionType;
    private PDFView pdfView;

    public ModifyDocumentAction() {
        //Specify UserActionType
        userActionType = UserActionType.DocumentAction;
    }

    /**************************** PDF Viewer ******************************/
    /* Open Document using File */
    public void openDocumentWithFile(File file, PDFView pView) {
        pdfView = pView;
        pdfView.useBestQuality(true);
        pdfView.fromFile(file).enableSwipe(false)
                .swipeHorizontal(false).enableAnnotationRendering(true).enableDoubletap(false)
                    .enableAnnotationRendering(false).onLoad(this).onDraw(new OnDrawListener() {
            @Override
            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                //Implement drawing here
            }
        }).load();

        pdfView.setVisibility(View.VISIBLE);

    }

    /* Open Document using Filename */
    public void openDocumentWithFile(String filePath, PDFView pView) {
        pdfView = pView;
        pdfView.useBestQuality(true);
        pdfView.fromFile(new File(filePath)).enableSwipe(false)
                .swipeHorizontal(false).enableAnnotationRendering(true).enableDoubletap(false)
                .enableAnnotationRendering(false).onLoad(this).onDraw(new OnDrawListener() {
            @Override
            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                //Implement drawing here
            }
        }).load();

        pdfView.setVisibility(View.VISIBLE);

    }

    /* Open Document using Filename */
    public void openDocumentWithURI(Uri uri, PDFView pView) {
        pdfView = pView;
        pdfView.useBestQuality(true);
        pdfView.fromUri(uri).enableSwipe(false)
                .swipeHorizontal(false).enableAnnotationRendering(true).enableDoubletap(false)
                .enableAnnotationRendering(false).onLoad(this).onDraw(new OnDrawListener() {
            @Override
            public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                //Implement drawing here
            }
        }).load();

        pdfView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        Toast.makeText(pdfView.getContext(), "Page: " + page, Toast.LENGTH_SHORT);
    }

    @Override
    public void loadComplete(int nbPages) {
        Toast.makeText(pdfView.getContext(), "PDF Loaded", Toast.LENGTH_LONG);
    }

    /*********************** NetworkMessage Interface *************************/
    @Override
    public Payload pack() {
        return null;
    }

    @Override
    public NetworkMessage unpack(Payload payload) {
        return null;
    }


    /**************** Utility Functions ****************/
    public File fromInputStreamToFile(String filePath, InputStream inputStream) {
        File file = new File(filePath);

        try {
            OutputStream outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
}

