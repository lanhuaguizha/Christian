/*
 * Copyright 2016. SHENQINCI(沈钦赐)<dev@qinc.me>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ren.qinc.markdowneditors.presenter;


import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.christian.R;
import com.christian.util.ChristianUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import ren.qinc.markdowneditors.AppContext;
import ren.qinc.markdowneditors.base.mvp.BasePresenter;
import ren.qinc.markdowneditors.view.EditorActivity;
import ren.qinc.markdowneditors.view.EditorFragment;

/**
 * 编辑界面Presenter
 * Created by 沈钦赐 on 16/1/18.
 */
public class EditorFragmentPresenter extends BasePresenter<IEditorFragmentView> {
    private static final String TAG = EditorFragmentPresenter.class.getSimpleName();
    //当前文件路径
    private String filePath;
    //当前本地文件名字(如果创建,则为"",可以和当前标题输入框的值不同)
    private String fileName;
    //时候为新创建文件
    private boolean isCreateFile;

    public EditorFragmentPresenter() {
//        if (file.isDirectory()) {
//            this.filePath = file.getAbsolutePath();
//            this.fileName = "";
//            isCreateFile = true;
//        } else {
//            this.fileName = file.getName();
//            this.filePath = file.getParent();
//        }
    }


    /**
     * 加载当前文件
     */
    public void loadFile() {
        mCompositeSubscription.add(mDataManager.readFile(getMDFile())
                .subscribe(content -> {
                    if (getMvpView() == null) return;
                    getMvpView().onReadSuccess(fileName, content);
                }, throwable -> {
                    callFailure(-1, throwable.getMessage(), IEditorFragmentView.CALL_LOAOD_FILE);
                }));

    }

    @NonNull
    public File getMDFile() {
        return new File(filePath, fileName);
    }

    private boolean textChanged = false;

    /**
     * 刷新保存图标的状态
     */
    public void refreshMenuIcon() {
        if (getMvpView() != null) return;
        if (textChanged)
            getMvpView().otherSuccess(IEditorFragmentView.CALL_NO_SAVE);
        else
            getMvpView().otherSuccess(IEditorFragmentView.CALL_SAVE);

    }

    public void textChange() {
        textChanged = true;
        if (getMvpView() != null) {
            getMvpView().otherSuccess(IEditorFragmentView.CALL_NO_SAVE);
        }

    }

    /**
     * 保存当前内容
     *
     * @param name    the name
     * @param content the content
     */
    public void save(String name, String content) {
        saveForExit(name, content, false);
    }

    /**
     * 保存当前内容并退出
     *
     * @param name    the name
     * @param content the content
     * @param exit    the exit
     */
    public void saveForExit(String name, String content, boolean exit) {
        if (TextUtils.isEmpty(name)) {
            callFailure(-1, "名字不能为空", IEditorFragmentView.CALL_SAVE);
            return;
        }
        if (content == null) return;

        //上一次文件名为空
        if (TextUtils.isEmpty(fileName)) {
            //新建文件
            if (isCreateFile) {
                //新创建文件，但是文件已经存在了
                File file = new File(filePath, name + ".md");
                if (!file.isDirectory() && file.exists()) {
                    callFailure(-1, "文件已经存在", IEditorFragmentView.CALL_SAVE);
                    return;
                }
            }
            fileName = name;
        }


        if (!fileName.endsWith(".md") &&
                !fileName.endsWith(".markdown") &&
                !fileName.endsWith(".mdown")) {
            fileName = fileName + ".md";
        }


        EditorFragment editorFragment = (EditorFragment) getMvpView();
//        if (editorFragment.documentGospelPath != null) { // Edit exist documents
//        } else { // Create a new document
        editorFragment.firebaseFirestore.collection(String.valueOf(R.string.gospels));
        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        if (!editorFragment.mSpinner.getText().toString().trim().isEmpty()) {
            data.put(editorFragment.getString(R.string.desc), editorFragment.mSpinner.getText());
        } else {
            data.put(editorFragment.getString(R.string.desc), editorFragment.getString(R.string.uncategorized));
        }
        data.put(editorFragment.getString(R.string.name), editorFragment.mName.getText().toString().trim());
        if (!editorFragment.mAuthor.getText().toString().trim().isEmpty()) {
            data.put(editorFragment.getString(R.string.author), editorFragment.mAuthor.getText().toString().trim());
        } else {
            data.put(editorFragment.getString(R.string.author), editorFragment.getString(R.string.no_author));
        }
        if (!editorFragment.mChurch.getText().toString().trim().isEmpty()) {
            data.put(editorFragment.getString(R.string.church_lower_case), editorFragment.mChurch.getText().toString().trim());
        } else {
            data.put(editorFragment.getString(R.string.church_lower_case), editorFragment.getString(R.string.no_church));
        }
        data.put(editorFragment.getString(R.string.content_lower_case), editorFragment.mContent.getText().toString().trim());
        data.put(editorFragment.getString(R.string.time), ChristianUtil.getDateAndCurrentTime());
        EditorActivity activity = (EditorActivity) ((EditorFragment) getMvpView()).getActivity();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            data.put(editorFragment.getString(R.string.userId), FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        if (!editorFragment.mName.getText().toString().trim().isEmpty()) {
            if (!editorFragment.mContent.getText().toString().trim().isEmpty()) {
                DocumentReference gospelRef = editorFragment.firebaseFirestore.collection(editorFragment.getString(R.string.gospels)).document(editorFragment.mName.getText().toString().trim());
                gospelRef.set(data)
                        .addOnSuccessListener(documentReference -> {
                            Log.d(TAG, "DocumentSnapshot written with ID: " + gospelRef.getId());

                            isCreateFile = false;
                            textChanged = false;
                            if (!rename(name)) {
                                callFailure(-1, "重命名失败", IEditorFragmentView.CALL_SAVE);
                                return;
                            }
                            if (getMvpView() != null) {
                                if (exit)
                                    getMvpView().otherSuccess(IEditorFragmentView.CALL_EXIT);
                                else
                                    getMvpView().otherSuccess(IEditorFragmentView.CALL_SAVE);
                            }
                        })
                        .addOnFailureListener(e -> {
                            callFailure(-1, editorFragment.getString(R.string.please_sign_in), IEditorFragmentView.CALL_SAVE);
                            if (activity != null) {
                                activity.startSignInActivity();
                            }
                        });
            } else {
                AppContext.showSnackbar(editorFragment.mContent, editorFragment.getString(R.string.content_empty));
            }
        } else {
            AppContext.showSnackbar(editorFragment.mContent, editorFragment.getString(R.string.title_empty));
        }

//        }

//        mDataManager.saveFile(getMDFile(), content).subscribe(success -> {
//            if (success) {
//                isCreateFile = false;
//                textChanged = false;
//                if (!rename(name)) {
//                    callFailure(-1, "重命名失败", IEditorFragmentView.CALL_SAVE);
//                    return;
//                }
//                if (getMvpView() != null) {
//                    if (exit)
//                        getMvpView().otherSuccess(IEditorFragmentView.CALL_EXIT);
//                    else
//                        getMvpView().otherSuccess(IEditorFragmentView.CALL_SAVE);
//                }
//            } else {
//                callFailure(-1, "保存失败", IEditorFragmentView.CALL_SAVE);
//            }
//        }, throwable -> {
//            callFailure(-1, "保存失败", IEditorFragmentView.CALL_SAVE);
//        });
    }

    public void getDocument(EditorFragment editorFragment) {
        DocumentReference documentReference = editorFragment.firebaseFirestore.collection(editorFragment.getString(R.string.gospels)).document(editorFragment.documentGospelPath);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null) {
                        if (document.exists()) {
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            editorFragment.mSpinner.setText((CharSequence) document.get(editorFragment.getString(R.string.desc)));
                            editorFragment.mName.setText((CharSequence) document.get(editorFragment.getString(R.string.name)));
                            editorFragment.mAuthor.setText((CharSequence) document.get(editorFragment.getString(R.string.author)));
                            editorFragment.mChurch.setText((CharSequence) document.get(editorFragment.getString(R.string.church_lower_case)));
                            editorFragment.mContent.setText((CharSequence) document.get(editorFragment.getString(R.string.content_lower_case)));
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    private boolean rename(String newName) {

        int end = fileName.lastIndexOf(".");
        String name = fileName.substring(0, end);
        if (newName.equals(name)) return true;

        String suffix = fileName.substring(end, fileName.length());
        if (suffix.endsWith(".md") ||
                suffix.endsWith(".markdown") ||
                suffix.endsWith(".mdown")) {
            //重命名
            File oldFile = getMDFile();
            File newPath = new File(filePath, newName + suffix);
            if (oldFile.getAbsolutePath().equals(newPath.getAbsolutePath())) return true;

            fileName = newPath.getName();

            if (newPath.exists())//文件已经存在了
                return false;
            boolean b = oldFile.renameTo(newPath);
            return b;
        }
        return false;
    }

    public boolean isSave() {
        return !textChanged;
    }
}
