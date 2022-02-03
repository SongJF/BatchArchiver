package com.laplace.dove.batcharchiver.ui.stepper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.snackbar.Snackbar;
import com.laplace.dove.batcharchiver.R;
import com.laplace.dove.batcharchiver.databinding.ArchiveFragmentBinding;
import com.laplace.dove.batcharchiver.ui.dialog.DoubleProgressDialog;
import com.laplace.dove.batcharchiver.ui.dialog.LoadingDialog;
import com.laplace.dove.batcharchiver.ui.stepper.step.ArchiveCompressLevelStep;
import com.laplace.dove.batcharchiver.ui.stepper.step.ArchiveFileSelectStep;
import com.laplace.dove.batcharchiver.ui.stepper.step.ArchiveFolderSelectStep;
import com.laplace.dove.batcharchiver.ui.stepper.step.ArchivePasswordStep;
import com.laplace.dove.batcharchiver.ui.stepper.step.ArchiveSqlSetStep;
import com.laplace.dove.batcharchiver.utils.ClipboardUtils;
import com.laplace.dove.batcharchiver.utils.DataBaseManager;
import com.laplace.dove.batcharchiver.utils.archive.ArchiveParam;
import com.laplace.dove.batcharchiver.utils.archive.ArchiveUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import ernestoyaquello.com.verticalstepperform.listener.StepperFormListener;

public class DbArchiveFragment extends Fragment implements StepperFormListener {
    private static final String STATE_SOURCE = "state_source";
    private static final String STATE_DEST = "state_dest";
    private static final String STATE_COMPRESS_LEVEL = "state_compress_level";
    private static final String STATE_PASSWORD = "state_password";
    private static final String STATE_ENCRYPTION_TYPE= "state_encryption_type";
    private static final String STATE_DB_FILE = "state_db_file";
    private static final String STATE_SQL_CMD = "state_sql_cmd";

    private static final int STAGE_ARCHIVE_FAILURE = -1;
    private static final int STAGE_ARCHIVE_FINISH = 0;
    private static final int STAGE_INDEX_FINISH = 1;
    private static final int STAGE_ARCHIVE_NEXT = 2;
    private static final int STAGE_FILE_COUNT = 3;
    private static final int STAGE_FILE_NAME_NEXT = 4;

    private ArchiveFragmentBinding binding;
    private ArchiveFolderSelectStep sourceDirStep;
    private ArchiveFolderSelectStep destDirStep;
    private ArchivePasswordStep passWordStep;
    private ArchiveCompressLevelStep compressLevelStep;
    private ArchiveFileSelectStep dbSelectStep;
    private ArchiveSqlSetStep sqlSetStep;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = ArchiveFragmentBinding.inflate(inflater, container, false);

        sourceDirStep = new ArchiveFolderSelectStep(getString(R.string.step_title_select_source_dir));
        dbSelectStep = new ArchiveFileSelectStep(getString(R.string.step_title_select_db_file));
        sqlSetStep = new ArchiveSqlSetStep(getString(R.string.step_title_select_sql));
        destDirStep = new ArchiveFolderSelectStep(getString(R.string.step_title_select_dest_dir));
        compressLevelStep = new ArchiveCompressLevelStep(getString(R.string.step_title_set_compress_level));
        passWordStep = new ArchivePasswordStep(getString(R.string.step_title_set_password));
        binding.fragmentArchive
                .setup(this,
                        sourceDirStep,
                        dbSelectStep,
                        sqlSetStep,
                        destDirStep,
                        compressLevelStep,
                        passWordStep
                )
                .init();
        return binding.getRoot();
    }
    @Override
    public void onCancelledForm() {}

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(STATE_SOURCE, sourceDirStep.getStepData());
        outState.putString(STATE_DEST, destDirStep.getStepData());
        outState.putString(STATE_COMPRESS_LEVEL, compressLevelStep.getStepData());
        outState.putCharArray(STATE_PASSWORD, passWordStep.getStepData().getPassword());
        outState.putString(STATE_ENCRYPTION_TYPE, passWordStep.getStepData().getEncryptionType());
        outState.putString(STATE_DB_FILE, dbSelectStep.getStepData());
        outState.putString(STATE_SQL_CMD, sqlSetStep.getStepData());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState != null) {
            if (savedInstanceState.containsKey(STATE_SOURCE)){
                sourceDirStep.restoreStepData(savedInstanceState.getString(STATE_SOURCE));
            }

            if (savedInstanceState.containsKey(STATE_DEST)){
                destDirStep.restoreStepData(savedInstanceState.getString(STATE_DEST));
            }

            if (savedInstanceState.containsKey(STATE_COMPRESS_LEVEL)){
                compressLevelStep.restoreStepData(savedInstanceState.getString(STATE_COMPRESS_LEVEL));
            }

            if (savedInstanceState.containsKey(STATE_PASSWORD) && savedInstanceState.containsKey(STATE_ENCRYPTION_TYPE)){
                ArchiveParam param = new ArchiveParam();
                param.setPassword(savedInstanceState.getCharArray(STATE_PASSWORD));
                param.setEncryptionType(savedInstanceState.getString(STATE_ENCRYPTION_TYPE));
                passWordStep.restoreStepData(param);
            }

            if (savedInstanceState.containsKey(STATE_DB_FILE)){
                dbSelectStep.restoreStepData(savedInstanceState.getString(STATE_DB_FILE));
            }

            if (savedInstanceState.containsKey(STATE_SQL_CMD)){
                sqlSetStep.restoreStepData(savedInstanceState.getString(STATE_SQL_CMD));
            }
        }

        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onCompletedForm() {
        LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        loadingDialog.dialog.setCancelable(false);
        loadingDialog.setText(getString(R.string.dialog_indexing));
        loadingDialog.show();

        DoubleProgressDialog archiveDialog = new DoubleProgressDialog(getContext());
        archiveDialog.dialog.setTitle(getString(R.string.dialog_archiving));
        archiveDialog.dialog.setCancelable(false);

        Handler handler = new Handler(Looper.getMainLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == STAGE_INDEX_FINISH){
                    loadingDialog.dismiss();
                    archiveDialog.firstProgressView.setMax(msg.arg1);
                    archiveDialog.firstProgressView.setProgress(0);
                    archiveDialog.show();
                }

                if (msg.what == STAGE_ARCHIVE_NEXT){
                    archiveDialog.firstProgressNext((String) msg.obj);
                    archiveDialog.secondProgressView.setTitle(getString(R.string.dialog_indexing));
                }

                if (msg.what == STAGE_FILE_COUNT){
                    archiveDialog.secondProgressView.setMax(msg.arg1);
                    archiveDialog.secondProgressView.setProgress(0);
                }

                if (msg.what == STAGE_FILE_NAME_NEXT){
                    archiveDialog.secondProgressNext((String) msg.obj);
                }

                if (msg.what == STAGE_ARCHIVE_FAILURE){
                    finish();
                    Snackbar.make(getView(), (String)msg.obj, Snackbar.LENGTH_LONG).show();
                }

                if (msg.what == STAGE_ARCHIVE_FINISH) {
                    finish();
                    Snackbar.make(getView(), getString(R.string.snakebar_operate_complete), Snackbar.LENGTH_LONG).show();
                }
            }

            private void finish(){
                loadingDialog.dismiss();
                archiveDialog.dismiss();
                goBack();
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> srcDestNameMap = getSrcDestFromDb();
                List<Path> folders = getSubDirs(sourceDirStep.getStepData(), srcDestNameMap);

                if (folders.size() == 0){
                    Message msg = Message.obtain();
                    msg.what = STAGE_ARCHIVE_FAILURE;
                    msg.obj = getString(R.string.snakebar_no_avaliable_folder);
                    handler.sendMessage(msg);
                    return;
                }else {
                    Message msg = Message.obtain();
                    msg.what = STAGE_INDEX_FINISH;
                    msg.arg1 = folders.size();
                    handler.sendMessage(msg);
                }

                List<Path> failedArchives = new ArrayList<>();
                Consumer<String> archiveNotifyer = fileName -> {
                    Message msg = Message.obtain();
                    msg.what = STAGE_ARCHIVE_NEXT;
                    msg.obj = fileName;
                    handler.sendMessage(msg);
                };
                Consumer<Integer> fileConutNotifyer = cnt -> {
                    Message msg = Message.obtain();
                    msg.what = STAGE_FILE_COUNT;
                    msg.arg1 = cnt;
                    handler.sendMessage(msg);
                };
                Consumer<File> currentFileNotifyer = file -> {
                    Message msg = Message.obtain();
                    msg.what = STAGE_FILE_NAME_NEXT;
                    String name = file.getName();
                    if (file.isDirectory()){
                        name = "[d] " + name;
                    }
                    msg.obj = name;
                    handler.sendMessage(msg);
                };
                for (Path path : folders) {
                    if (!archiveFolder(path, srcDestNameMap.get(path.getFileName().toString()),
                            archiveNotifyer, fileConutNotifyer, currentFileNotifyer)){
                        failedArchives.add(path);
                    }
                }
                if (failureExist(failedArchives)){
                    Message failure = Message.obtain();
                    failure.what = STAGE_ARCHIVE_FAILURE;
                    failure.obj = getString(R.string.snakebar_archive_failed);
                    handler.sendMessage(failure);
                }

                handler.sendEmptyMessage(STAGE_ARCHIVE_FINISH);
            }
        }).start();
    }

    private Map<String, String> getSrcDestFromDb(){
        try {
            return DataBaseManager.mapQuery(dbSelectStep.getStepData(), sqlSetStep.getStepData(), cursor -> {
                HashMap<String, String> srcDestMap = new HashMap<>();
                while (cursor.moveToNext()){
                    int srcIndex = cursor.getColumnIndex("src");
                    String src = srcIndex >=0 && srcIndex < cursor.getColumnCount() ?
                            cursor.getString(srcIndex) : "";
                    int destIndex = cursor.getColumnIndex("dest");
                    String dest = destIndex >= 0 && destIndex < cursor.getColumnCount() ?
                            cursor.getString(destIndex) : "";
                    srcDestMap.put(src, dest);
                }
                return srcDestMap;
            });
        }catch (Exception e){
            return new HashMap<>();
        }
    }

    private List<Path> getSubDirs(@NonNull String sourcePath, @NonNull Map<String, String> dirNames){
        List<Path> subDirs = new ArrayList<>();

        Path root = Paths.get(sourcePath);
        try {
            Files.walk(root, 1)
                    .filter(dir -> Files.isDirectory(dir) &&
                            !dir.equals(root) &&
                            dirNames.containsKey(dir.getFileName().toString()))
                    .forEach(subDirs::add);
        } catch (IOException e) {
            return subDirs;
        }

        return subDirs;
    }

    private boolean archiveFolder(Path path, String archiveName,
                                  Consumer<String> archiveNotifyer,
                                  Consumer<Integer> fileCountNotifyer,
                                  Consumer<File> currentFileNotifyer){
        File src = new File(path.toUri());
        if (!src.exists()){
            return false;
        }

        archiveName = archiveName == null || archiveName.isEmpty() ?
                path.getFileName().toString() : archiveName;
        File archive = new File(destDirStep.getStepData() + "/" + archiveName + ".zip");
        archiveNotifyer.accept(archive.getName());
        try {
            ArchiveParam param = new ArchiveParam(
                    passWordStep.getStepData().getPassword(),
                    passWordStep.getStepData().getEncryptionType(),
                    compressLevelStep.getStepData()
            );
            ArchiveUtil.zip(src, archive, param,fileCountNotifyer, currentFileNotifyer);
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    private boolean failureExist(List<Path> paths){
        List<String> failurePath = new ArrayList<>();
        paths.forEach(path -> failurePath.add(path.toString()));
        if (failurePath.size() > 0){
            ClipboardUtils.toClipBoard(getActivity(), failurePath);
            return true;
        }
        return false;
    }

    private void goBack(){
        NavHostFragment.findNavController(this).navigateUp();
    }
}
