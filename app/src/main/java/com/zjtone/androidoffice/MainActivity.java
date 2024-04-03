package com.zjtone.androidoffice;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;

import com.wxiwei.office.IOffice;
import com.wxiwei.office.simpletext.model.IDocument;
import com.wxiwei.office.system.IControl;
import com.wxiwei.office.wp.control.Word;
import com.zjtone.androidoffice.databinding.ActivityMainBinding;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private IOffice iOffice;
    private static final String TAG = "MainActivity";

    private ActivityMainBinding binding; // Khai báo biến binding

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Khởi tạo và sử dụng binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        iOffice = new IOffice() {
            @Override
            public Activity getActivity() {
                return MainActivity.this;
            }

            @Override
            public String getAppName() {
                return "ioffice";
            }

            @Override
            public File getTemporaryDirectory() {
                File file = MainActivity.this.getExternalFilesDir(null);
                if (file != null) {
                    return file;
                } else {
                    return MainActivity.this.getFilesDir();
                }
            }

            @Override
            public void openFileFinish() {
                binding.outlayout.addView(getView(), new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            }
        };

        binding.button.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();

                    if (uri != null) {

// Chuyển đổi Uri thành DocumentFile
                        DocumentFile documentFile = DocumentFile.fromSingleUri(this, uri);

// Lấy đường dẫn tuyệt đối của tập tin từ DocumentFile
//                        String filePath = documentFile.getUri().getPath();
                        String filePath = "/storage/emulated/0/Download/A.docx";
                        Log.d(TAG, "onActivityResult: " + uri.getPath());

                        binding.wordView.init();
                        binding .wordView = Word(
                                getApplicationContext(),
                                new IDocument(),
                                filePath,
                                new IControl()
                        );

                        
                        iOffice.openFile(filePath);
                    }
                    break;
            }
        }
    }
}