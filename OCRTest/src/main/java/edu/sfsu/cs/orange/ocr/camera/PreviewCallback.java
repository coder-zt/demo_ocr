/*
 * Copyright (C) 2010 ZXing authors
 * Copyright 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.sfsu.cs.orange.ocr.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.sfsu.cs.orange.ocr.CaptureActivity;

/**
 * Called when the next preview frame is received.
 * 
 * The code for this class was adapted from the ZXing project: http://code.google.com/p/zxing
 */
final class PreviewCallback implements Camera.PreviewCallback {

  private static final String TAG = PreviewCallback.class.getSimpleName();

  private final CameraConfigurationManager configManager;
  private final CaptureActivity mActivity;
  private Handler previewHandler;
  private int previewMessage;

  PreviewCallback(CameraConfigurationManager configManager, CaptureActivity activity) {
    this.configManager = configManager;
    mActivity = activity;
  }

  void setHandler(Handler previewHandler, int previewMessage) {
    this.previewHandler = previewHandler;
    this.previewMessage = previewMessage;
  }

  // Since we're not calling setPreviewFormat(int), the data arrives here in the YCbCr_420_SP 
  // (NV21) format.
  @Override
  public void onPreviewFrame(byte[] data, Camera camera) {
//    int[] pixels = new int[data.length];
//    int i = 0;
//    for (byte datum : data) {
//      int grey = datum & 0xff;
//      pixels[i] = 0xFF000000 | (grey * 0x00010101);
//      i++;
//    }
//
//    int width = 640;
//    int height = 360;
//    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//    mActivity.showBitmap(bitmap);

    Point cameraResolution = configManager.getCameraResolution();
    Handler thePreviewHandler = previewHandler;
    if (cameraResolution != null && thePreviewHandler != null) {
      Message message = thePreviewHandler.obtainMessage(previewMessage,
          cameraResolution.y,  cameraResolution.x,data);
      message.sendToTarget();
      previewHandler = null;
    } else {
      Log.d(TAG, "Got preview callback, but no handler or resolution available");
    }
  }

}
