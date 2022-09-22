package com.company;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import net.sourceforge.tess4j.ITessAPI;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.TessAPI1;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageIOHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;

import static net.sourceforge.tess4j.ITessAPI.TRUE;
import static org.junit.Assert.assertTrue;

public class BoxModelHandler {
    String datapath = System.getProperty("user.dir") + "\\tessdata";
    String testResourcesDataPath = System.getProperty("user.dir") + "\\test\\resources\\test-data";
    String language = "eng";
    HashMap<Integer, String> boxModelMap;

    public HashMap<Integer, String> getBoxmodelHashMap () throws Exception {
        ITesseract instance = new Tesseract();
        instance.setDatapath (new File(datapath).getPath());
        boxModelMap = testResultIterator();

        return boxModelMap;
    }
    public HashMap<Integer, String> testResultIterator() throws Exception {
        HashMap<Integer, String> tempMap = new HashMap<Integer, String>();
        ITessAPI.TessBaseAPI handle = TessAPI1.TessBaseAPICreate();
        File tiff = new File(testResourcesDataPath, "ocrb_ex.jpg");
        BufferedImage image = ImageIO.read(new FileInputStream(tiff));
        ByteBuffer buf = ImageIOHelper.convertImageData(image);
        int bpp = image.getColorModel().getPixelSize();
        int bytespp = bpp / 8;
        int bytespl = (int) Math.ceil(image.getWidth() * bpp / 8.0);
        TessAPI1.TessBaseAPIInit3(handle, datapath, language);
        TessAPI1.TessBaseAPISetPageSegMode(handle, ITessAPI.TessPageSegMode.PSM_AUTO);
        TessAPI1.TessBaseAPISetImage(handle, buf, image.getWidth(), image.getHeight(), bytespp, bytespl);
        ITessAPI.ETEXT_DESC monitor = new ITessAPI.ETEXT_DESC();
        ITessAPI.TimeVal timeout = new ITessAPI.TimeVal();
        timeout.tv_sec = new NativeLong(0L); // time > 0 causes blank ouput
        monitor.end_time = timeout;
        TessAPI1.TessBaseAPIRecognize(handle, monitor);
        ITessAPI.TessResultIterator ri = TessAPI1.TessBaseAPIGetIterator(handle);
        ITessAPI.TessPageIterator pi = TessAPI1.TessResultIteratorGetPageIterator(ri);
        TessAPI1.TessPageIteratorBegin(pi);
        int level = ITessAPI.TessPageIteratorLevel.RIL_SYMBOL;

        do {
            Pointer ptr = TessAPI1.TessResultIteratorGetUTF8Text(ri, level);
            String word = ptr.getString(0);
            TessAPI1.TessDeleteText(ptr);
            float confidence = TessAPI1.TessResultIteratorConfidence(ri, level);
            IntBuffer leftB = IntBuffer.allocate(1);
            IntBuffer topB = IntBuffer.allocate(1);
            IntBuffer rightB = IntBuffer.allocate(1);
            IntBuffer bottomB = IntBuffer.allocate(1);
            TessAPI1.TessPageIteratorBoundingBox(pi, level, leftB, topB, rightB, bottomB);
            int left = leftB.get();
            int top = topB.get();
            int right = rightB.get();
            int bottom = bottomB.get();
            tempMap.put(left + top + right +  bottom, word);
            System.out.printf("%s %d %d %d %d %f%n", word, left, top, right, bottom, confidence);


        } while (TessAPI1.TessPageIteratorNext(pi, level) == TRUE);
        assertTrue(true);
        return tempMap;
    }
}
