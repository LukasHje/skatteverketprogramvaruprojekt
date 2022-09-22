package com.company;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import net.sourceforge.tess4j.*;
import net.sourceforge.tess4j.util.ImageIOHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static net.sourceforge.tess4j.ITessAPI.TRUE;
import static org.junit.Assert.assertTrue;

/*
 * Class that contains the main-method.
 *
 * @author Lukas Hjernquist & Olle Gardell
 */
public class Main {

    public static void main(String[] args) throws Exception {
        GUI gui = new GUI();
        gui.startMenu();
    }
}


