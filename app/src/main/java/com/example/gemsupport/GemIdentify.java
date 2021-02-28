package com.example.gemsupport;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GemIdentify extends AppCompatActivity {

    protected Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private TensorImage inputImageB;
    private  int pictureSizeX;
    private  int pictureSizeY;
    private TensorBuffer outputProbabilityB;
    private TensorProcessor probabilityP;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private Bitmap bitmap1;
    private List<String> mlabels;
    ImageView imageView;
    Uri imageuri;
    Button button1;
    TextView classitext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gem_identify);
        imageView=(ImageView)findViewById(R.id.image);
        button1=(Button)findViewById(R.id.btnIdentify);
        classitext=(TextView)findViewById(R.id.txtIdentify);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),12);
            }
        });

        try{
            tflite=new Interpreter(loadmodelfile(this));
        }catch (Exception e) {
            e.printStackTrace();
        }

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int imageTensorIndex = 0;
                int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape();
                pictureSizeY = imageShape[1];
                pictureSizeX = imageShape[2];
                DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

                int probabilityTensorIndex = 0;
                int[] probabilityShape =
                        tflite.getOutputTensor(probabilityTensorIndex).shape();
                DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

                inputImageB = new TensorImage(imageDataType);
                outputProbabilityB = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
                probabilityP = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

                inputImageB = loadImage(bitmap1);

                tflite.run(inputImageB.getBuffer(),outputProbabilityB.getBuffer().rewind());
                showresult();
            }
        });



    }

    private TensorImage loadImage(final Bitmap bitmap) {
        // Loads bitmap
        inputImageB.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());

        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(pictureSizeX, pictureSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageB);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("model-export_icn_tflite-Gems_DS_20210117021325-2021-01-17T11_51_31.514465Z_model.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    private TensorOperator getPostprocessNormalizeOp(){
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }

    private void showresult(){

        try{
            mlabels = FileUtil.loadLabels(this,"model-export_icn_tflite-Gems_DS_20210117021325-2021-01-17T11_51_31.514465Z_dict.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Float> labeledProbability =
                new TensorLabel(mlabels, probabilityP.process(outputProbabilityB))
                        .getMapWithFloatValue();
        float maxValueInMap =(Collections.max(labeledProbability.values()));

        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                classitext.setText(entry.getKey());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();
            try {
                bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
