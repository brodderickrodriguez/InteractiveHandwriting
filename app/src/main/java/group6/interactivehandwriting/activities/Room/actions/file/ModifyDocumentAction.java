package group6.interactivehandwriting.activities.Room.actions.file;

import android.content.Context;
import android.net.Uri;
import android.view.View;

import java.io.File;

import group6.interactivehandwriting.R;
import group6.interactivehandwriting.activities.Room.draw.CanvasManager;
import group6.interactivehandwriting.common.app.Profile;
import group6.interactivehandwriting.common.network.NetworkLayer;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;

public class ModifyDocumentAction extends View implements OnPageChangeListener, OnLoadCompleteListener {
    private PDFView pdfView;

    private NetworkLayer networkLayer;

    private CanvasManager canvasManager;
    private String deviceName;

    private static final float TOUCH_TOLERANCE = 4;

        public ModifyDocumentAction(Context context, Profile profile, NetworkLayer networkLayerIn) {
            super(context);

            pdfView = findViewById(R.id.pdf_view);

        }

        /* Open Document using File */
        public void openDocumentWithFile(File file, PDFView pView) {
            pdfView = pView;
            pdfView.useBestQuality(true);
            pdfView.fromFile(file).defaultPage(0).pages(0).enableDoubletap(false).onLoad(this).load();

            pdfView.setVisibility(View.VISIBLE);

        }

        /* Open Document using thefile path */
        public void openDocumentWithFile(String filePath, PDFView pView) {
           pdfView = pView;
            pdfView.useBestQuality(true);
            pdfView.setMaxZoom(1);
            pdfView.fromFile(new File(filePath)).defaultPage(0).pages(0).enableDoubletap(false).onLoad(this).load();
            pView.setVisibility(View.VISIBLE);

        }

        /* Open Document using Uri */
        public void openDocumentWithURI(Uri uri, PDFView pView) {
            pdfView = pView;
            pdfView.useBestQuality(true);
            pdfView.setMaxZoom(1);
            pdfView.fromUri(uri).defaultPage(0).pages(0).enableDoubletap(false).onLoad(this).load();

            pdfView.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageChanged(int page, int pageCount) {
          //  Toast.makeText(pdfView.getContext(), "Page: " + page, Toast.LENGTH_SHORT);
        }

        @Override
        public void loadComplete(int nbPages) {
          //  Toast.makeText(pdfView.getContext(), "PDF Loaded", Toast.LENGTH_LONG);
        }
}
